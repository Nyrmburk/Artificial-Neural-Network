package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) {

		NeuralNetwork ann = new NeuralNetwork(2, 3, 16, 2);
		//NeuralNetwork ann = new NeuralNetwork(new File("saves\\2015-01-11T03.56.40.ann"));
		//NeuralNetwork ann = new NeuralNetwork(new File("saves\\example.ann"));
		//NeuralNetwork ann = new NeuralNetwork(new File("saves\\sample.ann"));
		//NeuralNetwork ann = new NeuralNetwork(new File("saves\\image.ann"));

//		float[] inputs = { 0.35, 0.9 };
//		float[] targets = { 0.5};
//		float[] outputs = null;
//
//		for (int i = 0; i < 50; i++) {
//
//			outputs = ann.forwardPropogate(inputs);
//			System.out.println(Arrays.toString(outputs));
//			ann.backPropogate(targets);
//		}
		
		try {
			ImageIO.write(trainFromImage(ann, new File("training\\flower.png")), "PNG", new File("saves\\flower.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			CSVUtils.saveCSV(trainFromCSV(ann, new File("training\\data.csv")), new File("saves\\learned.csv"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static float[][] trainFromCSV(NeuralNetwork ann, File csv) {
		
		float[][] values = null;
		
		try {
			 values = CSVUtils.toFloats(CSVUtils.loadCSV(csv));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		float max = Integer.MIN_VALUE;
		float min = Integer.MAX_VALUE;
		for (int i = 0; i < values.length; i++) {
			
			for (int j = 0; j < values[i].length; j++) {
				
				if (values[i][j] > max)
					max = values[i][j];
				if (values[i][j] < min)
					min = values[i][j];
			}
		}
		
		for (int i = 0; i < values.length; i++) {
			
			for (int j = 0; j < values[i].length; j++) {
				
				values[i][j] = (values[i][j] - min) / (max - min);
			}
		}
		
		Random rand = new Random();
		
		for (int i = 0; i < 50000000; i++) {
			
			int index = rand.nextInt(values.length);
			
			ann.forwardPropagate(new float[]{(float) index / values.length});
			ann.backPropagate(values[index]);
		}
		
		for (int i = 0; i < values.length; i++) {
			
			values[i] = ann.forwardPropagate(new float[]{(float) i / values.length});
		}
		
		return values;
	}

	public static BufferedImage trainFromImage(NeuralNetwork ann, File imageFile) {

		float[] inputs = new float[2];
		float[] outputs = null;
		float[] fullOutput = null;
		float[][] otherTargets = null;

		BufferedImage image = null;
		try {
			image = ImageIO.read(imageFile);
			otherTargets = Image.getTargets(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Random rand = new Random();
		int iterations = 10000000;
		for (int i = 0; i < iterations; i++) {

			int x = rand.nextInt(image.getWidth());
			int y = rand.nextInt(image.getHeight());
			inputs[0] = (float) x / image.getWidth();
			inputs[1] = (float) y / image.getHeight();
			ann.forwardPropagate(inputs);
			ann.backPropagate(otherTargets[x + y * image.getWidth()]);

			if (i % (iterations / 100) == 0)
				System.out.println((i / (iterations / 100)) + 1 + "%");
		}

		// int width = image.getWidth();
		// int height = image.getHeight();
		int width = 1024;
		int height = 1024;

		fullOutput = new float[height * width * 3];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				inputs[0] = (float) x / width;
				inputs[1] = (float) y / height;
				outputs = ann.forwardPropagate(inputs);
				System.arraycopy(outputs, 0, fullOutput, (x + y * width) * 3, 3);
			}
		}

		return Image.createImageFromOutput(fullOutput, width, height);
	}

	public static void save(NeuralNetwork ann) {

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
