package com.itc.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.itc.model.BookmarkModel;
import com.itc.service.BookmarkService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("bookmark")
//@CrossOrigin(origins = "http://localhost:3000",allowedHeaders = "*", methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE} )
public class BookmarkController {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkController.class);

    @Autowired
    BookmarkService bookmarkService;

    // Add Bookmark
    @PostMapping("/add")
    public ResponseEntity<?> addBookmark(@Valid @RequestBody BookmarkModel bookmark) {
        logger.info("Request to add bookmark: {}", bookmark);
        
        BookmarkModel savedBookmark = bookmarkService.addBookmark(bookmark);

        if (savedBookmark == null) {
            logger.error("Failed to add bookmark: {}", bookmark);
            return new ResponseEntity<>("Failed to add bookmark", HttpStatus.BAD_REQUEST);
        }

        logger.info("Bookmark added successfully with ID: {}", savedBookmark.getId());
        return new ResponseEntity<>(savedBookmark, HttpStatus.CREATED);
    }

    // Get Bookmarks by User Name
//    @CrossOrigin("http://")
    @GetMapping("/user/{userName}")
    public ResponseEntity<?> getBookmarkByUserId(@PathVariable String userName) {
        logger.info("Request to fetch bookmarks for user: {}", userName);
        
        List<BookmarkModel> bookmarks = bookmarkService.getBookmarkByUserName(userName);

        if (bookmarks == null || bookmarks.isEmpty()) {
            logger.warn("No bookmarks found for user: {}", userName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No bookmarks found for user with ID: " + userName);
        }

        logger.info("Found {} bookmarks for user: {}", bookmarks.size(), userName);
        return ResponseEntity.ok(bookmarks);
    }

    // Update Bookmark
    @PutMapping("/update/{bookmarkId}/{userName}")
    public ResponseEntity<?> updateBookmark(
            @PathVariable Long bookmarkId, 
            @PathVariable String userName, 
            @RequestBody BookmarkModel newBookmark) {

        logger.info("Request to update bookmark with ID: {} for user: {}", bookmarkId, userName);

        BookmarkModel updatedBookmark = bookmarkService.updateBookmark(bookmarkId, userName, newBookmark);

        if (updatedBookmark != null) {
            logger.info("Bookmark with ID: {} updated successfully", bookmarkId);
            return ResponseEntity.ok(updatedBookmark);
        }

        logger.warn("Bookmark with ID: {} not found or user {} is not authorized", bookmarkId, userName);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Bookmark with ID " + bookmarkId + " not found or user not authorized.");
    }

    // Delete Bookmark
    @DeleteMapping("/delete/{bookmarkId}/{userName}")
    public ResponseEntity<String> deleteBookmark(@PathVariable Long bookmarkId, @PathVariable String userName) {
        logger.info("Request to delete bookmark with ID: {} for user: {}", bookmarkId, userName);

        boolean deleted = bookmarkService.deleteBookmarkById(bookmarkId, userName);
        if (deleted) {
            logger.info("Bookmark with ID: {} deleted successfully by user: {}", bookmarkId, userName);
            return ResponseEntity.ok("Bookmark deleted successfully.");
        }

        logger.warn("Bookmark with ID: {} not found or user {} is not authorized", bookmarkId, userName);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bookmark not found or user not authorized.");
    }

    // Delete All Bookmarks by User Name
    @DeleteMapping("/delete/all/{userName}")
    public ResponseEntity<String> deleteAllBookmarks(@PathVariable String userName) {
        logger.info("Request to delete all bookmarks for user: {}", userName);
        try {
            bookmarkService.deleteAllBookmarksByUserName(userName);
            logger.info("All bookmarks deleted successfully for user: {}", userName);
            return ResponseEntity.ok("All bookmarks deleted successfully for user: " + userName);
        } catch (Exception e) {
            logger.error("Error deleting bookmarks for user: {}", userName, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No bookmarks found for user: " + userName);
        }
    }
}
