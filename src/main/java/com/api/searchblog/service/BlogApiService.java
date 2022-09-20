package com.api.searchblog.service;

import com.api.searchblog.api.BlogApiClient;
import com.api.searchblog.config.SortStatus;
import com.api.searchblog.domain.Keyword;
import com.api.searchblog.dto.BlogResponseDTO;
import com.api.searchblog.dto.PopularKeywordResponseDTO;
import com.api.searchblog.dto.ResponseDTO;
import com.api.searchblog.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

@SuppressWarnings("rawtypes")
@Service
@RequiredArgsConstructor
public class BlogApiService {
    private final BlogApiClient blogApiClient;

    private final KeywordRepository keywordRepository;

    @Transactional
    public ResponseDTO findBlogMyKeyword(String query, String sort, int page, int size) {
        Keyword kw = keywordRepository.findByKeyword(query);
        if (kw == null) {
            kw = new Keyword();
            kw.setKeyword(query);
            kw.setCount(1);
            keywordRepository.save(kw);
        } else {
            keywordRepository.findByKeyword(query)
                             .increaseCount();
        }

        ResponseDTO result;
        try {
            BlogResponseDTO.KakaoBlogResponseDTO blogResponseDTO = blogApiClient.findBlogByKakao(query, sort, page,
                    size);

            result = ResponseDTO.of("001", "Success", blogResponseDTO);
        } catch (RestClientException e) {
            BlogResponseDTO.NaverResponseDTO blogResponseDTO = blogApiClient.findBlogByNaver(query,
                    SortStatus.valueOf(sort)
                              .getNaver()
                    , page, size);

            result = ResponseDTO.of("001", "Success", blogResponseDTO);
        }


        return result;
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
