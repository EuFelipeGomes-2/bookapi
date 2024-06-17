package com.eufelipegomes.bookapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eufelipegomes.bookapi.models.BookModel;
import com.eufelipegomes.bookapi.models.UserModel;

@Repository
public interface BookRepository extends JpaRepository<BookModel, UUID> {
  @Query("SELECT * FROM books INNER JOIN users ON books.userid = users.userid")
  List<BookModel> findBookByUser(UserModel user);

}
