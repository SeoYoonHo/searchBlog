package com.api.searchblog.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BlogResponseDTO {
    private MetaItem meta;
    private List<DocumentItem> documents;

    @Data
    static class MetaItem {
        public int total_count;
        public int pageable_count;
        public boolean is_end;
    }

    @Data
    static class DocumentItem {
        public String title;
        public String contents;
        public String url;
        public String blogname;
        public String thumbnail;
        public Date datetime;
    }
}
