package com.eufelipegomes.bookapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eufelipegomes.bookapi.models.CollectionModel;
import com.eufelipegomes.bookapi.models.UserModel;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionModel, UUID> {
  @Query("SELECT * FROM collections INNER JOIN users ON collections.user_id = users.userid")
  List<CollectionModel> findByUser(UserModel user);
}
