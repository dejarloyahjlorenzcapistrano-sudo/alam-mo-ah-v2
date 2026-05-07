package com.alammoah.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

public class BookmarkDto {

    @Data
    public static class CreateRequest {
        @NotBlank private String entryId;
        @NotBlank private String languageId;
        private String languageName;
        private String languageColor;
        private String entryTitle;
        private String entryDescription;
        private String categoryTitle;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String entryId;
        private String languageId;
        private String languageName;
        private String languageColor;
        private String entryTitle;
        private String entryDescription;
        private String categoryTitle;
        private LocalDateTime bookmarkedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusResponse {
        private boolean bookmarked;
        private String entryId;
    }
}
