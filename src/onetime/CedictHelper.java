package onetime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class CedictHelper {

	public static void main(String[] args) throws Exception {
		URL cedictPath = CedictHelper.class.getClass()
				.getResource("cedict_origin");
		BufferedReader br = new BufferedReader(
				new InputStreamReader(cedictPath.openStream()));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("#")) {
				continue;
			}
		}
		br.close();
	}

}
