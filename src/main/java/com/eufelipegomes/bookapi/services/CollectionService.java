package com.eufelipegomes.bookapi.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.eufelipegomes.bookapi.exceptions.CustomException;
import com.eufelipegomes.bookapi.models.BookModel;
import com.eufelipegomes.bookapi.models.CollectionModel;
import com.eufelipegomes.bookapi.models.UserModel;
import com.eufelipegomes.bookapi.repositories.CollectionRepository;

@Service
public class CollectionService {
  private CollectionRepository collectionRepository;
  private UserService userService;
  private BookService bookService;

  public CollectionService(CollectionRepository collectionRepository, UserService userService,
      BookService bookService) {
    this.collectionRepository = collectionRepository;
    this.userService = userService;
    this.bookService = bookService;
  }

  public CollectionModel createCollection(UUID userId, String name) {
    Optional<UserModel> user = userService.getUserById(userId);

    if (user.isEmpty()) {
      throw new CustomException("User Not Found.");
    } else {
      UserModel userModel = user.get();

      CollectionModel collection = new CollectionModel();
      collection.setUser(userModel);
      collection.setName(name);

      return collectionRepository.save(collection);
    }
  }

  public CollectionModel addBookToCollection(UUID collectionId, UUID bookId) {
    Optional<CollectionModel> collectionOptional = collectionRepository.findById(collectionId);

    Optional<BookModel> bookOptional = bookService.getBookById(bookId);

    if (collectionOptional.isPresent() && bookOptional.isPresent()) {
      CollectionModel collection = collectionOptional.get();
      BookModel book = bookOptional.get();

      collection.getCollectionBooks().add(book);
      return collectionRepository.save(collection);
    } else {
      throw new CustomException("An Error ocurred while book is add in collection.");
    }
  }

  public List<CollectionModel> getUserCollections(UUID userId) {
    Optional<UserModel> userOptional = userService.getUserById(userId);

    if (userOptional.isEmpty()) {
      throw new CustomException("User Not Found.");
    } else {
      UserModel user = userOptional.get();
      return collectionRepository.findByUser(user);
    }
  }

  public Set<BookModel> getCollectionBooks(UUID collectionId) {
    Optional<CollectionModel> collectionOptional = collectionRepository.findById(collectionId);

    return collectionOptional.map(CollectionModel::getCollectionBooks).orElse(null);
  }

  public void removeBookFromCollection(UUID collectionId, UUID bookId) {
    Optional<CollectionModel> collectionOptional = collectionRepository.findById(collectionId);

    if (collectionOptional.isPresent()) {
      CollectionModel collection = collectionOptional.get();
      collection.getCollectionBooks().removeIf(book -> book.getBookid().equals(bookId));
      collectionRepository.save(collection);
    }
  }

  public void deleteCollection(UUID collectionid) {
    collectionRepository.deleteById(collectionid);
  }
}
