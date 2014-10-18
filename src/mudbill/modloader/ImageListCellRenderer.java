package mudbill.modloader;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

class ImageListCellRenderer implements ListCellRenderer<Object>
{
	/**
	 * Method to render a JList cell with both text and icon by making a list of JPanels.
	 * @param jlist = The JList to enable for.
	 * @param value = JPanel created (?).
	 * @param cellIndex = Index of the list.
	 * @param isSelected = Whether the cell is selected.
	 * @param cellHasFocus = Whether the cell has focus.
	 * @return Component of a JPanel.
	 */
	public Component getListCellRendererComponent(JList<?> jlist, Object value, int cellIndex, boolean isSelected, boolean cellHasFocus)
	{
		if (value instanceof JPanel) {
			Component component = (Component) value;
			component.setForeground(new Color(100, 100, 100));
			component.setBackground(isSelected ? UIManager.getColor("Table.focusCellForeground") : new Color(200, 200, 200));
			return component;
		}
		else {
			return new JLabel("???");
		}
	}
}