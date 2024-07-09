package com.eufelipegomes.bookapi.controllers;

import com.eufelipegomes.bookapi.dtos.CreateCollectionDTO;
import com.eufelipegomes.bookapi.exceptions.CustomException;
import com.eufelipegomes.bookapi.models.BookModel;
import com.eufelipegomes.bookapi.models.CollectionModel;
import com.eufelipegomes.bookapi.services.CollectionService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/collections")
public class CollectionController {
  private CollectionService collectionService;

  public CollectionController(CollectionService collectionService) {
    this.collectionService = collectionService;
  }

  @PostMapping("/{uid}")
  public ResponseEntity<CollectionModel> createCollection(@PathVariable("uid") UUID userid,
      @RequestBody CreateCollectionDTO createCollectionDTO) {
    try {
      CollectionModel collection = collectionService.createCollection(userid,
          createCollectionDTO.name());
      return ResponseEntity.status(HttpStatus.CREATED).body(collection);
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PostMapping("/collection/{collectionId}/book/{bookId}")
  public ResponseEntity<String> addBookToCollection(@PathVariable("collectionId") UUID collectionId,
      @PathVariable("bookId") UUID bookId) {
    try {
      collectionService.addBookToCollection(collectionId, bookId);

      return ResponseEntity.status(HttpStatus.OK).body("Book added to collection");
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping("/{uid}")
  public ResponseEntity<List<CollectionModel>> getUserCollections(@PathVariable("uid") UUID userId) {
    try {
      List<CollectionModel> collections = collectionService.getUserCollections(userId);
      return ResponseEntity.status(HttpStatus.OK).body(collections);

    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping("/collection/{collectionId}/books")
  public ResponseEntity<Set<BookModel>> getCollectionBooks(@PathVariable("collectionId") UUID collectionId) {
    try {
      Set<BookModel> books = collectionService.getCollectionBooks(collectionId);
      return ResponseEntity.status(HttpStatus.OK).body(books);

    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @DeleteMapping("/collection/{collectionId}/books/{bookId}")
  public ResponseEntity<Void> removeBookFromCollection(@PathVariable("collectionId") UUID collectionId,
      @PathVariable("bookId") UUID bookId) {
    try {
      collectionService.removeBookFromCollection(collectionId, bookId);
      return ResponseEntity.noContent().build();
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @DeleteMapping("/collection/{collectionid}")
  public ResponseEntity<Void> deleteCollection(@PathVariable("collectionid") UUID collectionid) {
    collectionService.deleteCollection(collectionid);
    return ResponseEntity.noContent().build();
  }

}
