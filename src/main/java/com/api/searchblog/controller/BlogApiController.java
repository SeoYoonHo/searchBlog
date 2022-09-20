package com.api.searchblog.controller;

import com.api.searchblog.dto.PopularKeywordResponseDTO;
import com.api.searchblog.dto.ResponseDTO;
import com.api.searchblog.service.BlogApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlogApiController {

    private final BlogApiService blogApiService;

    @GetMapping("api/v1/search/blog")
    public ResponseDTO getBlog(@RequestParam String query,
                               @RequestParam(required = false) String sort,
                               @RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer size
    ) {

        return blogApiService.findBlogMyKeyword(query, sort, page, size);
    }

    @GetMapping("api/v1/search/popularKeyword")
    public ResponseDTO<PopularKeywordResponseDTO> getKeyword(
            @SortDefault.SortDefaults({@SortDefault(sort = "count", direction =
                    Sort.Direction.DESC), @SortDefault(sort = "query", direction = Sort.Direction.ASC)
            }) Pageable pageable) {

        PopularKeywordResponseDTO result = blogApiService.findPopularKeyword(pageable);

        return ResponseDTO.of("001", "Success", result);
    }

}
