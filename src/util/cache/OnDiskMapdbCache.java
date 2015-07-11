package util.cache;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import util.Resources;

/**
 * A simple on disk cache using MapDB.
 * 
 * @author kewang
 *
 */
public class OnDiskMapdbCache extends Cache {

	private DB database;
	private ConcurrentNavigableMap<String, String> map;
	
	public OnDiskMapdbCache() {
		File databaseFile = new File(Resources.ON_DISK_CACHE_FILE);
		database = DBMaker
				.fileDB(databaseFile)
				.transactionDisable()
				.closeOnJvmShutdown()
				.make();
		map = database.treeMap(Resources.YOUDAO);
	}
	
	public OnDiskMapdbCache(String cacheFileName) {
		database = DBMaker
				.fileDB(new File(cacheFileName))
				.closeOnJvmShutdown()
				.transactionDisable()
				.make();
		map = database.treeMap(Resources.YOUDAO);
	}
	
	@Override
	public String get(String word) {
		return map.get(word);
	}

	@Override
	public void put(String word, String defination) {
		map.put(word, defination);
	}

}
