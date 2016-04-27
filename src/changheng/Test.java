package changheng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.filechooser.FileSystemView;

public class Test {

	public static void main(String[] args) throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("C:\\Users\\haha\\Desktop\\new2.bmp"));
		byte x = (byte) 0x99;
		byte y = (byte) 0x30;
		for (int i = 0; i < 1000; i++) {
			out.write(x);
			out.write(y);
		}

		out.flush();
	}

	public void getPath() {
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
		System.out.println(out);
	}

}
