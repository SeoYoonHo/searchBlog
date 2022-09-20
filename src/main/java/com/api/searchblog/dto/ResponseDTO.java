package com.api.searchblog.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class ResponseDTO<D> {
    private final String resultCode;
    private final String message;
    private final D data;
}
