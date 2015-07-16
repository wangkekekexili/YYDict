package util.dict;

import java.io.File;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import util.Resources;

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
	
	public static String getAudio(String word) {
		
		word = word.replaceAll(Pattern.quote(" "), "+");
		String contentUrl = COLLEGIATE_BASE_URL +
				word + "?key=7a996464-b5ff-42e4-a2b3-3df2554fee55";
		
		try {
			// get the whole word result
			Document document = Jsoup.connect(contentUrl).ignoreContentType(true).get();
			
			// get audio file name from the result
			String wavFileName = document.getElementsByTag("wav").get(0).text();
			
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
			
			return wavFileName;
		} catch (Exception e) {
			return null;
		}
	}
}
