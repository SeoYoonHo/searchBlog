package com.api.searchblog.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PopularKeywordResponseDTO {
    private List<Item> keywords;

    @Data
    @Builder
    public static class Item {
        public String keyword;
        public int count;
    }
}
