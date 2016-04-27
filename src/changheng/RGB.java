package changheng;

import java.awt.Color;

public class RGB {
	private int B;
	private int G;
	private int R;

	RGB() {

	}

	RGB(int B, int G, int R) {
		this.B = B;
		this.G = G;
		this.R = R;
	}

	public static void main(String[] args) {

	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public int getR() {
		return R;
	}

	public void setR(int r) {
		R = r;
	}

	public Color getColor() {
		return new Color(R, G, B);
	}

}
