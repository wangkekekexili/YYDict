package util.dict;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import util.Resources;
import util.SearchResult;

public class Cedict {

	public static SearchResult search(String word) {
		word = word.trim();
		if (word.contains(" ") == true || word.length() <= 0) {
			return new SearchResult(false, "");
		}
		
		char firstChar = word.charAt(0);
		String pinyin = null;
		try {
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			format.setVCharType(HanyuPinyinVCharType.WITH_V);
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			pinyin = PinyinHelper
					.toHanyuPinyinStringArray(firstChar, format)[0];
		} catch (Exception e) {
			return new SearchResult(false, "");
		}
		
		File file = Resources.getCedictDatabaseFile(pinyin);
		List<String> lines = null;
		try {
			lines =FileUtils.readLines(file);
		} catch (IOException e) {
			return new SearchResult(false, "");
		}

		for (String line : lines) {
			if (line.startsWith(word + "\t")) {
				StringBuilder resultBuilder = new StringBuilder();
				resultBuilder.append("Cedict\n");
				String[] item = line.split(Pattern.quote("\t"));
				for (int i = 1;i != item.length;i++) {
					resultBuilder.append(Integer.toString(i))
							.append(": ")
							.append(item[i])
							.append("\n");
				}
				resultBuilder.append("\n\n");
				return new SearchResult(true, resultBuilder.toString());
			}
		}
		
		return new SearchResult(false, "");
	}
	
}
