package main;

import java.util.Random;

public class Synapse {

	Neuron connectedTo;
	Neuron connectedFrom;
	double weight;
	double deltaWeight;

	private static Random random = new Random();
	
	public Synapse() {

		this(null, null);
	}
	
	public Synapse(Neuron from, Neuron to) {
		
		this(from, to, random.nextDouble()*2f-1f);
//		this(from, to, random.nextDouble());
	}

	public Synapse(Neuron from, Neuron to, double weight) {

		this.connectedFrom = from;
		this.connectedTo = to;
		this.weight = weight;
	}

	@Override
	public String toString() {

		return super.toString() + "[" + connectedTo + ", " + weight + "]";
	}
}
