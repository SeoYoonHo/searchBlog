package com.api.searchblog.service;

import com.api.searchblog.api.BlogApiClient;
import com.api.searchblog.domain.Keyword;
import com.api.searchblog.dto.BlogDTO;
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
@Transactional
public class BlogApiService {
    private final BlogApiClient blogApiClient;

    private final KeywordRepository keywordRepository;

    public ResponseDTO findBlogMyKeyword(BlogDTO.BlogRequestDTO requestDTO) {
        Keyword kw = keywordRepository.findByKeyword(requestDTO.getQuery());
        if (kw == null) {
            kw = new Keyword();
            kw.setKeyword(requestDTO.getQuery());
            kw.setCount(1);
            keywordRepository.save(kw);
        } else {
            keywordRepository.findByKeyword(requestDTO.getQuery())
                             .increaseCount();
        }

        ResponseDTO result;
        try {
            BlogDTO.KakaoBlogResponseDTO blogResponseDTO = blogApiClient.findBlogByKakao(requestDTO);

            result = ResponseDTO.of("001", "Success", blogResponseDTO);
        } catch (RestClientException e) {
            BlogDTO.NaverResponseDTO blogResponseDTO = blogApiClient.findBlogByNaver(requestDTO);

            result = ResponseDTO.of("001", "Success", blogResponseDTO);
        }


        return result;
    }

    @Transactional(readOnly = true)
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
