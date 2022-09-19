package com.api.searchblog.api;

import com.api.searchblog.dto.BlogResponseDTO;
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

    private final String KakaoUrl_getMovies = "https://dapi.kakao.com/v2/search/blog?query={keyword}&sort={sort}&page={page}&size={size}";

    public BlogResponseDTO requestBlog(String sort, int page, int size, String keyword) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK 57ae58e24e2aa14b1cc93f2ffadc1ddd");

        final HttpEntity<String> entity = new HttpEntity<>(headers);

        log.info("url: " + KakaoUrl_getMovies);
        return restTemplate.exchange(KakaoUrl_getMovies, HttpMethod.GET, entity, BlogResponseDTO.class, keyword, sort, page, size).getBody();
    }


}
