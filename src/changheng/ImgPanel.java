package changheng;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ImgPanel extends JPanel {
	private RGB[][] colorMap;
	private int height;
	private int width;

	ImgPanel(RGB[][] colorMap, int height, int width) {
		super();
		this.colorMap = colorMap;
		this.height = height;
		this.width = width;
	}

	public void paint(Graphics g) {
		super.paint(g);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				g.setColor(colorMap[j][i].getColor());
				g.drawLine(width-j, height - i, width-j, height - i);
			}
		}

	}

}
