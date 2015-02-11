package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) {

		NeuralNetwork ann = new NeuralNetwork(2, 3, 28, 3);
		// NeuralNetwork ann = new NeuralNetwork(new
		// File("saves\\example.ann"));
		// NeuralNetwork ann = new NeuralNetwork(new
		// File("saves\\example_2.ann"));
		// NeuralNetwork ann = new NeuralNetwork(new File("saves\\sample.ann"));
//		 NeuralNetwork ann = new NeuralNetwork(new File("saves\\image.ann"));

		double[] inputs = { 0.35, 0.9 };
		double[] targets = { 0.5, 0.3 };
		double[] outputs = null;
		double[][] otherTargets = null;
		double[] fullOutput;

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("training\\bears.jpg"));
			otherTargets = Image.getTargets(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Random rand = new Random();
		for (int i = 0; i < 1000000; i++) {

			int x = rand.nextInt(image.getWidth());
			int y = rand.nextInt(image.getHeight());
			inputs[0] = (double) x / image.getWidth();
			inputs[1] = (double) y / image.getHeight();
			outputs = ann.forwardPropogate(inputs);
			ann.backPropogate(inputs, otherTargets[x + y * image.getWidth()]);
		}

//		for (int i = 0; i < 10; i++) {
//			System.out.println("training iteration " + (i + 1));
//			double precision = 0;
//			for (int y = 0; y < image.getHeight(); y += 5) {
//				for (int x = 0; x < image.getWidth(); x += 5) {
//					inputs[0] = (double) x / image.getWidth();
//					inputs[1] = (double) y / image.getHeight();
//					outputs = ann.forwardPropogate(inputs);
//					// System.out.println(Arrays.toString(outputs));
//					precision += ann.backPropogate(inputs, otherTargets[x + y
//							* image.getWidth()]);
//				}
//				System.out.println(inputs[1]);
//			}
//			System.out.println("error = " + precision
//					/ (image.getHeight() * image.getWidth()));
//		}

		fullOutput = new double[image.getHeight() * image.getWidth() * 3];

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				inputs[0] = (double) x / image.getWidth();
				inputs[1] = (double) y / image.getHeight();
				outputs = ann.forwardPropogate(inputs);
				System.arraycopy(outputs, 0, fullOutput,
						(x + y * image.getWidth()) * 3, 3);
			}
			System.out.println(inputs[1]);
		}

		// for (int i = 0; i < 50; i++) {
		//
		// outputs = ann.forwardPropogate(inputs[0]);
		// // System.out.println(Arrays.toString(outputs));
		// ann.backPropogate(inputs, inputs);
		// }

		try {
			ImageIO.write(Image.createImageFromOutput(fullOutput,
					image.getWidth(), image.getHeight()), "PNG", new File(
					"saves\\image.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		java.util.Calendar now = java.util.Calendar.getInstance();
		File file = new File("saves" + File.separator
				+ String.format("%04d", now.get(Calendar.YEAR)) + "-"
				+ String.format("%02d", now.get(Calendar.MONTH)) + "-"
				+ String.format("%02d", now.get(Calendar.DAY_OF_MONTH)) + "T"
				+ String.format("%02d", now.get(Calendar.HOUR)) + "."
				+ String.format("%02d", now.get(Calendar.MINUTE)) + "."
				+ String.format("%02d", now.get(Calendar.SECOND)) + ".ann");
		 ann.saveNetwork(file);
	}
}
