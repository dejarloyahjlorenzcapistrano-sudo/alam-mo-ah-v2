package com.alammoah.service;

import com.alammoah.dto.BookmarkDto;
import com.alammoah.entity.Bookmark;
import com.alammoah.entity.User;
import com.alammoah.repository.BookmarkRepository;
import com.alammoah.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    public List<BookmarkDto.Response> getUserBookmarks(String username) {
        User user = getUser(username);
        return bookmarkRepository.findByUserIdOrderByBookmarkedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookmarkDto.Response addBookmark(String username, BookmarkDto.CreateRequest request) {
        User user = getUser(username);

        if (bookmarkRepository.existsByUserIdAndEntryId(user.getId(), request.getEntryId())) {
            throw new RuntimeException("Already bookmarked");
        }

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .entryId(request.getEntryId())
                .languageId(request.getLanguageId())
                .languageName(request.getLanguageName())
                .languageColor(request.getLanguageColor())
                .entryTitle(request.getEntryTitle())
                .entryDescription(request.getEntryDescription())
                .categoryTitle(request.getCategoryTitle())
                .build();

        return toResponse(bookmarkRepository.save(bookmark));
    }

    @Transactional
    public void removeBookmark(String username, String entryId) {
        User user = getUser(username);
        if (!bookmarkRepository.existsByUserIdAndEntryId(user.getId(), entryId)) {
            throw new RuntimeException("Bookmark not found");
        }
        bookmarkRepository.deleteByUserIdAndEntryId(user.getId(), entryId);
    }

    public BookmarkDto.StatusResponse checkBookmark(String username, String entryId) {
        User user = getUser(username);
        boolean bookmarked = bookmarkRepository.existsByUserIdAndEntryId(user.getId(), entryId);
        return BookmarkDto.StatusResponse.builder()
                .bookmarked(bookmarked)
                .entryId(entryId)
                .build();
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    private BookmarkDto.Response toResponse(Bookmark b) {
        return BookmarkDto.Response.builder()
                .id(b.getId())
                .entryId(b.getEntryId())
                .languageId(b.getLanguageId())
                .languageName(b.getLanguageName())
                .languageColor(b.getLanguageColor())
                .entryTitle(b.getEntryTitle())
                .entryDescription(b.getEntryDescription())
                .categoryTitle(b.getCategoryTitle())
                .bookmarkedAt(b.getBookmarkedAt())
                .build();
    }
}
