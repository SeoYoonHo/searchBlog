package com.api.searchblog.controller;

import com.api.searchblog.dto.BlogDTO;
import com.api.searchblog.dto.PopularKeywordResponseDTO;
import com.api.searchblog.dto.ResponseDTO;
import com.api.searchblog.service.BlogApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BlogApiController {

    @Qualifier("kakaoBlogApiServiceImpl")
    private final BlogApiService blogApiServiceImpl;

    @ApiOperation(value = "블로그 검색", notes = "open api를 활용한 블로그 검색")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "query", value = "검색 키워드", example = "테스트", required = true),
            @ApiImplicitParam(name = "sort", value = "정렬 방식(accuracy, recency)", example = "accuracy"),
            @ApiImplicitParam(name = "page", value = "페이지", example = "1"),
            @ApiImplicitParam(name = "size", value = "페이지 크기", example = "10")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "API 정상 작동"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    @GetMapping("api/v1/search/blog")
    public ResponseDTO<BlogDTO.BlogResponseDTO> getBlog(@Valid @ModelAttribute BlogDTO.BlogRequestDTO requestDTO) {
        BlogDTO.BlogResponseDTO blogResponseDTO = blogApiServiceImpl.findBlogMyKeyword(requestDTO);
        return ResponseDTO.of("001", "Success", blogResponseDTO);
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

        PopularKeywordResponseDTO result = blogApiServiceImpl.findPopularKeyword(pageable);

        return ResponseDTO.of("001", "Success", result);
    }

}
