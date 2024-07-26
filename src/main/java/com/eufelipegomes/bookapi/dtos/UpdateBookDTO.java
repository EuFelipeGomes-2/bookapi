package com.eufelipegomes.bookapi.dtos;

public record UpdateBookDTO(String bookname,

        String bookauthor,
        String bookstatus,
        Integer total_pages,
        Integer current_page,
        String publisher,
        String description,
        Boolean completed,
        Float rating) {
                

}
