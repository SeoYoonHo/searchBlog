package com.api.searchblog.service;

import com.api.searchblog.api.BlogApiClient;
import com.api.searchblog.domain.Keyword;
import com.api.searchblog.dto.BlogResponseDTO;
import com.api.searchblog.dto.PopularKeywordResponseDTO;
import com.api.searchblog.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogApiService {
    private final BlogApiClient blogApiClient;

    private final KeywordRepository keywordRepository;

    @Transactional
    public BlogResponseDTO findBlogMyKeyword(String sort, int page, int size, String keyword) {
        Keyword kw = keywordRepository.findByKeyword(keyword);
        if (kw == null) {
            kw = new Keyword();
            kw.setKeyword(keyword);
            kw.setCount(1);
            keywordRepository.save(kw);
        } else {
            keywordRepository.findByKeyword(keyword)
                             .increaseCount();
        }

        return blogApiClient.requestBlog(sort, page, size, keyword);
    }

    public PopularKeywordResponseDTO findPopularKeyword(Pageable pageable) {
        Page<Keyword> keywordList = keywordRepository.findAll(pageable);


        Page<PopularKeywordResponseDTO.Item> kwList =
                keywordList.map(keyword -> PopularKeywordResponseDTO.Item.builder()
                                                                         .keyword(keyword.getKeyword())
                                                                         .count(keyword.getCount())
                                                                         .build());
        PopularKeywordResponseDTO kwDTO = new PopularKeywordResponseDTO();
        kwDTO.setKeywords(kwList.getContent());
        return kwDTO;
    }

}
