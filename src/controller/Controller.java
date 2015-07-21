package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import gui.SimpleGui;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;
import util.Resources;
import util.SearchResult;
import util.dict.Bing;
import util.dict.Bnc;
import util.dict.MerriamWebster;
import util.dict.Stands4;
import util.dict.Youdao;

public class Controller implements ActionListener {

	private SimpleGui frame;
	
	private String lastItemToSearch = null;
	private String audioFileName = null;
	
	public Controller(SimpleGui frame) {
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "search":
			if (needToSearch(frame.getWordToSearch())) {
				search();
			}
			break;
		case "play":
			play();
			break;
		default:
		}
	}
	
	private boolean needToSearch(String item) {
		if (lastItemToSearch == null || 
				lastItemToSearch.equals(item) == false) {
			lastItemToSearch = item;
			return true;
		} else {
			return false;
		}
	}
	
	private void search() {
		
		class YoudaoThread extends Thread {
			@Override
			public void run() {
				String word = frame.getWordToSearch();
				SearchResult result = Youdao.search(word);
				if (result.hasResult()) {
					frame.appendResult(result.getContent());
				}
			}
		}
		
		class WebsterThread extends Thread {
			@Override
			public void run() {
				String wordToSearch = frame.getWordToSearch();
				SearchResult result = MerriamWebster.search(wordToSearch);
				audioFileName = result.getAudioFileName();
				if (audioFileName == null) {
					frame.getPlayButton().setVisible(false);
				} else {
					frame.getPlayButton().setVisible(true);
				}
				if (result.hasResult()) {
					frame.appendResult(result.getContent());
				}
			}
		}
		
		class BingThread extends Thread {
			@Override
			public void run() {
				SearchResult result = Bing.search(frame.getWordToSearch());
				if (result.hasResult()) {
					frame.appendResult(result.getContent());
				}
			}
		}
		
		class Stands4AbbrThread extends Thread {
			@Override
			public void run() {
				SearchResult result = Stands4
						.searchAbbr(frame.getWordToSearch());
				if (result.hasResult()) {
					frame.appendResult(result.getContent());
				}
			}
		}
		
		class BncThread extends Thread {
			@Override
			public void run() {
				SearchResult result = Bnc.search(frame.getWordToSearch());
				if (result.hasResult()) {
					frame.appendResult(result.getContent());
				}
			}
		}
		
		frame.getResultArea().setText("");
		
		Thread[] threadPool = {
				new YoudaoThread(),
				new WebsterThread(),
				new BingThread(),
				new Stands4AbbrThread(),
				new BncThread()
		};
		
		for (Thread thread : threadPool) {
			thread.start();
		}
		
	}
	
	// play audio
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
