import java.util.ArrayList;

public class Network
{
	public int size;
	public int layerSize;
	public int inputSize;
	public layer[] l;
	public layer in;
	public layer out;
	public Network[] subNets = new Network[0];
	boolean init = false;
	boolean pr = true;
	public String s = "Heya";
	
	public Network(int size)
	{
		this.size = size;
		this.layerSize = 11;
		this.inputSize = 20;
		basicInit();
	}
	
	public Network(int size, int inputSize, int layerSize)
	{
		this.size = size;
		this.inputSize = inputSize;
		this.layerSize = layerSize;
		basicInit();
	}
	
	public Network()
	{
		this.size = 0;
		this.inputSize = 0;
		this.layerSize = 0;
		//basicInit();
	}
	
	public double tS[][];
	
	public void basicInit()
	{
		init = true;
		l = new layer[size];
		l[size-1] = new layer(layerSize);
		out = l[size-1];
		for(int i = size-2; i > 0; i--)
		{
			l[i] = new layer(layerSize, l[i+1]);
			//l[i].printWeights(i);
		}
		l[0] = new layer(inputSize, l[1]);
		System.out.println("Setting input layer: "+inputSize+", "+layerSize);
		//l[0].printWeights(0);
		in = l[0];
	}
	
	public static double[][] toDoubleArr(int[][] iA)
	{
		double[][] Q = new double[iA.length][];
		for(int i = 0; i < Q.length; i++)
		{
			Q[i] = new double[iA.length];
			for(int ii = 0; ii < Q[i].length; ii++)
				Q[i][ii] = iA[i][ii];
		}
		return Q;
	}
	
	public static double[] toDoubleArr(int[] iA)
	{
		double[] Q = new double[iA.length];
		for(int i = 0; i < Q.length; i++)
				Q[i] = iA[i];
		return Q;
	}
	
	public static int[][] toIntArr(double[][] dA)
	{
		int[][] Q = new int[dA.length][];
		for(int i = 0; i < Q.length; i++)
		{
			Q[i] = new int[dA.length];
			for(int ii = 0; ii < Q[i].length; ii++)
				Q[i][ii] = (int)dA[i][ii];
		}
		return Q;
	}
	
	public static int[] toDoubleArr(double[] dA)
	{
		int[] Q = new int[dA.length];
		for(int i = 0; i < Q.length; i++)
				Q[i] = (int)dA[i];
		return Q;
	}

	public void reverseInit()
	{
		size = l.length;
		in = l[0];
		inputSize = in.n.size();
		out = l[size-1];
		layerSize = out.n.size();
	}
	
