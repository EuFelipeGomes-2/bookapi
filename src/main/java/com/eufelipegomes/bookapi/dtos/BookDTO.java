package com.eufelipegomes.bookapi.dtos;

import java.util.UUID;

public record BookDTO(UUID bookid, UUID userId, String bookname, String bookauthor, String bookstatus,
    String description, Boolean completed, Float rating) {

}
