package com.eufelipegomes.bookapi.dtos;

import java.util.List;
import java.util.UUID;

public record UserBooksDTO(UUID userId, List<BookDTO> books) {
}
