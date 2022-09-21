package com.api.searchblog.service;

import com.api.searchblog.dto.BlogDTO;
import com.api.searchblog.dto.PopularKeywordResponseDTO;
import com.api.searchblog.dto.ResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface BlogApiService {
    ResponseDTO findBlogMyKeyword(BlogDTO.BlogRequestDTO requestDTO);

    @Transactional(readOnly = true)
    PopularKeywordResponseDTO findPopularKeyword(Pageable pageable);
}
