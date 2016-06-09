package main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MNISTLoader {

	private static final int CHUNK_SIZE = 32768;
	private static final int IMAGES_MAGIC_NUMBER = 0x00000803;
	private static final int LABELS_MAGIC_NUMBER = 0x00000801;
	private static final int LABEL_SIZE = 10; //10 digits

	public static float[][] loadImages(File file) throws IOException {
		
		BufferedInputStream in = null;
		
		in = new BufferedInputStream(new FileInputStream(file), CHUNK_SIZE);
		
		int magicNumber = readInt(in);
		if (magicNumber != IMAGES_MAGIC_NUMBER) {
			in.close();
			throw new IOException("not an image idx file");
		}
		
		int imageCount = readInt(in);
		int rows = readInt(in);
		int columns = readInt(in);
		int size = rows * columns;
		
		float[][] images = new float[imageCount][size];
		byte[] pixels = new byte[size];
		
		for (int i = 0; i < imageCount; i++) {
			
			in.read(pixels, 0, size);
			
			for (int j = 0; j < size; j++) {
				
				
				images[i][j] = ((float) Byte.toUnsignedInt(pixels[j])) / 255f;
			}
		}
		
		in.close();
		return images;
	}
	
	public static byte[] loadLabels(File file) throws IOException {
		
		BufferedInputStream in = null;
		
		in = new BufferedInputStream(new FileInputStream(file), CHUNK_SIZE);
		
		int magicNumber = readInt(in);
		if (magicNumber != LABELS_MAGIC_NUMBER) {
			in.close();
			throw new IOException("not a label idx file");
		}
		
		int labelCount = readInt(in);
		
		byte[] labels = new byte[labelCount];
		in.read(labels, 0, labelCount);
		
		in.close();
		return labels;
	}
	
	private static int readInt(InputStream in) throws IOException {
		
		return in.read() << 24 | in.read() << 16 | in.read() << 8 | in.read();
	}
}
