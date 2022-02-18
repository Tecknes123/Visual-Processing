
public class pixel
{
	public int x = -1;
	public int y = -1;
	public double theta;
	public pixel previous;
	public int pX;
	public int pY;
	public int RGB;
	public int totH = -7;
	public int totV = -7;
	
	public pixel()
	{
		
	}
	
	public pixel(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public pixel(int x, int y, double theta)
	{
		this.x = x;
		this.y = y;
		this.theta = theta;
	}
	
	public pixel(int x, int y, double theta, pixel previous, int pX, int pY)
	{
		this.x = x;
		this.y = y;
		this.theta = theta;
		this.previous = previous;
		this.pX = pX;
		this.pY = pY;
	}
	
	public pixel(int x, int y, double theta, int RGB, pixel previous, int pX, int pY)
	{
		this.x = x;
		this.y = y;
		this.theta = theta;
		this.RGB = RGB;
		this.previous = previous;
		this.pX = pX;
		this.pY = pY;
	}
	
	public String toString()
	{
		return "("+x+", "+y+")";
	}
}
