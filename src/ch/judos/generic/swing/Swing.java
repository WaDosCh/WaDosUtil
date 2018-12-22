package ch.judos.generic.swing;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * @since 02.03.2013
 * @author Julian Schelker
 * @version 1.0 / 02.03.2013
 */
public class Swing {

	/**
	 * sets the scrollbar of the scrollPane to the given value
	 * 
	 * @param panel
	 * @param position
	 */
	public static void scrollPaneSetVerticalPosition(JScrollPane panel, int position) {
		final JScrollPane x = panel;
		final int y = position;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				x.getVerticalScrollBar().setValue(y);
			}
		});
	}

	/**
	 * @param text
	 * @param farbe
	 * @return a border with both a line and the given text as title
	 */
	public static Border createTitledLineBorder(String text, Color farbe) {
		TitledBorder border = BorderFactory.createTitledBorder(text);
		border.setTitleColor(farbe);
		border.setBorder(BorderFactory.createLineBorder(farbe));
		return border;
	}

	/**
	 * @param tree
	 * @return the number of nodes in the JTree
	 */
	public static int getTreeNodesTotal(JTree tree) {
		TreeModel model = tree.getModel();
		Object root = model.getRoot();
		if (root == null)
			return 0;
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) root;
		int anz = 1;
		anz += getTreeNodesTotalAdd(rootNode);
		return anz;
	}

	private static int getTreeNodesTotalAdd(DefaultMutableTreeNode node) {
		int anz = 0;
		for (int i = 0; i < node.getChildCount(); i++) {
			anz += getTreeNodesTotalAdd((DefaultMutableTreeNode) node.getChildAt(i));
		}
		return anz + node.getChildCount();
	}

}
