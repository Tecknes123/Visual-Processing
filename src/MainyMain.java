import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainyMain
{
	public static Image img;
	public static BufferedImage BI;
	public static BufferedImage backup;
	public static BufferedImage previous;
	public static Graphics Q;
	public static JFrame JF = new JFrame();
	public static ArchPanel JP;
	public static String input = "";
	public static boolean run = true;
	public static Scanner in = new Scanner(System.in);
	public static boolean highlight = false;
	public static pixel selPix = null;
	public static boolean zoom = false;
	public static boolean zSel = false;
	public static BufferedImage preHigh;
	public static ArrayList<pixel> edge = new ArrayList<pixel>();
	public static String process = "null";
	
	public static void main(String[] args) throws IOException
	{
		// TODO Auto-generated method stub
		System.out.println("Which image would you like to open?");
		input = in.nextLine();
		BI = ImageIO.read(new File("images\\"+input));
		backup = getCopy(BI);
		JP = new ArchPanel(BI);
		JP.setPreferredSize(new Dimension(BI.getWidth(null), BI.getHeight(null)));
		JF.getContentPane().add(JP);
		JF.pack();
		JF.setVisible(true);
		JP.setVisible(true);
		JF.setTitle("Tom's Homework");
		JF.setIconImage(BI);
		while(run == true)
		{
			System.out.println("Enter a command:");
			input = in.nextLine();
			while(input.equals(""))
				input = in.nextLine();
			execute();
			//p("done");
		}
//		makeGreyScale();
//		BI = JP.img;
//		evalPrewitt();
//		//evalSegHSV();
//		//evalZoomShrink();
//		JF.pack();
//		System.out.println("Done");
	}
	
	private static void execute()
	{
		//p("executing");
		input = input.toLowerCase();
		if(input.equals("help"))
		{
			System.out.println("Available Commands:");
			System.out.println("  help");
			System.out.println("  neg");
			System.out.println("  segReg");
			System.out.println("  segHSV");
			System.out.println("  segBG");
			System.out.println("  greyScale");
			System.out.println("  roberts");
			System.out.println("  sobel");
			System.out.println("  prewitt");
			System.out.println("  zoom");
			System.out.println("  unzoom");
			System.out.println("  zoomShrink");
			System.out.println("  resetImg");
			System.out.println("  newImg");
			System.out.println("  undo");
			System.out.println("  bin");
			//System.out.println("  pix x, y");
			System.out.println("  pix");
			System.out.println("  exit");
			return;
		}else if(input.equals("exit")){
			run = false;
			System.out.println("Goodbye!");
			JF.dispose();
			System.exit(0);
		}
		if(zoom == true)
		{
			if(input.equals("unzoom"))
			{
				JP.updateImg(BI);
				zoom = false;
			}else if(input.equals("sel")){
				BufferedImage zi = null;
				if(zSel == false)
				{
					zSel = true;
					zi = BI.getSubimage(selPix.x-15, selPix.y-15, 30, 30);
				}else{
					zSel = false;
					zi = preHigh.getSubimage(selPix.x-15, selPix.y-15, 30, 30);
				}
				Image q = zi.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
				zi = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
				zi.getGraphics().drawImage(q, 0, 0 , null);
				JP.updateImg(zi);
				zi.getGraphics().dispose();
				return;
				
			}else{
				p("Cannot execute while zoomed (use unzoom)");
			}
			return;
		}
		if(highlight == true)
		{
			if(input.equals("zoom"))
			{
				zoom = true;
				BufferedImage zi = preHigh.getSubimage(selPix.x-15, selPix.y-15, 30, 30);
				Image q = zi.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
				zi = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
				zi.getGraphics().drawImage(q, 0, 0 , null);
				JP.updateImg(zi);
				zi.getGraphics().dispose();
				return;
			}
			BI = preHigh;
			JP.updateImg(preHigh);
			highlight = false;
		}
		if(input.equals("undo")){
			JP.updateImg(getCopy(previous));
		}else if(input.equals("newimg")){
			previous = getCopy(BI);
			System.out.println("Which image would you like to open?");
			input = in.nextLine();
			try {
				BI = ImageIO.read(new File("images\\"+input));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Unable to parse filename");
			}
			backup = getCopy(BI);
			JP.updateImg(BI);
		}else if(input.equals("reset")){
			JP.updateImg(getCopy(backup));
		}else if(input.equals("pix")){
			System.out.println("X?");
			int pX = in.nextInt();
			System.out.println("Y?");
			int pY = in.nextInt();
			Color c = new Color(BI.getRGB(pX, pY));
			int r = c.getRed();
			int g = c.getGreen();
			int b = c.getBlue();
			System.out.println("("+pX+", "+pY+"): R: "+r+", G: "+g+", B: "+b);
			//marker:
			preHigh = getCopy(BI);
			selPix = new pixel(pX, pY);
			highlight = true;
			markBI(pX, pY, Color.PINK);
//			input = input.substring(4);
//			String sub = "";
//			for(int q = 0; q < input.length() && input.charAt(q) != ','; q++)
//			{
//				sub = sub + "" + input.charAt(q);
//			}
				
		}else if(input.equals("t")){
			JP.updateImg(BI);
			JF.pack();
			return;
		}else if(input.equals("neg")){
			previous = getCopy(BI);
			evalNegative();
		}else if(input.equals("edge")){
			previous = getCopy(BI);
			System.out.println("X?");
			int sX = in.nextInt();
			System.out.println("Y?");
			int sY = in.nextInt();
			System.out.println("previous X?");
			int pX = in.nextInt();
			System.out.println("previous Y?");
			int pY = in.nextInt();
			findEdge(sX, sY, pX, pY);
		}else if(input.equals("verts")){
			previous = getCopy(BI);
			findVerts();
		}else if(input.equals("bin")){
			previous = getCopy(BI);
			evalBinary();
		}else if(input.equals("segreg")){
			previous = getCopy(BI);
			makeGreyScale();
			JF.pack();
			evalSeg();
		}else if(input.equals("gr")){
			previous = getCopy(BI);
			//makeGreyScale();
			evalGaussianReduce();
		}else if(input.equals("ge")){
			previous = getCopy(BI);
			//makeGreyScale();
			evalGaussianExpand();
		}else if(input.equals("lap")){
			previous = getCopy(BI);
			System.out.println("Level?");
			int l = in.nextInt();
			makeGreyScale();
			evalLaplacian(l);
			BI = JP.img;
			JF.pack();
		}else if(input.equals("testlap")){
			previous = getCopy(BI);
			System.out.println("Level?");
			int l = in.nextInt();
			JP.updateImg(evalLaplacian(l, BI));
			
		}else if(input.equals("com")){
			previous = getCopy(BI);
			System.out.println("Level?");
			int l = in.nextInt();
			makeGreyScale();
			evalImgBlend(l);
			BI = JP.img;
			JF.pack();
		}else if(input.equals("como")){
			previous = getCopy(BI);
			System.out.println("Level?");
			int l = in.nextInt();
			makeGreyScale();
			evalImgBlendOrig(l);
			BI = JP.img;
			JF.pack();
		}else if(input.equals("net")){
			previous = getCopy(BI);
			netOps.gen(in);
		}else if(input.equals("netw")){
			previous = getCopy(BI);
			netOps.weights(in);
		}else if(input.equals("netwl")){
			previous = getCopy(BI);
			netOps.lap(in);
		}else if(input.equals("netrt")){
			previous = getCopy(BI);
			netOps.retrain(in);
		}else if(input.equals("netms")){
			previous = getCopy(BI);
			netOps.multiScale(in);
		}else if(input.equals("netwb")){
			previous = getCopy(BI);
			netOps.weightedBase(in);
		}else if(input.equals("greyscale") || input.equals("gs")){
			previous = getCopy(BI);
			makeGreyScale();
		}else if(input.equals("maxcol")){
			previous = getCopy(BI);
			JP.updateImg(maxColorSpread(BI));
		}else if(input.equals("ebs")){
			previous = getCopy(BI);
			System.out.println("Which image would you like to open?");
			input = in.nextLine();
			EBSynthManager.EBS(input);
		}else if(input.equals("ebsb")){
			previous = getCopy(BI);
			System.out.println("How many frames?");
			input = in.nextLine();
			int num = Integer.parseInt(input);
			System.out.println("What increment?");
			input = in.nextLine();
			int inc = Integer.parseInt(input);
			EBSynthManager.BatchEBS(num, inc);
		}else if (input.equals("testcmd")){
			//rip
		}else{
			p("Command not recognized");
		}
		BI = JP.img;
		JF.pack();
		input = "";
	}
	
	
	
	public static void p(String s)
	{
		System.out.println(s);
	}
	
	public static void markBI(int pX, int pY, Color c)
	{
		try{
			BI.setRGB(pX-1, pY, c.getRGB());
			BI.setRGB(pX+1, pY, c.getRGB());
			BI.setRGB(pX, pY-1, c.getRGB());
			BI.setRGB(pX, pY+1, c.getRGB());
			BI.setRGB(pX-1, pY-1, c.getRGB());
			BI.setRGB(pX+1, pY+1, c.getRGB());
			BI.setRGB(pX+1, pY-1, c.getRGB());
			BI.setRGB(pX-1, pY+1, c.getRGB());
			BI.setRGB(pX-2, pY, c.getRGB());
			BI.setRGB(pX+2, pY, c.getRGB());
			BI.setRGB(pX, pY-2, c.getRGB());
			BI.setRGB(pX, pY+2, c.getRGB());
		}catch(ArrayIndexOutOfBoundsException a){
			//do nothing
		}
		JP.updateImg(BI);
		JF.pack();
	}
	
	public static BufferedImage getCopy(BufferedImage i)
	{
		BufferedImage copy = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < i.getWidth(); x++)
		{
			for(int y = 0; y < i.getHeight(); y++)
			{
				Color c = new Color(i.getRGB(x,y));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				c = new Color(r, g, b);
				copy.setRGB(x, y, c.getRGB());
			}
		}
		return copy;
	}
	
	public static void findEdge(int x, int y, int qX, int qY)
	{
		int count = 0;
		pixel prev = null;
		int pX = -7;
		int pY = -7;
		for(int pCount = 0; pCount < 3000; pCount++)
		{
			//Horizontal Evaluation:
			System.out.println("HE:");
			int totH = 0;
			for(int yC = -1; yC < 2; yC++)
			{
				for(int xC = -1; xC < 2; xC++)
				{
					
					Color cT = new Color(BI.getRGB(xC+x, yC+y));
					int r = 0;
					if(cT.getBlue() < 200)
						r = xC;
					if(yC == 0)
						r = r*2;
					totH = totH + r;
					System.out.print(r+" ");
				}
				System.out.println("");
			}
			//Vertical Evaluation:
			System.out.println("VE:");
			int totV = 0;
			for(int yC = -1; yC < 2; yC++)
			{
				for(int xC = -1; xC < 2; xC++)
				{
					Color cT = new Color(BI.getRGB(xC+x, yC+y));
					int r = 0;
					if(cT.getBlue() < 200)
						r = yC;
					if(xC == 0)
						r = r*2;
					totV = totV + r;
					System.out.print(r+" ");
				}
				System.out.println("");
			}
			double theta = 0.0;
			if(totH == 0 && totV > 0)
				theta = 90.0;
			else if(totH == 0 && totV < 0)
				theta = -90.0;
			else
				theta = Math.toDegrees(Math.atan((totV*1.0)/totH));
			
			if(totV >= 0 && totH >= 0){
				//do nothing
			}else if(totV < 0  && totH >= 0){
				//also do nothing
			}else if(totV < 0 && totH < 0){
				theta = 180.0 - theta;
			}else if(totV >= 0 && totH < 0){
				theta = -180.0 + theta;
			}
			pixel p = null;
			if(count == 0)
			{
				p = new pixel(x, y, theta);
				pX = qX; pY = qY;
			}else
				p = new pixel(x, y, theta, prev, pX, pY);
			p.totH = totH;
			p.totV = totV;
			count++;
			edge.add(p);
			p(p.toString()+" added - totV: "+totV+", totH: "+totH+", theta: "+theta);
			BI.setRGB(x, y, Color.red.getRGB());
			JP.updateImg(BI);
			prev = p;
			
			//clockwise location:
			cwLocator.ret r = cwLocator.start(x, y, pX, pY);
			if(r.x == -7)
				return;
			x = x + r.x;
			y = y + r.y;
			pX = prev.x - x;
			pY = prev.y - y;
			if(x == edge.get(0).x && y == edge.get(0).y)
			{
				p("Edge evaluated");
				return;
			}
		}
	}
	
	public static void findVerts()
	{
		
		int LV = 0;
		boolean cont = true;
		ArrayList<pixel> verts = new ArrayList<pixel>();
		while(cont)
		{
			ArrayList<Double> TL = new ArrayList<Double>();
			ArrayList<Double> ATSDL = new ArrayList<Double>();
			Double AT = 0.0;
			Double ATSD = 0.0;
			boolean cont2 = true;
			for(int c = 0; cont2; c++)
			{
				
				TL.add(edge.get(LV+c).theta);
				for(int c2 = 0; c2 < TL.size(); c2++)
					AT = AT + TL.get(c2);
				AT = AT/(c+1.0);
				for(int c2 = 0; c2 < TL.size(); c2++)
					ATSD = ATSD + Math.abs(TL.get(c2)-AT);
				ATSD = ATSD/(c+1.0);
				p("pixel "+(LV+c)+", "+c+"L - "+"tH: "+edge.get(LV+c).totH+", tV: "+edge.get(LV+c).totV+" - Theta: "+TL.get(c)+" - ATSD: "+ATSD+" - ("+edge.get(LV+c).x+", "+edge.get(LV+c).y+")");
				ATSDL.add(ATSD);
				//if(LV+c+1 == edge.size() || (c > 2 && ATSDL.get(c) > ATSDL.get(c-1)))
				//if(LV+c+1 == edge.size() || (c > 2 && ATSDL.get(c) > ATSDL.get(c-1) && ATSDL.get(c-1) > ATSDL.get(c-2)))
				if(LV+c+1 == edge.size() || (c > 3 && ATSDL.get(c) > ATSDL.get(c-1) && ATSDL.get(c-1) > ATSDL.get(c-2) && ATSDL.get(c-2) > ATSDL.get(c-3)))
				{
					if(LV+c+1 == edge.size())
						cont = false;
					else{
						LV = LV + c-1;
						int fX = edge.get(LV).x;
						int fY = edge.get(LV).y;
						markBI(fX, fY, Color.MAGENTA);
						JP.updateImg(BI);
						//p("Vertex found at: ("+fX+", "+fY+")");
						pixel q = new pixel();
						q.x = fX;
						q.y = fY;
						verts.add(q);
					}
					cont2 = false;
				}
			}
		}
		
		for(int c = 0; c < verts.size(); c++)
		{
			p(c+": Vertex found at: ("+verts.get(c).x+", "+verts.get(c).y+")");
		}
	}
	
	public static void evalGaussianReduce()
	{
		process = "GR";
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = new BufferedImage(w/2, h/2, BufferedImage.TYPE_INT_RGB);
		if(w%2 != 0)
		{
			p("odd width");
		}
		if(h%2 != 0)
		{
			p("odd height");
		}
		for(int x = 1; x < w; x=x+2)
		{
			for(int y = 1; y < h; y=y+2)
			{
				if(x == w-1 && y == h-1)
				{
					// (1/3) + (4/8) + (4/24) = 1, in Gaussian proportions
					double A = avgR(x, y)/3.0;
					double B = (avgR(x-1, y) + avgR(x, y) + avgR(x, y-1) + avgR(x, y))/8.0;
					double C = (avgR(x-1, y-1) + avgR(x-1, y) + avgR(x, y-1) + avgR(x, y))/24.0;
					int resR = (int)(A + B + C);
					
					A = avgG(x, y)/3.0;
					B = (avgG(x-1, y) + avgG(x, y) + avgG(x, y-1) + avgG(x, y))/8.0;
					C = (avgG(x-1, y-1) + avgG(x-1, y) + avgG(x, y-1) + avgG(x, y))/24.0;
					int resG = (int)(A + B + C);
					
					A = avgB(x, y)/3.0;
					B = (avgB(x-1, y) + avgB(x, y) + avgB(x, y-1) + avgB(x, y))/8.0;
					C = (avgB(x-1, y-1) + avgB(x-1, y) + avgB(x, y-1) + avgB(x, y))/24.0;
					int resB = (int)(A + B + C);
					
					BIR.setRGB(x/2, y/2, new Color(resR, resG, resB).getRGB());
				}else if(x == w-1){
					double A = avgR(x, y)/3.0;
					double B = (avgR(x-1, y) + avgR(x, y+1) + avgR(x, y-1) + avgR(x, y))/8.0;
					double C = (avgR(x-1, y-1) + avgR(x-1, y+1) + avgR(x, y-1) + avgR(x, y+1))/24.0;
					int resR = (int)(A + B + C);
					
					A = avgG(x, y)/3.0;
					B = (avgG(x-1, y) + avgG(x, y+1) + avgG(x, y-1) + avgG(x, y))/8.0;
					C = (avgG(x-1, y-1) + avgG(x-1, y+1) + avgG(x, y-1) + avgG(x, y+1))/24.0;
					int resG = (int)(A + B + C);
					
					A = avgB(x, y)/3.0;
					B = (avgB(x-1, y) + avgB(x, y+1) + avgB(x, y-1) + avgB(x, y))/8.0;
					C = (avgB(x-1, y-1) + avgB(x-1, y+1) + avgB(x, y-1) + avgB(x, y+1))/24.0;
					int resB = (int)(A + B + C);
					
					BIR.setRGB(x/2, y/2, new Color(resR, resG, resB).getRGB());
					
					/*double A = avgC(x, y)/3.0;
					double B = (avgC(x-1, y) + avgC(x, y) + avgC(x, y-1) + avgC(x, y+1))/8.0;
					double C = (avgC(x-1, y-1) + avgC(x-1, y+1) + avgC(x, y-1) + avgC(x, y+1))/24.0;
					int res = (int)(A + B + C);
					BIR.setRGB(x/2, y/2, new Color(res, res, res).getRGB());*/
				}else if(y == h-1){
					double A = avgR(x, y)/3.0;
					double B = (avgR(x-1, y) + avgR(x, y) + avgR(x, y-1) + avgR(x+1, y))/8.0;
					double C = (avgR(x-1, y-1) + avgR(x-1, y) + avgR(x+1, y-1) + avgR(x+1, y))/24.0;
					int resR = (int)(A + B + C);
					
					A = avgG(x, y)/3.0;
					B = (avgG(x-1, y) + avgG(x, y) + avgG(x, y-1) + avgG(x+1, y))/8.0;
					C = (avgG(x-1, y-1) + avgG(x-1, y) + avgG(x+1, y-1) + avgG(x+1, y))/24.0;
					int resG = (int)(A + B + C);
					
					A = avgB(x, y)/3.0;
					B = (avgB(x-1, y) + avgB(x, y) + avgB(x, y-1) + avgB(x+1, y))/8.0;
					C = (avgB(x-1, y-1) + avgB(x-1, y) + avgB(x+1, y-1) + avgB(x+1, y))/24.0;
					int resB = (int)(A + B + C);
					
					BIR.setRGB(x/2, y/2, new Color(resR, resG, resB).getRGB());
					
					/*double A = avgC(x, y)/3.0;
					double B = (avgC(x-1, y) + avgC(x+1, y) + avgC(x, y-1) + avgC(x, y))/8.0;
					double C = (avgC(x-1, y-1) + avgC(x-1, y) + avgC(x+1, y-1) + avgC(x+1, y))/24.0;
					int res = (int)(A + B + C);
					BIR.setRGB(x/2, y/2, new Color(res, res, res).getRGB());*/
				}else{
					double A = avgR(x, y)/3.0;
					double B = (avgR(x-1, y) + avgR(x+1, y) + avgR(x, y-1) + avgR(x, y+1))/8.0;
					double C = (avgR(x-1, y-1) + avgR(x-1, y+1) + avgR(x+1, y-1) + avgR(x+1, y+1))/24.0;
					int resR = (int)(A + B + C);
					
					A = avgG(x, y)/3.0;
					B = (avgG(x-1, y) + avgG(x+1, y) + avgG(x, y-1) + avgG(x, y+1))/8.0;
					C = (avgG(x-1, y-1) + avgG(x-1, y+1) + avgG(x+1, y-1) + avgG(x+1, y+1))/24.0;
					int resG = (int)(A + B + C);
					
					A = avgB(x, y)/3.0;
					B = (avgB(x-1, y) + avgB(x+1, y) + avgB(x, y-1) + avgB(x, y+1))/8.0;
					C = (avgB(x-1, y-1) + avgB(x-1, y+1) + avgB(x+1, y-1) + avgB(x+1, y+1))/24.0;
					int resB = (int)(A + B + C);
					
					BIR.setRGB(x/2, y/2, new Color(resR, resG, resB).getRGB());
					
					/*double A = avgC(x, y)/3.0;
					double B = (avgC(x-1, y) + avgC(x+1, y) + avgC(x, y-1) + avgC(x, y+1))/8.0;
					double C = (avgC(x-1, y-1) + avgC(x-1, y+1) + avgC(x+1, y-1) + avgC(x+1, y+1))/24.0;
					int res = (int)(A + B + C);
					BIR.setRGB(x/2, y/2, new Color(res, res, res).getRGB());*/
				}
			}
		}
		JP.updateImg(BIR);
	}
	
	public static BufferedImage evalGaussianReduce(BufferedImage deng)
	{
		int w = deng.getWidth();
		int h = deng.getHeight();
		BufferedImage BIR  = new BufferedImage(w/2, h/2, BufferedImage.TYPE_INT_RGB);
		if(w%2 != 0)
		{
			p("odd width");
		}
		if(h%2 != 0)
		{
			p("odd height");
		}
		for(int x = 1; x < w; x=x+2)
		{
			for(int y = 1; y < h; y=y+2)
			{
				if(x == w-1 && y == h-1)
				{
					// (1/3) + (4/8) + (4/24) = 1, in Gaussian proportions
					double A = avgR(x, y,deng)/3.0;
					double B = (avgR(x-1, y,deng) + avgR(x, y,deng) + avgR(x, y-1,deng) + avgR(x, y,deng))/8.0;
					double C = (avgR(x-1, y-1,deng) + avgR(x-1, y,deng) + avgR(x, y-1,deng) + avgR(x, y,deng))/24.0;
					int resR = (int)(A + B + C);
					
					A = avgG(x, y,deng)/3.0;
					B = (avgG(x-1, y,deng) + avgG(x, y,deng) + avgG(x, y-1,deng) + avgG(x, y,deng))/8.0;
					C = (avgG(x-1, y-1,deng) + avgG(x-1, y,deng) + avgG(x, y-1,deng) + avgG(x, y,deng))/24.0;
					int resG = (int)(A + B + C);
					
					A = avgB(x, y,deng)/3.0;
					B = (avgB(x-1, y,deng) + avgB(x, y,deng) + avgB(x, y-1,deng) + avgB(x, y,deng))/8.0;
					C = (avgB(x-1, y-1,deng) + avgB(x-1, y,deng) + avgB(x, y-1,deng) + avgB(x, y,deng))/24.0;
					int resB = (int)(A + B + C);
					
					BIR.setRGB(x/2, y/2, new Color(resR, resG, resB).getRGB());
				}else if(x == w-1){
					double A = avgR(x, y,deng)/3.0;
					double B = (avgR(x-1, y,deng) + avgR(x, y+1,deng) + avgR(x, y-1,deng) + avgR(x, y,deng))/8.0;
					double C = (avgR(x-1, y-1,deng) + avgR(x-1, y+1,deng) + avgR(x, y-1,deng) + avgR(x, y+1,deng))/24.0;
					int resR = (int)(A + B + C);
					
					A = avgG(x, y,deng)/3.0;
					B = (avgG(x-1, y,deng) + avgG(x, y+1,deng) + avgG(x, y-1,deng) + avgG(x, y,deng))/8.0;
					C = (avgG(x-1, y-1,deng) + avgG(x-1, y+1,deng) + avgG(x, y-1,deng) + avgG(x, y+1,deng))/24.0;
					int resG = (int)(A + B + C);
					
					A = avgB(x, y,deng)/3.0;
					B = (avgB(x-1, y,deng) + avgB(x, y+1,deng) + avgB(x, y-1,deng) + avgB(x, y,deng))/8.0;
					C = (avgB(x-1, y-1,deng) + avgB(x-1, y+1,deng) + avgB(x, y-1,deng) + avgB(x, y+1,deng))/24.0;
					int resB = (int)(A + B + C);
					
					BIR.setRGB(x/2, y/2, new Color(resR, resG, resB).getRGB());
					
				}else if(y == h-1){
					double A = avgR(x, y,deng)/3.0;
					double B = (avgR(x-1, y,deng) + avgR(x, y,deng) + avgR(x, y-1,deng) + avgR(x+1, y,deng))/8.0;
					double C = (avgR(x-1, y-1,deng) + avgR(x-1, y,deng) + avgR(x+1, y-1,deng) + avgR(x+1, y,deng))/24.0;
					int resR = (int)(A + B + C);
					
					A = avgG(x, y,deng)/3.0;
					B = (avgG(x-1, y,deng) + avgG(x, y,deng) + avgG(x, y-1,deng) + avgG(x+1, y,deng))/8.0;
					C = (avgG(x-1, y-1,deng) + avgG(x-1, y,deng) + avgG(x+1, y-1,deng) + avgG(x+1, y,deng))/24.0;
					int resG = (int)(A + B + C);
					
					A = avgB(x, y,deng)/3.0;
					B = (avgB(x-1, y,deng) + avgB(x, y,deng) + avgB(x, y-1,deng) + avgB(x+1, y,deng))/8.0;
					C = (avgB(x-1, y-1,deng) + avgB(x-1, y,deng) + avgB(x+1, y-1,deng) + avgB(x+1, y,deng))/24.0;
					int resB = (int)(A + B + C);
					
					BIR.setRGB(x/2, y/2, new Color(resR, resG, resB).getRGB());
					
				}else{
					double A = avgR(x, y,deng)/3.0;
					double B = (avgR(x-1, y,deng) + avgR(x+1, y,deng) + avgR(x, y-1,deng) + avgR(x, y+1,deng))/8.0;
					double C = (avgR(x-1, y-1,deng) + avgR(x-1, y+1,deng) + avgR(x+1, y-1,deng) + avgR(x+1, y+1,deng))/24.0;
					int resR = (int)(A + B + C);
					
					A = avgG(x, y,deng)/3.0;
					B = (avgG(x-1, y,deng) + avgG(x+1, y,deng) + avgG(x, y-1,deng) + avgG(x, y+1,deng))/8.0;
					C = (avgG(x-1, y-1,deng) + avgG(x-1, y+1,deng) + avgG(x+1, y-1,deng) + avgG(x+1, y+1,deng))/24.0;
					int resG = (int)(A + B + C);
					
					A = avgB(x, y,deng)/3.0;
					B = (avgB(x-1, y,deng) + avgB(x+1, y,deng) + avgB(x, y-1,deng) + avgB(x, y+1,deng))/8.0;
					C = (avgB(x-1, y-1,deng) + avgB(x-1, y+1,deng) + avgB(x+1, y-1,deng) + avgB(x+1, y+1,deng))/24.0;
					int resB = (int)(A + B + C);
					
					BIR.setRGB(x/2, y/2, new Color(resR, resG, resB).getRGB());
					
				}
			}
		}
		return BIR;
	}
	
	public static void evalGaussianExpand()
	{
		process = "GE";
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = new BufferedImage(w*2, h*2, BufferedImage.TYPE_INT_RGB);
		System.out.println("W: "+w);
		System.out.println("H: "+h);
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				//pass on the coords*2
				BIR.setRGB(x*2, y*2, BI.getRGB(x, y));
				//pass between the coords on x
				int resR = -1;
				int resG = -1;
				int resB = -1;
				if(x != w-1)
				{
					resR = (int)((avgR(x,y)+avgR(x+1,y))/2.0);
					resG = (int)((avgG(x,y)+avgG(x+1,y))/2.0);
					resB = (int)((avgB(x,y)+avgB(x+1,y))/2.0);
				}else{
					resR = (int)avgR(x,y);
					resG = (int)avgG(x,y);
					resB = (int)avgB(x,y);
				}
				BIR.setRGB((x*2)+1, y*2, new Color(resR, resG, resB).getRGB());
			}
			//pass between the coords on y
			if(y != h-1)
			{
				for(int x = 0; x < w; x++)
				{
					//regular
					int resR = (int)((avgR(x,y)+avgR(x,y+1))/2.0);
					int resG = (int)((avgG(x,y)+avgG(x,y+1))/2.0);
					int resB = (int)((avgB(x,y)+avgB(x,y+1))/2.0);
					BIR.setRGB((x*2), (y*2)+1, new Color(resR, resG, resB).getRGB());
					//Between x AND y
					if(x != w-1)
					{
						resR = (int)((avgR(x,y)+avgR(x,y+1)+avgR(x+1,y)+avgR(x+1,y+1))/4.0);
						resG = (int)((avgG(x,y)+avgG(x,y+1)+avgG(x+1,y)+avgG(x+1,y+1))/4.0);
						resB = (int)((avgB(x,y)+avgB(x,y+1)+avgB(x+1,y)+avgB(x+1,y+1))/4.0);
					}else{
						//really, they're already set... we don't gotta do anything...
						resR = (int)((avgR(x,y)+avgR(x,y+1))/2.0);
						resG = (int)((avgG(x,y)+avgG(x,y+1))/2.0);
						resB = (int)((avgB(x,y)+avgB(x,y+1))/2.0);
					}
					BIR.setRGB((x*2)+1, (y*2)+1, new Color(resR, resG, resB).getRGB());
				}
			}else{
				for(int x = 0; x < w; x++)
				{
					//regular
					int resR = (int)avgR(x,y);
					int resG = (int)avgG(x,y);
					int resB = (int)avgB(x,y);
					BIR.setRGB((x*2), (y*2)+1, new Color(resR, resG, resB).getRGB());
					//Between x AND y
					if(x != w-1)
					{
						resR = (int)((avgR(x,y)+avgR(x+1,y))/2.0);
						resG = (int)((avgG(x,y)+avgG(x+1,y))/2.0);
						resB = (int)((avgB(x,y)+avgB(x+1,y))/2.0);
					}else{
						//Again, they stay the same, so don't need to really do anything here
					}
					BIR.setRGB((x*2)+1, (y*2)+1, new Color(resR, resG, resB).getRGB());
				}
			}
		}
		JP.updateImg(BIR);
	}
	
	public static BufferedImage maxColorSpread(BufferedImage deng)
	{
		int w = deng.getWidth();
		int h = deng.getHeight();
		BufferedImage BIR  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		System.out.println("W: "+w);
		System.out.println("H: "+h);
		int[] maxC = {0, 0, 0};
		int[] minC = {255, 255, 255};
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				double[] c = avgCAr(x, y, deng);
				for(int i = 0; i < 3; i++)
				{
					if(c[i] > maxC[i])
						maxC[i] = (int) c[i];
					if(c[i] < minC[i])
						minC[i] = (int) c[i];
				}
			}
		}
		p("Max: ("+maxC[0]+","+maxC[1]+","+maxC[2]+"), Min: ("+minC[0]+","+minC[1]+","+minC[2]+")");
		
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				double[] c = avgCAr(x, y, deng);
				for(int i = 0; i < 3; i++)
					c[i] = (c[i]-minC[i]/((double)(maxC[i]-minC[i]))*255.0);
				Color Q = new Color((int)c[0], (int)c[1], (int)c[2]);
				BIR.setRGB(x, y, Q.getRGB());
			}
		}
		return BIR;
	}
	
	public static BufferedImage evalGaussianExpand(BufferedImage deng)
	{
		int w = deng.getWidth();
		int h = deng.getHeight();
		BufferedImage BIR  = new BufferedImage(w*2, h*2, BufferedImage.TYPE_INT_RGB);
		System.out.println("W: "+w);
		System.out.println("H: "+h);
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				//pass on the coords*2
				BIR.setRGB(x*2, y*2, deng.getRGB(x, y));
				//pass between the coords on x
				int resR = -1;
				int resG = -1;
				int resB = -1;
				if(x != w-1)
				{
					resR = (int)((avgR(x,y,deng)+avgR(x+1,y,deng))/2.0);
					resG = (int)((avgG(x,y,deng)+avgG(x+1,y,deng))/2.0);
					resB = (int)((avgB(x,y,deng)+avgB(x+1,y,deng))/2.0);
				}else{
					resR = (int)avgR(x,y,deng);
					resG = (int)avgG(x,y,deng);
					resB = (int)avgB(x,y,deng);
				}
				BIR.setRGB((x*2)+1, y*2, new Color(resR, resG, resB).getRGB());
			}
			//pass between the coords on y
			if(y != h-1)
			{
				for(int x = 0; x < w; x++)
				{
					//regular
					int resR = (int)((avgR(x,y,deng)+avgR(x,y+1,deng))/2.0);
					int resG = (int)((avgG(x,y,deng)+avgG(x,y+1,deng))/2.0);
					int resB = (int)((avgB(x,y,deng)+avgB(x,y+1,deng))/2.0);
					BIR.setRGB((x*2), (y*2)+1, new Color(resR, resG, resB).getRGB());
					//Between x AND y
					if(x != w-1)
					{
						resR = (int)((avgR(x,y)+avgR(x,y+1,deng)+avgR(x+1,y,deng)+avgR(x+1,y+1,deng))/4.0);
						resG = (int)((avgG(x,y)+avgG(x,y+1,deng)+avgG(x+1,y,deng)+avgG(x+1,y+1,deng))/4.0);
						resB = (int)((avgB(x,y)+avgB(x,y+1,deng)+avgB(x+1,y,deng)+avgB(x+1,y+1,deng))/4.0);
					}else{
						//really, they're already set... we don't gotta do anything...
						resR = (int)((avgR(x,y,deng)+avgR(x,y+1,deng))/2.0);
						resG = (int)((avgG(x,y,deng)+avgG(x,y+1,deng))/2.0);
						resB = (int)((avgB(x,y,deng)+avgB(x,y+1,deng))/2.0);
					}
					BIR.setRGB((x*2)+1, (y*2)+1, new Color(resR, resG, resB).getRGB());
				}
			}else{
				for(int x = 0; x < w; x++)
				{
					//regular
					int resR = (int)avgR(x,y,deng);
					int resG = (int)avgG(x,y,deng);
					int resB = (int)avgB(x,y,deng);
					BIR.setRGB((x*2), (y*2)+1, new Color(resR, resG, resB).getRGB());
					//Between x AND y
					if(x != w-1)
					{
						resR = (int)((avgR(x,y,deng)+avgR(x+1,y,deng))/2.0);
						resG = (int)((avgG(x,y,deng)+avgG(x+1,y,deng))/2.0);
						resB = (int)((avgB(x,y,deng)+avgB(x+1,y,deng))/2.0);
					}else{
						//Again, they stay the same, so don't need to really do anything here
					}
					BIR.setRGB((x*2)+1, (y*2)+1, new Color(resR, resG, resB).getRGB());
				}
			}
		}
		return BIR;
	}
	
	public static void setRGB(int[][][] a, int x, int y, int r, int g, int b)
	{
		a[x][y][0] = r;
		a[x][y][1] = g;
		a[x][y][2] = b;
	}
	
	public static int[][][] evalGaussianExpand(int[][][] a)
	{
		process = "GE";
		int w = a.length;
		int h = a[0].length;
		int[][][] BIR  = new int[w*2][h*2][3];
		System.out.println("W: "+w);
		System.out.println("H: "+h);
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				//pass on the coords*2
				setRGB(BIR, x*2, y*2, a[x][y][0], a[x][y][1], a[x][y][2]);
				//pass between the coords on x
				int resR = -1;
				int resG = -1;
				int resB = -1;
				if(x != w-1)
				{
					resR = (int)((avgR(a, x,y)+avgR(a, x+1,y))/2.0);
					resG = (int)((avgG(a, x,y)+avgG(a, x+1,y))/2.0);
					resB = (int)((avgB(a, x,y)+avgB(a, x+1,y))/2.0);
				}else{
					resR = (int)avgR(a, x,y);
					resG = (int)avgG(a, x,y);
					resB = (int)avgB(a, x,y);
				}
				setRGB(BIR, (x*2)+1, y*2, resR, resG, resB);
			}
			//pass between the coords on y
			if(y != h-1)
			{
				for(int x = 0; x < w; x++)
				{
					//regular
					int resR = (int)((avgR(a, x,y)+avgR(a, x,y+1))/2.0);
					int resG = (int)((avgG(a, x,y)+avgG(a, x,y+1))/2.0);
					int resB = (int)((avgB(a, x,y)+avgB(a, x,y+1))/2.0);
					setRGB(BIR, (x*2), (y*2)+1, resR, resG, resB);
					//Between x AND y
					if(x != w-1)
					{
						resR = (int)((avgR(a, x,y)+avgR(a, x,y+1)+avgR(a, x+1,y)+avgR(a, x+1,y+1))/4.0);
						resG = (int)((avgG(a, x,y)+avgG(a, x,y+1)+avgG(a, x+1,y)+avgG(a, x+1,y+1))/4.0);
						resB = (int)((avgB(a, x,y)+avgB(a, x,y+1)+avgB(a, x+1,y)+avgB(a, x+1,y+1))/4.0);
					}else{
						//really, they're already set... we don't gotta do anything...
						resR = (int)((avgR(a, x,y)+avgR(a, x,y+1))/2.0);
						resG = (int)((avgG(a, x,y)+avgG(a, x,y+1))/2.0);
						resB = (int)((avgB(a, x,y)+avgB(a, x,y+1))/2.0);
					}
					setRGB(BIR, (x*2)+1, (y*2)+1, resR, resG, resB);
				}
			}else{
				for(int x = 0; x < w; x++)
				{
					//regular
					int resR = (int)avgR(a, x,y);
					int resG = (int)avgG(a, x,y);
					int resB = (int)avgB(a, x,y);
					setRGB(BIR, (x*2), (y*2)+1, resR, resG, resB);
					//Between x AND y
					if(x != w-1)
					{
						resR = (int)((avgR(a, x,y)+avgR(a, x+1,y))/2.0);
						resG = (int)((avgG(a, x,y)+avgG(a, x+1,y))/2.0);
						resB = (int)((avgB(a, x,y)+avgB(a, x+1,y))/2.0);
					}else{
						//Again, they stay the same, so don't need to really do anything here
					}
					setRGB(BIR, (x*2)+1, (y*2)+1, resR, resG, resB);
				}
			}
		}
		return BIR;
	}
	
	public static int[][][] evalLaplacian(int level)
	{
		process = "lap";
		for(int c = 0; c < level-1; c++)
		{
			evalGaussianReduce();
			BI = JP.img;
			JF.pack();
		}
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIT = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		BIT = getCopy(BI);
		//p("BIT at copy: "+new Color(BIT.getRGB(346, 133)).getRed());
		BufferedImage BIR = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		evalGaussianReduce();
		process = "lap";
		BI = JP.img;
		JF.pack();
		//p("BIT after reduce: "+new Color(BIT.getRGB(346, 133)).getRed());
		//p("BI after reduce: "+new Color(BI.getRGB(346, 133)).getRed());
		evalGaussianExpand();
		process = "lap";
		BI = JP.img;
		JF.pack();
		
		int ret[][][] = new int[w][h][3];
		
		for(int x = 0; x < w && x < BI.getWidth(); x++)
		{
			for(int y = 0; y < h && y < BI.getHeight(); y++)
			{
				int a = (int)avgC(x,y,BIT);
				int b = (int)avgC(x,y);
				int aR = (int)avgR(x,y,BIT);
				int bR = (int)avgR(x,y);
				int aG = (int)avgG(x,y,BIT);
				int bG = (int)avgG(x,y);
				int aB = (int)avgB(x,y,BIT);
				int bB = (int)avgB(x,y);
				int res = 100 - (a - b);
				
				ret[x][y][0] = (aR-bR);
				ret[x][y][1] = (aG-bG);
				ret[x][y][2] = (aB-bB);
				
				//p("x: "+x+", y: "+y+", a: "+a+", b: "+b+", res: "+res);
				if(res < 0)
				{
					//p("Floored res: "+res);
					res = 0;
				}
				if(res > 255)
				{
					//p("Ceilinged res: "+res);
					res = 255;
				}
				BIR.setRGB(x, y, (new Color(res, res, res).getRGB()));
			}
		}
		//int a = new Color(BIT.getRGB(346, 133)).getRed();
		//int b = new Color(BI.getRGB(346, 133)).getRed();
		//p("Both finally: "+a+" - "+b+" = "+(a-b));
		JP.updateImg(BIR);
		return ret;
	}
	
	public static BufferedImage evalLaplacian(int level, BufferedImage deng)
	{
		for(int c = 0; c < level-1; c++)
		{
			deng = evalGaussianReduce(deng);
		}
		int w = deng.getWidth();
		int h = deng.getHeight();
		BufferedImage BIT = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		BIT = getCopy(deng);
		BufferedImage BIR = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		deng = evalGaussianReduce(deng);
		deng = evalGaussianExpand(deng);
		int ret[][][] = new int[w][h][3];
		
		for(int x = 0; x < w && x < deng.getWidth(); x++)
		{
			for(int y = 0; y < h && y <= deng.getHeight(); y++)
			{
				int a = (int)avgC(x,y,BIT);
				int b = (int)avgC(x,y,deng);
				int aR = (int)avgR(x,y,BIT);
				int bR = (int)avgR(x,y,deng);
				int aG = (int)avgG(x,y,BIT);
				int bG = (int)avgG(x,y,deng);
				int aB = (int)avgB(x,y,BIT);
				int bB = (int)avgB(x,y,deng);
				int res = 100 - (a - b);
				
				ret[x][y][0] = (aR-bR);
				ret[x][y][1] = (aG-bG);
				ret[x][y][2] = (aB-bB);
				
				//p("x: "+x+", y: "+y+", a: "+a+", b: "+b+", res: "+res);
				if(res < 0)
				{
					//p("Floored res: "+res);
					res = 0;
				}
				if(res > 255)
				{
					//p("Ceilinged res: "+res);
					res = 255;
				}
				BIR.setRGB(x, y, (new Color(res, res, res).getRGB()));
			}
		}
		//int a = new Color(BIT.getRGB(346, 133)).getRed();
		//int b = new Color(BI.getRGB(346, 133)).getRed();
		//p("Both finally: "+a+" - "+b+" = "+(a-b));
		return BIR;
	}
	
	public static void evalImgBlend(int level)
	{
		process = "blend1";
		BufferedImage aI = null;
		BufferedImage oI = null;
		BufferedImage dC = null;
		try {
			aI = ImageIO.read(new File("images\\A1.png"));
			oI = ImageIO.read(new File("images\\O1.png"));
			dC = ImageIO.read(new File("images\\DC.png"));
			ArrayList<int[][][]> aG = new ArrayList<int[][][]>();
			ArrayList<int[][][]> oG = new ArrayList<int[][][]>();
			ArrayList<int[][][]> combo = new ArrayList<int[][][]>();
			
			for(int i = 0; i < level; i++)
			{
				//find apple laplacian
				p("a"+i);
				JP.updateImg(aI);
				aG.add(evalLaplacian(i+1));
				JF.pack();
				
				//find orange laplacian
				p("o"+i);
				JP.updateImg(oI);
				oG.add(evalLaplacian(i+1));
				JF.pack();
				
				if(aG.get(i)[0].length != oG.get(i)[0].length)
					p("CAUTION: Differing Heights");
				if(aG.get(i).length != oG.get(i).length)
					p("CAUTION: Differing Widths");
				
				//find combo
				combo.add(new int[aG.get(i).length][aG.get(i)[0].length][3]);
				for(int y = 0; y < aG.get(i)[0].length; y++)
				{
					for(int x = 0; x < aG.get(i).length; x++)
					{
						double rat = -1.0;
						if(x < aG.get(i).length/2)
						{
							rat = Math.pow(2.0, (x-(aG.get(i).length/2.0)));
							for(int q = 0; q < 3; q++)
								combo.get(i)[x][y][q] = (int) ((aG.get(i)[x][y][q]+(oG.get(i)[x][y][q]*rat))/(rat+1.0));
						}else{
							rat = Math.pow(2.0, ((aG.get(i).length/2.0)-x));
							for(int q = 0; q < 3; q++)
								combo.get(i)[x][y][q] = (int) ((oG.get(i)[x][y][q]+(aG.get(i)[x][y][q]*rat))/(rat+1.0));
						}
						//if(y==0)
						//	p("x: "+x+", rat: "+rat);
					}
				}
			}
			
			//Finally, use the combo laplacian pyramid to rebuild a gaussian-reduced
			//directly-connected image into a believable merge:
			
			//First gaussian-reducing the DC image:
			JP.updateImg(dC);
			while(BI.getWidth() > combo.get(combo.size()-1).length && BI.getHeight() > combo.get(combo.size()-1)[0].length)
			{
				System.out.println("Reduced from "+BI.getWidth()+" x "+BI.getHeight()+", to ");
				evalGaussianReduce();
				JF.pack();
				p(BI.getWidth()+" x "+BI.getHeight());
			}
			p("Final Reduce");
			evalGaussianReduce();
			JF.pack();
			p("First Expand");
			evalGaussianExpand();
			JF.pack();
			
			//Then begin rebuilding it from the lowest combo up, expanding the result each time
			int[][][] sub = new int[1][1][1];
			int[][][] subSub = new int[1][1][1];
			for(int i = combo.size()-1; i > -1; i--)
			{
				p("Rebuild layer "+i);
				sub = new int[combo.get(i).length][combo.get(i)[0].length][3];
				int w = combo.get(i).length;
				if(BI.getWidth() != w || (subSub.length != w && i < combo.size()-1))
				{
					p("CAUTION: Differing Widths in recombination:");
					if(BI.getWidth() < w)
						w = BI.getWidth();
				}
				p("   comboW: "+combo.get(i).length+", BIw: "+BI.getWidth()+", subSubW: "+subSub.length);
				
				int h = combo.get(i)[0].length;
				if(BI.getHeight() != h || (subSub.length != h && i < combo.size()-1))
				{
					p("CAUTION: Differing Heights in recombination:");
					if(BI.getHeight() < h)
						h = BI.getHeight();
				}
				p("   comboH: "+combo.get(i)[0].length+", BIh: "+BI.getHeight()+", subSubH: "+subSub[0].length);
				
				BufferedImage ret = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				for(int x = 0; x < w; x++)
				{
					System.out.print(x+",");
					for(int y = 0; y < h; y++)
					{
						int r = -255;
						int g = -255;
						int b = -255;
						if(i == combo.size()-1)
						{
							r = new Color(BI.getRGB(x, y)).getRed();
							g = new Color(BI.getRGB(x, y)).getGreen();
							b = new Color(BI.getRGB(x, y)).getBlue();
						}else{
							r = subSub[x][y][0];
							g = subSub[x][y][1];
							b = subSub[x][y][2];
						}
						int[] temp = combo.get(i)[x][y];
						
						int resR = r + temp[0];

						int resG = g + temp[1];
						
						int resB = b + temp[2];
						
						if((resR < 0) || (resR > 255) || (resG < 0) || (resG > 255) || (resB < 0) || (resB > 255))
						{
							p("");
							p("("+x+", "+y+") - "+r+"+"+temp[0]+"="+resR+", "+g+"+"+temp[1]+"="+resG+", "+b+"+"+temp[2]+"="+resB);
						}
						if(resR < 0)
							resR = 0;
						if(resR > 255)
							resR = 255;
						if(resG < 0)
							resG = 0;
						if(resG > 255)
							resG = 255;
						if(resB < 0)
							resB = 0;
						if(resB > 255)
							resB = 255;
						
						Color t = new Color(resR, resG, resB);
						ret.setRGB(x, y, t.getRGB());
						setRGB(sub, x, y, resR, resG, resB);
					}
				}
				p("");
				JP.updateImg(ret);
				JF.pack();
				if(i != 0)
				{
					subSub = evalGaussianExpand(sub).clone();
					evalGaussianExpand();
					JF.pack();
				}
			}
			//JP.updateImg(combo[0]);
			//JP.updateImg(oG[1]);
			p("Final dims: "+BI.getWidth()+" x "+BI.getHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void evalPsychoBlend(int level)
	{
		process = "blend1";
		BufferedImage aI = null;
		BufferedImage oI = null;
		BufferedImage dC = null;
		try {
			aI = ImageIO.read(new File("images\\A1.png"));
			oI = ImageIO.read(new File("images\\O1.png"));
			dC = ImageIO.read(new File("images\\DC.png"));
			ArrayList<int[][][]> aG = new ArrayList<int[][][]>();
			ArrayList<int[][][]> oG = new ArrayList<int[][][]>();
			ArrayList<int[][][]> combo = new ArrayList<int[][][]>();
			
			for(int i = 0; i < level; i++)
			{
				//find apple laplacian
				p("a"+i);
				JP.updateImg(aI);
				aG.add(evalLaplacian(i+1));
				JF.pack();
				
				//find orange laplacian
				p("o"+i);
				JP.updateImg(oI);
				oG.add(evalLaplacian(i+1));
				JF.pack();
				
				if(aG.get(i)[0].length != oG.get(i)[0].length)
					p("CAUTION: Differing Heights");
				if(aG.get(i).length != oG.get(i).length)
					p("CAUTION: Differing Widths");
				
				//find combo
				combo.add(new int[aG.get(i).length][aG.get(i)[0].length][3]);
				for(int y = 0; y < aG.get(i)[0].length; y++)
				{
					for(int x = 0; x < aG.get(i).length; x++)
					{
						double rat = -1.0;
						if(x < aG.get(i).length/2)
						{
							rat = Math.pow(2.0, (x-(aG.get(i).length/2.0)));
							for(int q = 0; q < 3; q++)
								combo.get(i)[x][y][q] = (int) ((aG.get(i)[x][y][q]+(oG.get(i)[x][y][q]*rat))/(rat+1.0));
						}else{
							rat = Math.pow(2.0, ((aG.get(i).length/2.0)-x));
							for(int q = 0; q < 3; q++)
								combo.get(i)[x][y][q] = (int) ((oG.get(i)[x][y][q]+(aG.get(i)[x][y][q]*rat))/(rat+1.0));
						}
						//if(y==0)
						//	p("x: "+x+", rat: "+rat);
					}
				}
			}
			
			//Finally, use the combo laplacian pyramid to rebuild a gaussian-reduced
			//directly-connected image into a believable merge:
			
			//First gaussian-reducing the DC image:
			JP.updateImg(dC);
			while(BI.getWidth() > combo.get(combo.size()-1).length && BI.getHeight() > combo.get(combo.size()-1)[0].length)
			{
				System.out.println("Reduced from "+BI.getWidth()+" x "+BI.getHeight()+", to ");
				evalGaussianReduce();
				JF.pack();
				p(BI.getWidth()+" x "+BI.getHeight());
			}
			p("Final Reduce");
			evalGaussianReduce();
			JF.pack();
			p("First Expand");
			evalGaussianExpand();
			JF.pack();
			
			//Then begin rebuilding it from the lowest combo up, expanding the result each time
			int[][][] sub = new int[1][1][1];
			int[][][] subSub = new int[1][1][1];
			for(int i = combo.size()-1; i > -1; i--)
			{
				p("Rebuild layer "+i);
				sub = new int[combo.get(i).length][combo.get(i)[0].length][3];
				int w = combo.get(i).length;
				if(BI.getWidth() != w || (subSub.length != w && i < combo.size()-1))
				{
					p("CAUTION: Differing Widths in recombination:");
					if(BI.getWidth() < w)
						w = BI.getWidth();
				}
				p("   comboW: "+combo.get(i).length+", BIw: "+BI.getWidth()+", subSubW: "+subSub.length);
				
				int h = combo.get(i)[0].length;
				if(BI.getHeight() != h || (subSub.length != h && i < combo.size()-1))
				{
					p("CAUTION: Differing Heights in recombination:");
					if(BI.getHeight() < h)
						h = BI.getHeight();
				}
				p("   comboH: "+combo.get(i)[0].length+", BIh: "+BI.getHeight()+", subSubH: "+subSub[0].length);
				
				BufferedImage ret = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				for(int x = 0; x < w; x++)
				{
					System.out.print(x+",");
					for(int y = 0; y < h; y++)
					{
						int r = -255;
						int g = -255;
						int b = -255;
						if(i == combo.size()-1)
						{
							r = new Color(BI.getRGB(x, y)).getRed();
							g = new Color(BI.getRGB(x, y)).getGreen();
							b = new Color(BI.getRGB(x, y)).getBlue();
						}else{
							r = subSub[x][y][0];
							g = subSub[x][y][1];
							b = subSub[x][y][2];
						}
						int[] temp = combo.get(i)[x][y];
						
						int resR = r + temp[0];

						int resG = g + temp[1];
						
						int resB = b + temp[2];
						
						if((resR < 0) || (resR > 255) || (resG < 0) || (resG > 255) || (resB < 0) || (resB > 255))
						{
							p("");
							p("("+x+", "+y+") - "+r+"+"+temp[0]+"="+resR+", "+g+"+"+temp[1]+"="+resG+", "+b+"+"+temp[2]+"="+resB);
						}
						if(resR < 0)
							resR = 0;
						if(resR > 255)
							resR = 255;
						if(resG < 0)
							resG = 0;
						if(resG > 255)
							resG = 255;
						if(resB < 0)
							resB = 0;
						if(resB > 255)
							resB = 255;
						
						Color t = new Color(resR, resG, resB);
						ret.setRGB(x, y, t.getRGB());
						setRGB(sub, x, y, resR, resG, resB);
					}
				}
				p("");
				JP.updateImg(ret);
				JF.pack();
				if(i != 0)
				{
					subSub = evalGaussianExpand(sub).clone();
					evalGaussianExpand();
					JF.pack();
				}
			}
			//JP.updateImg(combo[0]);
			//JP.updateImg(oG[1]);
			p("Final dims: "+BI.getWidth()+" x "+BI.getHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void evalImgBlendOrig(int level)
	{
		process = "blend1";
		BufferedImage aI = null;
		BufferedImage oI = null;
		BufferedImage dC = null;
		try {
			aI = ImageIO.read(new File("images\\A1.png"));
			oI = ImageIO.read(new File("images\\O1.png"));
			dC = ImageIO.read(new File("images\\DC.png"));
			ArrayList<int[][][]> aG = new ArrayList<int[][][]>();
			ArrayList<int[][][]> oG = new ArrayList<int[][][]>();
			ArrayList<int[][][]> combo = new ArrayList<int[][][]>();
			
			for(int i = 0; i < level; i++)
			{
				//find apple laplacian
				p("a"+i);
				JP.updateImg(aI);
				aG.add(evalLaplacian(i+1));
				JF.pack();
				
				//find orange laplacian
				p("o"+i);
				JP.updateImg(oI);
				oG.add(evalLaplacian(i+1));
				JF.pack();
				
				if(aG.get(i)[0].length != oG.get(i)[0].length)
					p("CAUTION: Differing Heights");
				if(aG.get(i).length != oG.get(i).length)
					p("CAUTION: Differing Widths");
				
				//find combo
				combo.add(new int[aG.get(i).length][aG.get(i)[0].length][3]);
				for(int y = 0; y < aG.get(i)[0].length; y++)
				{
					for(int x = 0; x < aG.get(i).length; x++)
					{
						double rat = -1.0;
						if(x < aG.get(i).length/2)
						{
							rat = Math.pow(2.0, (x-(aG.get(i).length/2.0)));
							for(int q = 0; q < 3; q++)
								combo.get(i)[x][y][q] = (int) ((aG.get(i)[x][y][q]+(oG.get(i)[x][y][q]*rat))/(rat+1.0));
						}else{
							rat = Math.pow(2.0, ((aG.get(i).length/2.0)-x));
							for(int q = 0; q < 3; q++)
								combo.get(i)[x][y][q] = (int) ((oG.get(i)[x][y][q]+(aG.get(i)[x][y][q]*rat))/(rat+1.0));
						}
					}
				}
			}
			
			//Finally, use the combo laplacian pyramid to rebuild a gaussian-reduced
			//directly-connected image into a believable merge:
			
			//First gaussian-reducing the DC image:
			JP.updateImg(dC);
			while(BI.getWidth() > combo.get(combo.size()-1).length && BI.getHeight() > combo.get(combo.size()-1)[0].length)
			{
				p("Reduced from "+BI.getWidth()+" x "+BI.getHeight()+", to...");
				evalGaussianReduce();
				JF.pack();
				p("..."+BI.getWidth()+" x "+BI.getHeight());
			}
			p("Final Reduce");
			evalGaussianReduce();
			JF.pack();
			p("First Expand");
			evalGaussianExpand();
			JF.pack();
			
			//Then begin rebuilding it from the lowest combo up, expanding the result each time
			int[][][] sub = new int[1][1][1];
			int[][][] subSub = new int[1][1][1];
			for(int i = combo.size()-1; i > -1; i--)
			{
				p("Rebuild layer "+i);
				sub = new int[combo.get(i).length][combo.get(i)[0].length][3];
				int w = combo.get(i).length;
				if(BI.getWidth() != w || (subSub.length != w && i < combo.size()-1))
				{
					p("CAUTION: Differing Widths in recombination:");
					if(BI.getWidth() < w)
						w = BI.getWidth();
				}
				p("   comboW: "+combo.get(i).length+", BIw: "+BI.getWidth()+", subSubW: "+subSub.length);
				
				int h = combo.get(i)[0].length;
				if(BI.getHeight() != h || (subSub.length != h && i < combo.size()-1))
				{
					p("CAUTION: Differing Heights in recombination:");
					if(BI.getHeight() < h)
						h = BI.getHeight();
				}
				p("   comboH: "+combo.get(i)[0].length+", BIh: "+BI.getHeight()+", subSubH: "+subSub[0].length);
				
				BufferedImage ret = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				for(int x = 0; x < w; x++)
				{
					System.out.print(x+",");
					for(int y = 0; y < h; y++)
					{
						int r = -255;
						int g = -255;
						int b = -255;
						r = new Color(BI.getRGB(x, y)).getRed();
						g = new Color(BI.getRGB(x, y)).getGreen();
						b = new Color(BI.getRGB(x, y)).getBlue();
						int[] temp = combo.get(i)[x][y];
						
						int resR = r + temp[0];

						int resG = g + temp[1];
						
						int resB = b + temp[2];
						
						if((resR < 0) || (resR > 255) || (resG < 0) || (resG > 255) || (resB < 0) || (resB > 255))
						{
							//p("");
							//p("("+x+", "+y+") - "+r+"+"+temp[0]+"="+resR+", "+g+"+"+temp[1]+"="+resG+", "+b+"+"+temp[2]+"="+resB);
						}
						if(resR < 0)
							resR = 0;
						if(resR > 255)
							resR = 255;
						if(resG < 0)
							resG = 0;
						if(resG > 255)
							resG = 255;
						if(resB < 0)
							resB = 0;
						if(resB > 255)
							resB = 255;
						
						Color t = new Color(resR, resG, resB);
						ret.setRGB(x, y, t.getRGB());
						setRGB(sub, x, y, resR, resG, resB);
					}
				}
				p("");
				JP.updateImg(ret);
				JF.pack();
				if(i != 0)
				{
					subSub = evalGaussianExpand(sub).clone();
					evalGaussianExpand();
					JF.pack();
				}
			}
			//JP.updateImg(combo[0]);
			//JP.updateImg(oG[1]);
			p("Final dims: "+BI.getWidth()+" x "+BI.getHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static double avgC(int x, int y)
	{
		Color t = new Color(BI.getRGB(x, y));
		return (t.getRed()+t.getGreen()+t.getBlue())/3.0;
	}
	
	public static double[] avgCAr(int x, int y)
	{
		Color t = new Color(BI.getRGB(x, y));
		double[] ret = new double[3];
		ret[0] = t.getRed();
		ret[1] = t.getGreen();
		ret[2] = t.getBlue();
		return ret;
	}
	
	public static double[] avgCAr(int x, int y, BufferedImage i)
	{
		Color t = new Color(i.getRGB(x, y));
		double[] ret = new double[3];
		ret[0] = t.getRed();
		ret[1] = t.getGreen();
		ret[2] = t.getBlue();
		return ret;
	}
	
	public static double avgC(int x, int y, BufferedImage i)
	{
		Color t = new Color(i.getRGB(x, y));
		return (t.getRed()+t.getGreen()+t.getBlue())/3.0;
	}
	
	public static double avgR(int x, int y)
	{
		return (new Color(BI.getRGB(x, y))).getRed();
	}
	
	public static double avgR(int x, int y, BufferedImage i)
	{
		return (new Color(i.getRGB(x, y))).getRed();
	}
	
	public static double avgR(int[][][] a, int x, int y)
	{
		return a[x][y][0];
	}
	
	public static double avgB(int x, int y)
	{
		return (new Color(BI.getRGB(x, y))).getBlue();
	}
	
	public static double avgB(int x, int y, BufferedImage i)
	{
		return (new Color(i.getRGB(x, y))).getBlue();
	}
	
	public static double avgB(int[][][] a, int x, int y)
	{
		return a[x][y][1];
	}
	
	public static double avgG(int x, int y)
	{
		return (new Color(BI.getRGB(x, y))).getGreen();
	}
	
	public static double avgG(int x, int y, BufferedImage i)
	{
		return (new Color(i.getRGB(x, y))).getGreen();
	}
	
	public static double avgG(int[][][] a, int x, int y)
	{
		return a[x][y][2];
	}
	
	public static void evalBinary()
	{
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				Color c = new Color(BI.getRGB(x, y));
				//if(c.getRed() <= 250 && c.getGreen() <= 250 && c.getBlue() <= 250)
				if(c.getRed() <= 210 && c.getGreen() <= 210 && c.getBlue() <= 210)
				{
					//System.out.println("("+x+", "+y+") not white, but infact: "+c.getRed()+", "+c.getGreen()+", "+c.getBlue());
					BIR.setRGB(x, y, Color.BLACK.getRGB());
				}else{
					BIR.setRGB(x, y, Color.WHITE.getRGB());
				}
			}
		}
		JP.updateImg(BIR);
	}
	
	public static void evalNegative()
	{
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				Color c = new Color(BI.getRGB(x, y));
				int rR = 255-c.getRed();
				int gR = 255-c.getGreen();
				int bR = 255-c.getBlue();
				c = new Color(rR, gR, bR);
				BIR.setRGB(x, y, c.getRGB());
			}
		}
		JP.updateImg(BIR);
	}
	
	public static void evalSeg()
	{
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				Color c = new Color(BI.getRGB(x, y));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				Color cR = Color.pink;
				if(r >= 160 && g <= 100 && b >= 50 && b <= 170)
					cR = Color.RED;
				else if(r >= 50 && r <= 130 && g >= 100 && b <= 20)
					cR = Color.GREEN;
				else if(r <= 30 && g >= 50 && g <= 160 && b >= 100)
					cR = Color.BLUE;
				else if(r >= 200 && g >= 90 && b <= 10)
					cR = Color.YELLOW;
				BIR.setRGB(x, y, cR.getRGB());
			}
		}
		JP.updateImg(BIR);
	}
	
	public static void evalSegHSV()
	{
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				Color c = new Color(BI.getRGB(x, y));
				float [] vals = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
				float hue = vals[0];
				float s = vals[1];
				Color cR = Color.pink;
				if(s >= 0.05)
				{
					if(hue >= 0.8 && hue <= 1.0)
						cR = Color.RED;
					else if(hue >= 0.2 && hue <= 0.4)
						cR = Color.GREEN;
					else if(hue >= 0.5 && hue <= 0.7)
						cR = Color.BLUE;
					else if(hue >= 0.05 && hue <= 0.2)
						cR = Color.YELLOW;
				}
				BIR.setRGB(x, y, cR.getRGB());
			}
		}
		JP.updateImg(BIR);
	}
	
	public static void evalSegBG()
	{
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				Color c = new Color(BI.getRGB(x, y));
				int r = c.getRed();
				Color cR = c;
				if(r >= 180)
					cR = Color.PINK;
				BIR.setRGB(x, y, cR.getRGB());
			}
		}
		JP.updateImg(BIR);
	}
	
	public static void evalSegBGandObj()
	{
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = BI;
		makeGreyScale();
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				Color c = new Color(BI.getRGB(x, y));
				int r = c.getRed();
				Color cR = c;
				if(r >= 180)
				{
					cR = Color.PINK;
				}else if(r >= 130){
					//System.out.print("Y");
					cR = Color.yellow;
				}else{
					//System.out.println(r+"");
				}
				BIR.setRGB(x, y, cR.getRGB());
			}
		}
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				Color c = new Color(BIR.getRGB(x, y));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				Color cR = Color.BLACK;
				if(!c.equals(Color.pink))
				{
					if(r >= 160 && g <= 100 && b >= 50 && b <= 170)
						cR = Color.RED;
					else if(r >= 50 && r <= 130 && g >= 100 && b <= 20)
						cR = Color.GREEN;
					else if(r <= 30 && g >= 50 && g <= 160 && b >= 100)
						cR = Color.BLUE;
					else if(r >= 200 && g >= 90 && b <= 10)
						cR = Color.YELLOW;
					BIR.setRGB(x, y, cR.getRGB());
				}
			}
		}
		JP.updateImg(BIR);
	}
	
	public static BufferedImage makeGreyScale()
	{
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				Color c = new Color(BI.getRGB(x, y));
				int avg = ((c.getRed() + c.getGreen() + c.getBlue())/3);
				c = new Color(avg, avg, avg);
				BIR.setRGB(x, y, c.getRGB());
			}
		}
		JP.updateImg(BIR);
		return BIR;
	}
	
	public static BufferedImage makeGreyScale(BufferedImage deng)
	{
		int w = deng.getWidth();
		int h = deng.getHeight();
		BufferedImage BIR  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				Color c = new Color(deng.getRGB(x, y));
				int avg = ((c.getRed() + c.getGreen() + c.getBlue())/3);
				c = new Color(avg, avg, avg);
				BIR.setRGB(x, y, c.getRGB());
			}
		}
		return BIR;
	}
	
	public static void evalRoberts()
	{
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				if(y == h-1 || x == w-1)
					BIR.setRGB(x, y, Color.BLACK.getRGB());
				else
				{
					
					//Horizontal Evaluation:
					int totH = 0;
					for(int yC = 0; yC < 2 && (yC+y) < h; yC++)
					{
						for(int xC = 0; xC < 2 && (xC+x) < w; xC++)
						{
							
							Color cT = new Color(BI.getRGB(xC+x, yC+y));
							int r = 0;
							if(xC == 0 && yC == 0)
								r = 1;
							else if(xC == 1 && yC == 1)
								r = -1;
							totH = totH + (cT.getRed()*r);
						}
					}
					
					//Vertical Evaluation:
					int totV = 0;
					for(int yC = 0; yC < 2  && (yC+y) < h; yC++)
					{
						for(int xC = 0; xC < 2 && (xC+x) < w; xC++)
						{
							//System.out.println("x: "+(xC+x)+", y: "+(yC+y)+", w: "+w+", h: "+h);
							Color cT = new Color(BI.getRGB(xC+x, yC+y));
							int r = 0;
							if(xC == 1 && yC == 0)
								r = 1;
							else if(xC == 0 && yC == 1)
								r = -1;
							totV = totV + (cT.getRed()*r);
						}
					}
					
					//Combination:
					int tot = (int)(Math.sqrt(((totH*totH)+(totV*totV))*1.0));
					if(tot > 10)
						BIR.setRGB(x, y, Color.WHITE.getRGB());
					else
						BIR.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
		}
		
		JP.updateImg(BIR);
	}
	
	public static void evalSobel()
	{
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				if(y == 0 || x == 0 || y > (3*(h/3)) || x > (3*(w/3)))
					BIR.setRGB(x, y, Color.BLACK.getRGB());
				else
				{
					
					//Horizontal Evaluation:
					int totH = 0;
					for(int yC = -1; yC < 2 && (yC+y) < h; yC++)
					{
						for(int xC = -1; xC < 2 && (xC+x) < w; xC++)
						{
							
							Color cT = new Color(BI.getRGB(xC+x, yC+y));
							int r = xC;
							if(yC == 0)
								r = r*2;
							totH = totH + (cT.getRed()*r);
						}
					}
					
					//Vertical Evaluation:
					int totV = 0;
					for(int yC = -1; yC < 2  && (yC+y) < h; yC++)
					{
						for(int xC = -1; xC < 2 && (xC+x) < w; xC++)
						{
							//System.out.println("x: "+(xC+x)+", y: "+(yC+y)+", w: "+w+", h: "+h);
							Color cT = new Color(BI.getRGB(xC+x, yC+y));
							int r = yC;
							if(xC == 0)
								r = r*2;
							totV = totV + (cT.getRed()*r);
						}
					}
					
					//Combination:
					int tot = (int)(Math.sqrt(((totH*totH)+(totV*totV))*1.0));
					if(tot > 50)
						BIR.setRGB(x, y, Color.WHITE.getRGB());
					else
						BIR.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
		}
		
		JP.updateImg(BIR);
	}
	
	public static void evalPrewitt()
	{
		int w = BI.getWidth();
		int h = BI.getHeight();
		BufferedImage BIR  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				if(y == 0 || x == 0 || y > (3*(h/3)) || x > (3*(w/3)))
					BIR.setRGB(x, y, Color.BLACK.getRGB());
				else
				{
					
					//Horizontal Evaluation:
					int totH = 0;
					for(int yC = -1; yC < 2 && (yC+y) < h; yC++)
					{
						for(int xC = -1; xC < 2 && (xC+x) < w; xC++)
						{
							
							Color cT = new Color(BI.getRGB(xC+x, yC+y));
							int r = xC;
							totH = totH + (cT.getRed()*r);
						}
					}
					
					//Vertical Evaluation:
					int totV = 0;
					for(int yC = -1; yC < 2  && (yC+y) < h; yC++)
					{
						for(int xC = -1; xC < 2 && (xC+x) < w; xC++)
						{
							//System.out.println("x: "+(xC+x)+", y: "+(yC+y)+", w: "+w+", h: "+h);
							Color cT = new Color(BI.getRGB(xC+x, yC+y));
							int r = yC;
							totV = totV + (cT.getRed()*r);
						}
					}
					
					//Combination:
					int tot = (int)(Math.sqrt(((totH*totH)+(totV*totV))*1.0));
					if(tot > 30)
						BIR.setRGB(x, y, Color.WHITE.getRGB());
					else
						BIR.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
		}
		
		JP.updateImg(BIR);
	}
	
	public static void evalZoomShrink()
	{
		int Factor = -2;
		double toFrac = (Factor*1.0);
		if(Factor < 0)
			toFrac = (-1.0/Factor);
		if(toFrac<1)
			Shrink(toFrac);
		else
			Zoom(toFrac);
	}
	
	public static void Shrink(double F)
	{
		System.out.println("Shrinking");
		int inc = (int)(1/F);
		System.out.println("Increment: "+inc);
		int w = BI.getWidth();
		int h = BI.getHeight();
		System.out.println("W,H: "+w+", "+h);
		int wR = (int)(w*F);
		int wMax = w-(w%inc);
		int hR = (int)(h*F);
		int hMax = h-(h%inc);
		System.out.println("WR,HR: "+wR+", "+hR);
		System.out.println("WMax,HMax: "+wMax+", "+hMax);
		BufferedImage BIR = new BufferedImage(wR, hR, BufferedImage.TYPE_INT_RGB);
		int tWR = 0;
		for(int tW=0; tW<wMax; tW=tW+inc)
		{
			int tHR = 0;
			for(int tH=0; tH<hMax; tH=tH+inc)
			{
				BIR.setRGB(tWR, tHR, BI.getRGB(tW, tH));
				tHR++;
			}
			tWR++;
		}
		JP.updateImg(BIR);
	}
	
	public static void Zoom(double F)
	{
		//Does not actually require F to be a whole number
		System.out.println("Zooming");
		int w = BI.getWidth();
		int h = BI.getHeight();
		System.out.println("W,H: "+w+", "+h);
		int wR = (int)(w*F);
		int hR = (int)(h*F);
		System.out.println("WR,HR: "+wR+", "+hR);
		BufferedImage BIR = new BufferedImage(wR, hR, BufferedImage.TYPE_INT_RGB);
		for(int tWR=0; tWR<wR; tWR++)
		{
			int tW = (int)(tWR/F);
			for(int tHR=0; tHR<hR; tHR++)
			{
				int tH = (int)(tHR/F);
				if(tW < w && tH < h)
				{
					BIR.setRGB(tWR, tHR, BI.getRGB((int)(tWR/F), (int)(tHR/F)));
				}else{
					BIR.setRGB(tWR, tHR, Color.black.getRGB());
					System.out.println("nope");
				}
			}
		}
		JP.updateImg(BIR);
	}
}
