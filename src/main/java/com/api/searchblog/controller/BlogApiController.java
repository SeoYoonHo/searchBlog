package com.api.searchblog.controller;

import com.api.searchblog.dto.BlogResponseDTO;
import com.api.searchblog.dto.PopularKeywordResponseDTO;
import com.api.searchblog.service.BlogApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogApiController {

    private final BlogApiService blogApiService;

    @GetMapping("api/v1/search/blog")
    public BlogResponseDTO getBlog(@RequestParam(required = false) String sort,
                                   @RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer size,
                                   @RequestParam(required = false) String keyword) {

        return blogApiService.findBlogMyKeyword(sort, page, size, keyword);
    }

    @GetMapping("api/v1/search/popularKeyword")
    public PopularKeywordResponseDTO getKeyword(
            @PageableDefault(size = 10) @SortDefault.SortDefaults({@SortDefault(sort = "count", direction = Sort.Direction.DESC), @SortDefault(sort = "keyword", direction = Sort.Direction.ASC)
            }) Pageable pageable) {
        return blogApiService.findPopularKeyword(pageable);
    }

}
