package com.eufelipegomes.bookapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eufelipegomes.bookapi.dtos.UpdateBookDTO;
import com.eufelipegomes.bookapi.exceptions.CustomException;
import com.eufelipegomes.bookapi.models.BookModel;
import com.eufelipegomes.bookapi.models.UserModel;
import com.eufelipegomes.bookapi.repositories.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("Should create a book")
    void testCreateBook() throws Exception {
        UUID userId = UUID.randomUUID();
        UserModel userModel = new UserModel("Felipe", "0felipegomessilva@gmail.com", "felipe1234");
        userModel.setUserid(userId);

        BookModel bookModel = new BookModel("Antifragil", "nassim taleb", "reading", 850, 25, "publish inc", "description", false, (float) 7.5);

        // Mock the userService methods
        when(userService.verifyUserById(userId)).thenReturn(true);
        when(userService.getUserById(userId)).thenReturn(Optional.of(userModel));

        // Mock the bookRepository save method
        when(bookRepository.save(any(BookModel.class))).thenReturn(bookModel);

        // Call the createBook method
        BookModel createdBook = bookService.createBook(userId, bookModel);

        // Validate the result
        assertEquals(bookModel.getBookname(), createdBook.getBookname());
        assertEquals(userModel, createdBook.getUser());

        // Verify the interactions
        verify(userService).verifyUserById(userId);
        verify(userService).getUserById(userId);
        verify(bookRepository).save(bookModel);
    }

    @Test
    @DisplayName("Should throw CustomException when user is not found for createBook")
    void testCustomExceptionForCreateBook() throws Exception {
        UUID userId = UUID.randomUUID();
        UserModel userModel = new UserModel("Felipe", "0felipegomessilva@gmail.com", "felipe1234");
        userModel.setUserid(userId);

        BookModel bookModel = new BookModel("Antifragil", "nassim taleb", "reading", 850, 25, "publish inc", "description", false, (float) 7.5);

        when(userService.verifyUserById(userId)).thenReturn(false);

        CustomException thrown = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            bookService.createBook(userId, bookModel);
        });

        org.junit.jupiter.api.Assertions.assertEquals("User Not Found", thrown.getMessage());
    }

    @Test
    @DisplayName("Should delete book")
    void testDeleteBook() throws Exception {
        UUID userId = UUID.randomUUID();
        UserModel userModel = new UserModel("Felipe", "0felipegomessilva@gmail.com", "felipe1234");
        userModel.setUserid(userId);

        UUID bookId = UUID.randomUUID();
        BookModel bookModel = new BookModel("Antifragil", "nassim taleb", "reading", 850, 25, "publish inc", "description", false, (float) 7.5);
        bookModel.setBookid(bookId);

        when(bookRepository.existsById(bookId)).thenReturn(true);

        bookService.deleteBook(bookId, userModel.getUserid());

        verify(bookRepository).deleteById(bookId);
        verify(bookRepository).existsById(bookId);
    }

    @Test
    @DisplayName("Should throw CustomException when book is not found for deleteBook")
    void testDeleteBookException() {
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        BookModel bookModel = new BookModel("Antifragil", "nassim taleb", "reading", 850, 25, "publish inc", "description", false, (float) 7.5);
        bookModel.setBookid(bookId);

        when(bookRepository.existsById(bookId)).thenReturn(false);

        CustomException thrown = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            bookService.deleteBook(bookId, userId);
        });

        org.junit.jupiter.api.Assertions.assertEquals("Book Not Found with the id: " + bookId, thrown.getMessage());
    }

    @Test
    @DisplayName("Should edit book information")
    void testEditBookInfo() throws Exception {
        UUID bookId = UUID.randomUUID();
        BookModel bookModel = new BookModel("Antifragil", "nassim taleb", "reading", 850, 25, "publish inc", "description", false, (float) 7.5);

        UpdateBookDTO newBookInfo = new UpdateBookDTO(
            "Codigo limpo",
            "bob marley",
            "Completed",
            550,
            550,
            "publicher att",
            "description updated",
            true,
            (float) 5.5
        );

        // Mock the findById method to return the book
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookModel));

        // Mock the save method to return the updated book
        when(bookRepository.save(any(BookModel.class))).thenReturn(bookModel);

        // Call the editBookInfo method
        BookModel updatedBook = bookService.editBookInfo(bookId, newBookInfo);

        // Validate the updated fields
        assertEquals(newBookInfo.bookname(), updatedBook.getBookname());
        assertEquals(newBookInfo.bookauthor(), updatedBook.getBookauthor());
        assertEquals(newBookInfo.description(), updatedBook.getDescription());
        assertEquals(newBookInfo.bookstatus(), updatedBook.getBookstatus());
        assertEquals(newBookInfo.total_pages(), updatedBook.getTotal_pages());
        assertEquals(newBookInfo.current_page(), updatedBook.getCurrent_page());
        assertEquals(newBookInfo.publisher(), updatedBook.getPublisher());
        assertEquals(newBookInfo.completed(), updatedBook.getCompleted());
        assertEquals(newBookInfo.rating(), updatedBook.getRating());

        // Verify the interactions
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(bookModel);
    }

    @Test
    @DisplayName("Should throw CustomException when book is not found for editBookInfo")
    void testEditBookInfoBookNotFound() {
        UUID bookId = UUID.randomUUID();

        UpdateBookDTO newBookInfo = new UpdateBookDTO(
            "Codigo limpo",
            "bob marley",
            "Completed",
            550,
            550,
            "publicher att",
            "description updated",
            true,
            (float) 5.5
        );

        // Mock the findById method to return empty
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Assert that CustomException is thrown
        CustomException thrown = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            bookService.editBookInfo(bookId, newBookInfo);
        });

        // Verify the exception message
        org.junit.jupiter.api.Assertions.assertEquals("Book Not Found with the id: " + bookId, thrown.getMessage());

        // Verify the interactions
        verify(bookRepository).findById(bookId);
    }

    @Test
    @DisplayName("Should get a book by its ID")
    void testGetBookById() {
        UUID bookId = UUID.randomUUID();

        BookModel bookModel = new BookModel("Antifragil", "nassim taleb", "reading", 850, 25, "publish inc", "description", false, (float) 7.5);
        bookModel.setBookid(bookId);

        Optional<BookModel> bookOptional = Optional.of(bookModel);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookModel));

        bookService.getBookById(bookId);

        assertEquals(bookOptional.get(), bookModel);
    }

    @Test
    @DisplayName("Should get all books")
    void testGetBooks() {
        UUID bookId = UUID.randomUUID();

        BookModel bookModel = new BookModel("Antifragil", "nassim taleb", "reading", 850, 25, "publish inc", "description", false, (float) 7.5);
        bookModel.setBookid(bookId);

        List<BookModel> books = Arrays.asList(bookModel);

        when(bookRepository.findAll()).thenReturn(books);

        List<BookModel> booksRetrieved = bookService.getBooks();

        assertEquals(booksRetrieved.size(), 1);
    }

    @Test
    @DisplayName("Should get books by user ID")
    void testGetBooksByUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UserModel userModel = new UserModel("Felipe", "0felipegomessilva@gmail.com", "felipe1234");
        userModel.setUserid(userId);

        UUID bookId = UUID.randomUUID();
        BookModel bookModel = new BookModel("Antifragil", "nassim taleb", "reading", 850, 25, "publish inc", "description", false, (float) 7.5);
        bookModel.setBookid(bookId);

        List<BookModel> books = Arrays.asList(bookModel);

        bookModel.setUser(userModel);

        when(userService.verifyUserById(userId)).thenReturn(true);
        when(userService.getUserById(userId)).thenReturn(Optional.of(userModel));
        when(bookRepository.findBookByUser(userModel)).thenReturn(books);

        List<BookModel> booksByUser = bookService.getBooksByUser(userId);

        verify(userService).verifyUserById(userId);
        verify(userService).getUserById(userId);
        verify(bookRepository).findBookByUser(userModel);

        assertEquals(booksByUser.size(), 1);
        assertEquals(booksByUser.get(0).getUser(), userModel);
    }

    @Test
    @DisplayName("Should throw CustomException when user is not found for getBooksByUser")
    void testGetBooksByUserUserNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        // Mock the userService methods
        when(userService.verifyUserById(userId)).thenReturn(false);

        // Assert that CustomException is thrown
        CustomException thrown = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            bookService.getBooksByUser(userId);
        });

        // Verify the exception message
        org.junit.jupiter.api.Assertions.assertEquals("User not found.", thrown.getMessage());

        // Verify the interactions
        verify(userService).verifyUserById(userId);
    }
}
