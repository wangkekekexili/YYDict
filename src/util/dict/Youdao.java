package util.dict;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import util.SearchResult;

/**
 * Use Cambridge web service to translate from English to simplified Chinese.
 * 
 * @author kewang
 *
 */
public class Youdao {

	public static final String YOUDAO_DICT_URL = 
			"http://fanyi.youdao.com/openapi.do?"
			+ "keyfrom=KeAndYiyangsFamily"
			+ "&key=1266068425"
			+ "&type=data"
			+ "&doctype=json"
			+ "&version=1.1"
			+ "&only=dict"
			+ "&q=";
	
	public static final String getJsonString(String url) throws UnsupportedEncodingException, MalformedURLException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new URL(url).openStream(), "UTF-8"));
		String content = br.readLine();
		br.close();
		return content;
	}
	
	public static SearchResult search(String word) {
		String jsonString = null;
		try {
			jsonString = getJsonString(YOUDAO_DICT_URL + 
					word.replaceAll(Pattern.quote(" "), "+"));
		} catch (Exception e) {
			return new SearchResult(false, 
					"Cannot get result from Youdao Dict.\n"
					+ e.getMessage());
		}
		JsonNode rootNode = null;
		try {
			rootNode = new ObjectMapper().readTree(jsonString);
		} catch (Exception e) {
			return new SearchResult(false, 
					"Cannot parse result."
					+ e.getMessage());
		}
		JsonNode defination = null;
		try {
			defination = rootNode.path("basic").path("explains");
		} catch (NullPointerException e) {
			return new SearchResult(false, "No result.");
		}
		if (defination.size() <= 0) {
			return new SearchResult(false, "No result.");
		}
		StringBuilder result = new StringBuilder();
		result.append("Youdao\n");
		for (int i = 0;i != defination.size();i++) {
			result.append(defination.get(i).toString()
					.trim().replace('"', ' '));
			result.append("\n");
		}
		result.append("\n\n");
		return new SearchResult(true, result.toString());
	}

}
