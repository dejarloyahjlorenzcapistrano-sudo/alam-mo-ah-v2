package com.alammoah.repository;

import com.alammoah.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserIdOrderByBookmarkedAtDesc(Long userId);
    Optional<Bookmark> findByUserIdAndEntryId(Long userId, String entryId);
    boolean existsByUserIdAndEntryId(Long userId, String entryId);
    void deleteByUserIdAndEntryId(Long userId, String entryId);
    long countByUserId(Long userId);
}
