package com.eufelipegomes.bookapi.dtos;

import lombok.NonNull;

public record UpdateBookDTO(String bookname,

    String bookauthor,
    String bookstatus,
    String description,
    Boolean completed,
    Float rating) {

}
