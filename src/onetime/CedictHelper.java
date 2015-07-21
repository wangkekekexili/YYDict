package onetime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class CedictHelper {
	
	@SuppressWarnings("unused")
	private static void extractContent() throws Exception {
		URL cedictPath = CedictHelper.class.getClass()
				.getResource("/onetime/cedict_origin");
		
		BufferedWriter bw = new BufferedWriter(
				new FileWriter(new File("src/onetime/cedict")));
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(cedictPath.openStream()));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("#")) {
				continue;
			}
			String[] item = line.split(Pattern.quote("/"));
			if (item[1].startsWith("archaic variant of") ||
					item[1].startsWith("old variant of") ||
					item[1].startsWith("variant of")) {
				continue;
			}
			String[] chineseItem = item[0].split(Pattern.quote(" "));
			bw.write(chineseItem[1]);
			for (int i = 1;i != item.length;i++) {
				bw.write("\t");
				bw.write(item[i]);
			}
			bw.write("\n");
			
		}
		br.close();
		bw.close();

	}

	@SuppressWarnings("unused")
	private static void hashContent() throws Exception {
		
		Map<String, Set<String>> map = new HashMap<>();
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		
		File file = new File("src/onetime/cedict");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = br.readLine()) != null) {
			char firstChar = line.charAt(0);
			
			// convert first char to pinyin
			// if unsuccessful, ignore that line
			try {
				String pinyin = PinyinHelper.toHanyuPinyinStringArray(
						firstChar, format)[0];
				Set<String> set = map.get(pinyin);
				if (set == null) {
					map.put(pinyin, new HashSet<>());
				}
				set.add(line);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
		br.close();
		
		for (Entry<String, Set<String>> entry : map.entrySet()) {
			File f = new File("cedict/" + entry.getKey());
			f.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (String s : entry.getValue()) {
				bw.write(s);
				bw.write("\n");
			}
			bw.close();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
	}
}
