import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class EBSynthManager
{
	public static void EBS(String name)
	{
		BufferedImage TDD = null;
		BufferedImage TSeg = null;
		try {
			TDD = ImageIO.read(new File("thesis\\frames\\"+name+"_TDD.png"));
			TSeg = ImageIO.read(new File("thesis\\frames\\"+name+"_TSeg.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse filename");
		}
		int TW = TSeg.getWidth();
		int TH = TSeg.getHeight();
		ArrayList<Color> mats = new ArrayList<Color>();
		
		//Records all unique colors (materials) in TSeg:
		for(int x = 0; x < TW; x++)
		{
			for(int y = 0; y < TH; y++)
			{
				Color c = new Color(TSeg.getRGB(x, y));
				boolean pres = false;
				for(int q = 0; q < mats.size(); q++)
				{
					if(c.equals(mats.get(q)))
						pres = true;
				}
				if(!pres && !c.equals(Color.BLACK))
					mats.add(c);
				
			}
		}
		
		//Creates material-sphere segmentation map:
		BufferedImage SSeg = new BufferedImage(300*mats.size(), 200, BufferedImage.TYPE_INT_RGB);
		for(int q = 0; q < mats.size(); q++)
		{
			BufferedImage SSub = null;
			try {
				SSub = ImageIO.read(new File("thesis\\materials\\blank.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int y = 0; y < 200; y++)
			{
				for(int x = 0; x < 300; x++)
				{
					Color c = null;
					if(x < 200)
					{
						c = new Color(SSub.getRGB(x, y));
						if(c.getRed() == 255)
							SSeg.setRGB(x+(300*q), y, mats.get(q).getRGB());
						else
							SSeg.setRGB(x+(300*q), y, Color.BLACK.getRGB());
					}else{
						SSeg.setRGB(x+(300*q), y, Color.BLACK.getRGB());
					}
				}
			}
		}
		
		//Creates ARDD seg map:
		BufferedImage SDD = new BufferedImage(300*mats.size(), 200, BufferedImage.TYPE_INT_RGB);
		for(int q = 0; q < mats.size(); q++)
		{
			BufferedImage SSub = null;
			try {
				SSub = ImageIO.read(new File("thesis\\SDD.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int y = 0; y < 200; y++)
			{
				for(int x = 0; x < 300; x++)
				{
					Color c = new Color(0, 74, 127);
					if(x < 200)
						c = new Color(SSub.getRGB(x, y));
					SDD.setRGB(x+(300*q), y, c.getRGB());
				}
			}
		}
		
		//Asks user to assign materials to corresponding segments
		BufferedImage SStyle = new BufferedImage(SSeg.getWidth(), SSeg.getHeight(), BufferedImage.TYPE_INT_RGB);
		BufferedImage segTemp = new BufferedImage(TW, TH, BufferedImage.TYPE_INT_RGB);
		for(int q = 0; q < mats.size(); q++)
		{
			for(int x = 0; x < TW; x++)
			{
				for(int y = 0; y < TH; y++)
				{
					Color c = new Color(TSeg.getRGB(x, y));
					if(!c.equals(mats.get(q)))
						segTemp.setRGB(x, y, Color.BLACK.getRGB());
					else
						segTemp.setRGB(x, y, c.getRGB());
				}
			}
			MainyMain.BI = segTemp;
			MainyMain.backup = MainyMain.getCopy(MainyMain.BI);
			MainyMain.JP.updateImg(MainyMain.BI);
			MainyMain.p("What material would you like to assign to this segment?");
			MainyMain.input = MainyMain.in.nextLine();
			BufferedImage SSub = null;
			try {
				SSub = ImageIO.read(new File("thesis\\materials\\"+MainyMain.input+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Unable to parse filename");
			}
			for(int x = 0; x < 300; x++)
			{
				for(int y = 0; y < 200; y++)
				{
					Color c = new Color(0, 74, 127);
					if(x < 200)
						c = new Color(SSub.getRGB(x, y));
					SStyle.setRGB(x+(300*q), y, c.getRGB());
				}
			}
		}
		
		try {
		    File outputfile = new File("thesis\\materials\\SSeg_"+name+".png");
		    ImageIO.write(SSeg, "png", outputfile);
		    outputfile = new File("thesis\\materials\\SStyle_"+name+".png");
		    ImageIO.write(SStyle, "png", outputfile);
		    outputfile = new File("thesis\\materials\\SDD_"+name+".png");
		    ImageIO.write(SDD, "png", outputfile);
		} catch (IOException e) {
			System.out.println("ERROR");
		}
		
		//Runs EBSynth on the provided and generated images to get the final result:
		try
        {  
         Runtime.getRuntime().exec(".\\thesis\\ebsynth -style thesis\\materials\\SStyle_"+name+".png -guide thesis\\materials\\SSeg.png thesis\\frames\\"+name+"_TSeg.png -weight 4.0 -guide thesis\\materials\\SDD.png thesis\\frames\\"+name+"_TDD.png -weight 1.0 -output thesis\\output.png");
        } 
        catch (Exception e) 
        { 
            System.out.println("HEY Buddy ! U r Doing Something Wrong "); 
            e.printStackTrace(); 
        } 
		
		
		p("Finished");
	}
	
	public static void BatchEBS(int num, int inc)
	{
		ArrayList<palette> pL = new ArrayList<palette>();
		for(int count = 1; count < num; count=count+inc)
		{
			boolean rep = true;
			while(rep)
			{
				palette p = new palette();
				String name = Integer.toString(count);
				while(name.length() < 4)
					name = "0"+name;
				BufferedImage TDD = null;
				BufferedImage TSeg = null;
				try {
					TDD = ImageIO.read(new File("thesis\\frames\\TDD_"+name+".png"));
					TSeg = ImageIO.read(new File("thesis\\frames\\TSeg_"+name+".png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Unable to parse filename");
				}
				p("Frame "+name);
				int TW = TSeg.getWidth();
				int TH = TSeg.getHeight();
				ArrayList<Color> mats = new ArrayList<Color>();
				
				//clears black BG from TDD
				for(int x = 0; x < TW; x++)
				{
					for(int y = 0; y < TH; y++)
					{
						Color c = new Color(TSeg.getRGB(x, y));
						if(c.equals(Color.BLACK))
							TDD.setRGB(x, y, Color.BLUE.getRGB());
					}
				}
				File outputfile = new File("thesis\\frames\\TDD_"+name+".png");
			    try {
					ImageIO.write(TDD, "png", outputfile);
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				
				
				//Records all unique colors (materials) in TSeg:
				for(int x = 0; x < TW; x++)
				{
					for(int y = 0; y < TH; y++)
					{
						Color c = new Color(TSeg.getRGB(x, y));
						boolean pres = false;
						for(int q = 0; q < mats.size(); q++)
						{
							if(c.equals(mats.get(q)))
								pres = true;
						}
						if(!pres && !c.equals(Color.BLACK))
							mats.add(c);
						
					}
				}
				
				//Creates material-sphere segmentation map:
				BufferedImage SSeg = new BufferedImage(300*mats.size(), 200, BufferedImage.TYPE_INT_RGB);
				for(int q = 0; q < mats.size(); q++)
				{
					BufferedImage SSub = null;
					try {
						SSub = ImageIO.read(new File("thesis\\materials\\blank.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for(int y = 0; y < 200; y++)
					{
						for(int x = 0; x < 300; x++)
						{
							Color c = null;
							if(x < 200)
							{
								c = new Color(SSub.getRGB(x, y));
								if(c.getRed() == 255)
									SSeg.setRGB(x+(300*q), y, mats.get(q).getRGB());
								else
									SSeg.setRGB(x+(300*q), y, Color.BLACK.getRGB());
							}else{
								SSeg.setRGB(x+(300*q), y, Color.BLACK.getRGB());
							}
						}
					}
				}
				
				//Creates ARDD seg map:
				BufferedImage SDD = new BufferedImage(300*mats.size(), 200, BufferedImage.TYPE_INT_RGB);
				for(int q = 0; q < mats.size(); q++)
				{
					BufferedImage SSub = null;
					try {
						SSub = ImageIO.read(new File("thesis\\SDD.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for(int y = 0; y < 200; y++)
					{
						for(int x = 0; x < 300; x++)
						{
							Color c = new Color(0, 74, 127);
							if(x < 200)
								c = new Color(SSub.getRGB(x, y));
							SDD.setRGB(x+(300*q), y, c.getRGB());
						}
					}
				}
				
				//Asks user to assign materials to corresponding segments
				BufferedImage SStyle = new BufferedImage(SSeg.getWidth(), SSeg.getHeight(), BufferedImage.TYPE_INT_RGB);
				BufferedImage segTemp = new BufferedImage(TW, TH, BufferedImage.TYPE_INT_RGB);
				for(int q = 0; q < mats.size(); q++)
				{
					BufferedImage SSub = null;
					
					//Checks previous palettes to see if already assigned
					boolean set = false;
					for(int c = 0; c < pL.size() && !set; c++)
					{
						for(int c2 = 0; c2 < pL.get(c).colors.size() && !set; c2++)
						{
							if(mats.get(q).equals(pL.get(c).colors.get(c2)))
							{
								String t = pL.get(c).textures.get(c2);
								p.addTex(t, mats.get(q));
								try {
									SSub = ImageIO.read(new File(t));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									System.out.println("Unable to parse filename");
								}
								set = true;
							}
						}
					}
					if(!set)
					{
						//Highlights mat area in question
						for(int x = 0; x < TW; x++)
						{
							for(int y = 0; y < TH; y++)
							{
								Color c = new Color(TSeg.getRGB(x, y));
								if(!c.equals(mats.get(q)))
									segTemp.setRGB(x, y, Color.BLACK.getRGB());
								else
									segTemp.setRGB(x, y, c.getRGB());
							}
						}
						MainyMain.BI = segTemp;
						MainyMain.backup = MainyMain.getCopy(MainyMain.BI);
						MainyMain.JP.updateImg(MainyMain.BI);
						
						//Takes user input
						MainyMain.p("What material would you like to assign to this segment?");
						MainyMain.input = MainyMain.in.nextLine();
						String t = "";
						try {
							t = "thesis\\materials\\"+MainyMain.input+".png";
							SSub = ImageIO.read(new File(t));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println("Unable to parse filename");
						}
						p.addTex(t, mats.get(q));
					}
					for(int x = 0; x < 300; x++)
					{
						for(int y = 0; y < 200; y++)
						{
							Color c = new Color(0, 74, 127);
							if(x < 200)
								c = new Color(SSub.getRGB(x, y));
							SStyle.setRGB(x+(300*q), y, c.getRGB());
						}
					}
				}
				
				try {
				    outputfile = new File("thesis\\materials\\SSeg_"+name+".png");
				    ImageIO.write(SSeg, "png", outputfile);
				    outputfile = new File("thesis\\materials\\SStyle_"+name+".png");
				    ImageIO.write(SStyle, "png", outputfile);
				    outputfile = new File("thesis\\materials\\SDD_"+name+".png");
				    ImageIO.write(SDD, "png", outputfile);
				} catch (IOException e) {
					System.out.println("ERROR");
				}
				
				//Deletes image if already present
				try {
					File test = new File("thesis\\output\\out_"+name+".png");
					test.delete();
				} catch (Exception e) {
					p("File space already available");
				}
				
				//Runs EBSynth on the provided and generated images to get the final result:
				try
		        {  
					Runtime.getRuntime().exec(".\\thesis\\ebsynth -style thesis\\materials\\SStyle_"+name+".png -guide thesis\\materials\\SSeg_"+name+".png thesis\\frames\\TSeg_"+name+".png -weight 2.0 -guide thesis\\materials\\SDD_"+name+".png thesis\\frames\\TDD_"+name+".png -weight 1.0 -output thesis\\output\\out_"+name+".png");
		        } 
		        catch (Exception e) 
		        { 
		            System.out.println("HEY Buddy ! U r Doing Something Wrong "); 
		            e.printStackTrace(); 
		        }
				
				BufferedImage test = null;
				int tries = 0;
				System.out.print("Rendering...");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				while(test == null)
				{
					tries++;
					try {
						test = ImageIO.read(new File("thesis\\output\\out_"+name+".png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.print(".");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				MainyMain.BI = test;
				MainyMain.backup = MainyMain.getCopy(MainyMain.BI);
				MainyMain.JP.updateImg(MainyMain.BI);
				MainyMain.p("\nIs this acceptable?");
				String input = MainyMain.in.nextLine();
				if(input.equals("y"))
				{
					p("Ok, moving on...");
					pL.add(p);
					rep = false;
				}
			}
		}
		p("Finished");
	}
	
	public static void p(String s)
	{
		System.out.println(s);
	}
}
