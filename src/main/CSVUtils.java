package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CSVUtils {

	public static String[][] loadCSV(File file) throws IOException {

		ArrayList<String[]> table = new ArrayList<String[]>();

		BufferedReader in = null;

		try {

			in = new BufferedReader(new FileReader(file));

			Pattern p = Pattern.compile("\\s*,\\s*");

			while (in.ready()) {

				table.add(p.split(in.readLine()));
			}

		} finally {
			if (in != null) {
				in.close();
			}
		}

		String[][] output = new String[table.size()][0];

		for (int i = 0; i < output.length; i++) {

			output[i] = table.get(i);
		}

		table.clear();

		return output;
	}

	public static void saveCSV(float[][] data, File file) throws IOException {

		BufferedWriter out = null;

		try {

			if (!file.exists())
				file.createNewFile();

			out = new BufferedWriter(new FileWriter(file));

			for (int i = 0; i < data.length; i++) {

				out.write(Float.toString(data[i][0]));

				for (int j = 1; j < data[i].length; j++) {

					out.write(", " + data[i][j]);
				}

				out.newLine();
			}

		} finally {

			if (out != null) {
				out.close();
			}
		}
	}

	public static float[][] toFloats(String[][] data) {

		float[][] values = new float[data.length][0];

		for (int i = 0; i < data.length; i++) {

			values[i] = new float[data[i].length];
			for (int j = 0; j < data[i].length; j++) {

				values[i][j] = Float.valueOf(data[i][j]);
			}
		}

		return values;
	}
}
