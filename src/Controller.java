import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DBMaker;

import cache.Cache;
import cache.SimpleCache;

public class Controller implements ActionListener {

	private SimpleGui frame;
	private Cache inMemoryCache;
	private ConcurrentNavigableMap<String, String> onDiskCache;
	
	public Controller(SimpleGui frame) {
		this.frame = frame;
		inMemoryCache = new SimpleCache();
		onDiskCache = DBMaker.fileDB(new File("dictionary"))
				.closeOnJvmShutdown()
				.make()
				.treeMap("youdao");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		class SearchThread extends Thread {
			private SearchResult result = new SearchResult(
					false, "Search thread error.");
			@Override
			public void run() {
				String wordToSearch = frame.getWordToSearch();
				if (inMemoryCache.get(wordToSearch) != null) {
					result = new SearchResult(true, 
							inMemoryCache.get(wordToSearch));
				} else if (onDiskCache.get(wordToSearch) != null) {
					result = new SearchResult(true, 
							onDiskCache.get(wordToSearch));
					inMemoryCache.put(wordToSearch, result.getContent());
				} else {
					result = Dictionary.search(
							frame.getWordToSearch());
					if (result.hasResult() == true) {
						inMemoryCache.put(wordToSearch, result.getContent());
						onDiskCache.put(wordToSearch, result.getContent());
					}
				}
			}
			
			public SearchResult getSearchResult() {
				return result;
			}
		}
		
		SearchThread thread = new SearchThread();
		thread.start();
		
		try {
			thread.join();
		} catch (Exception e1) {
			thread.interrupt();
		}
		
		SearchResult result = thread.getSearchResult();
		frame.setResultArea(result);
	}

}
