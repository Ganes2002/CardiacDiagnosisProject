//package com.itc.repositoryTest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
//
//import com.itc.Bookmark;
//import com.itc.model.BookmarkModel;
//import com.itc.repository.BookmarkRepository;
//
//@SpringBootTest
//@ContextConfiguration(classes = Bookmark.class)
//@AutoConfigureTestDatabase(replace = Replace.NONE)
//public class BookmarkRepositoryTest {
//
//    @Autowired
//    private BookmarkRepository bookmarkRepo;
//
//    private BookmarkModel bookmark;
//
//    @BeforeEach
//    public void setUp() {
//        // Create and save a sample bookmark
//        bookmark = new BookmarkModel();
//        bookmark.setUserName("sagar");
//        bookmark.setAge(30); 
//        bookmark.setBp(120);
//        bookmark.setCholesterol(180);
//        bookmark.setDiabetic("Yes");
//        bookmark.setSmoking_status("No");
//        bookmark.setPain_type("Chest Pain");
//        bookmark.setTreatment("Medication");
//        bookmark.setGender("Male");        
//        // Save the bookmark
//        bookmarkRepo.save(bookmark);
//    }
//
//    @Test
//    public void testFindByUserId() {
//        // Find bookmarks by User ID
//        List<BookmarkModel> bookmarks = bookmarkRepo.findByUserName("sagar");
//        
//        // Ensure that the list is not empty
//        assertFalse(bookmarks.isEmpty());
//        
//        // Ensure that the bookmark returned has the correct userId
//        assertEquals("sagar", bookmarks.get(0).getUserName());
//    }
//
//    @Test
//    public void testFindByUserId_NoResults() {
//        // Try to find bookmarks with a userId that doesn't exist
//        List<BookmarkModel> bookmarks = bookmarkRepo.findByUserName("Asrar");
//        
//        // Ensure that no bookmarks are returned
//        assertTrue(bookmarks.isEmpty());
//    }
//}