	//Out-dated Method
	/*public Network(Network[] netAr, int depth, int layerSize)
	{
		subNets = netAr;
		size = depth;
		l = new layer[size];
		l[0] = new layer(layerSize, layerSize);
		//l[0].printWeights(0);
		in = l[0];
		//for(int i = 0; i < l[0].l.length; i++)
		//	l[0].l[i] = 
		for(int i = 1; i < (size-1); i++)
		{
			l[i] = new layer(layerSize, layerSize);
			//l[i].printWeights(i);
		}
		l[size-1] = new layer(10);
		out = l[size-1];
	}*/
	
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
				l[a].n.get(b).val = 1.0/(1.0+Math.exp(0.0-sum));
				//System.out.print(l[a].l[b] + ",");
			}
				//System.out.println(")");
		}
		return out.getVals();
	}
	
	public void train(double[][] Q)
	{
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
		
		//while(matchPer <= 99.0 && cycles < 10000)
		while(cycles < 10000)
		{
			cycles++;
			match = 0.0;
			rMatch = 0.0;
			int countR = 0;
			int countT = 0;
			for(int k = 0; k < Q.length; k++)
			{
				countT++;
				int kTr = k;
				
				int ran = (int)(2 * Math.random());
				//ran = 2;
				double[] res = null;
				if(ran == 1)
					res = eval(Q[k]);
				else
				{
					double[] rand = new double[Q[0].length];
					for(int i = 0; i < rand.length; i++)
						rand[i] = (int)(Math.random()*2);
					/*boolean check = true;
					while(check)
					{
						check = false;
						for(int i = 0; i < Q.length; i++)
						{
							if(rand == Q[i])
							{
								check = true;
								rand = new double[Q[0].length];
								for(int i2 = 0; i2 < rand.length; i2++)
									rand[i2] = (int)(Math.random()*2);
								System.out.println("--- Yeah, this actually happened with "+i);
								return;
							}
						}
					}*/
					kTr = Q.length;
					k--;
					countR++;
					//System.out.print("random input (");
					for(int c = 0; c < Q[0].length; c++)
					{
						rand[c]=(int)(2*Math.random());
						//System.out.print(rand[c]);
						//if(c < Q[0].length-1)
						//	System.out.print(", ");
					}
					//System.out.println(") generated");
					res = eval(rand);
				}
				//System.out.println("kTr = "+kTr+":");
				
				double[] dE = new double[res.length];
				for(int i = 0; i < res.length; i++)
				{
					double test = 0.0;
					if(i == kTr)
						test = 1.0;
					dE[i] = (test - res[i]) * res[i] * (1 - res[i]); 
				}
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
					if(i == kTr)
						test = 1.0;
					sum = sum + ( ((2.0 * out.n.get(i).val) - 1.0) * ((2.0 * test) - 1.0) );
				}
				match = match + sum;
				if(kTr == Q.length)
				{
					double max = 0.0;
					for(int i = 0; i < out.n.size(); i++)
						if(out.n.get(i).val > max)
							max = out.n.get(i).val;
					if(max == out.n.get(Q.length).val)
					{
						rMatch = rMatch + 1;
						//out.printValues(2);
					}
				}
					
			}
			//if(cycles < 50 || ((double)cycles % 50.0) == 0.0)
			rMatch = (rMatch / countR)*100.0;
			//rMTot = rMTot + rMatch;
			rMAvg = ((rMAvg * cycles-1) + rMatch) / (cycles);
			matchPer = (match / (countT * out.n.size() * 1.0)) * 100.0;
			if(cycles == 1 || cycles % 1000 == 0)
				System.out.println("\n"+matchPer + " out of 100 for regular, and "+rMatch+" for random in epoch " + cycles + ", "+countR+"/"+countT+" were random");
			else if(cycles % 10 == 0)
				System.out.print(".");
		}
	}
	
	public void train(int[][] Q)
	{
		train(toDoubleArr(Q));
	}
	
	public void train()
	{
		train(tS);
	}
	
	public void addNew(int[] Q)
	{
		System.out.println("Adding New Input");
		int pos = out.n.size()-1;
		System.out.println("POS = "+pos+", O.n.s(): "+out.n.size());
		out.n.add(pos, new Neuron());
		System.out.println("POS = "+pos+", O.n.s(): "+out.n.size());
		for(int i = 0; i < l[size-2].n.size(); i++)
		{
			//l[size-2].n.get(i).w.add(pos, (Math.random() * 0.6)-0.3);
			l[size-2].n.get(i).w.add(pos, 0.0);
			l[size-2].n.get(i).con.add(pos, out.n.get(out.n.size()-3));
		}
		/*//Resizes output
		double lVal[] = l[l.length-1].l;
		double[] lsub = new double[lVal.length];
		for(int i = 0; i < lsub.length; i++)
			lsub[i] = lVal[i];
		l[l.length-1].l = new double[lVal.length+1];
		lVal = l[l.length-1].l;
		for(int i = 0; i < lsub.length; i++)
			lVal[i] = lsub[i];
		
		//Resizes weights leading to output
		double wVal[][] = l[l.length-2].w;
		double[][] wSub = new double[wVal.length][];
		for(int i = 0; i < wSub.length; i++)
		{
			wSub[i] = new double[wVal[i].length];
			for(int ii = 0; ii < wSub[i].length; ii++)
			{
				wSub[i][ii] = wVal[i][ii];
			}
			l[l.length-2].w[i] = new double[wVal[i].length+1];
			wVal[i] = l[l.length-2].w[i];
			wVal[i][wSub[i].length] = (Math.random() * 0.6)-0.3;
			for(int ii = 0; ii < wSub[i].length; ii++)
			{
				wVal[i][ii] = wSub[i][ii];
			}
		}*/
		
		//Training just the new input
		double[] outp = new double[out.n.size()];
		System.out.print("Output Array: ");
		for(int i = 0; i < outp.length; i++)
		{
			if(i != outp.length-2)
				outp[i] = 0.0;
			else
				outp[i] = 1.0;
			System.out.print(i+": "+outp[i]+", ");
		}
		System.out.println("");
		l[2].printWeights(2);
		l[3].printValues(3);
		trainNew(toDoubleArr(Q), outp);
	}
	
	public void trainNew(double[] inp, double[] outp)
	{
		System.out.println("Training New Input");
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
		
		//while(matchPer <= 99.0 && cycles < 10000)
		while(cycles < 5000)
		{
			int pos = outp.length-2;
			cycles++;
			match = 0.0;
			rMatch = 0.0;
			int countR = 0;
			int countT = 0;
			countT++;
			int ran = (int)(3 * Math.random());
			//ran = 1;
			double[] res = null;
			if(ran == 2)
				ran = 0;
			if(ran == 1)
				res = eval(inp);
			else
			{
				double[] rand = new double[inp.length];
				for(int i = 0; i < rand.length; i++)
					rand[i] = (int)(Math.random()*2);
				boolean check = true;
				/*while(check)
				{
					check = false;
					if(rand == inp)
					{
						check = true;
						rand = new double[inp.length];
						for(int i2 = 0; i2 < rand.length; i2++)
							rand[i2] = (int)(Math.random()*2);
						System.out.println("--- Yeah, this actually happened");
						return;
					}
				}*/
				countR++;
				//System.out.print("random input (");
				for(int c = 0; c < rand.length; c++)
				{
					rand[c]=(int)(2*Math.random());
					//System.out.print(rand[c]);
					//if(c < Q[0].length-1)
					//	System.out.print(", ");
				}
				//System.out.println(") generated");
				res = eval(rand);
			}
			
			double[] dE = new double[res.length];
			double[] outpSub = outp;
			if(ran == 0)
			{
				outpSub[outp.length-2] = 0.0;
				outpSub[outp.length-1] = 1.0;
			}else{
				outpSub[outp.length-2] = 1.0;
				outpSub[outp.length-1] = 0.0;
			}
			if(cycles == 1 || cycles % 100 == 0)
				System.out.print("opS last 2: "+outpSub[outp.length-2]+", "+outpSub[outp.length-1]);
			for(int i = 0; i < res.length; i++)
			{
				double test = outpSub[i];
				dE[i] = (test - res[i]) * res[i] * (1 - res[i]); 
			}
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
				double test = outp[i];
				sum = sum + ( ((2.0 * out.n.get(i).val) - 1.0) * ((2.0 * test) - 1.0) );
			}
			match = match + sum;
			int mPos = -1;
			/*if(ran == 1)
			{*/
				double max = 0.0;
				for(int i = 0; i < out.n.size(); i++)
				{
					if(out.n.get(i).val > max)
					{
						max = out.n.get(i).val;
						mPos = i;
					}
				}
				if(max == out.n.get(pos+1).val)
				{
					rMatch = rMatch + 1;
					//out.printValues(2);
				}
				if(max == out.n.get(pos).val)
					matchPer = 1.0;
				else
					matchPer = 0.0;
			//}
			
			//if(cycles < 50 || ((double)cycles % 50.0) == 0.0)
			rMatch = (rMatch / countR)*100.0;
			//rMTot = rMTot + rMatch;
			rMAvg = ((rMAvg * cycles-1) + rMatch) / (cycles);
			//matchPer = (match / (countT * out.n.size() * 1.0)) * 100.0;
			if(cycles == 1 || cycles % 100 == 0)
				System.out.println("\n"+matchPer + " out of 100 for regular, max at "+mPos+", and "+rMatch+" for random in epoch " + cycles + ", "+countR+"/"+countT+" were random");
			else if(cycles % 10 == 0)
				System.out.print(".");
		}
	}
	
	/*public ArrayList<charRes> scan(imageArray iA)
	{
		//System.out.println("Scanning 2...");
		double sum = 0.0;
		double match = 0.0;
		double maxVal = 0.0;
		int pos = -1;
		int iW = 6;
		int iH = 7;
		double sW = 6.0;
		double sH = 7.0;
		int sX = 0;
		int sY = 0;
		int cN = -1;
		ArrayList<Integer> maxX = new ArrayList<Integer>();
		ArrayList<Integer> minX = new ArrayList<Integer>();
		ArrayList<Integer> maxY = new ArrayList<Integer>();
		ArrayList<Integer> minY = new ArrayList<Integer>();
		ArrayList<ArrayList<charRes>> allRes = new ArrayList<ArrayList<charRes>>();
		ArrayList<ArrayList<charRes>> allExp = new ArrayList<ArrayList<charRes>>();
		while(sH <= iA.h && sW <= iA.w)
		//while(sH < 51 && sW < iA.w)
		{
			cN++;
			maxX.add(0);
			minX.add(0);
			maxY.add(0);
			minY.add(0);
			allRes.add(new ArrayList<charRes>());
			allExp.add(new ArrayList<charRes>());
			for(sX = 0; sX+sW <= iA.w; sX++)
			//for(sX = 0; sX < 11; sX++)
			{
				for(sY = 0; sY+sH <= iA.h; sY++)
				//for(sY = 0; sY < 11; sY++)
				{
					int[][] arr = iA.scaleSample(iW, iH, sX, sY, sH);
					eval(imageArray.toFlatArr(arr));
					//iA.scaleSample(4, 5, 10, 10, 40.0);
					maxVal = 0.0;
					pos = -1;
					for(int i = 0; i < out.n.size(); i++)
					{
						double val = (out.n.get(i).val);
						if(val > maxVal)
						{
							maxVal = val;
							pos = i;
						}
					}
					/*if(sW == 8.0 && sH == 10.0 && sX == 2 && sY == 2)
					{
						System.out.println("Area at (sW: "+sW+", sH: "+sH+", sX: "+sX+", sY: "+sY+"):");
						imageArray.printBinMap(arr);
					} (end comment was here)
					if(maxVal > 0.75 && pos != out.n.size()-1)
					{
						if(pr)
						{
							System.out.println("found area with "+((int)(maxVal*100.0))+"% match as "+pos+"\nat sW: "+sW+", sH: "+sH+", sX: "+sX+", sY: "+sY);
							imageArray.printBinMap(arr);
							out.printValues(size-1);
						}
						allExp.get(cN).add(new charRes((sX*1.0), (sY*1.0), sW, sH, pos));
						if(sX < minX.get(cN))
							minX.set(cN, sX);
						if(sX > maxX.get(cN))
							maxX.set(cN, sX);
						if(sY < minY.get(cN))
							minY.set(cN, sY);
						if(sY > maxY.get(cN))
							maxY.set(cN, sY);
					}
				}
			}
			
			allRes.set(cN, allExp.get(cN));
			println("maxX: "+maxX+", minX: "+minX+", maxY: "+maxY+", minY: "+minY);
			for(int c = 0; c < allRes.get(cN).size(); c++)
			{
				charRes Q = allRes.get(cN).get(c);
				if(maxX.get(cN) - minX.get(cN) > 0)
					Q.x = (Q.x - minX.get(cN)) / (maxX.get(cN) - minX.get(cN));
				else
					Q.x = 0.0;
				Q.w = Q.w / ((maxX.get(cN) + Q.w) - minX.get(cN));
				if(maxY.get(cN) - minY.get(cN) > 0)
					Q.y = (Q.y - minY.get(cN)) / (maxY.get(cN) - minY.get(cN));
				else
					Q.y = 0.0;
				Q.h = Q.h / ((maxY.get(cN) + Q.h) - minY.get(cN));
				allRes.get(cN).set(c, Q);
			//  ^ Is this redundant...? (Is Q already modifying that spot in memory?)
			}
			sH++;
			sW = (sH/iH)*iW;
		}
		int retNum = 0;
		for(int r = 0; r < allExp.size(); r++)
		{
			if(allExp.get(r).size() > allExp.get(retNum).size())
				retNum = r;
		}
		return allRes.get(retNum);
	}*/
	
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
