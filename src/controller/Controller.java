package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import javax.print.attribute.standard.MediaPrintableArea;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import gui.SimpleGui;
import util.SearchResult;
import util.bnc.OnlineBnc;
import util.cache.Cache;
import util.cache.InMemoryHashMapCache;
import util.cache.OnDiskMapdbCache;
import util.webster.WebsterHelper;
import util.youdao.YoudaoDictionary;

public class Controller implements ActionListener {

	private SimpleGui frame;
	private Cache inMemoryCache;
	private Cache onDiskCache;
	
	private String audioFileName = null;
	
	public Controller(SimpleGui frame) {
		this.frame = frame;
		inMemoryCache = new InMemoryHashMapCache();
		onDiskCache = new OnDiskMapdbCache();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "search":
			search();
			break;
		case "play":
			play();
			break;
		default:
		}
	}
	
	private void search() {
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
		
		// get audio
		audioFileName = WebsterHelper.getAudio(frame.getWordToSearch());
		if (audioFileName == null) {
			frame.getPlayButton().setVisible(false);
		} else {
			frame.getPlayButton().setVisible(true);
		}
	}
	
	private void play() {
		if (audioFileName == null) {
			frame.getPlayButton().setVisible(false);
		}
		try {
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(
					new File("yydict" + File.separator + "audio" +
							File.separator + audioFileName));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInput);
			clip.start();
		} catch (Exception e) {
			frame.getPlayButton().setVisible(false);
		}
	}

}
