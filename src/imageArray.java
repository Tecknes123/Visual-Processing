import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class imageArray
{
	public BufferedImage img;
	public int h = 0;
	public int w = 0;
	public double[][] binMap = new double[0][0];
	public boolean p = false;
	
	public imageArray(String dir)
	{
		img = null;
		try {
		    img = ImageIO.read(new File(dir));
		} catch (IOException e) {
			e.printStackTrace();
		}
		h = img.getHeight();
		w = img.getWidth();
		binMap = new double[img.getHeight()][img.getWidth()];
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				binMap[y][x] = getPixAvgD(x, y);
			}
		}
	}
	
	public imageArray(int[][] arr)
	{
		for(int i = 0; i < arr.length; i++)
			for(int ii = 0; ii < arr[i].length; ii++)
				binMap[i][ii] = arr[i][ii];
		h = arr.length;
		w = arr[0].length;
		img = null;
	}
	
	public imageArray(int[] arr, int w, int h)
	{
		this.w = w;
		this.h = h;
		binMap = new double[h][w];
		for(int i = 0; i < arr.length; i++)
		{
			for(int hC = 0; hC < h; hC++)
			{
				for(int wC = 0; wC < w; wC++)
				{
					binMap[hC][wC] = arr[i];
					i++;
				}
			}
		}
	}
	
	public imageArray(int w, int h)
	{
		binMap = new double[h][w];
		for(int y = 0; y < h; y++)
			for(int x = 0; x < w; x++)
				binMap[y][x] = 0;
		this.h = h;
		this.w = w;
		img = null;
	}
	
	public void pasteAt(int[][] a, int x, int y)
	{
		for(int tY = y; tY < y+a.length; tY++)
			for(int tX = x; tX < x+a[tY-y].length; tX++)
			{
				binMap[tY][tX] = a[tY-y][tX-x];
			}
	}
	
	public void pasteAt(double[][] a, int x, int y)
	{
		for(int tY = y; tY < y+a.length; tY++)
			for(int tX = x; tX < x+a[tY-y].length; tX++)
				binMap[tY][tX] = (int)a[tY-y][tX-x];
	}
	
	public int getPixAvgI(int x, int y)
	{
		int ret;
		Color c = new Color(img.getRGB(x, y));
		ret = (int)((c.getBlue()+c.getGreen()+c.getRed())/3.0);
		if(ret > 127)
			ret = 0;
		else
			ret = 1;
		return ret;
	}
	
	public double getPixAvgD(int x, int y)
	{
		double ret;
		Color c = new Color(img.getRGB(x, y));
		ret = ((c.getBlue()+c.getGreen()+c.getRed())/3.0)/255.0;
		return ret;
	}
	
	public void printBinMap()
	{
		System.out.println("binary Map: ");
		for(int y = 0; y < h; y++)
		{
			System.out.print("   ");
			for(int x = 0; x < w; x++)
			{
				System.out.print(binMap[y][x] + " ");
			}
			System.out.println("");
		}
	}
	
	public static void printBinMap(int[][] arr)
	{
		System.out.println("binary Map: ");
		for(int y = 0; y < arr.length; y++)
		{
			System.out.print("   ");
			for(int x = 0; x < arr[y].length; x++)
			{
				System.out.print(arr[y][x] + " ");
			}
			System.out.println("");
		}
	}
	
	public double[][] sample(int sX, int sY, int sW, int sH)
	{
		System.out.println("sample binary Map: ");
		double[][] ret = new double[sH][sW];
		for(int y = sY; y < sY+sH; y++)
		{
			System.out.print("   ");
			for(int x = 0; x < w; x++)
			{
				ret[y-sY][x-sX] = binMap[y][x];
				System.out.print(ret[y-sY][x-sX] + " ");
			}
			System.out.println("");
		}
		return ret;
	}
	
	public int[][] scaleSample(int iW, int iH, int sX, int sY, double sH)
	{
		double retMap[][] = new double[iH][iW];
		double sW = (sH/iH)*iW;
		double xSS = ((int)((sW/iW)*100))/100.0;
		double ySS = ((int)((sH/iH)*100))/100.0;
		double yR = (ySS%1.0);
		int yB = (int) (ySS-yR);
		double xR = (xSS%1.0);
		int xB = (int) (xSS-xR);
		//if(sH == 12.0 && sX == 89)
		//	p = true;
		//System.out.println("\nsW: "+sW+", sH: "+sH+", sX: "+sX+", sY: "+sY+", iW: "+iW+", iH: "+iH);
		//System.out.println("xSS: "+xSS+", ySS: "+ySS+", yR: "+yR+", yB: "+yB+", xR: "+xR+", xB: "+xB);
			
		int xCount = 0;
		int x;
		int lastCX = sX;
		for(int xI = 0; xI < iW; xI++)
		{
			x = lastCX;
			int yCount = 0;
			println("x = "+x);
			double yCar = 0.0;
			int y;
			int lastCY = sY;
			for(int yI = 0; yI < iH; yI++)
			{
				y = lastCY;
				println("    y = "+y+", yI = "+yI);
				double xCar = 0.0;
				double totY = 0.0;
				double yTr = ySS;
				if(xCount == 0 && yCount != 0)
					yTr = ySS;
				for(int cY = y; yTr > 0; cY++)
				{
					double parY = 1.0;
					if(cY == y && y != sY)
						parY = 1 - yCar;
					if(yTr == 1.0)
					{
						yCar = 0.0;
						lastCY = cY+1;
					}
					if(yTr < 1.0)
					{
						parY = yTr;
						yCar = yTr;
						lastCY = cY;
					}
					//println("    cY = "+cY+", parY: "+parY+", yTr: "+yTr+", yCar: "+yCar);
					double totX = 0.0;
					double xTr = xSS;
					if(cY == y && xCount != 0)
						xTr = xSS;
					for(int cX = x; xTr > 0; cX++)
					{
						double parX = 1.0;
						if(cX == x && x != sX)
							parX = 1 - xCar;
						if(xTr == 1.0)
						{
							xCar = 0.0;
							lastCX = cX+1;
						}
						if(xTr < 1.0)
						{
							parX = xTr;
							xCar = xTr;
							lastCX = cX;
						}
						double bM = 0;
						if(cX < w && cY < h)
							bM = binMap[cY][cX];
						//println("        cX = "+cX+", parX: "+parX+", xTr: "+xTr+", xCar"+xCar+", bM[cY][cX]: "+bM);
						totX = totX + bM * parX;
						xTr = xTr - parX;
					}
					//print("        totX/xSS = "+totX+" / "+xSS+" = ");
					totX = totX / xSS;
					//println(""+totX);
					totY = totY + totX * parY;
					//yCar = yTr;
					yTr = yTr - parY;
				}
				//print("    totY/ySS = "+totY+" / "+ySS+" = ");
				totY = totY / ySS;
				retMap[yI][xI] = totY;
				//println(""+retMap[yI][xI]+", Saved to rM["+yI+"]["+xI+"]");
				/*if(totY > 0.5)
					retMap[yCount][xCount] = 1;
				else
					retMap[yCount][xCount] = 0;
				*/
				xCount++;
			}
			
			yCount++;
		}
		if(p)
		{
			System.out.println("Scaled sample double map: ");
			for(int a = 0; a < retMap.length; a++)
			{
				System.out.print("   ");
				for(int b = 0; b < retMap[a].length; b++)
				{
					System.out.print((int)roundToPlace(retMap[a][b], 2) + " ");
				}
				System.out.println("");
			}
		}
		int[][] i = toBinMap(retMap);
		return i;
	}
	
	public static int[][] toBinMap(double[][] d)
	{
		int[][] retMap = new int[d.length][d[0].length];
		for(int y = 0; y < d.length; y++)
		{
			for(int x = 0; x < d[y].length; x++)
			{
				if(d[y][x] > 0.5)
					retMap[y][x] = 1;
				else
					retMap[y][x] = 0;
			}
		}
		return retMap;
	}
	
	public double[] toFlatArr()
	{
		double[] ret = new double[w*h];
		int c = 0;
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				ret[c] = binMap[y][x];
				c++;
			}
		}
		return ret;
	}
	
	public static int[] toFlatArr(double[][] samMap)
	{
		int[] ret = new int[samMap.length*samMap[0].length];
		int c = 0;
		for(int y = 0; y < samMap.length; y++)
		{
			for(int x = 0; x < samMap[y].length; x++)
			{
				if(samMap[y][x] > 0.5)
					ret[c] = 1;
				else
					ret[c] = 0;
				c++;
			}
		}
		return ret;
	}
	
	public static int[] toFlatArr(int[][] samMap)
	{
		int[] ret = new int[samMap.length*samMap[0].length];
		int c = 0;
		for(int y = 0; y < samMap.length; y++)
		{
			for(int x = 0; x < samMap[y].length; x++)
			{
				ret[c] = samMap[y][x];
				c++;
			}
		}
		return ret;
	}
	
	public void println(String s)
	{
		if(!p)
			return;
		System.out.println(s);
	}
	
	public void print(String s)
	{
		if(!p)
			return;
		System.out.print(s);
	}
	
	public double roundToPlace(double d, int i)
	{
		int t = 1;
		while(i > 1)
		{
			t = t*10;
			i--;
		}
		return ((int)(d*t))/(t*1.0);
	}
}
