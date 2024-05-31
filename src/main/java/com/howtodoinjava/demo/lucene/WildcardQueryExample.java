package com.howtodoinjava.demo.lucene;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.uhighlight.UnifiedHighlighter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class WildcardQueryExample {

  //This contains the lucene indexed documents
  private static final String INDEX_DIR = "c:/temp/lucene/indexedFiles";

  private static String searchTerm_1 = "prefer*";
  private static String searchTerm_2 = "prefer??d";

  public static void main(String[] args) throws Exception {
    //Get directory reference
    Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));

    //Index reader - an interface for accessing a point-in-time view of a lucene index
    IndexReader reader = DirectoryReader.open(dir);

    //Create lucene searcher. It search over a single IndexReader.
    IndexSearcher searcher = new IndexSearcher(reader);

    //analyzer with the default stop words
    Analyzer analyzer = new StandardAnalyzer();


    /**
     * Wildcard "*" Example
     * */

    //Create wildcard query
    Query query = new WildcardQuery(new Term("contents", searchTerm_1));

    //Search the lucene documents
    TopDocs hits = searcher.search(query, 10, Sort.INDEXORDER);

    System.out.println("Search terms found in :: " + hits.totalHits + " files");

    UnifiedHighlighter highlighter = new UnifiedHighlighter(searcher, analyzer);
    String[] fragments = highlighter.highlight("contents", query, hits);

    for (String f : fragments) {
      System.out.println(f);
    }

    /**
     * Wildcard "?" Example
     * */

    //Create wildcard query
    query = new WildcardQuery(new Term("contents", searchTerm_2));

    //Search the lucene documents
    hits = searcher.search(query, 10, Sort.INDEXORDER);

    System.out.println("Search terms found in :: " + hits.totalHits + " files");

    highlighter = new UnifiedHighlighter(searcher, analyzer);
    fragments = highlighter.highlight("contents", query, hits);

    for (String f : fragments) {
      System.out.println(f);
    }

    dir.close();
  }
}
