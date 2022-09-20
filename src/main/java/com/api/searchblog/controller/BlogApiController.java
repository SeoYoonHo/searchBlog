package com.api.searchblog.controller;

import com.api.searchblog.dto.BlogDTO;
import com.api.searchblog.dto.PopularKeywordResponseDTO;
import com.api.searchblog.dto.ResponseDTO;
import com.api.searchblog.service.BlogApiService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("ALL")
@RestController
@RequiredArgsConstructor
public class BlogApiController {

    private final BlogApiService blogApiService;

    @ApiOperation(value = "블로그 검색", notes = "open api를 활용한 블로그 검색")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    @PostMapping("api/v1/search/blog")
    public ResponseDTO getBlog(@RequestBody BlogDTO.BlogRequestDTO requestDTO) {

        return blogApiService.findBlogMyKeyword(requestDTO);
    }

    @ApiOperation(value = "상위 키워드 검색", notes = "상위 키워드 검색")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    @GetMapping("api/v1/search/popularKeyword")
    public ResponseDTO<PopularKeywordResponseDTO> getKeyword() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("count")
                                                      .descending()
                                                      .and(Sort.by("keyword")
                                                               .ascending()));

        PopularKeywordResponseDTO result = blogApiService.findPopularKeyword(pageable);

        return ResponseDTO.of("001", "Success", result);
    }

}
