package com.api.searchblog.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortStatus {
    accuracy("accuracy", "sim"),
    recency("recency", "date");

    private final String kakao;
    private final String naver;
}
