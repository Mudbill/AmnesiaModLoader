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