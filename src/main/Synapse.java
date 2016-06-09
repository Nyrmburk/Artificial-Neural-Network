package main;

import java.util.Random;

public class Synapse {

	Neuron connectedTo;
	Neuron connectedFrom;
	float weight;
	float deltaWeight;

	private static Random random = new Random();
	
	public Synapse() {

		this(null, null);
	}
	
	public Synapse(Neuron from, Neuron to) {
		
		this(from, to, random.nextFloat()*2f-1f);
//		this(from, to, random.nextFloat());
	}

	public Synapse(Neuron from, Neuron to, float weight) {

		this.connectedFrom = from;
		this.connectedTo = to;
		this.weight = weight;
	}

	@Override
	public String toString() {

		return super.toString() + "[" + connectedTo + ", " + weight + "]";
	}
}
