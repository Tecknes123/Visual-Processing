import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class styleNetwork extends Network
{
	public int size;
	public int layerSize;
	public int inputSize;
	public BufferedImage source = null;
	public String fileName = "";
	public int iW;
	public int iH;
	int fS = 10;
	public layer in;
	public layer out;
	public Network[] subNets = new Network[0];
	boolean init = false;
	boolean pr = true;
	public String s = "Heya";
	public String args;
	public boolean lap = false;
	public boolean retrain = false;
	public int tC = 0;
	
	/*public styleNetwork(int size)
	{
		this.size = size;
		this.layerSize = 11;
		this.inputSize = 20;
		basicInit();
	}
	
	public styleNetwork(int size, int frameSize, BufferedImage source)
	{
		p("Initializing part 1...");
		this.size = size;
		fS = frameSize;
		this.source = source;
		iW = source.getWidth();
		iH = source.getHeight();
		this.layerSize = (iW/fS)*(iH/fS);
		this.inputSize = frameSize*frameSize;
		basicInit();
	}
	
	public styleNetwork(int size, int frameSize, BufferedImage source, boolean lap)
	{
		p("Initializing part 1...");
		this.lap = lap;
		this.size = size;
		fS = frameSize;
		this.source = source;
		iW = source.getWidth();
		iH = source.getHeight();
		this.layerSize = (iW/fS)*(iH/fS);
		this.inputSize = frameSize*frameSize;
		basicInit();
	}*/
	
	public styleNetwork(int size, int frameSize, int trainingCycles, String fileName, String args)
	{
		p("Initializing part 1...");
		this.size = size;
		fS = frameSize;
		this.source = null;
		this.fileName = fileName;
		this.tC = trainingCycles;
		try {
			source = ImageIO.read(new File("images\\"+fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse filename");
		}
		iW = source.getWidth();
		iH = source.getHeight();
		this.layerSize = (iW/fS)*(iH/fS);
		this.inputSize = frameSize*frameSize;
		basicInit();
	}
	
	public styleNetwork(int size, int frameSize, String fileName, String weightFile, String args)
	{
		p("Initializing part 1...");
		this.size = size;
		fS = frameSize;
		this.source = null;
		this.fileName = fileName;
		try {
			source = ImageIO.read(new File("images\\"+fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse filename");
		}
		iW = source.getWidth();
		iH = source.getHeight();
		this.layerSize = (iW/fS)*(iH/fS);
		this.inputSize = frameSize*frameSize;
		basicInit(weightFile);
	}
	
	//For further training a weightSet
	public styleNetwork(int size, int frameSize, int trainingCycles, String fileName, String weightFile, String args)
	{
		p("Initializing part 1...");
		this.size = size;
		fS = frameSize;
		this.tC = trainingCycles;
		this.source = null;
		this.fileName = fileName;
		try {
			source = ImageIO.read(new File("images\\"+fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse filename");
		}
		iW = source.getWidth();
		iH = source.getHeight();
		this.layerSize = (iW/fS)*(iH/fS);
		this.inputSize = frameSize*frameSize;
		basicInit(weightFile);
	}
	
	/*public styleNetwork(int size, int frameSize, String fileName)
	{
		// TODO Auto-generated constructor stub
		p("Initializing part 1...");
		this.size = size;
		this.fS = frameSize;
		this.fileName = fileName;
		try {
			this.source = ImageIO.read(new File("images\\"+fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse filename");
		}
		iW = source.getWidth();
		iH = source.getHeight();
		this.layerSize = (iW/fS)*(iH/fS);
		this.inputSize = frameSize*frameSize;
		basicInit();
		
	}
	
	public styleNetwork(int size, int frameSize, BufferedImage source, String weightFile)
	{
		p("Initializing part 1...");
		this.size = size;
		fS = frameSize;
		this.source = source;
		iW = source.getWidth();
		iH = source.getHeight();
		this.layerSize = (iW/fS)*(iH/fS);
		this.inputSize = frameSize*frameSize;
		basicInit(weightFile);
	}
	
	public styleNetwork(int size, int inputSize, int layerSize)
	{
		this.size = size;
		this.inputSize = inputSize;
		this.layerSize = layerSize;
		basicInit();
	}
	
	public styleNetwork()
	{
		this.size = 0;
		this.inputSize = 0;
		this.layerSize = 0;
		//basicInit();
	}*/

	public double tS[][];
	
	public void basicInit()
	{
		p("Initializing part 2...");
		init = true;
		l = new layer[size];
		l[size-1] = new layer(layerSize);
		out = l[size-1];
		for(int i = size-2; i > 0; i--)
		{
			l[i] = new layer(layerSize, l[i+1]);
		}
		l[0] = new layer(inputSize, l[1]);
		System.out.println("Setting input layer: "+inputSize+", "+layerSize);
		in = l[0];
		if(source != null)
		{
			if(lap)
				source = MainyMain.evalLaplacian(1, source);
			train(source);
		}
	}
	
	public void basicInit(String weightFile)
	{
		p("Initializing part 2...");
		init = true;
		l = new layer[size];
		l[size-1] = new layer(layerSize);
		out = l[size-1];
		for(int i = size-2; i > 0; i--)
		{
			l[i] = new layer(layerSize, l[i+1]);
		}
		l[0] = new layer(inputSize, l[1]);
		System.out.println("Setting input layer: "+inputSize+", "+layerSize);
		in = l[0];
		fileManager.readFrom(weightFile, this);
		
		if(tC != 0)
			train(source);
	}
	
	public void basicInitRetrain(String weightFile)
	{
		p("Initializing part 2...");
		init = true;
		l = new layer[size];
		l[size-1] = new layer(layerSize);
		out = l[size-1];
		for(int i = size-2; i > 0; i--)
		{
			l[i] = new layer(layerSize, l[i+1]);
		}
		l[0] = new layer(inputSize, l[1]);
		System.out.println("Setting input layer: "+inputSize+", "+layerSize);
		in = l[0];
		fileManager.readFrom(weightFile, this);
		
		train(source);
	}
	
	public void evalArgs()
	{
		if(args.contains("L"))
			lap = true;
	}
	
	public double[] eval(int[] Q)
	{
		in.set(Q);
		for(int a = 1; a < size; a++)
		{
			layer q = l[a];
			//l[a-1].printWeights(a-1);
			//if(a == 1)
				//System.out.print("Changing hidden to (");
			//if(a == 2)
				//System.out.print("Changing out to (");
			for(int b = 0; b < q.n.size(); b++)
			{
				double sum = 0.0;
				for(int c = 0; c < l[a-1].n.size(); c++)
				{
					sum = sum + (l[a-1].n.get(c).w.get(b) * l[a-1].n.get(c).val);
				}
				//System.out.print(" Sum: "+sum+" ");
				l[a].n.get(b).val = 1.0/(1.0+Math.exp(0.0-sum));
				//System.out.print(l[a].l[b] + ",");
			}
				//System.out.println(")");
		}
		return out.getVals();
	}
	
	public double[] eval(double[] Q)
	{
		in.set(Q);
		for(int a = 1; a < size; a++)
		{
			layer q = l[a];
			//l[a-1].printWeights(a-1);
			//if(a == 1)
				//System.out.print("Changing hidden to (");
			//if(a == 2)
				//System.out.print("Changing out to (");
			for(int b = 0; b < q.n.size(); b++)
			{
				double sum = 0.0;
				for(int c = 0; c < l[a-1].n.size(); c++)
				{
					sum = sum + (l[a-1].n.get(c).w.get(b) * l[a-1].n.get(c).val);
				}
				//System.out.print(" Sum: "+sum+" ");
				q.n.get(b).val = 1.0/(1.0+Math.exp(0.0-sum));
				//System.out.print(l[a].l[b] + ",");
			}
				//System.out.println(")");
		}
		return out.getVals();
	}
	
	public void p(String s)
	{
		System.out.println(s);
	}
	
	public void train(BufferedImage img)
	{
		p("Building Training Set...");
		int w = img.getWidth();
		int h = img.getHeight();
		img = MainyMain.makeGreyScale(img);
		double[][] tS = new double[(w/fS)*(h/fS)][fS*fS];
		int c = 0;
		
		if(w%fS != 0)
			p("Warning: img width not divisible by frame size: "+w+" - "+fS);
		if(h%fS != 0)
			p("Warning: img height not divisible by frame size: "+h+" - "+fS);
		
		for(int y = 0; y < h; y = y+fS)
		{
			for(int x = 0; x < w; x = x+fS)
			{
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						//Up("("+x+", "+y+")");
						Color col = new Color(img.getRGB(x+x2, y+y2));
						int avg = (col.getRed() + col.getGreen() + col.getBlue())/3;
						tS[c][x2+(fS*y2)] = avg;
					}
				}
				c++;
			}
		}
		train(tS);
	}
	
	public void train(double[][] Q)
	{
		p("Training...");
		System.out.println("Q.length: "+Q.length);
		int cycles = 0;
		double match = 0.0;
		double lr = 0.4;
		
		//Initializing Arrays to keep track of Old Weights and Weight Changes 
		double[][][] wL2LD = new double[l.length][][];
		double[][][] wL2LOld = new double[l.length][][];
		for(int i = 0; i < size-1; i++)
		{
			wL2LD[i] = new double[l[i].n.size()][];
			wL2LOld[i] = new double[l[i].n.size()][];
			//System.out.println("wL2L["+i+"] = "+l[i].w.toString());
			for(int a = 0; a < l[i].n.size(); a++)
			{
				wL2LD[i][a] = new double[l[i].n.get(a).w.size()];
				wL2LOld[i][a] = new double[l[i].n.get(a).w.size()];
				for(int b = 0; b < l[i].n.get(a).w.size(); b++)
				{
					wL2LD[i][a][b] = l[i].n.get(a).w.get(b);
					wL2LOld[i][a][b] = l[i].n.get(a).w.get(b);
				}
			}
		}
		
		
		double rMatch = 0.0;
		double rMTot = 0.0;
		double rMAvg = 0.0;
		double matchPer = 0.0;
		double perc = 0.0;
		
		while(perc <= 99.0 && cycles < tC)
		{
			p("Cycle "+cycles);
			cycles++;
			match = 0.0;
			rMatch = 0.0;
			int countR = 0;
			int countT = 0;
			int numCorrect = 0;
			//total is Q.length
			for(int k = 0; k < Q.length; k++)
			{
				if(k%10 == 0)
					System.out.print(k+", ");
				double max = -100;
				int maxPos = -1;
				//System.out.print(k+",");
				countT++;
				
				double[] res = null;
				res = eval(Q[k]);
				//System.out.println("kTr = "+kTr+":");
				
				double[] dE = new double[res.length];
				for(int i = 0; i < res.length; i++)
				{
					if(res[i] > max)
					{
						max = res[i];
						maxPos = i;
					}
					double test = 0.0;
					if(i == k)
						test = 1.0;
					dE[i] = (test - res[i]) * res[i] * (1 - res[i]); 
				}
				if(maxPos == k)
					numCorrect++;
				//adjusting weights based on results
				for(int len = size-2; len >= 0; len--)
				{
					double dH[] = new double[l[len+1].n.size()];
					if(len == size - 2)
						dH = dE;
					else{
						for(int i = 0; i < l[len+1].n.size(); i++)
						{
							double sum = 0.0;
							for(int j = 0; j < l[len].n.size(); j++)
							{
								//System.out.println("len: "+len+", j: "+j+", i: "+i+", Res.l: "+res.length+", l[len].w.l:"+l[len].w.length+", l[len].w[j].l:"+l[len].w[j].length);
								sum = sum + (dE[i] * l[len].n.get(j).w.get(i));
							}
							dH[i] = (l[len+1].n.get(i).val) * (1.0 - l[len+1].n.get(i).val) * sum;
						}
					}
					/*System.out.print("D<"+(len+1)+"-"+len+"> = (");
					for(int i = 0; i < dH.length; i++)
					{
						System.out.print(" "+dH[i]+",");
					}
					System.out.println(")");*/
					for(int i = 0; i < l[len+1].n.size(); i++)
					{
						for(int j = 0; j < l[len].n.size(); j++)
						{
							wL2LD[len][j][i] = lr * dH[i] * l[len].n.get(j).val;
							l[len].n.get(j).w.set(i, l[len].n.get(j).w.get(i) + wL2LD[len][j][i] + wL2LOld[len][j][i] * 0.5);
							wL2LOld[len][j][i] = wL2LD[len][j][i];
						}
					}
				}
				double sum = 0.0;
				/*System.out.print("Out = (");
				for(int i = 0; i < out.l.length; i++)
					System.out.print(" "+out.l[i]+",");
				System.out.print(")\nHidden = (");
				for(int i = 0; i < l[1].l.length; i++)
					System.out.print(" "+l[1].l[i]+",");
				System.out.println(")");*/
				for(int i = 0; i < out.n.size(); i++)
				{
					double test = 0.0;
					if(i == k)
						test = 1.0;
					sum = sum + ( ((2.0 * out.n.get(i).val) - 1.0) * ((2.0 * test) - 1.0) );
				}
				match = match + sum;
					
			}
			//if(cycles < 50 || ((double)cycles % 50.0) == 0.0)
			rMatch = (rMatch / countR)*100.0;
			//rMTot = rMTot + rMatch;
			rMAvg = ((rMAvg * cycles-1) + rMatch) / (cycles);
			matchPer = (match / (countT * out.n.size() * 1.0)) * 100.0;
			perc = numCorrect/(Q.length*1.0)*100;
			p(perc+"% correct: "+numCorrect+"/"+Q.length);
			/*if(cycles == 1 || cycles % 10 == 0)
				System.out.println("\n"+matchPer + " out of 100 for regular, and "+rMatch+" for random in epoch " + cycles + ", "+countR+"/"+countT+" were random");
			else if(cycles % 10 == 0)
				System.out.print(".");*/
		}
		String wfn = "sn_"+appFN()+"_"+tC+"_"+fS+".txt";
		fileManager.writeTo(wfn, this);
		p("Saving network weights to: "+wfn);
		p("Network ready");
	}
	
	public String appFN()
	{
		String ret = fileName;
		char c;
		for(int i = 0; i < ret.length() && ret.length() == fileName.length(); i++)
		{
			c = ret.charAt(i);
			if(c == '.')
				ret = ret.substring(0, i);
		}
		return ret;
	}
	
	public void train(int[][] Q)
	{
		train(toDoubleArr(Q));
	}
	
	public BufferedImage setGreen(BufferedImage i)
	{
		for(int x = 0; x < i.getWidth(); x++)
		{
			for(int y = 0; y < i.getHeight(); y++)
			{
				Color g = new Color(0, 255, 0);
				i.setRGB(x, y, g.getRGB());
			}
		}
		return i;
	}
	
	public BufferedImage removeGreen(BufferedImage i)
	{
		for(int x = 0; x < i.getWidth(); x++)
		{
			for(int y = 0; y < i.getHeight(); y++)
			{
				Color g = new Color(i.getRGB(x, y));
				if(g.getRed() == 0 && g.getGreen() == 255 && g.getBlue() == 0);
					g = new Color(0, 254, 0);
				i.setRGB(x, y, g.getRGB());
			}
		}
		return i;
	}
	
	public potImg scan(BufferedImage target)
	{
		p("part 1...");
		if(lap)
			target = MainyMain.evalLaplacian(1, target);
		target = MainyMain.makeGreyScale(target);
		int w = target.getWidth();
		int h = target.getHeight();
		
		potImg tes = new potImg(w, h);
		
		p("part 2...");
		int rand = (int)((fS/2.0)*Math.random());
		int x = 0;
		int y = 0;
		for(int yb = 0; yb < h-fS+1; yb=yb+rand)
		{
			
			rand = (int)((fS/2.0)*Math.random());
			System.out.print(y+",");
			x = 0;
			for(y = yb; y < h-fS+1 && x < w-fS+1; x = y-yb)
			{
				
				rand = (int)((fS/2.0)*Math.random());
				double[] t = new double[fS*fS];
				int c = 0;
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						Color col = new Color(target.getRGB(x+x2, y+y2));
						t[c] = (col.getRed()+col.getGreen()+col.getBlue())/3;
						c++;
					}
				}
				double[] res = eval(t);
				int pos = 0;
				double max = res[0];
				for(int i = 1; i < res.length; i++)
				{
					if(res[i] > max)
					{
						pos = i;
						max = res[i];
					}
				}
				int pX = (pos%(iW/fS))*fS;
				int pY = (pos/(iW/fS))*fS;
				int[][][] ret = getVals(pX, pY);
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						Color col = new Color(ret[x2][y2][0], ret[x2][y2][1], ret[x2][y2][2]);
						double r1 = (int)((Math.abs(Math.abs((fS/2)-y2) - (fS/2))*2)-2);
						if(r1 < 0)
							r1 = 0;
						r1 = r1 / fS;
						double r2 = (int)((Math.abs(Math.abs((fS/2)-x2) - (fS/2))*2)-2);
						if(r2 < 0)
							r2 = 0;
						r2 = r2 / fS;
						/*if(r2 != 0)
							r2 = (fS/2)/((r2+1)*2);
						else
							r2 = 1;*/
						tes.ar[x+x2][y+y2].add(col, max*r1*r2);
					}
				}
				y = y+rand;
			}
		}
		p("and again...");
		x = 1;
		y = 1;
		for(int xb = 0; xb < w-fS+1; xb=xb+rand)
		{
			rand = (int)((fS/2.0)*Math.random());
			System.out.print(x+",");
			y = 0;
			for(x = xb; y < h-fS+1 && x < w-fS+1; y = x-xb)
			{
				rand = (int)((fS/2.0)*Math.random());
				double[] t = new double[fS*fS];
				int c = 0;
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						//p("("+(x)+", "+(y)+") - ("+(x+x2)+", "+(y+y2)+")");
						Color col = new Color(target.getRGB(x+x2, y+y2));
						t[c] = (col.getRed()+col.getGreen()+col.getBlue())/3;
						c++;
					}
				}
				double[] res = eval(t);
				int pos = 0;
				double max = res[0];
				for(int i = 1; i < res.length; i++)
				{
					if(res[i] > max)
					{
						pos = i;
						max = res[i];
					}
				}
				int pX = (pos%(iW/fS))*fS;
				int pY = (pos/(iW/fS))*fS;
				int[][][] ret = getVals(pX, pY);
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						Color col = new Color(ret[x2][y2][0], ret[x2][y2][1], ret[x2][y2][2]);
						//double r2 = (int)(Math.abs(((int)fS/2)-x2));
						double r1 = (int)((Math.abs(Math.abs((fS/2)-y2) - (fS/2))*2)-2);
						if(r1 < 0)
							r1 = 0;
						r1 = r1 / fS;
						double r2 = (int)((Math.abs(Math.abs((fS/2)-x2) - (fS/2))*2)-2);
						if(r2 < 0)
							r2 = 0;
						r2 = r2 / fS;
						/*if(r2 != 0)
							r2 = (fS/2)/((r2+1)*2);
						else
							r2 = 1;*/
						tes.ar[x+x2][y+y2].add(col, max*r1*r2);
					}
				}
				x = x+rand;
			}
		}
		return tes;
	}
	
	public potImg baseScan(BufferedImage target)
	{
		p("part 1...");
		if(lap)
			target = MainyMain.evalLaplacian(1, target);
		target = MainyMain.makeGreyScale(target);
		int w = target.getWidth();
		int h = target.getHeight();
		
		potImg tes  = new potImg(w, h);
		
		p("part 2...");
		for(int y = 0; y < h-fS+1; y=y+fS)
		{
			System.out.print(y+",");
			for(int x = 0; x < w-fS+1; x = x+fS)
			{
				double[] t = new double[fS*fS];
				int c = 0;
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						Color col = new Color(target.getRGB(x+x2, y+y2));
						t[c] = (col.getRed()+col.getGreen()+col.getBlue())/3;
						c++;
					}
				}
				double[] res = eval(t);
				int pos = 0;
				double max = res[0];
				for(int i = 1; i < res.length; i++)
				{
					if(res[i] > max)
					{
						pos = i;
						max = res[i];
					}
				}
				int pX = (pos%(iW/fS))*fS;
				int pY = (pos/(iW/fS))*fS;
				int[][][] ret = getVals(pX, pY);
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						Color col = new Color(ret[x2][y2][0], ret[x2][y2][1], ret[x2][y2][2]);
						double r1 = (int)((Math.abs(Math.abs((fS/2)-y2) - (fS/2))*2)-2);
						if(r1 < 0)
							r1 = 0;
						r1 = r1 / fS;
						double r2 = (int)((Math.abs(Math.abs((fS/2)-x2) - (fS/2))*2)-2);
						if(r2 < 0)
							r2 = 0;
						r2 = r2 / fS;
						/*if(r2 != 0)
							r2 = (fS/2)/((r2+1)*2);
						else
							r2 = 1;*/
						tes.ar[x+x2][y+y2].add(col, max*r1*r2);
					}
				}
			}
		}
		return tes;
	}
	
	public potImg compScan(BufferedImage target, potImg pI1, int oldFS)
	{
		p("part 1...");
		if(lap)
			target = MainyMain.evalLaplacian(1, target);
		target = MainyMain.makeGreyScale(target);
		int w = target.getWidth();
		int h = target.getHeight();
		
		potImg tes = new potImg(w, h);
		
		p("part 2...");
		int rand = (int)((fS/2.0)*Math.random());
		int x = 0;
		int y = 0;
		for(int yb = 0; yb < h-fS+1; yb=yb+rand)
		{
			rand = (int)((fS/2.0)*Math.random());
			System.out.print(y+",");
			x = 0;
			for(y = yb; y < h-fS+1 && x < w-fS+1; x = y-yb)
			{
				
				rand = (int)((fS/2.0)*Math.random());
				double[] t = new double[fS*fS];
				int c = 0;
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						Color col = new Color(target.getRGB(x+x2, y+y2));
						t[c] = (col.getRed()+col.getGreen()+col.getBlue())/3;
						c++;
					}
				}
				double[] res = eval(t);
				int pos = 0;
				double max = res[0];
				for(int i = 1; i < res.length; i++)
				{
					if(res[i] > max)
					{
						pos = i;
						max = res[i];
					}
				}
				int pX = (pos%(iW/fS))*fS;
				int pY = (pos/(iW/fS))*fS;
				int[][][] ret = getVals(pX, pY);
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						Color col = new Color(ret[x2][y2][0], ret[x2][y2][1], ret[x2][y2][2]);
						double r1 = (int)((Math.abs(Math.abs((fS/2)-y2) - (fS/2))*2)-2);
						if(r1 < 0)
							r1 = 0;
						r1 = r1 / fS;
						double r2 = (int)((Math.abs(Math.abs((fS/2)-x2) - (fS/2))*2)-2);
						if(r2 < 0)
							r2 = 0;
						r2 = r2 / fS;
						/*if(r2 != 0)
							r2 = (fS/2)/((r2+1)*2);
						else
							r2 = 1;*/
						pI1.ar[x+x2][y+y2].add(col, (max*r1*r2)*(oldFS/fS));
					}
				}
				y = y+rand;
			}
		}
		p("and again...");
		x = 1;
		y = 1;
		for(int xb = 0; xb < w-fS+1; xb=xb+rand)
		{
			rand = (int)((fS/2.0)*Math.random());
			System.out.print(x+",");
			y = 0;
			for(x = xb; y < h-fS+1 && x < w-fS+1; y = x-xb)
			{
				rand = (int)((fS/2.0)*Math.random());
				double[] t = new double[fS*fS];
				int c = 0;
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						//p("("+(x)+", "+(y)+") - ("+(x+x2)+", "+(y+y2)+")");
						Color col = new Color(target.getRGB(x+x2, y+y2));
						t[c] = (col.getRed()+col.getGreen()+col.getBlue())/3;
						c++;
					}
				}
				double[] res = eval(t);
				int pos = 0;
				double max = res[0];
				for(int i = 1; i < res.length; i++)
				{
					if(res[i] > max)
					{
						pos = i;
						max = res[i];
					}
				}
				int pX = (pos%(iW/fS))*fS;
				int pY = (pos/(iW/fS))*fS;
				int[][][] ret = getVals(pX, pY);
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						Color col = new Color(ret[x2][y2][0], ret[x2][y2][1], ret[x2][y2][2]);
						double r1 = (int)((Math.abs(Math.abs((fS/2)-y2) - (fS/2))*2)-2);
						if(r1 < 0)
							r1 = 0;
						r1 = r1 / fS;
						double r2 = (int)((Math.abs(Math.abs((fS/2)-x2) - (fS/2))*2)-2);
						if(r2 < 0)
							r2 = 0;
						r2 = r2 / fS;
						/*if(r2 != 0)
							r2 = (fS/2)/((r2+1)*2);
						else
							r2 = 1;*/
						pI1.ar[x+x2][y+y2].add(col, (max*r1*r2)*(oldFS/fS));
						tes.ar[x+x2][y+y2] = pI1.ar[x+x2][y+y2];
					}
				}
				x = x+rand;
			}
		}
		return pI1;
	}
	
	public potImg compBaseScan(BufferedImage target, potImg pI1, int oldFS)
	{
		p("part 1...");
		if(lap)
			target = MainyMain.evalLaplacian(1, target);
		target = MainyMain.makeGreyScale(target);
		int w = target.getWidth();
		int h = target.getHeight();
		
		potImg tes  = new potImg(w, h);
		
		p("part 2...");
		for(int y = 0; y < h-fS+1; y=y+fS)
		{
			System.out.print(y+",");
			for(int x = 0; x < w-fS+1; x = x+fS)
			{
				double[] t = new double[fS*fS];
				int c = 0;
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						Color col = new Color(target.getRGB(x+x2, y+y2));
						t[c] = (col.getRed()+col.getGreen()+col.getBlue())/3;
						c++;
					}
				}
				double[] res = eval(t);
				int pos = 0;
				double max = res[0];
				//p("res length: "+res.length);
				for(int i = 1; i < res.length; i++)
				{
					if(res[i] > max)
					{
						pos = i;
						max = res[i];
					}
				}
				int pX = (pos%(iW/fS))*fS;
				int pY = (pos/(iW/fS))*fS;
				int[][][] ret = getVals(pX, pY);
				for(int y2 = 0; y2 < fS; y2++)
				{
					for(int x2 = 0; x2 < fS; x2++)
					{
						Color col = new Color(ret[x2][y2][0], ret[x2][y2][1], ret[x2][y2][2]);
						double r1 = (int)((Math.abs(Math.abs((fS/2)-y2) - (fS/2))*2)-2);
						if(r1 < 0)
							r1 = 0;
						r1 = r1 / fS;
						double r2 = (int)((Math.abs(Math.abs((fS/2)-x2) - (fS/2))*2)-2);
						if(r2 < 0)
							r2 = 0;
						r2 = r2 / fS;
						/*if(r2 != 0)
							r2 = (fS/2)/((r2+1)*2);
						else
							r2 = 1;*/
						pI1.ar[x+x2][y+y2].add(col, (max*r1*r2)*(oldFS/fS));
						tes.ar[x+x2][y+y2] = pI1.ar[x+x2][y+y2];
					}
				}
			}
		}
		return tes;
	}
	
	public int[][][] getVals(int pX, int pY)
	{
		int[][][] ret = new int[fS][fS][3];
		
		for(int x = 0; x < fS; x++)
		{
			for(int y = 0; y < fS; y++)
			{
				Color col = new Color(source.getRGB(pX+x, pY+y)); 
				ret[x][y][0] = col.getRed();
				ret[x][y][1] = col.getGreen();
				ret[x][y][2] = col.getBlue();
			}
		}
		
		return ret;
	}
	
	
	public void print(String s)
	{
		if(pr)
			System.out.print(s);
	}
	
	public void println(String s)
	{
		if(pr)
			System.out.println(s);
	}
}

