package com.eufelipegomes.bookapi.dtos;

public record CreateBookDto(
    String bookname,

    String bookauthor,
    String bookstatus,
    String description,
    Boolean completed,
    Float rating) {
}
