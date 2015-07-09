package util.webster;

import java.io.File;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class WebsterHelper {
	public static String getAudio(String word) {
		word = word.replaceAll(Pattern.quote(" "), "+");
		String contentUrl = "http://www.dictionaryapi.com/api/v1/references/collegiate/xml/" +
				word + "?key=7a996464-b5ff-42e4-a2b3-3df2554fee55";
		try {
			Document document = Jsoup.connect(contentUrl).ignoreContentType(true).get();
			String wavFileName = document.getElementsByTag("wav").get(0).text();
			String wavUrl = "http://media.merriam-webster.com/soundc11/" +
					wavFileName.charAt(0) + "/" + wavFileName;
			FileUtils.copyURLToFile(new URL(wavUrl), new File("yydict"+File.separator+"audio"+File.separator+wavFileName));
			return wavFileName;
		} catch (Exception e) {
			return null;
		}
	}
}
