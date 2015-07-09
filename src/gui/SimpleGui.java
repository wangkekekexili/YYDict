package gui;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.Controller;
import util.SearchResult;

@SuppressWarnings("serial")
public class SimpleGui extends JFrame {
	
	private JTextField searchTextField;
	private JTextArea youdaoResultTextArea;
	private JTextArea onlineBncResultTextArea;
	
	public SimpleGui() {
		
		Controller listener = new Controller(this);
		
		setTitle("YY Dict");
		try {
			setIconImage(ImageIO.read(new File("resource" + File.separator + 
					"icon" + File.separator + "icon.png")));
		} catch (IOException e) {}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		setSize(320, 480);
		getContentPane().setLayout(null);
		
		searchTextField = new JTextField();
		searchTextField.setBounds(40, 5, getWidth()-80, 30);
		getContentPane().add(searchTextField);
		
		searchTextField.addActionListener(listener);
		
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(listener);
		searchButton.setBounds(60, 40, getWidth()-120, 30);
		getContentPane().add(searchButton);
		
		JTabbedPane resultPane = new JTabbedPane();
		resultPane.setBounds(10, 80, getWidth()-20, 350);
		getContentPane().add(resultPane);
		
		// Youdao
		JScrollPane youdaoScrollPane = new JScrollPane();
		youdaoResultTextArea = new JTextArea();
		youdaoResultTextArea.setText("");
		youdaoResultTextArea.setLineWrap(true);
		youdaoScrollPane.getViewport().add(youdaoResultTextArea);
		
		// Online BNC
		JScrollPane bncScrollPane = new JScrollPane();
		onlineBncResultTextArea = new JTextArea();
		onlineBncResultTextArea.setText("");
		onlineBncResultTextArea.setLineWrap(true);
		bncScrollPane.getViewport().add(onlineBncResultTextArea);

		// add panes to tabbed pane
		resultPane.add("Youdao", youdaoScrollPane);
		resultPane.add("BNC", bncScrollPane);
		
	}

	public String getWordToSearch() {
		return searchTextField.getText();
	}
	
	public JTextArea getYoudaoArea() {
		return youdaoResultTextArea;
	}
	
	public JTextArea getBncArea() {
		return onlineBncResultTextArea;
	}
	
	public void setResultArea(JTextArea area, SearchResult result) {
		if (result.hasResult() == true) {
			area.setForeground(Color.BLACK);
			area.setText(result.getContent());
		} else {
			area.setForeground(Color.RED);
			area.setText(result.getContent());
		}
	}
	
}
