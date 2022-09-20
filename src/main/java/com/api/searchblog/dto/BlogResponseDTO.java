package com.api.searchblog.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

public class BlogResponseDTO {

    @Data
    public static class KakaoBlogResponseDTO{
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

    @Data
    public static class NaverResponseDTO{
        private Date lastBuildDate;
        private int total;
        private int start;
        private int display;
        private List<DocumentItem> items;

        @Data
        static class DocumentItem {
            public String title;
            public String link;
            public String description;
            public String bloggername;
            public String bloggerlink;
            public String postdate;
        }
    }

}
