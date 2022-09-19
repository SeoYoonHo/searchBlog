package com.api.searchblog.controller;

import com.api.searchblog.dto.BlogResponseDTO;
import com.api.searchblog.service.BlogApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlogApiController {

    private final BlogApiService blogApiService;

    @GetMapping("api/v1/search/blog")
    public BlogResponseDTO get(@RequestParam(required = false) String sort,
                               @RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer size,
                               @RequestParam(required = false) String keyword) {

        return blogApiService.findBlogMyKeyword(sort, page, size, keyword);
    }

}
