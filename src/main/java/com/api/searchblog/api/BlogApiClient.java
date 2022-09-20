package com.api.searchblog.api;

import com.api.searchblog.config.SortStatus;
import com.api.searchblog.dto.BlogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
@Slf4j
public class BlogApiClient {
    private final RestTemplate restTemplate;

    public BlogDTO.KakaoBlogResponseDTO findBlogByKakao(BlogDTO.BlogRequestDTO requestDTO) {
        String kakaoUrl_getMovies = "https://dapi.kakao.com/v2/search/blog?query={query}&sort={sort}&page={page}&size={size}";

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK 57ae58e24e2aa14b1cc93f2ffadc1ddd");

        final HttpEntity<String> entity = new HttpEntity<>(headers);


        log.info("url: " + kakaoUrl_getMovies);
        return restTemplate.exchange(kakaoUrl_getMovies, HttpMethod.GET, entity,
                                   BlogDTO.KakaoBlogResponseDTO.class, requestDTO.getQuery(), SortStatus.valueOf(requestDTO.getSort())
                                                                                                        .getKakao(),
                                   requestDTO.getPage(), requestDTO.getSize())
                           .getBody();
    }

    public BlogDTO.NaverResponseDTO findBlogByNaver(BlogDTO.BlogRequestDTO requestDTO) {
        String kakaoUrl_getMovies = "https://openapi.naver.com/v1/search/blog" +
                ".json?query={query}&display={display}&start={start}&sort={sort}";

        final HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", "ITTmJylOV7iT4EAQgfY7");
        headers.set("X-Naver-Client-Secret", "U9bnHNKyT8");

        final HttpEntity<String> entity = new HttpEntity<>(headers);


        log.info("url: " + kakaoUrl_getMovies);
        return restTemplate.exchange(kakaoUrl_getMovies, HttpMethod.GET, entity, BlogDTO.NaverResponseDTO.class,
                                   requestDTO.getQuery(), requestDTO.getSize(),
                                   requestDTO.getPage(), SortStatus.valueOf(requestDTO.getSort())
                                                                   .getNaver())
                           .getBody();
    }


}
