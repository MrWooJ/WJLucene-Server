package com.lucene;

public class LuceneConstants {

	public static StringBuffer ProjectPath = new StringBuffer();
	
	public static String DATA_DIRECTORY					= "/MetaData/Data";
	public static String INDEX_DIRECTORY				= "/MetaData/Index";

	public static final String DEFAULT_FIELD 			= "core_newsurl";
	
	public static final String CORE_NEWSURL 			= "core_news_url";
	public static final String CORE_NEWSNUMBER 			= "core_news_number";
	public static final String CORE_NEWSTITLE 			= "core_new_title";
	public static final String CORE_NEWSBODY			= "core_news_body";
	public static final String CORE_NEWSDATE			= "core_news_date";
	public static final String CORE_NEWSSOURCE 			= "core_news_source";
	
	public static final String COMMENT_ID 				= "comment_id";
	public static final String COMMENT_PARENTID 		= "comment_parentId";
	public static final String COMMENT_COMMENTER 		= "comment_commenter";
	public static final String COMMENT_LOCATION 		= "comment_location";
	public static final String COMMENT_DATE 			= "comment_date";
	public static final String COMMENT_LIKECOMMENT 		= "comment_like_comment";
	public static final String COMMENT_DISLIKECOMMENT	= "comment_dislike_comment";
	public static final String COMMENT_RESPONSECOUNT 	= "comment_response_count";
	public static final String COMMENT_BODY 			= "comment_body";
	
	public static final String CATEGORY_NEWS 			= "core_category";
	public static final String LABEL_NEWS 				= "core_label";
	
	public static final String FILE_NAME				= "filename";
	public static final String FILE_PATH				= "filepath";
	
	public static String FILE_PATH_CORENEWS				= "/MetaData/Data/Core.xls";
	public static String FILE_PATH_COMMENTS				= "/MetaData/Data/Comments.xls";
	public static String FILE_PATH_CATEGORY				= "/MetaData/Data/Category.xls";
	public static String FILE_PATH_LABELS				= "/MetaData/Data/Label.xls";
	
	public static final int MAX_SEARCH = 50000;
	
	public static final String MESSAGE_INDEXING 		= "Wait for Merging raw data and Indexing!";
	public static final String MESSAGE_INDEXED 			= "Ready to Use, Give it a try!";
	public static final String MESSAGE_SEARCH 			= " Documents Found in ";

}