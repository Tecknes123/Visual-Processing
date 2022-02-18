import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class potImg
{
	public int h = 0;
	public int w = 0;
	public potPixel[][] ar = null;
	
	public potImg(int w, int h)
	{
		this.w = w;
		this.h = h;
		ar = new potPixel[w][h];
		for(int x = 0; x < w; x++)
			for(int y = 0; y < h; y++)
				ar[x][y] = new potPixel();
	}
	
	public BufferedImage toBI()
	{
		BufferedImage ret  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < w; x++)
			for(int y = 0; y < h; y++)
				ret.setRGB(x, y, ar[x][y].getC().getRGB());
		return ret;
	}
	
	public void flattenPix()
	{
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				Color c = ar[x][y].getC();
				ar[x][y].col = new ArrayList<Color>();
				ar[x][y].col.add(c);
				ar[x][y].rat = new ArrayList<Double>();
				ar[x][y].rat.add(1.0);
			}
		}
	}
	
	public void printSize()
	{
		int pixN = w*h;
		int colN = 0;
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				colN = colN+ar[x][y].col.size();
			}
		}
		System.out.println("Dims: ("+w+", "+h+", "+pixN+" potPixels, "+colN+" colors/weights (avg: "+(colN/pixN)+" per px).");
	}
}
