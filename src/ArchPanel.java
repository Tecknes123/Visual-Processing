import java.awt.Color;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ArchPanel extends JPanel
{

	public Graphics Q;
	public BufferedImage img;
	public ArchPanel(BufferedImage i)
	{
		super();
		img = i;
	}
	
	public void updateImg(BufferedImage i)
	{
		//System.out.println("Updating");
		img = i;
		this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		MainyMain.BI = img;
		repaint();
	}
	
	public void paint(Graphics g)
	{
		//Q.create();
		Q = g;
		Q.setColor(Color.WHITE);
		Q.drawRect(0, 0, this.getWidth(), this.getHeight());
		Q.drawImage(img, 0, 0, null);
	}
}
