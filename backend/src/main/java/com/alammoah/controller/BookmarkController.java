package com.alammoah.controller;

import com.alammoah.dto.BookmarkDto;
import com.alammoah.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // GET all bookmarks for logged-in user
    @GetMapping
    public ResponseEntity<List<BookmarkDto.Response>> getBookmarks(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookmarkService.getUserBookmarks(userDetails.getUsername()));
    }

    // POST add a bookmark
    @PostMapping
    public ResponseEntity<?> addBookmark(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BookmarkDto.CreateRequest request) {
        try {
            BookmarkDto.Response response = bookmarkService.addBookmark(
                    userDetails.getUsername(), request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE remove a bookmark
    @DeleteMapping("/{entryId}")
    public ResponseEntity<?> removeBookmark(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String entryId) {
        try {
            bookmarkService.removeBookmark(userDetails.getUsername(), entryId);
            return ResponseEntity.ok(Map.of("message", "Bookmark removed"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET check if entry is bookmarked
    @GetMapping("/check/{entryId}")
    public ResponseEntity<BookmarkDto.StatusResponse> checkBookmark(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String entryId) {
        return ResponseEntity.ok(
                bookmarkService.checkBookmark(userDetails.getUsername(), entryId));
    }
}
