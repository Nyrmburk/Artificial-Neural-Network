package main;

import java.awt.image.BufferedImage;

public class Image {

	public static double[][] getTargets(BufferedImage image) {

		int totalSize = image.getWidth() * image.getWidth();
		double[][] targets = new double[totalSize][3];

		for (int y = 0; y < image.getHeight(); y++) {

			for (int x = 0; x < image.getWidth(); x++) {

				targets[x + y * image.getWidth()] = getArrayFromInt(image.getRGB(x, y));
			}
		}

		return targets;
	}

	private static double[] getArrayFromInt(int rgb) {

		double[] array = new double[3];

		array[0] = (double) ((rgb & 0x00FF0000) >> 16) / 255;
		array[1] = (double) ((rgb & 0x0000FF00) >> 8) / 255;
		array[2] = (double) (rgb & 0x000000FF) / 255;

		return array;
	}
	
	private static int getIntFromArray(double[] array, int offset) {

		int rgb = 0;
		rgb |= (int)(array[offset + 0] * 255) << 16;
		rgb |= (int)(array[offset + 1] * 255) << 8;
		rgb |= (int)(array[offset + 2] * 255) << 0;

		return rgb;
	}

	public static BufferedImage createImageFromOutput(double[] output,
			int width, int height) {

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				image.setRGB(x, y, getIntFromArray(output, (x + y * width) * 3));
			}
		}

		return image;
	}
}
