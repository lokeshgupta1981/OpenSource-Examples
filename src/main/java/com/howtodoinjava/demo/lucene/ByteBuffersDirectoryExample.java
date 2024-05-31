package com.howtodoinjava.demo.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;


public class ByteBuffersDirectoryExample {
  public static void main(String[] args) throws IOException {

    //Create ByteBuffersDirectory instance
    ByteBuffersDirectory byteBufferDir = new ByteBuffersDirectory();

    //Builds an analyzer with the default stop words
    Analyzer analyzer = new StandardAnalyzer();

    //Write some docs to ByteBuffersDirectory
    writeIndex(byteBufferDir, analyzer);

    //Search indexed docs in ByteBuffersDirectory
    searchIndex(byteBufferDir, analyzer);
  }

  static void writeIndex(ByteBuffersDirectory byteBufferDir, Analyzer analyzer) {
    try {
      // IndexWriter Configuration
      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
      iwc.setOpenMode(OpenMode.CREATE);

      //IndexWriter writes new index files to the directory
      IndexWriter writer = new IndexWriter(byteBufferDir, iwc);

      //Create some docs with name and content
      indexDoc(writer, "document-1", "hello world");
      indexDoc(writer, "document-2", "hello happy world");
      indexDoc(writer, "document-3", "hello happy world");
      indexDoc(writer, "document-4", "hello hello world");

      //don't forget to close the writer
      writer.close();
    } catch (IOException e) {
      //Any error goes here
      e.printStackTrace();
    }
  }

  static void indexDoc(IndexWriter writer, String name, String content) throws IOException {
    Document doc = new Document();
    doc.add(new TextField("name", name, Store.YES));
    doc.add(new TextField("content", content, Store.YES));
    writer.addDocument(doc);
  }

  static void searchIndex(ByteBuffersDirectory byteBufferDir, Analyzer analyzer) {

    String searchTerm = "happy";

    IndexReader reader = null;
    try {
      //Create Reader
      reader = DirectoryReader.open(byteBufferDir);

      //Create index searcher
      IndexSearcher searcher = new IndexSearcher(reader);

      //Build query
      QueryParser qp = new QueryParser("content", analyzer);
      Query query = qp.parse(searchTerm);

      //Search the index
      TopDocs foundDocs = searcher.search(query, 10);

      // Total found documents
      System.out.println("Total Results :: " + foundDocs.totalHits);

      //Let's print found doc names and their content along with score
      for (ScoreDoc sd : foundDocs.scoreDocs) {
        Document d = searcher.doc(sd.doc);
        System.out.println("Document Name : " + d.get("name")
            + "  :: Content : " + d.get("content")
            + "  :: Score : " + sd.score);
      }
      //don't forget to close the reader
      reader.close();
    } catch (IOException | ParseException e) {
      //Any error goes here
      e.printStackTrace();
    }
  }
}