package com.howtodoinjava.demo.jackson;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Jackson2Demo {
	public static void main(String[] args) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();

		Article article = new Article(1L, "Test Title", Collections.singletonList("Test Tag"));

		String json = mapper.writeValueAsString(article);
		
		System.out.println(json);

		json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(article);

		System.out.println(json);

		json = "{\"id\":1,\"title\":\"Test Title\",\"tags\":[\"Test Tag\"]}";

		Article newArticle = mapper.readValue(json, Article.class);

		System.out.println(newArticle);
	}
}

class Article {
	private Long id;
	private String title;
	private List<String> tags;

	public Article() {
		super();
	}

	public Article(Long id, String title, List<String> tags) {
		super();
		this.id = id;
		this.title = title;
		this.tags = tags;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", tags=" + tags + "]";
	}
}
