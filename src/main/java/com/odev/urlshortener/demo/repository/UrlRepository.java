package com.odev.urlshortener.demo.repository;

import com.odev.urlshortener.demo.entity.UrlEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    Optional<UrlEntity> findByShortCode(String shortCode);
    List<UrlEntity> findByDeletedFalseOrderByClickCountDesc(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE UrlEntity u SET u.clickCount = u.clickCount + 1 WHERE u.shortCode = :shortCode")
    void incrementClickCount(@Param("shortCode") String shortCode);
}
