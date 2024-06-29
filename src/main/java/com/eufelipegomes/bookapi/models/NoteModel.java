package com.eufelipegomes.bookapi.models;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "notes")
public class NoteModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID noteid;

  @ManyToOne
  @JoinColumn(name = "userid", nullable = false)
  private UserModel user;

  @ManyToOne
  @JoinColumn(name = "bookid", nullable = false)
  @JsonBackReference
  private BookModel book;

  private String content;
  private LocalDateTime created_time;
  private LocalDateTime updated_at;

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    this.created_time = now;
    this.updated_at = now;
  }

  @PreUpdate
  protected void onUpdate() {
    this.updated_at = LocalDateTime.now();
  }

  public NoteModel() {
  }

  public UserModel getUser() {
    return user;
  }

  public void setUser(UserModel user) {
    this.user = user;
  }

  public BookModel getBook() {
    return book;
  }

  public void setBook(BookModel book) {
    this.book = book;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getCreated_time() {
    return created_time;
  }

  public void setCreated_time(LocalDateTime created_time) {
    this.created_time = created_time;
  }

  public LocalDateTime getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(LocalDateTime updated_at) {
    this.updated_at = updated_at;
  }

  public UUID getNoteid() {
    return noteid;
  }

  public void setNoteid(UUID noteid) {
    this.noteid = noteid;
  }
}
