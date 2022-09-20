package com.api.searchblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SearchBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchBlogApplication.class, args);
	}

}
