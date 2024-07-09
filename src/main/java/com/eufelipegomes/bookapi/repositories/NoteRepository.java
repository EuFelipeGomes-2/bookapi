package com.eufelipegomes.bookapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eufelipegomes.bookapi.models.BookModel;
import com.eufelipegomes.bookapi.models.NoteModel;

@Repository
public interface NoteRepository extends JpaRepository<NoteModel, UUID> {
  @Query("SELECT * FROM notes INNER JOIN books ON notes.bookid = books.bookid")
  List<NoteModel> findNotesByBook(BookModel book);
}
