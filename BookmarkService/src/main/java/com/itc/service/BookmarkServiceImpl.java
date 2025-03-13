package com.itc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itc.model.BookmarkModel;
import com.itc.repository.BookmarkRepository;

@Service
public class BookmarkServiceImpl implements BookmarkService {
	
	@Autowired
	private BookmarkRepository bookmarkRepository;
	@Override
	public BookmarkModel addBookmark(BookmarkModel bookmark) {
		return bookmarkRepository.save(bookmark);
	}
	
//	public List<BookmarkModel> getBookmarkByUserId(Long userId){
//		return bookmarkRepository.findById(userId);
//	}
	@Override
	public List<BookmarkModel>getBookmarkByUserName(String userName){
		return bookmarkRepository.findByUserName(userName);
	}
	@Override
	public BookmarkModel findBookmarkById(Long id) {
		Optional<BookmarkModel> opt = bookmarkRepository.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}
		return null;
	}
	
//	@Override
//	public boolean deleteBookmarkById(Long id) {
//		BookmarkModel bok = findBookmarkById(id);
//		if(bok!=null) {
//		bookmarkRepository.delete(bok);
//		return true;
//		}
//		return false;
//	}
	@Override
	public boolean deleteBookmarkById(Long id, String userName) {
	    Optional<BookmarkModel> bookmark = bookmarkRepository.findById(id);
	    if (bookmark.isPresent() && bookmark.get().getUserName().equals(userName)) {
	        bookmarkRepository.delete(bookmark.get());
	        return true;
	    }
	    return false;
	}

	@Override
	public BookmarkModel updateBookmark(Long id,String userName, BookmarkModel newBookmark) {
		BookmarkModel oldBookmark = findBookmarkById(newBookmark.getBookmarkId());
		if(oldBookmark!=null&& oldBookmark.getUserName().equals(userName)) {
			oldBookmark.setUserName(newBookmark.getUserName());
			oldBookmark.setAge(newBookmark.getAge());
			oldBookmark.setBp(newBookmark.getBp());
			oldBookmark.setCholesterol(newBookmark.getCholesterol());
			oldBookmark.setDiabetic(newBookmark.getDiabetic());
			oldBookmark.setSmoking_status(newBookmark.getSmoking_status());
			oldBookmark.setPain_type(newBookmark.getPain_type());
			oldBookmark.setTreatment(newBookmark.getTreatment());
			oldBookmark.setGender(newBookmark.getGender());
			return bookmarkRepository.save(oldBookmark);
		}
		BookmarkModel bok = bookmarkRepository.save(oldBookmark);
		return bok;
	}
	
	@Override
	public void deleteAllBookmarksByUserName(String userName) {
	    List<BookmarkModel> bookmarks = bookmarkRepository.findByUserName(userName);
	    if (bookmarks != null && !bookmarks.isEmpty()) {
	        bookmarkRepository.deleteAll(bookmarks);
	    }
	}





}
