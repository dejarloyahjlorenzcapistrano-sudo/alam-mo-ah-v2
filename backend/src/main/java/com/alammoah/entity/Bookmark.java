package com.alammoah.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "bookmarks",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "entry_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "entry_id", nullable = false, length = 100)
    private String entryId;

    @Column(name = "language_id", nullable = false, length = 50)
    private String languageId;

    @Column(name = "language_name", length = 50)
    private String languageName;

    @Column(name = "language_color", length = 20)
    private String languageColor;

    @Column(name = "entry_title", length = 100)
    private String entryTitle;

    @Column(name = "entry_description", length = 500)
    private String entryDescription;

    @Column(name = "category_title", length = 100)
    private String categoryTitle;

    @Column(name = "bookmarked_at")
    private LocalDateTime bookmarkedAt;

    @PrePersist
    protected void onCreate() {
        bookmarkedAt = LocalDateTime.now();
    }
}
