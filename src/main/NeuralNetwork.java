package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;

public class NeuralNetwork {

	private Neuron[] neurons;
	int inputs;
	int processing;
	int outputs;

	public NeuralNetwork(File file) {

		loadNetwork(file);
		Neuron.resetCounter();
	}

	public NeuralNetwork(int inputs, int outputs, int processingCount,
			int procLayerCount) {

		this.inputs = inputs;
		this.processing = processingCount * procLayerCount;
		this.outputs = outputs;

		neurons = new Neuron[inputs + processing + outputs];

		for (int i = 0; i < neurons.length; i++)
			neurons[i] = new Neuron();

		for (int i = 0; i < inputs; i++)
			neurons[i].setAsInput();

		NeuronLayer.attachLayers(0, inputs, inputs, inputs + processingCount,
				neurons);

		for (int i = 0; i < procLayerCount - 1; i++) {

			NeuronLayer.attachLayers(inputs + processingCount * i, inputs
					+ processingCount * (i + 1), inputs + processingCount
					* (i + 1), inputs + processingCount * (i + 2), neurons);
		}

		NeuronLayer.attachLayers(inputs + processing - processingCount, inputs
				+ processing, inputs + processing, inputs + processing
				+ outputs, neurons);

		Neuron.resetCounter();
	}

	public double[] forwardPropogate(double[] inputVals) {

		double[] outputVals = new double[outputs];

		for (Neuron neuron : neurons)
			neuron.value = 0;

		for (int i = 0; i < inputs; i++)
			neurons[i].forward(inputVals[i]);

		for (int i = 0; i < outputs; i++)
			outputVals[i] = neurons[i + inputs + processing].value;

		return outputVals;
	}

	public double backPropogate(double[] inputVals, double[] targetOutput) {

		for (Neuron neuron : neurons)
			neuron.gradient = 0;

		for (int i = inputs + processing; i < inputs + processing + outputs; i++) {

			neurons[i].backward(targetOutput[i - (inputs + processing)]
					- neurons[i].value);
		}

		double average = 0;
		for (int i = 0; i < targetOutput.length; i++) {

			average += targetOutput[i] - neurons[inputs + processing + i].value;
		}

		return average;
	}

	public void saveNetwork(File file) {

		BufferedWriter out = null;
		Formatter formatter = null;

		try {

			file.createNewFile();
			out = new BufferedWriter(new FileWriter(file));

			StringBuilder sb = new StringBuilder();
			formatter = new Formatter(sb);

			out.write("top " + inputs + " " + processing + " " + outputs
					+ "\n\n");

			for (Neuron neuron : neurons) {

				for (Synapse synapse : neuron.outputs) {

					out.write(formatter.format("s %+06f %d %d%n",
							synapse.weight, neuron.index,
							synapse.connectedTo.index).toString());
					sb.setLength(0);
				}
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			if (formatter != null)
				formatter.close();
		}
	}

	public void loadNetwork(File file) {

		BufferedReader in = null;

		try {

			file.createNewFile();
			in = new BufferedReader(new FileReader(file));

			String input;
			while (in.ready()) {

				input = in.readLine();

				if (input.isEmpty())
					continue;

				processLine(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		for (int i = 0; i < inputs; i++)
			neurons[i].setAsInput();

		Neuron.resetCounter();
	}

	private void processLine(String line) {

		String[] splitLine = line.split("\\s+");

		switch (splitLine[0]) {

		case "top":

			inputs = Integer.parseInt(splitLine[1]);
			processing = Integer.parseInt(splitLine[2]);
			outputs = Integer.parseInt(splitLine[3]);
			neurons = new Neuron[inputs + processing + outputs];
			break;
		case "s":

			double weight = Double.parseDouble(splitLine[1]);
			int from = Integer.parseInt(splitLine[2]);
			int to = Integer.parseInt(splitLine[3]);

			if (neurons[from] == null)
				neurons[from] = new Neuron(from);
			if (neurons[to] == null)
				neurons[to] = new Neuron(to);

			neurons[from].connectTo(neurons[to], weight);
			break;
		}
	}
}
