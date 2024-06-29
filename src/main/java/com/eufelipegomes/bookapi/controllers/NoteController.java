package com.eufelipegomes.bookapi.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eufelipegomes.bookapi.dtos.BookNoteDTO;
import com.eufelipegomes.bookapi.dtos.NoteDTO;
import com.eufelipegomes.bookapi.dtos.NotesDTO;
import com.eufelipegomes.bookapi.exceptions.CustomException;
import com.eufelipegomes.bookapi.models.NoteModel;
import com.eufelipegomes.bookapi.services.NoteService;

@Controller
@RequestMapping("/books/notes")
public class NoteController {
  private NoteService noteService;

  public NoteController(NoteService noteService) {
    this.noteService = noteService;
  }

  @GetMapping("/book/{bookid}")
  public ResponseEntity<NotesDTO> getNotesByBookId(@PathVariable("bookid") UUID bookid) {
    try {
      List<NoteModel> notes = noteService.getNotesByBookId(bookid);

      List<NoteDTO> noteDTOs = notes.stream().map(note -> new NoteDTO(
          note.getNoteid(),
          note.getUser().getUserid(),
          note.getBook().getBookid(),
          note.getContent(),
          note.getCreated_time(),
          note.getUpdated_at())).collect(Collectors.toList());

      NotesDTO notesResponse = new NotesDTO(noteDTOs);

      return ResponseEntity.status(HttpStatus.OK).body(notesResponse);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PostMapping("/user/{userId}/book/{bookId}")
  public ResponseEntity<NoteDTO> createNote(
      @PathVariable("userId") UUID userId,
      @PathVariable("bookId") UUID bookId,
      @RequestBody BookNoteDTO noteDTO) {
    try {
      NoteModel savedNote = noteService.createNote(userId, bookId, noteDTO);

      NoteDTO response = new NoteDTO(
          savedNote.getNoteid(),
          savedNote.getBook().getBookid(),
          savedNote.getUser().getUserid(),
          savedNote.getContent(),
          savedNote.getCreated_time(),
          savedNote.getUpdated_at());

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PutMapping("/note/{noteId}")
  public ResponseEntity<NoteDTO> updateNoteContent(@PathVariable("noteId") UUID noteId,
      @RequestBody BookNoteDTO newNoteContent) {
    try {
      NoteModel noteUpdated = noteService.editNoteInfo(noteId, newNoteContent);

      NoteDTO response = new NoteDTO(
          noteUpdated.getNoteid(),
          noteUpdated.getBook().getBookid(),
          noteUpdated.getUser().getUserid(),
          noteUpdated.getContent(),
          noteUpdated.getCreated_time(),
          noteUpdated.getUpdated_at());

      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @DeleteMapping("/note/{noteId}")
  public ResponseEntity<String> deleteNoteInfo(@PathVariable("noteId") UUID noteId) {
    try {
      noteService.deleteNote(noteId);

      return ResponseEntity.status(HttpStatus.OK).body("Note Deleted Sucess.");
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}
