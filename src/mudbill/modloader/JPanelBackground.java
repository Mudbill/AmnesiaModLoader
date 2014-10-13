package mudbill.modloader;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JPanelBackground extends JPanel {

	  public void paintComponent(Graphics g) {
	    super.paintComponent(g);

	    g.drawImage(bg, 0, 0, this);
	  }
	  
	private static final long serialVersionUID = 1L;
	Image bg;

    public JPanelBackground(String file) {
        ImageIcon icon = new ImageIcon(this.getClass().getResource(file));
        bg = icon.getImage();
    }
}