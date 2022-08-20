package com.howtodoinjava.demo.jackson;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

public class Jackson2Demo {
	public static void main(final String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		Article article = new Article(1L, "Test Title", Collections.singletonList("Test Tag"));

		String json = mapper.writeValueAsString(article);

		System.out.println(json);

		json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(article);

		System.out.println(json);

		json = "{\"id\":1,\"title\":\"Test Title\",\"tags\":[\"Test Tag\"]}";

		Article newArticle = mapper.readValue(json, Article.class);

		System.out.println(newArticle);

		URL url = Jackson2Demo.class.getClassLoader().getResource("article.json");

		newArticle = mapper.readValue(new File(url.getFile()), Article.class);

		System.out.println(newArticle);
	}
}

@JacksonXmlRootElement(localName = "article")
class Article {
	@JacksonXmlProperty(isAttribute = true)
	private Long id;
	private String title;

	@JacksonXmlElementWrapper(localName = "tags")
	@JacksonXmlProperty(localName = "tag")
	private List<String> tags;

	public Article() {
		super();
	}

	public Article(final Long id, final String title, final List<String> tags) {
		super();
		this.id = id;
		this.title = title;
		this.tags = tags;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(final List<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", tags=" + tags + "]";
	}
}
