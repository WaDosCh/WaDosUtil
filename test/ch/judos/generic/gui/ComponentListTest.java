package ch.judos.generic.gui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @since 20.09.2014
 * @author Julian Schelker
 */
public class ComponentListTest extends JFrame {

	private static final long serialVersionUID = 3202698926181997884L;

	public static void main(String[] args) {
		new ComponentListTest().showTest();
	}

	public void showTest() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ComponentList<Integer> list = new ComponentList<>();
		list.setCenterAligned(true);
		for (int i = 0; i < 3; i++) {
			ComponentListEntry entry = new ComponentListEntry();
			JLabel label = new JLabel("Entry Nr. " + i);
			label.setOpaque(false);
			entry.add(label);
			JButton b = new JButton("test");
			b.setPreferredSize(new Dimension(80, 50));
			entry.add(b);

			list.addEntry(entry, 0);
		}

		this.add(list.getContent());

		this.setSize(300, 400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
