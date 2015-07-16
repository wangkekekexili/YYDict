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
			
			StringBuilder simpleDefinition = new StringBuilder();
			simpleDefinition.append("Bing\n");
			
			for (Element li: ul.getElementsByTag("li")) {
				Element partOfSpeech = li.getElementsByClass("pos").get(0);
				Element defination = li.getElementsByClass("def").get(0);
				simpleDefinition.append(" ");
				simpleDefinition.append(partOfSpeech.text());
				simpleDefinition.append(" ");
				simpleDefinition.append(defination.text());
				simpleDefinition.append("\n");
			}
			simpleDefinition.append("\n\n");

			StringBuilder example = new StringBuilder();
			example.append("Bing Example\n");
			
			int numberOfExamples = 0;
			for (Element div : document.getElementsByClass("li_ex")) {
				String sentence = div.getElementsByClass("val_ex")
						.get(0).text();
				numberOfExamples++;
				example.append(numberOfExamples);
				example.append(": ");
				example.append(sentence);
				example.append("\n\n");
			}
			example.append("\n\n");
			
			StringBuilder result = new StringBuilder(
					simpleDefinition.length() + example.length());
			
			result.append(simpleDefinition.toString());
			
			if (numberOfExamples > 0) {
				result.append(example.toString());
			}
			
			
			return new SearchResult(true, result.toString());
			
		} catch (Exception e) {
			return new SearchResult(false, e.getMessage());
		}
	}
	
}
