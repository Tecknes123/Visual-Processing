import java.awt.Color;

public class cwLocator
{
	
	//Here, pX and pY represent the location of the center pixel, not the previous one (that's sX & sY)
	public static int pX;
	public static int pY;
	public static int count = 0;
	
	public static ret start(int pX, int pY, int sX, int sY)
	{
		cwLocator.pX = pX;
		cwLocator.pY = pY;
		int x = sX;
		int y = sY;
		ret r = new ret();
		count = 0;
		if(x == -1 && y == -1)
			r = U();
		else if(x == 0 && y == -1)
			r = UR();
		else if(x == 1 && y == -1)
			r = R();
		else if(x == 1 && y == 0)
			r = DR();
		else if(x == 1 && y == 1)
			r = D();
		else if(x == 0 && y == 1)
			r = DL();
		else if(x == -1 && y == 1)
			r = L();
		else if(x == -1 && y == 0)
			r = UL();
		else
			System.out.println("Wtf are you doing?");
		if(r.x == -7)
			System.out.println("Timed out");
		if(r.x == x && r.y == y)
			System.out.println("Returned to previous...");
		return r;
	}
	
	public static ret UL()
	{
		count++;
		if(count > 8)
			return new ret(-7, -7);
		Color c = new Color(MainyMain.BI.getRGB(pX-1, pY-1));
		if(c.getBlue() == 0)
			return new ret(-1, -1);
		return U();
	}
	
	public static ret U()
	{
		count++;
		if(count > 8)
			return new ret(-7, -7);
		Color c = new Color(MainyMain.BI.getRGB(pX, pY-1));
		if(c.getBlue() == 0)
			return new ret(0, -1);
		return UR();
	}
	
	public static ret UR()
	{
		count++;
		if(count > 8)
			return new ret(-7, -7);
		Color c = new Color(MainyMain.BI.getRGB(pX+1, pY-1));
		if(c.getBlue() == 0)
			return new ret(1, -1);
		return R();
	}
	
	public static ret R()
	{
		count++;
		if(count > 8)
			return new ret(-7, -7);
		Color c = new Color(MainyMain.BI.getRGB(pX+1, pY));
		if(c.getBlue() == 0)
			return new ret(1, 0);
		return DR();
	}
	
	public static ret DR()
	{
		count++;
		if(count > 8)
			return new ret(-7, -7);
		Color c = new Color(MainyMain.BI.getRGB(pX+1, pY+1));
		if(c.getBlue() == 0)
			return new ret(1, 1);
		return D();
	}

	public static ret D()
	{
		count++;
		if(count > 8)
			return new ret(-7, -7);
		Color c = new Color(MainyMain.BI.getRGB(pX, pY+1));
		if(c.getBlue() == 0)
			return new ret(0, 1);
		return DL();
	}
	
	public static ret DL()
	{
		count++;
		if(count > 8)
			return new ret(-7, -7);
		Color c = new Color(MainyMain.BI.getRGB(pX-1, pY+1));
		if(c.getBlue() == 0)
			return new ret(-1, 1);
		return L();
	}
	
	public static ret L()
	{
		count++;
		if(count > 8)
			return new ret(-7, -7);
		Color c = new Color(MainyMain.BI.getRGB(pX-1, pY));
		if(c.getBlue() == 0)
			return new ret(-1, 0);
		return UL();
	}
	
	static class ret
	{
		public int x;
		public int y;
		public ret(){}
		public ret(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
}


