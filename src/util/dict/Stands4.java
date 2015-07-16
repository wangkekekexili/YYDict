package util.dict;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.SearchResult;

public class Stands4 {

	private static final String STANDS4_ABBREVIATION_BASE_URL = 
			"http://www.stands4.com/services/v2/abbr.php";
	
	private static final String USER_ID = "4163";
	
	private static final String TOKEN = "e7gLvKFan8dxxRU4";
	
	private static Set<String> categories;
	
	static {
		categories = new HashSet<>();
		categories.add("Universities");
		categories.add("Hospitals");
		categories.add("Software");
	}
	
	public static SearchResult searchAbbr(String word) {
		word = word.trim();
		if (word.contains(" ")) {
			return new SearchResult(false, "");
		}
		
		String urlString = STANDS4_ABBREVIATION_BASE_URL
				+ "?uid=" + USER_ID
				+ "&tokenid=" + TOKEN
				+ "&term=" + word;
		
		int numberOfItems = 0;
		
		try {
			StringBuilder abbrResultBuilder = new StringBuilder();
			abbrResultBuilder.append("STANDS4 abbreviation\n");
			Document document = Jsoup.connect(urlString).get();
			Elements results = document.getElementsByTag("result");
			for (Element result : results) {
				String definition = result.getElementsByTag("definition")
						.get(0).text();
				String category = result.getElementsByTag("category")
						.get(0).text();
				if (categories.contains(category)) {
					numberOfItems++;
					abbrResultBuilder.append(category.toUpperCase());
					abbrResultBuilder.append(" ");
					abbrResultBuilder.append(definition);
					abbrResultBuilder.append("\n");
					if (numberOfItems == 2) {
						break;
					}
				}
			}
			abbrResultBuilder.append("\n\n");
			
			if (numberOfItems > 0) {
				return new SearchResult(true, abbrResultBuilder.toString());
			} else {
				return new SearchResult(false, "");
			}
			
		} catch (Exception e) {
			return new SearchResult(false, "");
		}
		
	}
	
}
