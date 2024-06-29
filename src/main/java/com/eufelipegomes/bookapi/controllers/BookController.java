package com.eufelipegomes.bookapi.controllers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eufelipegomes.bookapi.dtos.BookDTO;
import com.eufelipegomes.bookapi.dtos.BooksDTO;
import com.eufelipegomes.bookapi.dtos.CreateBookDto;
import com.eufelipegomes.bookapi.dtos.UpdateBookDTO;
import com.eufelipegomes.bookapi.dtos.UserBooksDTO;
import com.eufelipegomes.bookapi.exceptions.CustomException;
import com.eufelipegomes.bookapi.models.BookModel;
import com.eufelipegomes.bookapi.models.UserModel;
import com.eufelipegomes.bookapi.services.BookService;

@Controller
@RequestMapping("/books")
public class BookController {
  private BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping("/")
  public ResponseEntity<BooksDTO> getBooks() {
    try {
      List<BookModel> books = bookService.getBooks();

      List<BookDTO> bookDTOs = books.stream().map(book -> new BookDTO(
          book.getBookid(),
          book.getUser().getUserid(),
          book.getBookname(),
          book.getBookauthor(),
          book.getBookstatus(),
          book.getDescription(),
          book.getCompleted(),
          book.getRating())).collect(Collectors.toList());

      BooksDTO booksResponse = new BooksDTO(bookDTOs);

      return ResponseEntity.status(HttpStatus.OK).body(booksResponse);
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping("/user/{uid}")
  public ResponseEntity<UserBooksDTO> getBooksByUser(@PathVariable("uid") UUID userId) {
    try {
      List<BookModel> booksByUser = bookService.getBooksByUser(userId);

      // Map BookModel list to BookDTO list
      List<BookDTO> bookDTOs = booksByUser.stream().map(book -> new BookDTO(
          book.getBookid(),
          book.getUser().getUserid(),
          book.getBookname(),
          book.getBookauthor(),
          book.getBookstatus(),
          book.getDescription(),
          book.getCompleted(),
          book.getRating())).collect(Collectors.toList());

      UserBooksDTO userBooksDTO = new UserBooksDTO(userId, bookDTOs);

      return ResponseEntity.status(HttpStatus.OK).body(userBooksDTO);
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PostMapping("/user/{uid}")
  public ResponseEntity<BookDTO> createBook(@PathVariable("uid") UUID userId, @RequestBody CreateBookDto book) {
    try {
      var bookModel = new BookModel(book.bookname(), book.bookauthor(), book.bookstatus(), book.description(),
          book.completed(), book.rating());
      BookModel createdBook = bookService.createBook(userId, bookModel);

      // Map BookModel to BookDTO
      BookDTO bookDTO = new BookDTO(
          createdBook.getBookid(),
          createdBook.getUser().getUserid(),
          createdBook.getBookname(),
          createdBook.getBookauthor(),
          createdBook.getBookstatus(),
          createdBook.getDescription(),
          createdBook.getCompleted(),
          createdBook.getRating());

      return ResponseEntity.status(HttpStatus.CREATED).body(bookDTO);
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PutMapping("/book/{uid}")
  public ResponseEntity<BookModel> updateBookInfo(@PathVariable("uid") UUID bookId,
      @RequestBody UpdateBookDTO newBookInfo) {
    try {
      BookModel updatedBook = bookService.editBookInfo(bookId, newBookInfo);

      return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @DeleteMapping("/book/{uid}")
  public ResponseEntity<String> deleteBookInfo(@PathVariable("uid") UUID bookId) {
    try {
      bookService.deleteBook(bookId);

      return ResponseEntity.status(HttpStatus.OK).body("Book Deleted Sucess.");
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

}
