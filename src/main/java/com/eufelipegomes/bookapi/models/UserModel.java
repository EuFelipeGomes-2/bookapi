package com.eufelipegomes.bookapi.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.eufelipegomes.bookapi.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class UserModel implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID userid;

  private String username;
  private String password;
  private String useremail;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonBackReference
  private List<BookModel> books;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonBackReference
  private List<NoteModel> notes;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonBackReference
  private Set<CollectionModel> collections = new HashSet<>();

  @Enumerated(EnumType.STRING)
  private UserRole role;

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public UserModel(String username, String useremail, String password) {
    this.username = username;
    this.useremail = useremail;
    this.password = password;
  }

  public UserModel() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUseremail() {
    return useremail;
  }

  public void setUseremail(String useremail) {
    this.useremail = useremail;
  }

  public UUID getUserid() {
    return userid;
  }

  public void setUserid(UUID userid) {
    this.userid = userid;
  }

  public List<BookModel> getBooks() {
    return books;
  }

  public void setBooks(List<BookModel> books) {
    this.books = books;
  }

  public List<NoteModel> getNotes() {
    return notes;
  }

  public void setNotes(List<NoteModel> notes) {
    this.notes = notes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (this.role == UserRole.ADMIN)
      return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
    else
      return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
