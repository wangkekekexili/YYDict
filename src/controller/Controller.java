package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentNavigableMap;

import javax.print.attribute.standard.MediaPrintableArea;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import gui.SimpleGui;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;
import util.Resources;
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
		class YoudaoThread extends Thread {
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
					result = YoudaoDictionary.search(wordToSearch);
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
		
		class BNCThread extends Thread {
			private SearchResult result = new SearchResult(
					false, "Search thread error.");
			@Override
			public void run() {
				String wordTOSearch = frame.getWordToSearch();
				result = OnlineBnc.search(wordTOSearch);
			}
			public SearchResult getSearchResult() {
				return result;
			}
		}
		
		class WebsterThread extends Thread {
			private SearchResult result = new SearchResult(
					false, "Search thread error.");
			@Override
			public void run() {
				String wordToSearch = frame.getWordToSearch();
				String audioFileName = WebsterHelper.getAudio(wordToSearch);
				Controller.this.audioFileName = audioFileName;
			}
		}
		
		YoudaoThread youdaoThread = new YoudaoThread();
		BNCThread bncThread = new BNCThread();
		WebsterThread websterThread = new WebsterThread();
		
		youdaoThread.start();
		bncThread.start();
		websterThread.start();
		
		try {
			youdaoThread.join();
			bncThread.join();
			websterThread.join();
		} catch (Exception e1) {
			youdaoThread.interrupt();
			bncThread.interrupt();
			websterThread.interrupt();
		}
		
		frame.setResultArea(frame.getYoudaoArea(),
				youdaoThread.getSearchResult());
		frame.getYoudaoArea().setCaretPosition(0);
		
		frame.setResultArea(frame.getBncArea(), 
				bncThread.getSearchResult());
		frame.getBncArea().setCaretPosition(0);
		
		// get audio
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
			// Redirect out and err to prevent beats library to print on screen.
			PrintStream out = System.out;
			PrintStream err = System.err;
			System.setOut(new PrintStream(new OutputStream() {
				@Override
				public void write(int b) throws IOException {
				}
			}));
			System.setErr(new PrintStream(new OutputStream() {
				@Override
				public void write(int b) throws IOException {
				}
			}));
			
			AudioContext context = new AudioContext();
			String file = Resources.getAudioLocation(audioFileName);
			SamplePlayer player = new SamplePlayer(context, 
					SampleManager.sample(file));
			Gain g = new Gain(context, 2, 0.2f);
			g.addInput(player);
			context.out.addInput(g);
			context.start();
			
			// Set back standard out and err.
			try {
				Thread.sleep(1000);
			} catch (Exception e){}
			System.setOut(out);
			System.setErr(err);
		} catch (Exception e) {
			frame.getPlayButton().setVisible(false);
		}
	}

}
