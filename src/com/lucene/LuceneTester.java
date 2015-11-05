package com.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import jxl.read.biff.BiffException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {

	Indexer 	indexer;
	Searcher 	searcher;

	public void createIndex() throws IOException, BiffException {

		File file = new File(LuceneConstants.INDEX_DIRECTORY);

		if (file.listFiles().length <= 2) {
			System.out.println("Start Indexing Proccess!");
			
			indexer = new Indexer(LuceneConstants.INDEX_DIRECTORY);

			int numIndexed;
			long startTime = System.currentTimeMillis();	
			numIndexed = indexer.createIndex(LuceneConstants.DATA_DIRECTORY, new TextFileFilter());
			long endTime = System.currentTimeMillis();

			indexer.close();

			System.out.println(numIndexed+" File Indexed. Time: " +(endTime-startTime)+" ms");					
		}
		else 
			System.out.println("Indexed Files are Ready!");
	}

	public void search(String searchQuery) throws IOException, ParseException {

		searcher = new Searcher(LuceneConstants.INDEX_DIRECTORY);

		long startTime = System.currentTimeMillis();
		TopDocs hits = searcher.search(searchQuery);
		long endTime = System.currentTimeMillis();

		ArrayList<Document> docsArray = new ArrayList<Document>();

		System.out.println(hits.totalHits +" Documents Found. Time :" + (endTime - startTime));

		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			docsArray.add(doc);
			System.out.println("Result File: " + doc.toString());
		}

		searcher.close();
	}

	public ArrayList<Document> searchCombinedQuery(Map<String, String> dictionary, boolean Operator) throws IOException, ParseException {

		searcher = new Searcher(LuceneConstants.INDEX_DIRECTORY);

		long startTime = System.currentTimeMillis();
		TopDocs hits = searcher.searchCombinedQuery(dictionary, Operator);
		long endTime = System.currentTimeMillis();

		ArrayList<Document> docsArray = new ArrayList<Document>();

		System.out.println(hits.totalHits +" Focuments Found. Time :" + (endTime - startTime));

		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			docsArray.add(doc);
			System.out.println("File: " + doc.toString());
		}

		searcher.close();
		return docsArray;
	}

	public String getSearchQuery(Map<String, String> dictionary, boolean Operator) throws IOException, ParseException {

		searcher = new Searcher(LuceneConstants.INDEX_DIRECTORY);
		String str = new String(searcher.produceQuery(dictionary, Operator));
		searcher.close();

		return str;
	}

}