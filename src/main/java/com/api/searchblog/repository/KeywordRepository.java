package com.api.searchblog.repository;

import com.api.searchblog.domain.Keyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Keyword findByKeyword(String keyword);

    Page<Keyword> findAll(Pageable pageable);

}
