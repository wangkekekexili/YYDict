package util;

import java.io.File;
import java.io.InputStream;

/**
 * Manage resource location.
 * 
 * @author kewang
 *
 */
public class Resources {
	
	public static final String RESOURCE_ROOT_DIRECTORY = "yydict";
	
	public static final String ON_DISK_CACHE_FILE = 
			RESOURCE_ROOT_DIRECTORY + File.separator + "dictionary";
	
	public static final String AUDIO_ROOT_DIRECTORY = 
			RESOURCE_ROOT_DIRECTORY + File.separator + "audio";
	
	public static final String PLAY_BUTTON_IMAGE_RESOURCE = 
			File.separator + "image" + File.separator + "play.png";
	
	public static final String CEDICT_DICTIONARY_ROOT = 
			File.separator + "database" + File.separator + "cedict";
	
	// Unique identifier for each API party.
	public static final String YOUDAO = "youdao";
	public static final String BNC = "bnc";
	public static final String Merriam_Webster = "webster";
	
	public static final String getAudioLocation(String audioName) {
		return AUDIO_ROOT_DIRECTORY + File.separator + audioName;
	}
	
	public static final InputStream getCedictInputStream(String pinyin) {
		return Resources.class.getResourceAsStream(
				CEDICT_DICTIONARY_ROOT + File.separator + pinyin);
	}
}
