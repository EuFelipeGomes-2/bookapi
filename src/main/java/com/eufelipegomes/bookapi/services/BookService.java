package com.eufelipegomes.bookapi.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.eufelipegomes.bookapi.dtos.UpdateBookDTO;
import com.eufelipegomes.bookapi.exceptions.CustomException;
import com.eufelipegomes.bookapi.models.BookModel;
import com.eufelipegomes.bookapi.models.UserModel;
import com.eufelipegomes.bookapi.repositories.BookRepository;

@Service
public class BookService {
  private BookRepository bookRepository;

  private UserService userService;

  public BookService(BookRepository bookRepository, UserService userService) {
    this.bookRepository = bookRepository;
    this.userService = userService;
  }

  public List<BookModel> getBooks() {
    try {
      List<BookModel> books = bookRepository.findAll();

      return books;
    } catch (Exception e) {
      throw new CustomException(e.getMessage());
    }
  }

  public List<BookModel> getBooksByUser(UUID userId) throws Exception {
    Boolean userExists = userService.verifyUserById(userId);
    if (userExists) {
      Optional<UserModel> userOptional = userService.getUserById(userId);
      UserModel user = userOptional.get();
      List<BookModel> booksByUser = bookRepository.findBookByUser(user);
      return booksByUser;
    } else {
      throw new CustomException("User not found.");
    }
  }

  public BookModel createBook(UUID userId, BookModel book) {
    try {
      // Verifica se o usuário existe
      Boolean userExists = userService.verifyUserById(userId);
      if (userExists) {
        // Obtém o usuário pelo ID
        Optional<UserModel> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
          UserModel user = userOptional.get();
          // Define o usuário para o livro
          book.setUser(user);
          // Salva o livro
          return bookRepository.save(book);
        }
      }
      throw new CustomException("User Not Found");
    } catch (Exception e) {
      throw new CustomException("Error while creating the book: " + e.getMessage());
    }
  }

  public BookModel editBookInfo(UUID bookid, UpdateBookDTO newBookInfo) throws Exception {
    try {
      Optional<BookModel> book = bookRepository.findById(bookid);

      if (book.isEmpty()) {
        throw new CustomException("Book Not Found with the id: " + bookid);
      } else {
        BookModel bookModel = book.get();

        if (newBookInfo.bookname() != null) {
          bookModel.setBookname(newBookInfo.bookname());
        }

        if (newBookInfo.bookauthor() != null) {
          bookModel.setBookauthor(newBookInfo.bookauthor());
        }

        if (newBookInfo.description() != null) {
          bookModel.setDescription(newBookInfo.description());
        }

        if (newBookInfo.bookstatus() != null) {
          bookModel.setBookstatus(newBookInfo.bookstatus());
        }

        if (newBookInfo.completed() != null) {
          bookModel.setCompleted(newBookInfo.completed());
        }

        if (newBookInfo.rating() != null) {
          bookModel.setRating(newBookInfo.rating());
        }

        return bookRepository.save(bookModel);
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public void deleteBook(UUID bookid) throws Exception {
    try {
      Boolean bookExists = bookRepository.existsById(bookid);

      if (bookExists == false) {
        throw new CustomException("Book Not Found with the id: " + bookid);
      } else {
        bookRepository.deleteById(bookid);
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

}
