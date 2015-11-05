package com.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class CoreNews {

	ArrayList<Document> commentsArray = new ArrayList<Document>();
	ArrayList<Document> labelsArray = new ArrayList<Document>();
	ArrayList<Document> categoryArray = new ArrayList<Document>();
	
	public CoreNews () {

	}

	public ArrayList <Document> GenerateNewsData(File file) throws IOException, BiffException {

		ArrayList<Document> coreDocumentsArray = new ArrayList<Document>();

		Workbook workBook = Workbook.getWorkbook(file);
		Sheet sh1 = workBook.getSheet(0);

		Cell columns0[] = sh1.getColumn(0);
		Cell columns1[] = sh1.getColumn(1);
		Cell columns2[] = sh1.getColumn(2);
		Cell columns3[] = sh1.getColumn(3);
		Cell columns4[] = sh1.getColumn(4);
		Cell columns5[] = sh1.getColumn(5);

		for (int i = 0; i < columns0.length; i++) {

			Document document = new Document();

			String value0 = columns0[i].getContents();
			String value1 = columns1[i].getContents();
			String value2 = columns2[i].getContents();
			String value3 = columns3[i].getContents();
			String value4 = columns4[i].getContents();
			String value5 = columns5[i].getContents();

			Field CORE_NEWSURL_Field = new Field(LuceneConstants.CORE_NEWSURL, value0, Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.NO);
			Field CORE_NEWSNUMBER_Field = new Field(LuceneConstants.CORE_NEWSNUMBER, value1, Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.NO);
			Field CORE_NEWSTITLE_Field = new Field(LuceneConstants.CORE_NEWSTITLE, value2, Field.Store.YES,Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);
			Field CORE_NEWSBODY_Field = new Field(LuceneConstants.CORE_NEWSBODY, value3, Field.Store.NO,Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);
			Field CORE_NEWSDATE_Field = new Field(LuceneConstants.CORE_NEWSDATE, value4, Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.NO);
			Field CORE_NEWSSOURCE_Field = new Field(LuceneConstants.CORE_NEWSSOURCE, value5, Field.Store.YES,Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);

			Field fileNameField = new Field(LuceneConstants.FILE_NAME,file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED);
			Field filePathField = new Field(LuceneConstants.FILE_PATH,file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);

			//Comments
			CommentNews commentClass = new CommentNews();
			commentsArray = commentClass.GenerateCommentData(document, value0);
			
			//Labels
			LabelNews labelClass = new LabelNews();
			labelsArray = labelClass.GenerateLabelData(document, value0);
			
			//Labels
			CategoryNews categoryClass = new CategoryNews();
			categoryArray = categoryClass.GenerateCategoryData(document, value0);
			
			document.add(CORE_NEWSURL_Field);
			document.add(CORE_NEWSNUMBER_Field);
			document.add(CORE_NEWSTITLE_Field);
			document.add(CORE_NEWSBODY_Field);
			document.add(CORE_NEWSDATE_Field);
			document.add(CORE_NEWSSOURCE_Field);
			document.add(fileNameField);
			document.add(filePathField);

			coreDocumentsArray.add(document);
		}

		return coreDocumentsArray;
	}
}
