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
import util.dict.WebsterHelper;
import util.dict.Youdao;

public class Controller implements ActionListener {

	private SimpleGui frame;
	
	private String audioFileName = null;
	
	public Controller(SimpleGui frame) {
		this.frame = frame;
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
				String audioFileName = WebsterHelper.getAudio(wordToSearch);
				Controller.this.audioFileName = audioFileName;
				if (audioFileName == null) {
					frame.getPlayButton().setVisible(false);
				} else {
					frame.getPlayButton().setVisible(true);
				}
			}
		}
		
		frame.getResultArea().setText("");
		
		YoudaoThread youdaoThread = new YoudaoThread();
		WebsterThread websterThread = new WebsterThread();
		
		youdaoThread.start();
		websterThread.start();
		
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
