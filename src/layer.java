import java.util.ArrayList;

public class layer 
{
	public ArrayList<Neuron> n = new ArrayList<Neuron>();
	/*public double[] l;
	public double[][]w;*/
	public boolean isReal = false;
	
	public layer()
	{
		
	}
	
	public layer(int size, layer layerTo)
	{
		if(layerTo.n.size() == 38 && !isReal)
		{
			System.out.println("ISGOOD");
			isReal = true;
		}
		for(int i = 0; i < size; i++)
			n.add(new Neuron(layerTo));
	}
	
	public layer(int size)
	{
		for(int i = 0; i < size; i++)
			n.add(new Neuron());
	}
	
	public void set(int[] Q)
	{
		//System.out.print("Input set to: (");
		for(int i = 0; i < Q.length; i++)
		{
			n.get(i).val = (double)(Q[i]*1.0);
			//System.out.print(" "+l[i]+",");
		}
		//System.out.println(")");
	}
	
	public void set(double[] Q)
	{
		//System.out.print("Input set to: (");
		for(int i = 0; i < Q.length; i++)
		{
			n.get(i).val = Q[i]*1.0;
			//System.out.print(" "+l[i]+",");
		}
		//System.out.println(")");
	}
	
	/*public void randomizeWeights()
	{
		for(int a = 0; a < w.length; a++)
		{
			for(int b = 0; b < w[a].length; b++)
			{
				w[a][b] = (Math.random() * 0.6)-0.3;
			}
		}
	}*/
	
	public double[] getVals()
	{
		double[] ret = new double[n.size()];
		for(int i = 0; i < n.size(); i++)
		{
			ret[i] = n.get(i).val;
		}
		return ret;
	}
	
	public void printWeights(int num)
	{
		System.out.println("Layer "+num+" Weights = {");
		for(int a = 0; a < n.size(); a++)
		{
			System.out.print(a+": (");
			for(int b = 0; b < n.get(a).w.size(); b++)
			{
				System.out.print(b+": "+gNW(a, b));
				if(b != n.get(a).w.size()-1)
					System.out.print(", ");
			}
			System.out.println(")");
		}
		System.out.println("}");
	}
	
	public void printValues(int num)
	{
		System.out.print("Layer "+num+" Values = {");
		for(int a = 0; a < n.size(); a++)
		{
			System.out.print(a+": "+gNV(a));
			if(a != n.size()-1)
				System.out.print(", ");
		}
		System.out.println("}");
	}
	
	public double gNV(int a)
	{
		return n.get(a).val;
	}
	
	public double gNW(int a, int b)
	{
		return n.get(a).w.get(b);
	}
}
