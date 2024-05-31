package com.howtodoinjava.demo.lucene;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.uhighlight.UnifiedHighlighter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneUnifiedHighlighterExample {

  //This contains the lucene indexed documents
  private static final String INDEX_DIR = "c:/temp/lucene/indexedFiles";
  private static String search_query = "Society";

  public static void main(String[] args) throws Exception {
    //Get directory reference
    Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));

    //Index reader - an interface for accessing a point-in-time view of a lucene index
    IndexReader reader = DirectoryReader.open(dir);

    //Create lucene searcher. It searches over a single IndexReader.
    IndexSearcher searcher = new IndexSearcher(reader);

    //analyzer with the default stop words
    Analyzer analyzer = new StandardAnalyzer();

    //Query parser to be used for creating TermQuery
    QueryParser qp = new QueryParser("contents", analyzer);

    //Create the query
    Query query = qp.parse(search_query);

    //Search the lucene documents
    TopDocs hits = searcher.search(query, 10, Sort.INDEXORDER);

    System.out.println("Search terms found in :: " + hits.totalHits + " files");

    UnifiedHighlighter highlighter = new UnifiedHighlighter(searcher, analyzer);
    String[] fragments = highlighter.highlight("contents", query, hits);

    for (String f : fragments) {
      System.out.println(f);
    }

    //To get which fragment belong to which doc/file

    for (int i = 0; i < hits.scoreDocs.length; i++)
        {
      int docid = hits.scoreDocs[i].doc;
            Document doc = searcher.doc(docid);

            String filePath = doc.get("path");
            System.out.println(filePath);
            System.out.println(fragments[i]);
        }

    dir.close();
  }
}
