package main;

public class NeuronLayer {

	public static void attachLayers(int fromStart, int fromEnd, int toStart,
			int toEnd, Neuron[] neurons) {

		for (int i = fromStart; i < fromEnd; i++) {

			for (int j = toStart; j < toEnd; j++) {

				neurons[i].connectTo(neurons[j]);
			}
		}
	}
}
