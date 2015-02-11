package main;

import java.util.ArrayList;

// https://www4.rgu.ac.uk/files/chapter3%20-%20bp.pdf
public class Neuron {

	private ArrayList<Synapse> inputs;
	public ArrayList<Synapse> outputs;
	private int received;

	int index;
	private boolean isInput = false;
	private static int counter = 0;

	double value;
	double gradient;
	public double error = 0;

	private static double learnRate = 0.6;
	private static double momentumWeight = 0.7;

	public Neuron() {

		this(counter++);
	}

	public Neuron(int index) {

		this.index = index;
		inputs = new ArrayList<Synapse>();
		outputs = new ArrayList<Synapse>();
	}

	public void setAsInput() {

		isInput = true;
		connect(null);
	}

	public void connect(Synapse synapse) {

		inputs.add(synapse);
	}

	public void connectTo(Neuron neuron) {

		Synapse synapse = new Synapse(this, neuron);
		outputs.add(synapse);
		neuron.connect(synapse);
	}

	public void connectTo(Neuron neuron, double weight) {

		Synapse synapse = new Synapse(this, neuron, weight);
		outputs.add(synapse);
		neuron.connect(synapse);
	}

	public void forward(double value) {

		this.value += value;
		received++;

		if (received == inputs.size()) {

			received = 0;

			if (!isInput)
				this.value = activation(this.value);

			// if (outputs.size() == 0)
			// this.value = activation(this.value);
			// else

			for (Synapse synapse : outputs) {

				double outval = getOutputVal(synapse);
				// double out = activation(outval);
				synapse.connectedTo.forward(outval);
			}
		}
	}

	public void backward(double gradient) {

		this.gradient += gradient;
		received++;

		if (received >= outputs.size()) {

			received = 0;

			updateError();

			for (Synapse synapse : inputs) {

				if (synapse == null)
					return;

				updateWeight(synapse);

				synapse.connectedFrom.backward(this.error * synapse.weight);
			}
		}
	}

	public double getOutputVal(Synapse synapse) {

		return value * synapse.weight;
	}

	private double activation(double sum) {

		// Sigmoid [0, 1]
		return (1d / (1 + Math.exp(-sum)));

		// hyperbolic tanjent [-1, 1]
		// return Math.tanh(sum);
	}

	// TODO find out what to use for
	public static double activationDerivative(double x) {

		// sigmoid derivative
		double eToX = Math.pow(Math.E, x);
		return eToX / Math.pow(eToX + 1, 2);

		// hyperbolic derivative
		// return (1f - Math.pow(Math.tanh(x), 2));

		// hyperbolic derivative approximation
		// return 1f - x * x;
	}

	// public double getOutputError(double target) {
	//
	// // double error = Math.pow(target - output, 2) / 2;
	// // double value = activation(this.value);
	// double error = value * (1 - value) * (target - value);
	//
	// System.out.println("output error " + index + ": " + "(" + target
	// + " - " + value + ") * (1 - " + value + ") * " + value + " = "
	// + error);
	//
	// return error;
	// }

	private void updateError() {

		// double value = activation(this.value);

		error = value * (1 - value) * (gradient);
	}

	private void updateWeight(Synapse synapse) {

		// double deltaWeight = -learnRate * (2/5);
		if (synapse != null) {
			synapse.deltaWeight = learnRate * error
					* synapse.connectedFrom.value + synapse.deltaWeight
					* momentumWeight;
			synapse.weight += synapse.deltaWeight;
		}
	}

	public static void resetCounter() {

		counter = 0;
	}

	public String toString() {

		return "Neuron " + index + " = [in: " + inputs.size() + ", out: "
				+ outputs.size() + ", val: " + value + ']';
	}
}
