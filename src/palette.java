import java.util.ArrayList;
import java.awt.Color;

public class palette
{
	
	public ArrayList<String> textures;
	public ArrayList<Color> colors;
	
	public palette()
	{
		textures = new ArrayList<String>();
		colors = new ArrayList<Color>();
	}
	
	public void addTex(String texUrl, Color col)
	{
		for(int c = 0; c < colors.size(); c++)
		{
			if(colors.get(c).equals(col))
			{
				System.out.println("Color already present");
				return;
			}
		}
		textures.add(texUrl);
		colors.add(col);
	}
	
	public boolean equals(palette p)
	{
		if(colors.size() != p.colors.size())
			return false;
		for(int c = 0; c < colors.size(); c++)
		{
			if(!(colors.get(c).equals(p.colors.get(c)) && textures.get(c).equals(p.textures.get(c))))
			{
				return false;
			}
		}
		return true;
	}
}
