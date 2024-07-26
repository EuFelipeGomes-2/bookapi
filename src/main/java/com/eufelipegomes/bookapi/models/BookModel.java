package com.eufelipegomes.bookapi.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class BookModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID bookid;

  @ManyToOne
  @JoinColumn(name = "userid", referencedColumnName = "userid", nullable = false)
  @JsonManagedReference
  private UserModel user;

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<NoteModel> bookNotes;

  @ManyToMany(mappedBy = "books")
  @JsonBackReference
  private Set<CollectionModel> collections = new HashSet<>();

  private String bookname;
  private String bookauthor;
  private String bookstatus;
  private Integer total_pages;
  private Integer current_page;
  private String publisher;
  
  private String description;
  private Boolean completed;
  private Float rating;

  public BookModel() {
  }

  public BookModel(String name, String author, String status,Integer total_pages, Integer current_page, String publisher, String description, Boolean completed, Float rating) {
    this.bookname = name;
    this.bookauthor = author;
    this.bookstatus = status;
    this.total_pages =total_pages;
    this.current_page = current_page;
    this.publisher = publisher;
    this.description = description;
    this.completed = completed;
    this.rating = rating;
  }

  public UUID getBookid() {
    return bookid;
  }

  public void setBookid(UUID bookid) {
    this.bookid = bookid;
  }

  public UserModel getUser() {
    return user;
  }

  public void setUser(UserModel user) {
    this.user = user;
  }

  public List<NoteModel> getBookNotes() {
    return bookNotes;
  }

  public void setBookNotes(List<NoteModel> bookNotes) {
    this.bookNotes = bookNotes;
  }

  public String getBookname() {
    return bookname;
  }

  public void setBookname(String bookname) {
    this.bookname = bookname;
  }

  public String getBookauthor() {
    return bookauthor;
  }

  public void setBookauthor(String bookauthor) {
    this.bookauthor = bookauthor;
  }

  public String getBookstatus() {
    return bookstatus;
  }

  public void setBookstatus(String bookstatus) {
    this.bookstatus = bookstatus;
  }
  public int getTotal_pages() {
    return total_pages;
  }
  
  public void setTotal_pages(Integer total_pages) {
    this.total_pages = total_pages;
  }
  
  public int getCurrent_page() {
    return current_page;
  }
  
  public void setCurrent_page(Integer current_page) {
    this.current_page = current_page;
  }
  
  public String getPublisher() {
    return publisher;
  }
  
  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  public Float getRating() {
    return rating;
  }

  public void setRating(Float rating) {
    this.rating = rating;
  }
}
