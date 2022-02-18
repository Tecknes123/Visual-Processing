import java.util.ArrayList;

public class Neuron
{
	public double val;
	public ArrayList<Neuron> con = new ArrayList<Neuron>();
	public ArrayList<Double> w = new ArrayList<Double>();
	public layer layP;
	
	//For Neurons in hidden or input layers
	public Neuron(layer layerTo)
	{
		layP = layerTo;
		for(int i = 0; i < layP.n.size(); i++)
		{
			con.add(layP.n.get(i));
			w.add((Math.random() * 0.6)-0.3);
		}
	}
	
	//For Neurons in output layer
	public Neuron()
	{
		layP = null;
		con = null;
		w = null;
	}
	
	public Neuron(ArrayList<Neuron> nAL)
	{
		for(int i = 0; i < nAL.size(); i++)
		{
			con.add(layP.n.get(i));
			w.add((Math.random() * 0.6)-0.3);
		}
	}
	
	public void addCon(Neuron n)
	{
		con.add(n);
		w.add((Math.random() * 0.6)-0.3);
	}
}
