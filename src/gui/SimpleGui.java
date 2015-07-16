package gui;

import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.Controller;
import util.Resources;

@SuppressWarnings("serial")
public class SimpleGui extends JFrame {
	
	private JTextField searchTextField;
	private JTextArea resultTextArea;
	private JButton searchButton;
	private JButton playButton;
	
	private final Object object = new Object(); // for synchronization of result area
	
	public SimpleGui() {
		
		Controller listener = new Controller(this);
		
		// Set up this JFrame
		setTitle("YY Dict");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setSize(320, 480);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		// search field
		searchTextField = new JTextField();
		searchTextField.setBounds(40, 5, getWidth()-80, 30);
		getContentPane().add(searchTextField);
		
		searchTextField.addActionListener(listener);

		// audio button
		playButton = new JButton();
		try {
			Image playAudioImage = ImageIO.read(
					new File(Resources.PLAY_BUTTON_IMAGE_FILE));
			playButton.setIcon(new ImageIcon(playAudioImage));
		} catch (IOException e) {
			playButton.setText("play");
		}
		playButton.setBounds(285, 10, 20, 20);
		playButton.setBorder(BorderFactory.createEmptyBorder());
		getContentPane().add(playButton);
		playButton.setVisible(false);
		playButton.setActionCommand("play");
		playButton.addActionListener(listener);
		
		// search button
		searchButton = new JButton("Search");
		searchButton.addActionListener(listener);
		searchButton.setBounds(60, 40, getWidth()-120, 30);
		searchButton.setActionCommand("search");
		getContentPane().add(searchButton);
		
		// for ENTER 
		class KeyDispatcher implements KeyEventDispatcher {
		    public boolean dispatchKeyEvent(KeyEvent e) {
		    	if (e.getKeyCode() == KeyEvent.VK_ENTER &&
		    			e.getID() == KeyEvent.KEY_PRESSED) {
		    		searchButton.doClick();
		    		return true;
		    	} else {
		    		return false;
		    	}
		    }
		}
		KeyboardFocusManager manager =
		         KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyDispatcher());
		
		// result scroll pane
		JScrollPane resultScrollPane = new JScrollPane();
		resultTextArea = new JTextArea();
		resultTextArea.setText("");
		resultTextArea.setLineWrap(true);
		resultTextArea.setWrapStyleWord(true);
		resultScrollPane.getViewport().add(resultTextArea);
		resultScrollPane.setBounds(10, 80, getWidth()-20, 350);
		getContentPane().add(resultScrollPane);
		
	}
	
	public void appendResult(String result) {
		synchronized (object) {
			int currentPosition = resultTextArea.getCaretPosition();
			String oldValue = resultTextArea.getText();
			resultTextArea.setText(oldValue + result); 
			resultTextArea.setCaretPosition(currentPosition);
		}
	}
	
	public String getWordToSearch() {
		return searchTextField.getText();
	}
	
	public JTextArea getResultArea() {
		return resultTextArea;
	}
	
	public JButton getPlayButton() {
		return playButton;
	}
}
