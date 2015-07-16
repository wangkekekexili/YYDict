package util.dict;

import java.io.File;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.Resources;
import util.SearchResult;

/**
 * A helper class for Merriam-Webster API.
 * 
 * @author kewang
 *
 */
public class MerriamWebster {
	
	public static final String COLLEGIATE_BASE_URL = 
			"http://www.dictionaryapi.com/api/v1/references/collegiate/xml/";
	
	public static final String COLLEGIATE_AUDIO_BASE_URL = 
			"http://media.merriam-webster.com/soundc11/";
	
	public static SearchResult search(String word) {
		
		word = word.replaceAll(Pattern.quote(" "), "+");
		String contentUrl = COLLEGIATE_BASE_URL +
				word + "?key=7a996464-b5ff-42e4-a2b3-3df2554fee55";
		
		int numberOfExamples = 0;
		
		try {
			// get the whole word result
			Document document = Jsoup.connect(contentUrl)
					.ignoreContentType(true).get();
			
			StringBuilder exampleSentencesBuilder = new StringBuilder();
			exampleSentencesBuilder.append("Merriam Webster Examples\n");
			
			// get examples sentences (dt -> vi)
			Elements dts = document.getElementsByTag("dt");
			for (Element dt : dts) {
				Elements vis = dt.getElementsByTag("vi");
				for (Element vi : vis) {
					numberOfExamples++;
					exampleSentencesBuilder.append(numberOfExamples);
					exampleSentencesBuilder.append(": ");
					exampleSentencesBuilder.append(vi.text());
					exampleSentencesBuilder.append("\n");
				}
			}
			exampleSentencesBuilder.append("\n\n");
			
			// get audio file name from the result
			String wavFileName = null;
			try {
				wavFileName = document.getElementsByTag("wav").get(0).text();
			
				// construct a URL for the audio
				String subDirectory = null;
				if (Character.isDigit(wavFileName.charAt(0))) {
					subDirectory = "number";
				} else if (wavFileName.startsWith("bix")) {
					subDirectory = "bix";
				} else if (wavFileName.startsWith("gg")) {
					subDirectory = "gg";
				} else {
					subDirectory = wavFileName.substring(0, 1);
				}
				
				String wavUrl =  COLLEGIATE_AUDIO_BASE_URL + 
						subDirectory + "/" + wavFileName;
				
				// download audio
				FileUtils.copyURLToFile(new URL(wavUrl), 
						new File(Resources.getAudioLocation(wavFileName)));
			} catch (Exception e) {}
			
			if (numberOfExamples == 0) {
				return new SearchResult(
						false, exampleSentencesBuilder.toString(), wavFileName);
			} else {
				return new SearchResult(
						true, exampleSentencesBuilder.toString(), wavFileName);
			}
		} catch (Exception e) {
			return null;
		}
	}
}
