package com.api.searchblog.serviceImpl;

import com.api.searchblog.api.BlogApiClient;
import com.api.searchblog.domain.Keyword;
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class KakaoBlogApiServiceImplTest {

    @Autowired
    KakaoBlogApiServiceImpl kakaoBlogApiServiceImpl;
    @Autowired
    KeywordRepository keywordRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    @Transactional
    public void 조회카운트증가() {
        //given
        String keyword = "테스트";
        BlogDTO.BlogRequestDTO requestDTO = new BlogDTO.BlogRequestDTO();
        requestDTO.setQuery(keyword);
        requestDTO.setSize(10);
        requestDTO.setSort("accuracy");
        requestDTO.setSize(1);

        int prevCnt = keywordRepository.findByKeywordForUdate(keyword)
                                       .orElse(new Keyword())
                                       .getCount();

        //when
        kakaoBlogApiServiceImpl.findBlogMyKeyword(requestDTO);

        //then
        assertEquals("카운트 증가", prevCnt + 1, keywordRepository.findByKeywordForUdate(keyword)
                                                             .orElse(new Keyword())
                                                             .getCount());
    }

    @Test
    public void 카카오실패시네이버호출() {
        //given
        BlogDTO.BlogRequestDTO requestDTO = new BlogDTO.BlogRequestDTO();
        requestDTO.setQuery("테스트");
        requestDTO.setSize(10);
        requestDTO.setSort("accuracy");
        requestDTO.setSize(1);

        BlogApiClient blogApiClient = new BlogApiClient(restTemplate);

        ResponseDTO result;

        //when
        try {
            BlogDTO.KakaoBlogResponseDTO blogResponseDTO = blogApiClient.findBlogByKakao(requestDTO);
            result = ResponseDTO.of("001", "Success", blogResponseDTO);
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
            BlogDTO.BlogRequestDTO requestDTO = new BlogDTO.BlogRequestDTO();
            requestDTO.setQuery(keyword);
            requestDTO.setSize(10);
            requestDTO.setSort("accuracy");
            requestDTO.setSize(1);

            kakaoBlogApiServiceImpl.findBlogMyKeyword(requestDTO);
        }

        //when
        PopularKeywordResponseDTO responseDTO = kakaoBlogApiServiceImpl.findPopularKeyword(pageable);

        //then
        System.out.println(responseDTO.getKeywords());
        assertTrue(responseDTO.getKeywords()
                              .size() > 9);
    }

    @Test
    public void 동시성테스트() throws InterruptedException {
        //given
        ExecutorService service = Executors.newFixedThreadPool(10);
        AtomicInteger successCount = new AtomicInteger();

        String keyword = "테스트";

        Keyword kw = new Keyword();
        kw.setKeyword(keyword);
        kw.setCount(0);
        keywordRepository.save(kw);

        BlogDTO.BlogRequestDTO requestDTO = new BlogDTO.BlogRequestDTO();
        requestDTO.setQuery(kw.getKeyword());
        requestDTO.setSize(10);
        requestDTO.setSort("accuracy");
        requestDTO.setSize(1);

        //when
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            service.execute(() -> {
                try {
                    kakaoBlogApiServiceImpl.findBlogMyKeyword(requestDTO);
                    successCount.getAndIncrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                latch.countDown();
            });
        }

        latch.await();

        //then
        assertEquals("카운트 증가", 10, successCount.get());
    }
}