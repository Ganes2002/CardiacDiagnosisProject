package com.itc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itc.model.BookmarkModel;

public interface BookmarkRepository extends JpaRepository<BookmarkModel ,Long>{
List<BookmarkModel>findByUserName(String userName);
}
