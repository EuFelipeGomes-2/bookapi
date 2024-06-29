package com.eufelipegomes.bookapi.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eufelipegomes.bookapi.dtos.BookNoteDTO;
import com.eufelipegomes.bookapi.dtos.NoteDTO;
import com.eufelipegomes.bookapi.exceptions.CustomException;
import com.eufelipegomes.bookapi.models.BookModel;
import com.eufelipegomes.bookapi.models.NoteModel;
import com.eufelipegomes.bookapi.models.UserModel;
import com.eufelipegomes.bookapi.repositories.BookRepository;
import com.eufelipegomes.bookapi.repositories.NoteRepository;
import com.eufelipegomes.bookapi.repositories.UserRepository;

@Service
public class NoteService {
  private NoteRepository noteRepository;
  private BookRepository bookRepository;
  private UserRepository userRepository;

  public NoteService(NoteRepository noteRepository, BookRepository bookRepository, UserRepository userRepository) {
    this.noteRepository = noteRepository;
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
  }

  public List<NoteModel> getNotesByBookId(UUID bookid) {
    Optional<BookModel> bookOptional = bookRepository.findById(bookid);

    if (bookOptional.isEmpty()) {
      throw new CustomException("Book Not Found with the id: " + bookid);
    } else {
      BookModel book = bookOptional.get();
      List<NoteModel> notes = noteRepository.findNotesByBook(book);
      return notes;
    }
  }

  public NoteModel createNote(UUID userId, UUID bookId, BookNoteDTO noteInfo) {
    Optional<BookModel> bookOptional = bookRepository.findById(bookId);
    if (bookOptional.isEmpty()) {
      throw new CustomException("Book not found with the ID: " + bookId);
    }

    Optional<UserModel> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty()) {
      throw new CustomException("User not found with the ID: " + userId);
    }

    NoteModel note = new NoteModel();
    note.setBook(bookOptional.get());
    note.setUser(userOptional.get());
    note.setContent(noteInfo.content());

    return noteRepository.save(note);
  }

  public NoteModel editNoteInfo(UUID noteId, BookNoteDTO newNoteContent) throws Exception {
    try {
      Optional<NoteModel> note = noteRepository.findById(noteId);

      if (note.isEmpty()) {
        throw new CustomException("Note not find with the id:" + noteId);
      } else {
        NoteModel noteModel = note.get();

        if (newNoteContent.content() != null) {
          noteModel.setContent(newNoteContent.content());

        }

        return noteRepository.save(noteModel);
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public void deleteNote(UUID noteId) throws Exception {
    try {
      Boolean noteExists = noteRepository.existsById(noteId);

      if (noteExists == false) {
        throw new CustomException("Note Not Found with the id: " + noteId);
      } else {
        noteRepository.deleteById(noteId);
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

}
