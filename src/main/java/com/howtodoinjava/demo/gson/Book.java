package com.howtodoinjava.demo.gson;

import java.util.List;
import java.util.Objects;

/**
 * A plain Java class that Gson reads and writes without any annotation.
 * Gson uses the fields, not the getters, so the getters and setters are
 * only for our own code.
 */
public class Book {

  private int id;
  private String title;
  private String author;
  private int pages;
  private List<String> tags;

  public Book() {
  }

  public Book(int id, String title, String author, int pages, List<String> tags) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.pages = pages;
    this.tags = tags;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public int getPages() {
    return pages;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Book other)) {
      return false;
    }
    return id == other.id && pages == other.pages && Objects.equals(title, other.title)
        && Objects.equals(author, other.author) && Objects.equals(tags, other.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, author, pages, tags);
  }

  @Override
  public String toString() {
    return "Book[id=" + id + ", title=" + title + ", author=" + author
        + ", pages=" + pages + ", tags=" + tags + "]";
  }
}
