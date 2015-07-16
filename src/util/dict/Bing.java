package util.dict;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import util.SearchResult;

public class Bing {

	public static SearchResult search(String word) {
		word = word.trim().replaceAll(" ", "+");
		try {
			Document document = Jsoup
					.connect("http://cn.bing.com/dict/problem?q="+ word +"")
					.get();
			Element ul = document.getElementsByClass("qdef").get(0).getElementsByTag("ul").get(0);
			
			StringBuilder result = new StringBuilder();
			result.append("Bing\n");
			
			for (Element li: ul.getElementsByTag("li")) {
				Element partOfSpeech = li.getElementsByClass("pos").get(0);
				Element defination = li.getElementsByClass("def").get(0);
				result.append(" ");
				result.append(partOfSpeech.text());
				result.append(" ");
				result.append(defination.text());
				result.append("\n");
			}
			result.append("\n\n");
			
			return new SearchResult(true, result.toString());
			
		} catch (Exception e) {
			return new SearchResult(false, e.getMessage());
		}
	}
	
}
