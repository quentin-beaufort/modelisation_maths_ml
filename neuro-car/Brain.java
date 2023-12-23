import java.awt.Color;
import java.awt.Graphics2D;

public class Brain {

    public Layer[] layers;

    Brain(int... layerSizes) {
        this.layers = new Layer[layerSizes.length - 1];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new Layer(layerSizes[i], layerSizes[i+1]);
        }
    }

    public double[] think(double[] inputs) {
        for (Layer l : this.layers) {
            inputs = l.activate(inputs);
        }
        return inputs;
    }

    public void randomize() {
        for (Layer l : layers) {
            l.randomize();
        }
    }

    public void paint(Graphics2D g) {
        int biggestLay = layers[0].nbIn;
        for (Layer l : layers) {
            if (l.nbOut > biggestLay) biggestLay = l.nbOut;
        }
        int sep = 200 / biggestLay;
        g.setColor(Color.black);

        Point[] currentInputs = new Point[layers[0].nbIn];
        double xOff = sep;
        double yOff = sep + ((biggestLay - layers[0].nbIn * 1.0) * sep/2);
        for (int i = 0; i < layers[0].nbIn; i++) {
            g.fillRect((int) xOff - 2, (int) yOff - 2, 4, 4);
            currentInputs[i] = new Point(xOff, yOff);
            yOff += sep;
        }
        xOff += sep*2;
        Point[] lastInputs = currentInputs;

        for (Layer l : layers) {
            currentInputs = new Point[l.nbOut];
            yOff = sep + ((biggestLay - l.nbOut) * 1.0) * sep/2;
            for (int i = 0; i < l.nbOut; i++) {
                for (int j = 0; j < lastInputs.length; j++) {
                    g.setColor(this.colorFromWeight(l.weights[j][i]));
                    g.drawLine((int) lastInputs[j].x, (int) lastInputs[j].y, (int) xOff, (int) yOff);
                }
                g.setColor(Color.black);
                g.fillRect((int) xOff - 2, (int) yOff - 2, 4, 4);
                currentInputs[i] = new Point(xOff, yOff);
                yOff += sep;
            }
            xOff += sep*2;
            lastInputs = currentInputs;
        }

    }

    public Color colorFromWeight(double weight) {
        if (weight > 1) weight = 1;
        if (weight < -1) weight = -1;
        if (weight >= 0) {
            return new Color((float) 1, (float) (1-weight), (float) (1-weight), (float) weight);
        } else {
            return new Color((float) (1-(weight * -1)), (float) (1-(weight * -1)), 1, (float) weight * -1);
        }
    }

}
