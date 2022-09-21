package com.api.searchblog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

public class BlogDTO {

    @Data
    @NoArgsConstructor
    public static class BlogRequestDTO {
        @NotEmpty(message = "키워드를 입력해주세요")
        private String query;
        @Nullable
        private String sort = "accuracy";
        @Nullable
        private Integer page = 1;
        @Nullable
        private Integer size = 10;
    }

    @Data
    public static class BlogResponseDTO {
        private int total;
        private int page;
        private int size;
        private List<BlogItem> items;

        @Data
        @Builder
        public static class BlogItem{
            private String title;
            private String contents;
            private String url;
            private String blogname;
            private String thumbnail;
            private String postdate;
            private String bloggerlink;
        }
    }


    @Data
    @ApiIgnore
    public static class KakaoBlogResponseDTO {
        private MetaItem meta;
        private List<DocumentItem> documents;

        @Data
        @ApiIgnore
        public static class MetaItem {
            private int total_count;
            private int pageable_count;
            private boolean is_end;
        }

        @Data
        public static class DocumentItem {
            private String title;
            private String contents;
            private String url;
            private String blogname;
            private String thumbnail;
            private Date datetime;
        }
    }

    @Data
    public static class NaverResponseDTO {
        private Date lastBuildDate;
        private int total;
        private int start;
        private int display;
        private List<DocumentItem> items;

        @Data
        public static class DocumentItem {
            private String title;
            private String link;
            private String description;
            private String bloggername;
            private String bloggerlink;
            private String postdate;
        }
    }

}
