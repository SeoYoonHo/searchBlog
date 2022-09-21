package com.api.searchblog.serviceImpl;

import com.api.searchblog.api.BlogApiClient;
import com.api.searchblog.domain.Keyword;
import com.api.searchblog.dto.BlogDTO;
import com.api.searchblog.dto.PopularKeywordResponseDTO;
import com.api.searchblog.repository.KeywordRepository;
import com.api.searchblog.service.BlogApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
@Service(value = "kakaoBlogApiServiceImpl")
@RequiredArgsConstructor
@Transactional
public class KakaoBlogApiServiceImpl implements BlogApiService {
    private final BlogApiClient blogApiClient;

    private final KeywordRepository keywordRepository;

    @Override
    public BlogDTO.BlogResponseDTO findBlogMyKeyword(BlogDTO.BlogRequestDTO requestDTO) {
        BlogDTO.BlogResponseDTO result;
        try {
            BlogDTO.KakaoBlogResponseDTO kakaoBlogResponseDTO = blogApiClient.findBlogByKakao(requestDTO);
            BlogDTO.BlogResponseDTO blogResponseDTO = new BlogDTO.BlogResponseDTO();
            blogResponseDTO.setTotal(kakaoBlogResponseDTO.getMeta()
                                                         .getPageable_count());
            blogResponseDTO.setSize(requestDTO.getSize());
            blogResponseDTO.setPage(requestDTO.getPage());
            SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
            List<BlogDTO.BlogResponseDTO.BlogItem> itemList = kakaoBlogResponseDTO.getDocuments()
                                                                                  .stream()
                                                                                  .map(documentItem -> BlogDTO.BlogResponseDTO.BlogItem.builder()
                                                                                                                                       .title(documentItem.getTitle())
                                                                                                                                       .contents(
                                                                                                                                               documentItem.getContents())
                                                                                                                                       .url(documentItem.getUrl())
                                                                                                                                       .blogname(
                                                                                                                                               documentItem.getBlogname())
                                                                                                                                       .thumbnail(
                                                                                                                                               documentItem.getThumbnail())
                                                                                                                                       .postdate(
                                                                                                                                               fm.format(
                                                                                                                                                       documentItem.getDatetime()))
                                                                                                                                       .bloggerlink(
                                                                                                                                               "")
                                                                                                                                       .build())
                                                                                  .collect(Collectors.toList());
            blogResponseDTO.setItems(itemList);

            result = blogResponseDTO;
        } catch (RestClientException e) {
            BlogDTO.NaverResponseDTO naverBlogResponseDTO = blogApiClient.findBlogByNaver(requestDTO);

            BlogDTO.BlogResponseDTO blogResponseDTO = new BlogDTO.BlogResponseDTO();
            blogResponseDTO.setTotal(naverBlogResponseDTO.getTotal());
            blogResponseDTO.setSize(naverBlogResponseDTO.getDisplay());
            blogResponseDTO.setPage(naverBlogResponseDTO.getStart());
            List<BlogDTO.BlogResponseDTO.BlogItem> itemList = naverBlogResponseDTO.getItems()
                                                                                  .stream()
                                                                                  .map(item -> BlogDTO.BlogResponseDTO.BlogItem.builder()
                                                                                                                               .title(item.getTitle())
                                                                                                                               .contents(
                                                                                                                                       item.getDescription())
                                                                                                                               .url(item.getLink())
                                                                                                                               .blogname(
                                                                                                                                       item.getBloggername())
                                                                                                                               .thumbnail(
                                                                                                                                       "")
                                                                                                                               .postdate(
                                                                                                                                       item.getPostdate())
                                                                                                                               .bloggerlink(
                                                                                                                                       item.getBloggerlink())
                                                                                                                               .build())
                                                                                  .collect(Collectors.toList());
            blogResponseDTO.setItems(itemList);

            result = blogResponseDTO;
        }

        Keyword kw = keywordRepository.findByKeywordForUdate(requestDTO.getQuery())
                                      .orElseGet(() -> {
                                          Keyword k = new Keyword();
                                          k.setKeyword(requestDTO.getQuery());
                                          k.setCount(0);
                                          return k;
                                      });
        kw.increaseCount();
        keywordRepository.save(kw);

        return result;
    }

    @Override
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
