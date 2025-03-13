package com.itc.service;

import java.util.List;

import com.itc.model.BookmarkModel;

public interface BookmarkService {
	BookmarkModel addBookmark(BookmarkModel bookmark);
	BookmarkModel findBookmarkById(Long id);
	
	boolean deleteBookmarkById(Long id,String userName);
	BookmarkModel updateBookmark(Long id ,String userName ,BookmarkModel bookmark);
	List<BookmarkModel>getBookmarkByUserName(String userName);
	
	void deleteAllBookmarksByUserName(String userName);
	
	

}
