package com.httpserver.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.lucene.LuceneConstants;
import com.lucene.LuceneTester;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;

import java.net.URI;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.json.simple.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TestServer {
	
	final static TestServer view = new TestServer();
	final static LuceneTester tester = new LuceneTester();

	private static final String HOSTNAME 				= "0.0.0.0";
	private static final int PORT 						= 8000;
	private static final int BACKLOG 					= 1;

	private static final String HEADER_ALLOW 			= "Allow";
	private static final String HEADER_CONTENT_TYPE 	= "Content-Type";

	private static final Charset CHARSET 				= StandardCharsets.UTF_8;

	private static final int STATUS_OK 					= 200;
	private static final int STATUS_METHOD_NOT_ALLOWED 	= 405;

	private static final int NO_RESPONSE_LENGTH 		= -1;

	private static final String METHOD_GET 				= "GET";
	private static final String METHOD_OPTIONS 			= "OPTIONS";
	private static final String ALLOWED_METHODS 		= METHOD_GET + "," + METHOD_OPTIONS;

	static HttpServer server;
	static HttpsServer ser;

	static ArrayList<String> keyArrays = new ArrayList<String>();
	
	public static void main(String[] args) throws Exception {

		// Do Indexing Process
		// If there was a need to remove former indexed filed, Uncomment line below
		// FileUtils.deleteDirectory(LuceneConstants.INDEX_DIRECTORY);
		try {
			tester.createIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Do Creating Server
		server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
		server.createContext("/info", new InfoHandler());
		server.createContext("/kill", new KillHandler());
		server.createContext("/search", new ReqHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("Server is listening on port "+PORT);
	}

	static class KillHandler implements HttpHandler {

		public void handle(HttpExchange exchange) throws IOException {
			String response = "Server Process Killing in 1 Seconds!";
			exchange.sendResponseHeaders(200, response.length());
			exchange.getResponseBody().write(response.getBytes());
			exchange.close();
			server.stop(1);
		}
	}

	static class ReqHandler implements HttpHandler {
		@SuppressWarnings("unchecked")
		@Override
		public void handle(HttpExchange he) throws IOException {

			try {
				final Headers headers = he.getResponseHeaders();
				final String requestMethod = he.getRequestMethod().toUpperCase();
				switch (requestMethod) {
				case METHOD_GET:
					final Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());
					// do something with the request parameters
					System.out.println("REQ PARAMETERS: "+requestParameters.toString());

					Iterator<String> itr = requestParameters.keySet().iterator();
					Map<String, String> queryParams = new HashMap<String, String>();
					boolean operatorIsAnd = false;
					while(itr.hasNext()) {
						String key = itr.next();
						String value = requestParameters.get(key).get(0);
						if(key.equalsIgnoreCase("operator") && value.equalsIgnoreCase("and"))
							operatorIsAnd = true;
						else
							queryParams.put(key, value);
					}
					ArrayList<Document> docsArray = new ArrayList<Document>();
					try {
						docsArray = tester.searchCombinedQuery(queryParams, operatorIsAnd);
					} catch (IOException | ParseException e1) {
						e1.printStackTrace();
					}
					JSONObject obj = new JSONObject();
					StringWriter out = new StringWriter();
					obj.writeJSONString(out);
					if(docsArray.size() == 0)
						obj.put("Status","No Result");
					else {
						obj.put("Status","Successful");
						ArrayList<Map<String, String>> news = new ArrayList<Map<String, String>>();
						for (Document document : docsArray) {
							Map<String, String> newsMap = new HashMap<String, String>();
							newsMap.put("Title", document.get(LuceneConstants.CORE_NEWSTITLE));
							newsMap.put("Number", document.get(LuceneConstants.CORE_NEWSNUMBER));
							newsMap.put("URL", document.get(LuceneConstants.CORE_NEWSURL));
							news.add(newsMap);
						}
						obj.put("News", news);
					}
					String jsonText = obj.toString();
					final String responseBody = jsonText;
					headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
					final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
					he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
					he.getResponseBody().write(rawResponseBody);
					break;
				case METHOD_OPTIONS:
					headers.set(HEADER_ALLOW, ALLOWED_METHODS);
					he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
					break;
				default:
					headers.set(HEADER_ALLOW, ALLOWED_METHODS);
					he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
					break;
				}
			} finally {
				he.close();
			}
		}
	}


	static class InfoHandler implements HttpHandler {
		public void handle(HttpExchange httpExchange) throws IOException {
			String response = "Use /search?filedName1=value1&fieldName2=value2...";
			TestServer.writeResponse(httpExchange, response.toString());
		}
	}


	public static void writeResponse(HttpExchange httpExchange, String response) throws IOException {
		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	private static String decodeUrlComponent(final String urlComponent) {
		try {
			return URLDecoder.decode(urlComponent, CHARSET.name());
		} catch (final UnsupportedEncodingException ex) {
			throw new InternalError();
		}
	}

	private static Map<String, List<String>> getRequestParameters(final URI requestUri) {
		final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
		final String requestQuery = requestUri.getRawQuery();
		if (requestQuery != null) {
			final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
			for (final String rawRequestParameter : rawRequestParameters) {
				final String[] requestParameter = rawRequestParameter.split("=", 2);
				final String requestParameterName = decodeUrlComponent(requestParameter[0]);
				requestParameters.put(requestParameterName, new ArrayList<String>());
				final String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1]) : null;
				requestParameters.get(requestParameterName).add(requestParameterValue);
			}
		}
		return requestParameters;
	}

}