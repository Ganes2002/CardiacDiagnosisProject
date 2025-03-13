package com.itc.controllerTest;

import com.itc.controller.BookmarkController;
import com.itc.globalException.GlobalExceptionHandler;
import com.itc.model.BookmarkModel;
import com.itc.service.BookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class BookmarkControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookmarkService bookmarkService;

    @InjectMocks
    private BookmarkController bookmarkController;



    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookmarkController).build();
        // Use lenient stubbing
        Mockito.lenient().when(bookmarkService.addBookmark(Mockito.any(BookmarkModel.class)))
                .thenReturn(new BookmarkModel());  // Or your expected return object
    }

    @Test
    public void testAddBookmark_ValidData() throws Exception {
        BookmarkModel bookmark = new BookmarkModel();
        bookmark.setUserName("sagar");
        bookmark.setAge(30);  // Valid age
        bookmark.setBp(120);
        bookmark.setCholesterol(180);
        bookmark.setDiabetic("Yes");
        bookmark.setSmoking_status("No");  // Match smoking_status (snake_case)
        bookmark.setPain_type("Chest Pain");  // Match pain_type (snake_case)
        bookmark.setTreatment("Medication");
        bookmark.setGender("Male");
        // When the bookmark is added, return this bookmark
        when(bookmarkService.addBookmark(Mockito.any(BookmarkModel.class))).thenReturn(bookmark);

        // Perform the POST request with the valid bookmark data
        mockMvc.perform(post("/bookmark/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userName\":\"sagar\", \"age\":30, \"bp\":120, \"cholesterol\":180, \"diabetic\":\"Yes\", \"smoking_status\":\"No\", \"pain_type\":\"Chest Pain\", \"treatment\":\"Medication\",\"gender\":\"Male\"}"))
                .andExpect(status().isCreated())  // Expect HTTP status 201 (Created)
                .andExpect(jsonPath("$.age").value(30))  // Check that age is returned correctly
                .andExpect(jsonPath("$.smoking_status").value("No"))  // Check if smoking_status is returned correctly
                .andExpect(jsonPath("$.pain_type").value("Chest Pain"));  // Check if pain_type is returned correctly
    }


    @Test
    public void testGetBookmarkByUserId() throws Exception {
        String userName = "sagar";
        
        // Create bookmark models with age set to 30 for validation
        BookmarkModel bookmark1 = new BookmarkModel();
        bookmark1.setAge(30);  // Set age for the first bookmark
        
        BookmarkModel bookmark2 = new BookmarkModel();
        bookmark2.setAge(30);  // Set age for the second bookmark
        
        // Create a list of bookmarks
        List<BookmarkModel> bookmarks = List.of(bookmark1, bookmark2);

        // Mock the service call to return the bookmarks list when getBookmarkByUserId is called
        when(bookmarkService.getBookmarkByUserName(userName)).thenReturn(bookmarks);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/bookmark/user/{userId}", userName))
                .andExpect(status().isOk())  // Ensure HTTP 200 status
                .andExpect(jsonPath("$").isArray())  // Ensure the response is an array
                .andExpect(jsonPath("$[0].age").value(30))  // Verify the age of the first bookmark
                .andExpect(jsonPath("$[1].age").value(30));  // Verify the age of the second bookmark
    }


    @Test
    public void testUpdateBookmark() throws Exception {
        Long bookmarkId = 1L;
        String userName = "sagar"; 
        BookmarkModel bookmark = new BookmarkModel();
        bookmark.setAge(35);

        
        when(bookmarkService.updateBookmark(eq(bookmarkId), eq(userName), any(BookmarkModel.class))).thenReturn(bookmark);

        // Perform the PUT request with bookmarkId and userId in the URL and a valid JSON body
        mockMvc.perform(put("/bookmark/update/{bookmarkId}/{userId}", bookmarkId, userName)
                .contentType("application/json")
                .content("{\"age\":35, \"bp\":130, \"cholesterol\":200, \"diabetic\":\"No\", \"smokingStatus\":\"Yes\", \"painType\":\"Heartburn\", \"treatment\":\"Diet Change\"}"))
                .andExpect(status().isOk())  // Expect HTTP status 200 OK
                .andExpect(jsonPath("$.age").value(35));  // Expect the age field to be updated to 35
    }

    @Test
    public void deleteBookmarkControllerTest() throws Exception {
        Long bookmarkId = 1L;
        String userName = "sagar";

        when(bookmarkService.deleteBookmarkById(bookmarkId, userName)).thenReturn(true);

        mockMvc.perform(delete("/bookmark/delete/{bookmarkId}/{userId}", bookmarkId, userName)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bookmark deleted successfully."));
    }


    
    @Test
    public void testGetBookmarkByUserId_NoBookmarksFound() throws Exception {
        String userName = "nonExistingUser";

        when(bookmarkService.getBookmarkByUserName(userName)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookmark/user/{userName}", userName))
                .andExpect(status().isNotFound())  // Expect HTTP status 404 (Not Found)
                .andExpect(content().string("No bookmarks found for user with ID: " + userName));
    }

    @Test
    public void testUpdateBookmark_BookmarkNotFound() throws Exception {
        Long bookmarkId = 999L;
        String userName = "validUser";
        BookmarkModel bookmark = new BookmarkModel();
        bookmark.setAge(35);
        bookmark.setBp(130);
        bookmark.setCholesterol(200);
        bookmark.setDiabetic("No");
        bookmark.setSmoking_status("Yes");
        bookmark.setPain_type("Heartburn");
        bookmark.setTreatment("Diet Change");

        // Ensure mock object is created with the exact data you will be using
        BookmarkModel mockBookmark = new BookmarkModel();
        mockBookmark.setAge(35);
        mockBookmark.setBp(130);
        mockBookmark.setCholesterol(200);
        mockBookmark.setDiabetic("No");
        mockBookmark.setSmoking_status("Yes");
        mockBookmark.setPain_type("Heartburn");
        mockBookmark.setTreatment("Diet Change");

        // Stubbing the method to return null when the Bookmark is not found
        when(bookmarkService.updateBookmark(eq(bookmarkId), eq(userName), any(BookmarkModel.class)))
            .thenReturn(null);

        // Perform the PUT request to the controller
        mockMvc.perform(put("/bookmark/update/{bookmarkId}/{userName}", bookmarkId, userName)
                    .contentType("application/json")
                    .content("{\"age\":35, \"bp\":130, \"cholesterol\":200, \"diabetic\":\"No\", \"smokingStatus\":\"Yes\", \"painType\":\"Heartburn\", \"treatment\":\"Diet Change\"}"))
                .andExpect(status().isNotFound())  // Expect HTTP status 404 (Not Found)
                .andExpect(content().string("Bookmark with ID " + bookmarkId + " not found or user not authorized."));
    }



    @Test
    public void testDeleteBookmark_BookmarkNotFound() throws Exception {
        Long bookmarkId = 999L;
        String userName = "validUser";

        // Simulate the case where the bookmark doesn't exist in the system
        when(bookmarkService.deleteBookmarkById(bookmarkId, userName)).thenReturn(false);

        mockMvc.perform(delete("/bookmark/delete/{bookmarkId}/{userName}", bookmarkId, userName))
                .andExpect(status().isNotFound())  // Expect HTTP status 404 (Not Found)
                .andExpect(content().string("Bookmark not found or user not authorized."));
    }
 

}
