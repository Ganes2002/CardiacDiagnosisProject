package com.itc.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.itc.model.BookmarkModel;
import com.itc.repository.BookmarkRepository;
import com.itc.service.BookmarkServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookmarkServiceImplTest {

    @Mock
    private BookmarkRepository bookmarkRepo;

    @InjectMocks
    private BookmarkServiceImpl bookmarkService;

    private BookmarkModel bookmark;

    @BeforeEach
    public void setUp() {
        bookmark = new BookmarkModel();
        bookmark.setBookmarkId(1L);
        bookmark.setUserName("sagar");
        bookmark.setAge(30);
        bookmark.setBp(120);
        bookmark.setCholesterol(180);
        bookmark.setDiabetic("Yes");
        bookmark.setSmoking_status("No");
        bookmark.setPain_type("Chest Pain");
        bookmark.setTreatment("Medication");
    }

    @Test
    public void addBookmarkTest() {
        when(bookmarkRepo.save(any(BookmarkModel.class))).thenReturn(bookmark);

        BookmarkModel savedBookmark = bookmarkService.addBookmark(bookmark);

        assertNotNull(savedBookmark);
        assertEquals(1L, savedBookmark.getBookmarkId());
        assertEquals("Chest Pain", savedBookmark.getPain_type());
    }

    @Test
    public void findBookmarkByIdTest() {
        when(bookmarkRepo.findById(1L)).thenReturn(Optional.of(bookmark));

        BookmarkModel foundBookmark = bookmarkService.findBookmarkById(1L);

        assertNotNull(foundBookmark);
        assertEquals(1L, foundBookmark.getBookmarkId());
        assertEquals(30, foundBookmark.getAge());
    }

//    @Test
//    public void deleteBookmarkByIdTest() {
//        when(bookmarkRepo.findById(1L)).thenReturn(Optional.of(bookmark));
//
//        boolean isDeleted = bookmarkService.deleteBookmarkById(1L);
//
//        assertEquals(true, isDeleted);
//    }
    @Test
    public void deleteBookmarkTest() {
        Long bookmarkId = 1L;
        String userName = "sagar";

        // Mocking the bookmark repository response
        BookmarkModel bookmark = new BookmarkModel();
        bookmark.setBookmarkId(bookmarkId);
        bookmark.setUserName(userName);

        when(bookmarkRepo.findById(bookmarkId)).thenReturn(Optional.of(bookmark));
        doNothing().when(bookmarkRepo).delete(any(BookmarkModel.class));

        boolean result = bookmarkService.deleteBookmarkById(bookmarkId, userName);

        // Using JUnit 5 assertion instead of assertTrue
        Assertions.assertTrue(result, "Bookmark should be deleted successfully");
        
        // Verify if delete was called once
        verify(bookmarkRepo, times(1)).delete(any(BookmarkModel.class));
    }


//    @Test
//    public void updateBookmarkTest() {
//        // Mocking the original bookmark
//        when(bookmarkRepo.findById(1L)).thenReturn(Optional.of(bookmark));
//
//        // Create a new bookmark with updated values
//        bookmark.setAge(35);
//        bookmark.setBp(130);
//        when(bookmarkRepo.save(any(BookmarkModel.class))).thenReturn(bookmark);
//
//        BookmarkModel updatedBookmark = bookmarkService.updateBookmark(1L, bookmark);
//
//        assertNotNull(updatedBookmark);
//        assertEquals(35, updatedBookmark.getAge());
//        assertEquals(130, updatedBookmark.getBp());
//    }
    @Test
    public void updateBookmarkTest() {
        // Mocking the original bookmark
        BookmarkModel bookmark = new BookmarkModel();
        bookmark.setBookmarkId(1L);
        bookmark.setUserName("sagar"); // Setting the userId for the bookmark
        bookmark.setAge(30);
        bookmark.setBp(120);

        // Create a new bookmark with updated values
        BookmarkModel updatedBookmark = new BookmarkModel();
        updatedBookmark.setBookmarkId(1L);
        updatedBookmark.setUserName("sagar"); // UserId should match
        updatedBookmark.setAge(35);    // Updated age
        updatedBookmark.setBp(130);    // Updated blood pressure

        // Mocking the repository to return the original bookmark when findById is called
        when(bookmarkRepo.findById(1L)).thenReturn(Optional.of(bookmark));

        // Mock the save operation to return the updated bookmark
        when(bookmarkRepo.save(any(BookmarkModel.class))).thenReturn(updatedBookmark);

        // Call the service method to update the bookmark
        BookmarkModel actualUpdatedBookmark = bookmarkService.updateBookmark(1L,"sagar", updatedBookmark);

        // Assert that the updated bookmark is not null and that values were updated correctly
        assertNotNull(actualUpdatedBookmark);
        assertEquals(35, actualUpdatedBookmark.getAge());
        assertEquals(130, actualUpdatedBookmark.getBp());
        assertEquals("sagar", actualUpdatedBookmark.getUserName());
    }


    @Test
    public void getBookmarkByUserIdTest() {
        // Assuming there's a list of bookmarks for a user
        when(bookmarkRepo.findByUserName("sagar")).thenReturn(List.of(bookmark));

        List<BookmarkModel> bookmarks = bookmarkService.getBookmarkByUserName("sagar");

        assertNotNull(bookmarks);
        assertEquals(1, bookmarks.size());
        assertEquals("sagar", bookmarks.get(0).getUserName());
    }
}
