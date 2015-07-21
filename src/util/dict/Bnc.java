package util.dict;

import java.io.IOException;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import util.SearchResult;

/**
 * Get and parse British national corpus data from http://bnc.bl.uk/.
 * 
 * @author kwang
 *
 */
public class Bnc {

	public static final int TIMEOUT = 10 * 1000;
	
	public static final String ONLINE_BNC_URL = 
			"http://bnc.bl.uk/saraWeb.php?qy=";
	
	public static SearchResult search(String word) {
		String wordForUrl = word.trim().replaceAll(Pattern.quote(" "), "+");
		Document document = null;
		
		try {
			document = Jsoup.connect(ONLINE_BNC_URL + wordForUrl)
					.timeout(TIMEOUT).get();
		} catch (IOException e) {
			return new SearchResult(false, e.getMessage());
		}
		
		Elements solutions = null;
		try { 
			solutions = document.getElementById("solutions").
					getElementsByTag("p");
		} catch (NullPointerException e) {
			return new SearchResult(false, e.getMessage());
		}
		
		if (solutions.size() == 0) {
			return new SearchResult(false, "");
		}

		StringBuilder result = new StringBuilder();
		result.append("BNC example\n");
		for (int index = 1;index != solutions.size();index++) {
			solutions.get(index).getElementsByTag("b").remove();
			result.append(Integer.toString(index));
			result.append(": ");
			result.append(solutions.get(index).getAllElements().text());
			result.append("\n\n");
			if (index == 5) {
				break;
			}
		}
		result.append("\n");
		
		
		
		return new SearchResult(true, result.toString());
	}
	
}
