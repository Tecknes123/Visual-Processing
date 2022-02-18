import java.awt.Color;
import java.util.ArrayList;

public class potPixel
{
	public ArrayList<Color> col = new ArrayList<Color>();
	public ArrayList<Double> rat = new ArrayList<Double>();
	
	public int add(Color col, double rat)
	{
		this.col.add(col);
		this.rat.add(rat);
		
		/*if(this.col.size() > 3)
		{
			//System.out.println("resetting pix weights");
			int rat1 = -1;
			int rat2 = -1;
			int rat3 = -1;
			for(int i = 0; i < this.col.size(); i++)
			{
				double d = this.rat.get(i);
				if(rat1 == -1 || d > this.rat.get(rat1))
				{
					rat3 = rat2;
					rat2 = rat1;
					rat1 = i;
				}else if(rat2 == -1 || d > this.rat.get(rat2)){
					rat3 = rat2;
					rat2 = i;
				}else if(rat3 == -1 || d > this.rat.get(rat3)){
					rat3 = i;
				}
			}
			ArrayList<Color> colTemp = new ArrayList<Color>();
			ArrayList<Double> ratTemp = new ArrayList<Double>();
			colTemp.add(this.col.get(rat1));
			ratTemp.add(this.rat.get(rat1));
			colTemp.add(this.col.get(rat2));
			ratTemp.add(this.rat.get(rat2));
			colTemp.add(this.col.get(rat3));
			ratTemp.add(this.rat.get(rat3));
			
			this.col = colTemp;
			this.rat = ratTemp;
		}*/
		return this.col.size()-1;
	}
	
	public int add(int r, int g, int b, double rat)
	{
		Color c = new Color(r, g, b);
		col.add(c);
		this.rat.add(rat);
		
		/*if(this.col.size() > 3)
		{
			//System.out.println("resetting pix weights");
			int rat1 = -1;
			int rat2 = -1;
			int rat3 = -1;
			for(int i = 0; i < this.col.size(); i++)
			{
				double d = this.rat.get(i);
				if(rat1 == -1 || d > this.rat.get(rat1))
				{
					rat3 = rat2;
					rat2 = rat1;
					rat1 = i;
				}else if(rat2 == -1 || d > this.rat.get(rat2)){
					rat3 = rat2;
					rat2 = i;
				}else if(rat3 == -1 || d > this.rat.get(rat3)){
					rat3 = i;
				}
			}
			ArrayList<Color> colTemp = new ArrayList<Color>();
			ArrayList<Double> ratTemp = new ArrayList<Double>();
			colTemp.add(this.col.get(rat1));
			ratTemp.add(this.rat.get(rat1));
			colTemp.add(this.col.get(rat2));
			ratTemp.add(this.rat.get(rat2));
			colTemp.add(this.col.get(rat3));
			ratTemp.add(this.rat.get(rat3));
			
			this.col = colTemp;
			this.rat = ratTemp;
		}*/
		return col.size()-1;
	}
	
	public int size()
	{
		return col.size();
	}
	
	public Color getC()
	{
		double r = 0.0;
		double g = 0.0;
		double b = 0.0;
		double sumRat = 0.0;
		
		for(int i = 0; i < col.size(); i++)
		{
			r = r+col.get(i).getRed()*rat.get(i);
			g = g+col.get(i).getGreen()*rat.get(i);
			b = b+col.get(i).getBlue()*rat.get(i);
			sumRat = sumRat + rat.get(i);
		}
		r = r/sumRat;
		g = g/sumRat;
		b = b/sumRat;
		
		return new Color((int)r, (int)g, (int)b);
	}
}
