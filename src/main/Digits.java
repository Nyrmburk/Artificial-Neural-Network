package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

public class Digits {

	public static void main(String[] args) {
		
		int size = 28;
//		NeuralNetwork ann = new NeuralNetwork(3, 2, 2, 1);
		NeuralNetwork ann = new NeuralNetwork(new File("M:\\network.ann"));
//		NeuralNetwork ann = new NeuralNetwork(new File("M:\\test.ann"));
		float[][] data = null;
		byte[] expectedOutputs = null;
		
		try {
			data = MNISTLoader.loadImages(new File("mnist\\train-images.idx3-ubyte"));
			expectedOutputs = MNISTLoader.loadLabels(new File("mnist\\train-labels.idx1-ubyte"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		data = loadDigits(size);
//		expectedOutputs = new byte[data.length];
//		for (int i = 0; i < data.length; i++)
//			expectedOutputs[i] = (byte) (i / (data.length / 10));
		
//		Random random = new Random();
//		
//		int iterations = 500000;
//		float[] target = new float[10];
//		for (int i = 0; i < iterations; i++) {
//			
//			int index = random.nextInt(data.length);
//			int expected = expectedOutputs[index];
////			int expected = 3;
//			target[expected] = 1;
//			ann.forwardPropagate(data[index]);
//			ann.backPropagate(target);
//			target[expected] = 0;
//			if (100 * i % iterations == 0)
//				System.out.println(i * 100 / iterations + "%");
//		}
		
		System.out.println(Arrays.toString(data[12]));
		System.out.println(Arrays.toString(ann.forwardPropagate(data[12])));
//		System.out.println(Arrays.toString(ann.forwardPropagate(new float[]{0.2f, 0.6f, 0.8f})));
		
		//test
//		int correct = 0;
		for (int i = 46717; i < data.length; i++) {
//			
			try {
				PrintWriter out = new PrintWriter("M:\\" + i + ".dat");
				out.write(Arrays.toString(data[i]));
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			float[] outputs = ann.forwardPropagate(data[i]);
//			float maxVal = 0;
//			int maxIndex = 0;
//			
//			for (int j = 0; j < outputs.length; j++) {
//				
//				if (outputs[j] > maxVal) {
//					maxVal = outputs[j];
//					maxIndex = j;
//				}
//			}
//			
//			if (expectedOutputs[i] == maxIndex) {
//				correct++;
//			} else {
//				BufferedImage image = new BufferedImage(28,  28, BufferedImage.TYPE_BYTE_GRAY);
//				for (int y = 0; y < 28; y++) {
//					for (int x = 0; x < 28; x++) {
//						int rgb = (int)(data[i][y * 28 + x] * 255);
//						rgb = rgb << 16 | rgb << 8 | rgb;
//						image.setRGB(x, y, rgb);
//					}
//				}
//				try {
////					ImageIO.write(image, "PNG", new File("fail\\" + i + "_" + expectedOutputs[i] + ".png"));
//					ImageIO.write(image, "PNG", new File("M:\\" + i + "_" + expectedOutputs[i] + "_" + maxIndex + ".png"));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
////			System.out.println("expected " + expectedOutputs[i] + ", recieved " + maxIndex);
		}
//		
//		System.out.println(((float) 100 * correct) / data.length + "% correct");
		ann.saveNetwork(new File("M:\\test.ann"));
	}
	
	public static float[][] loadDigits(int size) {
		
		File dir = new File("digits");
		File[] images = dir.listFiles();
		
		float[][] digits = new float[images.length][size * size];
		
		BufferedImage loadedImage = null;
		BufferedImage scaledImage = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
		
		Graphics2D scaledGraphics = scaledImage.createGraphics();
		scaledGraphics.setBackground(Color.WHITE);
		for (int i = 0; i < images.length; i++) {
			
			try {
				loadedImage = ImageIO.read(images[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			scaledGraphics.clearRect(0, 0, size, size);
			scaledGraphics.drawImage(loadedImage, 0, 0, size, size, null);
			
			int[] data = scaledImage.getRGB(0, 0, size, size, null, 0, size);
			for (int j = 0; j < data.length; j++) {
				
				digits[i][j] = 1 - ((float) new Color(data[j]).getRed()) / 255;
			}
		}
		
		return digits;
	}
}
