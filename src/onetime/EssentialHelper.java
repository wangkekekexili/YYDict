package onetime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

public class EssentialHelper {

	@SuppressWarnings("unused")
	private static void extractInformation() throws Exception {
		BufferedReader br = new BufferedReader(
				new FileReader(new File("data/essential_origin")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				new File("data/essential")));
		
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] item = line.split(Pattern.quote("="));
			String english = item[0];
			String chinese = item[1];
			chinese = chinese.substring(chinese.indexOf('.')+1);
			bw.write(english.trim());
			bw.write("\t");
			bw.write(chinese.trim());
			bw.write("\n");
		}

		br.close();
		bw.close();

	}
	
	@SuppressWarnings("unused")
	private static void hashDictionary() throws Exception {
		
		Map<Character, Set<String>> map = new HashMap<>();
		
		BufferedReader br = new BufferedReader(new FileReader(
				new File("data/essential")));
		String line = null;
		
		while ((line = br.readLine()) != null) {
			char firstCharacter = line.charAt(0);
			Set<String> set = map.get(firstCharacter);
			if (set == null) {
				set = new HashSet<>();
				map.put(firstCharacter, set);
			}
			set.add(line);
		}
		
		br.close();
		
		for (Entry<Character, Set<String>> entry : map.entrySet()) {
			Character ch = entry.getKey();
			Set<String> items = entry.getValue();
			new File("essential").mkdir();
			File file = new File("essential" + File.separator + ch.toString());
			if (file.exists() == false) {
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (String s : items) {
				bw.write(s);
				bw.write("\n");
			}
			bw.close();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
	}

}
