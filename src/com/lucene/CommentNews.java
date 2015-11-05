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

public class CommentNews {

	public CommentNews() {

	}

	public ArrayList <Document> GenerateCommentData(Document document, String newsURL) throws IOException, BiffException {

		File file = new File(LuceneConstants.FILE_PATH_COMMENTS);
		
		ArrayList<Document> coreDocumentsArray = new ArrayList<Document>();

		Workbook workBook = Workbook.getWorkbook(file);
		Sheet sh1 = workBook.getSheet(0);

		Cell columns0[] = sh1.getColumn(0);

		for (int i = 0; i < columns0.length; i++) {

			String value0 = columns0[i].getContents();
			
			if (value0.equalsIgnoreCase(newsURL)) {
				
				Cell columns1[] = sh1.getColumn(1);
				Cell columns2[] = sh1.getColumn(2);
				Cell columns3[] = sh1.getColumn(3);
				Cell columns4[] = sh1.getColumn(4);
				Cell columns5[] = sh1.getColumn(5);
				Cell columns6[] = sh1.getColumn(6);
				Cell columns7[] = sh1.getColumn(7);
				Cell columns8[] = sh1.getColumn(8);
				Cell columns9[] = sh1.getColumn(9);
				
				String value1 = columns1[i].getContents();
				String value2 = columns2[i].getContents();
				String value3 = columns3[i].getContents();
				String value4 = columns4[i].getContents();
				String value5 = columns5[i].getContents();
				String value6 = columns6[i].getContents();
				String value7 = columns7[i].getContents();
				String value8 = columns8[i].getContents();
				String value9 = columns9[i].getContents();

				Field COMMENT_ID = new Field(LuceneConstants.COMMENT_ID, value1, Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.NO);
				Field COMMENT_PARENTID = new Field(LuceneConstants.COMMENT_PARENTID, value2, Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.NO);
				Field COMMENT_COMMENTER = new Field(LuceneConstants.COMMENT_COMMENTER, value3, Field.Store.YES,Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);
				Field COMMENT_LOCATION = new Field(LuceneConstants.COMMENT_LOCATION, value4, Field.Store.YES,Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);
				Field COMMENT_DATE = new Field(LuceneConstants.COMMENT_DATE, value5, Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.NO);
				Field COMMENT_LIKECOMMENT = new Field(LuceneConstants.COMMENT_LIKECOMMENT, value6, Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.NO);
				Field COMMENT_DISLIKECOMMENT = new Field(LuceneConstants.COMMENT_DISLIKECOMMENT, value7, Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.NO);
				Field COMMENT_RESPONSECOUNT = new Field(LuceneConstants.COMMENT_RESPONSECOUNT, value8, Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.NO);
				Field COMMENT_BODY = new Field(LuceneConstants.COMMENT_BODY, value9, Field.Store.NO,Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);

				Field fileNameField = new Field(LuceneConstants.FILE_NAME,file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED);
				Field filePathField = new Field(LuceneConstants.FILE_PATH,file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);

				document.add(COMMENT_ID);
				document.add(COMMENT_PARENTID);
				document.add(COMMENT_COMMENTER);
				document.add(COMMENT_LOCATION);
				document.add(COMMENT_DATE);
				document.add(COMMENT_LIKECOMMENT);
				document.add(COMMENT_DISLIKECOMMENT);
				document.add(COMMENT_RESPONSECOUNT);
				document.add(COMMENT_BODY);
				document.add(filePathField);
				document.add(fileNameField);

				coreDocumentsArray.add(document);
			}
		}

		return coreDocumentsArray;
	}
}
