package com.api.searchblog.repository;

import com.api.searchblog.domain.Keyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

//    Optional<Keyword> findByKeyword(String keyword);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select k from Keyword k where k.keyword = :keyword")
    Optional<Keyword> findByKeywordForUdate(@Param("keyword") String keyword);

    Page<Keyword> findAll(Pageable pageable);

}
