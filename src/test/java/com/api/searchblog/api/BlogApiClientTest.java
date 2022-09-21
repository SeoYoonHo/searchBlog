package com.api.searchblog.api;

import com.api.searchblog.dto.BlogDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApiClientTest {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void 카카오조회() {
        //given
        BlogDTO.BlogRequestDTO requestDTO = new BlogDTO.BlogRequestDTO();
        requestDTO.setQuery("테스트");
        requestDTO.setSize(10);
        requestDTO.setSort("accuracy");
        requestDTO.setSize(1);

        BlogApiClient blogApiClient = new BlogApiClient(restTemplate);

        //when
        BlogDTO.KakaoBlogResponseDTO responseDTO = blogApiClient.findBlogByKakao(requestDTO);

        //then
        assertNotNull(responseDTO);
    }

    @Test
    public void 네이버조회() {
        //given
        BlogDTO.BlogRequestDTO requestDTO = new BlogDTO.BlogRequestDTO();
        requestDTO.setQuery("테스트");
        requestDTO.setSize(10);
        requestDTO.setSort("accuracy");
        requestDTO.setSize(1);

        BlogApiClient blogApiClient = new BlogApiClient(restTemplate);

        //when
        BlogDTO.NaverResponseDTO responseDTO = blogApiClient.findBlogByNaver(requestDTO);

        //then
        assertNotNull(responseDTO);
    }
}