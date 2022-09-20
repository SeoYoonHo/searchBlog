package com.api.searchblog.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.api.searchblog.api.BlogApiClient;
import com.api.searchblog.dto.BlogDTO;
import com.api.searchblog.dto.PopularKeywordResponseDTO;
import com.api.searchblog.dto.ResponseDTO;
import com.api.searchblog.repository.KeywordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BlogApiServiceTest {

    @Autowired
    BlogApiService blogApiService;
    @Autowired
    KeywordRepository keywordRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void 조회카운트증가() {
        //given
        String keyword = "테스트";
        BlogDTO.BlogRequestDTO requestDTO =
                BlogDTO.BlogRequestDTO.builder()
                                      .query(keyword)
                                      .size(10)
                                      .sort("accuracy")
                                      .page(1)
                                      .build();

        int prevCnt = keywordRepository.findByKeyword(keyword) == null ? 0 :
                keywordRepository.findByKeyword(keyword)
                                 .getCount();

        //when
        blogApiService.findBlogMyKeyword(requestDTO);

        //then
        assertEquals("카운트 증가", prevCnt + 1, keywordRepository.findByKeyword(keyword)
                                                             .getCount());
    }

    @Test
    public void 카카오실패시네이버호출() {
        //given
        BlogDTO.BlogRequestDTO requestDTO =
                BlogDTO.BlogRequestDTO.builder()
                                      .query("테스트")
                                      .size(10)
                                      .sort("accuracy")
                                      .page(1)
                                      .build();

        BlogApiClient blogApiClient = new BlogApiClient(restTemplate);

        ResponseDTO result;

        //when
        try {
            BlogDTO.KakaoBlogResponseDTO blogResponseDTO = blogApiClient.findBlogByKakao(requestDTO);

            result = ResponseDTO.of("001", "Success", blogResponseDTO);

            throw new RestClientException("dddd");
        } catch (RestClientException e) {
            BlogDTO.NaverResponseDTO blogResponseDTO = blogApiClient.findBlogByNaver(requestDTO);

            result = ResponseDTO.of("001", "Success", blogResponseDTO);
        }

        //then
        assertTrue(result.getData() instanceof BlogDTO.NaverResponseDTO);
    }


    @Test
    public void 인기검색어조회() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("count")
                                                      .descending()
                                                      .and(Sort.by("keyword")
                                                               .ascending()));

        for (int i = 0; i < 20; i++) {
            String keyword = "테스트" + i;
            BlogDTO.BlogRequestDTO requestDTO =
                    BlogDTO.BlogRequestDTO.builder()
                                          .query(keyword)
                                          .size(10)
                                          .sort("accuracy")
                                          .page(1)
                                          .build();

            blogApiService.findBlogMyKeyword(requestDTO);
        }

        //when
        PopularKeywordResponseDTO responseDTO = blogApiService.findPopularKeyword(pageable);

        //then
        assertTrue(responseDTO.getKeywords().size() > 9);
    }
}