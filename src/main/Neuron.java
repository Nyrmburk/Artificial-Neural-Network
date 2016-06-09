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

	float value;
	float gradient;
	public float error = 0;

	private static float learnRate = 0.6f;
	private static float momentumWeight = 0.7f;
//	private static float learnRate = 3;
//	private static float momentumWeight = 1;

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

	public void connectTo(Neuron neuron, float weight) {

		Synapse synapse = new Synapse(this, neuron, weight);
		outputs.add(synapse);
		neuron.connect(synapse);
	}

	public void forward(float value) {

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

				float outval = getOutputVal(synapse);
				// float out = activation(outval);
				synapse.connectedTo.forward(outval);
			}
		}
	}

	public void backward(float gradient) {

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

	public float getOutputVal(Synapse synapse) {

		return value * synapse.weight;
	}

	private float activation(float sum) {

		// Sigmoid [0, 1]
		return (1f / (1 + (float) Math.exp(-sum)));
//            return sum;

		// hyperbolic tanjent [-1, 1]
		// return Math.tanh(sum);
	}

	// TODO find out what to use for
	public static float activationDerivative(float x) {

		// sigmoid derivative
		float eToX = (float) Math.pow(Math.E, x);
		float eToX1 = eToX + 1;
		return eToX / (eToX1 * eToX1);

		// hyperbolic derivative
		// return (1f - Math.pow(Math.tanh(x), 2));

		// hyperbolic derivative approximation
		// return 1f - x * x;
	}

	// public float getOutputError(float target) {
	//
	// // float error = Math.pow(target - output, 2) / 2;
	// // float value = activation(this.value);
	// float error = value * (1 - value) * (target - value);
	//
	// System.out.println("output error " + index + ": " + "(" + target
	// + " - " + value + ") * (1 - " + value + ") * " + value + " = "
	// + error);
	//
	// return error;
	// }

	private void updateError() {

		// float value = activation(this.value);

		error = value * (1 - value) * (gradient);
	}

	private void updateWeight(Synapse synapse) {

		// float deltaWeight = -learnRate * (2/5);
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
