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
        BlogDTO.BlogRequestDTO requestDTO =
                BlogDTO.BlogRequestDTO.builder()
                                      .query("테스트")
                                      .size(10)
                                      .sort("accuracy")
                                      .page(1)
                                      .build();

        BlogApiClient blogApiClient = new BlogApiClient(restTemplate);

        //when
        BlogDTO.KakaoBlogResponseDTO responseDTO = blogApiClient.findBlogByKakao(requestDTO);

        //then
        assertNotNull(responseDTO);
    }

    @Test
    public void 네이버조회() {
        //given
        BlogDTO.BlogRequestDTO requestDTO =
                BlogDTO.BlogRequestDTO.builder()
                                      .query("테스트")
                                      .size(10)
                                      .sort("accuracy")
                                      .page(1)
                                      .build();

        BlogApiClient blogApiClient = new BlogApiClient(restTemplate);

        //when
        BlogDTO.NaverResponseDTO responseDTO = blogApiClient.findBlogByNaver(requestDTO);

        //then
        assertNotNull(responseDTO);
    }
}