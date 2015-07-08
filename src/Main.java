import javax.swing.SwingUtilities;

import gui.SimpleGui;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new SimpleGui().setVisible(true);;
		});
	}

}
