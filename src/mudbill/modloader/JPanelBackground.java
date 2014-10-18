package mudbill.modloader;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JPanelBackground extends JPanel {

	private static final long serialVersionUID = 1L;
	Image bg;

	/**
	 * Paints a graphics file.
	 * @param g = The file to draw.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(bg, 0, 0, this);
	}

	/**
	 * Draws the background with an image
	 * @param file = The file to draw.
	 */
	public JPanelBackground(String file) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(file));
		bg = icon.getImage();
	}
}