package cache;

public abstract class Cache {
	public abstract String get(String word);
	public abstract void put(String word, String defination);
}
