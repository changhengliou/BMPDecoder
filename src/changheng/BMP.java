package changheng;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileSystemView;

public class BMP extends JFrame {
	private static byte[] bmpHeader = new byte[12];
	private static byte[] bmpInfo = new byte[40];
	private static int offset;
	private static int fSize;
	private static int headerSize;
	private static int width; // 500
	private static int height; // 375
	private static int bitsPerPixels; // 24
	private static int compression; // no
	private static int HResolution; // 4724
	private static int VResolution; // 4724
	private static int usedColor; // all
	private static int impColor; // all the same

	private static List<RGB> colorList;

	BMP() {
		init();
	}

	public static void main(String[] args) throws IOException {
		readInfo();
		imageOut();
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				BMP frame = new BMP();
				if (width < 800 && height < 500) {
					frame.setPreferredSize(new Dimension(width + 150, height * 2 + 50));

					ImageIcon img = new ImageIcon(getRawImage());
					JLabel data = new JLabel("Raw Image", img, JLabel.CENTER);
					data.setVerticalTextPosition(JLabel.CENTER);
					frame.getContentPane().add(data, BorderLayout.BEFORE_FIRST_LINE);
				} else {
					frame.setPreferredSize(new Dimension(1920, 1080));
				}
				JLabel result = new JLabel("Left-Right-Reverse Image");
				ImgPanel imgPanel = new ImgPanel(getColorMap(), height, width);
				imgPanel.setBackground(Color.white);
				frame.getContentPane().add(imgPanel, BorderLayout.CENTER);
				frame.getContentPane().add(result, BorderLayout.LINE_END);

				frame.pack();
				frame.setVisible(true);
			}
		});

	}

	public void init() {
		setTitle("BMP");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
	}

	public static String getPath() {
		FileSystemView filesys = FileSystemView.getFileSystemView();
		StringBuilder out = new StringBuilder();
		out.append(filesys.getHomeDirectory());

		for (int i = 0; i < out.length(); i++) {
			i = out.indexOf("\\", i);
			if (i == -1)
				break;
			out.insert(i, "\\");
			i++;
		}
		out.append("\\\\a.bmp");
		return out.toString();
	}
	
	public static String getOutPath() {
		FileSystemView filesys = FileSystemView.getFileSystemView();
		StringBuilder out = new StringBuilder();
		out.append(filesys.getHomeDirectory());

		for (int i = 0; i < out.length(); i++) {
			i = out.indexOf("\\", i);
			if (i == -1)
				break;
			out.insert(i, "\\");
			i++;
		}
		out.append("\\\\new.bmp");
		return out.toString();
	}
	

	public static BufferedImage getRawImage() {
		BufferedImage img;
		try {
			img = ImageIO.read(new File(getPath()));
			return img;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void readInfo() throws IOException {
		FileInputStream inputStream = new FileInputStream(getPath());
		DataInputStream in = new DataInputStream(inputStream);
		if (in.readShort() != 16973)
			throw new IOException("File format not supported!");

		in.readFully(bmpHeader);
		in.readFully(bmpInfo);

		fSize = ((bmpHeader[0] & 0xff)) + ((bmpHeader[1] & 0xff) << 8) + ((bmpHeader[2] & 0xff) << 16)
				+ (bmpHeader[3] & 0xff << 24);
		offset = bmpHeader[8];
		headerSize = bmpInfo[0];
		width = ((bmpInfo[4] & 0xff)) + ((bmpInfo[5] & 0xff) << 8) + ((bmpInfo[6] & 0xff) << 16)
				+ (bmpInfo[7] & 0xff << 24);
		height = ((bmpInfo[8] & 0xff)) + ((bmpInfo[9] & 0xff) << 8) + ((bmpInfo[10] & 0xff) << 16)
				+ (bmpInfo[11] & 0xff << 24);
		bitsPerPixels = bmpInfo[14];
		compression = bmpInfo[16];
		HResolution = ((bmpInfo[24] & 0xff)) + ((bmpInfo[25] & 0xff) << 8) + ((bmpInfo[26] & 0xff) << 16)
				+ (bmpInfo[27] & 0xff << 24);
		VResolution = ((bmpInfo[28] & 0xff)) + ((bmpInfo[29] & 0xff) << 8) + ((bmpInfo[30] & 0xff) << 16)
				+ (bmpInfo[31] & 0xff << 24);
		usedColor = bmpInfo[32];
		impColor = bmpInfo[36];
		readImg(in);
	}

	public static void readImg(DataInputStream in) throws IOException {
		byte[] imgData = new byte[fSize - offset];
		in.readFully(imgData);
		colorList = new ArrayList<RGB>();

		int tempR = 0, tempG = 0, tempB = 0;
		for (int i = 0; i < 3 * width * height; i++) {
			tempB = (imgData[i] & 0xff);
			i++;
			tempG = (imgData[i] & 0xff);
			i++;
			tempR = (imgData[i] & 0xff);
			colorList.add(new RGB(tempB, tempG, tempR));
		}
	}

	public static RGB[][] getColorMap() {
		RGB[][] map = new RGB[width][height];
		for (int i = 0, count = 0; i < height; i++) {// 500
			for (int j = 0; j < width; j++) { // 375
				map[j][i] = colorList.get(count);
				count++;
			}
		}
		return map;
	}

	public static void imageOut() {
		try {
			FileInputStream in = new FileInputStream(new File(getPath()));
			int[] temp = new int[fSize];
			int i = 0;

			while (in.available() > 0) {
				temp[i] = in.read();
				i++;
			}

			FileOutputStream out = new FileOutputStream(getOutPath());

			for (int x = 0; x < offset; x++) {
				out.write(temp[x]);
			}
			out.flush();

			int count = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width * 3; x += 3) {
					count = offset + (y + 1) * width * 3;
					out.write(temp[count - 3 - x]); // B
					out.write(temp[count - 2 - x]); // G
					out.write(temp[count - 1 - x]); // R
				}
			}

			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
