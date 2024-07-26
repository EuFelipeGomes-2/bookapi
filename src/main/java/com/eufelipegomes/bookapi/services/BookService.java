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
  private final BookRepository bookRepository;
  private final UserService userService;

  public BookService(BookRepository bookRepository, UserService userService) {
    this.bookRepository = bookRepository;
    this.userService = userService;
  }

  public List<BookModel> getBooks() {
    try {
      return bookRepository.findAll();
    } catch (Exception e) {
      throw new CustomException(e.getMessage());
    }
  }

  public List<BookModel> getBooksByUser(UUID userId) throws Exception {
    Boolean userExists = userService.verifyUserById(userId);
    if (userExists) {
      Optional<UserModel> userOptional = userService.getUserById(userId);
      if (userOptional.isPresent()) {
        UserModel user = userOptional.get();
        return bookRepository.findBookByUser(user);
      } else {
        throw new CustomException("User not found.");
      }
    } else {
      throw new CustomException("User not found.");
    }
  }

  public BookModel createBook(UUID userId, BookModel book) {
    try {
      Boolean userExists = userService.verifyUserById(userId);
      if (userExists) {
        Optional<UserModel> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
          UserModel user = userOptional.get();
          book.setUser(user);
          return bookRepository.save(book);
        }
      }
      throw new CustomException("User Not Found");
    } catch (Exception e) {
      throw new CustomException("Error while creating the book: " + e.getMessage());
    }
  }

  public BookModel editBookInfo(UUID bookId, UpdateBookDTO newBookInfo) throws Exception {
    try {
      Optional<BookModel> bookOptional = bookRepository.findById(bookId);
      if (bookOptional.isEmpty()) {
        throw new CustomException("Book Not Found with the id: " + bookId);
      } else {
        BookModel bookModel = bookOptional.get();
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
        if (newBookInfo.total_pages() != null ){
          bookModel.setTotal_pages(newBookInfo.total_pages());
        }
        if(newBookInfo.current_page() != null){
          bookModel.setCurrent_page(newBookInfo.current_page());
        }
        if(newBookInfo.publisher() != null){
          bookModel.setPublisher(newBookInfo.publisher());
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

  public void deleteBook(UUID bookId, UUID userId) throws Exception {
    try {
      Boolean bookExists = bookRepository.existsById(bookId);
      if (!bookExists) {
        throw new CustomException("Book Not Found with the id: " + bookId);
      } else {
        bookRepository.deleteById(bookId);
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public Optional<BookModel> getBookById(UUID bookId) {
    return bookRepository.findById(bookId);
  }
}
