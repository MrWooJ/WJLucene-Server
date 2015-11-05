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

public class LabelNews {

	public LabelNews() {

	}

	public ArrayList <Document> GenerateLabelData(Document document, String newsURL) throws IOException, BiffException {

		File file = new File(LuceneConstants.FILE_PATH_LABELS);
		
		ArrayList<Document> coreDocumentsArray = new ArrayList<Document>();

		Workbook workBook = Workbook.getWorkbook(file);
		Sheet sh1 = workBook.getSheet(0);

		Cell columns0[] = sh1.getColumn(0);

		for (int i = 0; i < columns0.length; i++) {

			String value0 = columns0[i].getContents();
			
			if (value0.equalsIgnoreCase(newsURL)) {
				
				Cell columns2[] = sh1.getColumn(2);
				
				String value2 = columns2[i].getContents();

				Field LABEL_NEWS = new Field(LuceneConstants.LABEL_NEWS, value2, Field.Store.YES,Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);

				Field fileNameField = new Field(LuceneConstants.FILE_NAME,file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED);
				Field filePathField = new Field(LuceneConstants.FILE_PATH,file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);

				document.add(LABEL_NEWS);
				document.add(filePathField);
				document.add(fileNameField);

				coreDocumentsArray.add(document);
			}
		}

		return coreDocumentsArray;
	}

	
}
