package com.lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import jxl.read.biff.BiffException;

import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {

	private IndexWriter writer;

	public Indexer(String indexDirectoryPath) throws IOException {

		Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));

		PersianAnalyzer persianAnalyzer = new PersianAnalyzer(Version.LUCENE_36);
		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36, persianAnalyzer);

		writer = new IndexWriter(indexDirectory, conf);
	}

	public void close() throws CorruptIndexException, IOException {

		writer.close();
	}

	private ArrayList <Document> getDocuments(File file) throws IOException, BiffException{

		ArrayList <Document> documentsArray = new ArrayList <Document>();

		CoreNews coreNews = new CoreNews();
		documentsArray = coreNews.GenerateNewsData(file);

		return documentsArray;
	}   

	private void indexFile(File file) throws IOException, BiffException {

		writer.addDocuments(getDocuments(file));
	}

	public int createIndex(String dataDirPath, FileFilter filter) throws IOException, BiffException{

		File file = new File(LuceneConstants.FILE_PATH_CORENEWS);

		if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file))
			indexFile(file);

		return writer.numDocs();
	}
}