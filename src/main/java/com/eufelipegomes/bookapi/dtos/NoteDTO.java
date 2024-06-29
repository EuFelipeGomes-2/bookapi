package com.eufelipegomes.bookapi.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record NoteDTO(UUID noteId, UUID userId, UUID bookId, String content, LocalDateTime created_at,
        LocalDateTime updated_at) {

}
