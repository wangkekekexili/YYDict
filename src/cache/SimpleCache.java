package cache;

import java.util.HashMap;
import java.util.Map;

public class SimpleCache extends Cache {

	Map<String, String> defination = new HashMap<>();
	
	@Override
	public String get(String word) {
		return defination.get(word);
	}

	@Override
	public void put(String word, String defination) {
		this.defination.put(word, defination);
	}

}
