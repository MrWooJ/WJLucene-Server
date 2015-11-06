package com.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {

	IndexSearcher indexSearcher;
	QueryParser queryParser;
	Query query;

	private String operatorStr;

	public Searcher(String indexDirectoryPath) throws IOException {

		Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
		IndexReader indexReader = IndexReader.open(indexDirectory);
		indexSearcher = new IndexSearcher(indexReader);
		PersianAnalyzer persianAnalyzer = new PersianAnalyzer(Version.LUCENE_36,PersianAnalyzer.getDefaultStopSet());
		queryParser = new QueryParser(Version.LUCENE_36,LuceneConstants.DEFAULT_FIELD,persianAnalyzer);
		queryParser.setAutoGeneratePhraseQueries(true);
	}

	public TopDocs search(String searchQuery) throws IOException, ParseException {

		query = queryParser.parse(searchQuery);
		System.out.println("ParseQuery: "+query.toString());
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
	}

	public TopDocs searchCombinedQuery(Map<String, String> map, boolean operatorStatus) throws IOException, ParseException {
		
		return search(produceQuery(map, operatorStatus));
	}

	public String produceQuery (Map<String, String> map, boolean operatorIsAnd) {

		StringBuffer queryString = new StringBuffer("");

		if(operatorIsAnd)
			operatorStr = "AND ";
		else
			operatorStr = "OR ";

		if(map.get(LuceneConstants.CORE_NEWSURL) != null)
			queryString.append(LuceneConstants.CORE_NEWSURL+":"+map.get(LuceneConstants.CORE_NEWSURL)+" "+operatorStr);
		if(map.get(LuceneConstants.CORE_NEWSNUMBER) != null)
			queryString.append(LuceneConstants.CORE_NEWSNUMBER+":"+map.get(LuceneConstants.CORE_NEWSNUMBER)+" "+operatorStr);
		if(map.get(LuceneConstants.CORE_NEWSTITLE) != null)
			queryString.append(LuceneConstants.CORE_NEWSTITLE+":"+map.get(LuceneConstants.CORE_NEWSTITLE)+" "+operatorStr);
		if(map.get(LuceneConstants.CORE_NEWSBODY) != null)
			queryString.append(LuceneConstants.CORE_NEWSBODY+":"+map.get(LuceneConstants.CORE_NEWSBODY)+" "+operatorStr);
		if(map.get(LuceneConstants.CORE_NEWSDATE) != null)
			queryString.append(LuceneConstants.CORE_NEWSDATE+":"+map.get(LuceneConstants.CORE_NEWSDATE)+" "+operatorStr);
		if(map.get(LuceneConstants.CORE_NEWSSOURCE) != null)
			queryString.append(LuceneConstants.CORE_NEWSSOURCE+":"+map.get(LuceneConstants.CORE_NEWSSOURCE)+" "+operatorStr);

		if(map.get(LuceneConstants.COMMENT_ID) != null)
			queryString.append(LuceneConstants.COMMENT_ID+":"+map.get(LuceneConstants.COMMENT_ID)+" "+operatorStr);
		if(map.get(LuceneConstants.COMMENT_PARENTID) != null)
			queryString.append(LuceneConstants.COMMENT_PARENTID+":"+map.get(LuceneConstants.COMMENT_PARENTID)+" "+operatorStr);
		if(map.get(LuceneConstants.COMMENT_COMMENTER) != null)
			queryString.append(LuceneConstants.COMMENT_COMMENTER+":"+map.get(LuceneConstants.COMMENT_COMMENTER)+" "+operatorStr);
		if(map.get(LuceneConstants.COMMENT_LOCATION) != null)
			queryString.append(LuceneConstants.COMMENT_LOCATION+":"+map.get(LuceneConstants.COMMENT_LOCATION)+" "+operatorStr);
		if(map.get(LuceneConstants.COMMENT_DATE) != null)
			queryString.append(LuceneConstants.COMMENT_DATE+":"+map.get(LuceneConstants.COMMENT_DATE)+" "+operatorStr);
		if(map.get(LuceneConstants.COMMENT_LIKECOMMENT) != null)
			queryString.append(LuceneConstants.COMMENT_LIKECOMMENT+":"+map.get(LuceneConstants.COMMENT_LIKECOMMENT)+" "+operatorStr);
		if(map.get(LuceneConstants.COMMENT_DISLIKECOMMENT) != null)
			queryString.append(LuceneConstants.COMMENT_DISLIKECOMMENT+":"+map.get(LuceneConstants.COMMENT_DISLIKECOMMENT)+" "+operatorStr);
		if(map.get(LuceneConstants.COMMENT_RESPONSECOUNT) != null)
			queryString.append(LuceneConstants.COMMENT_RESPONSECOUNT+":"+map.get(LuceneConstants.COMMENT_RESPONSECOUNT)+" "+operatorStr);
		if(map.get(LuceneConstants.COMMENT_BODY) != null)
			queryString.append(LuceneConstants.COMMENT_BODY+":"+map.get(LuceneConstants.COMMENT_BODY)+" "+operatorStr);

		if(map.get(LuceneConstants.CATEGORY_NEWS) != null)
			queryString.append(LuceneConstants.CATEGORY_NEWS+":"+map.get(LuceneConstants.CATEGORY_NEWS)+" "+operatorStr);
		if(map.get(LuceneConstants.LABEL_NEWS) != null)
			queryString.append(LuceneConstants.LABEL_NEWS+":"+map.get(LuceneConstants.LABEL_NEWS)+" "+operatorStr);

		String query = new String(queryString);

		if (queryString.length() != 0) {
			if (operatorIsAnd)
				query = query.substring(0, query.length()-5);
			else
				query = query.substring(0, query.length()-4);
		}
		
		System.out.println("QueryProducer: "+query);
		
		return query;
	}

	public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
		
		return indexSearcher.doc(scoreDoc.doc);	
	}

	public void close() throws IOException{
		
		indexSearcher.close();
	}
}