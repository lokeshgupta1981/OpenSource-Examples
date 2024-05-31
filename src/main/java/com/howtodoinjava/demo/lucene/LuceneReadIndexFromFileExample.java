package com.howtodoinjava.demo.lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneReadIndexFromFileExample {
  //directory contains the lucene indexes
  private static final String INDEX_DIR = "c:/temp/lucene/indexedFiles";
  private static String textToSearch = "agreeable";

  public static void main(String[] args) throws Exception {
    //Create lucene searcher. It searches over a single IndexReader.
    IndexSearcher searcher = createSearcher();

    //Search indexed contents using search term
    TopDocs foundDocs = searchInContent(textToSearch, searcher);

    //Total found documents
    System.out.println("Total Results :: " + foundDocs.totalHits);

    //Let's print out the path of files which have searched term
    for (ScoreDoc sd : foundDocs.scoreDocs) {
      Document d = searcher.doc(sd.doc);
      System.out.println("Path : " + d.get("path") + ", Score : " + sd.score);
    }
  }

  private static TopDocs searchInContent(String textToFind, IndexSearcher searcher) throws Exception {
    //Create search query
    QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
    Query query = qp.parse(textToFind);

    //search the index
    TopDocs hits = searcher.search(query, 10);
    return hits;
  }

  private static IndexSearcher createSearcher() throws IOException {
    Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));

    //It is an interface for accessing a point-in-time view of a lucene index
    IndexReader reader = DirectoryReader.open(dir);

    //Index searcher
    IndexSearcher searcher = new IndexSearcher(reader);
    return searcher;
  }
}
