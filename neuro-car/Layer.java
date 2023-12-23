public class Layer {
    
    public int nbIn, nbOut;
    public double[][] weights;
    public double[] biases;

    Layer(int nbIn, int nbOut) {
        this.nbIn = nbIn;
        this.nbOut = nbOut;
        this.weights = new double[nbIn][nbOut];
        this.biases = new double[nbOut];
    }

    public double[] activate(double[] inputs) {
        double[] outputs = new double[nbOut];
        for (int out = 0; out < nbOut; out++) {
            double output = biases[out];
            for (int in = 0; in < nbIn; in++) {
                output += inputs[in] * weights[in][out];
            }
            outputs[out] = activation(output);
        }
        return outputs;
    }

    public double activation(double in) {
        double e2w = Math.exp(2 * in);
        return (e2w - 1) / (e2w + 1);
    }

    public void randomize() {
        for (int i = 0; i < biases.length; i++) {
            biases[i] = (Math.random() * 2) - 1;
        }
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = (Math.random() * 2) - 1;
            }
        }
    }

}
