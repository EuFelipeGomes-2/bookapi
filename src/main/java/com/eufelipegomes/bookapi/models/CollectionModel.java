package com.eufelipegomes.bookapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "collections")
public class CollectionModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID collection_id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonManagedReference
  private UserModel user;

  @ManyToMany
  @JoinTable(name = "collections_books", joinColumns = @JoinColumn(name = "collection_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
  @JsonManagedReference
  private Set<BookModel> books = new HashSet<>();

  @Column(nullable = false)
  private String name;

  public UUID getCollectionid() {
    return collection_id;
  }

  public void setCollectionid(UUID collectionid) {
    this.collection_id = collectionid;
  }

  public UserModel getUser() {
    return user;
  }

  public void setUser(UserModel user) {
    this.user = user;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<BookModel> getCollectionBooks() {
    return books;
  }

  public void setCollectionBooks(Set<BookModel> collectionBooks) {
    this.books = collectionBooks;
  }

}
