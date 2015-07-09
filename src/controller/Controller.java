package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import cache.Cache;
import cache.SimpleCache;
import gui.SimpleGui;
import util.SearchResult;
import util.bnc.OnlineBnc;
import util.youdao.YoudaoDictionary;

public class Controller implements ActionListener {

	private SimpleGui frame;
	private Cache inMemoryCache;
	private DB db;
	private ConcurrentNavigableMap<String, String> onDiskCache;
	
	public Controller(SimpleGui frame) {
		this.frame = frame;
		inMemoryCache = new SimpleCache();
		db = DBMaker.fileDB(new File("yydict" + File.separator + "dictionary"))
				.closeOnJvmShutdown()
				.make();
		onDiskCache = db.treeMap("youdao");
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
					result = YoudaoDictionary.search(
							frame.getWordToSearch());
					if (result.hasResult() == true) {
						inMemoryCache.put(wordToSearch, result.getContent());
						onDiskCache.put(wordToSearch, result.getContent());
						db.commit();
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
		frame.setResultArea(frame.getYoudaoArea(), result);
		
		frame.setResultArea(frame.getBncArea(), 
				OnlineBnc.search(frame.getWordToSearch()));
		frame.getYoudaoArea().setCaretPosition(0);
		frame.getBncArea().setCaretPosition(0);
		
		frame.getPlayButton().setVisible(false);
		
	}

}
