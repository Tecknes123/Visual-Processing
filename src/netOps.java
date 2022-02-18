import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class netOps
{
	public static int depth = 3;
	
	public static void gen(Scanner in)
	{
		System.out.println("Which image would you like to use as a source?");
		String fN = in.nextLine();
		System.out.println("What frameSize would you like?");
		int fS = Integer.parseInt(in.nextLine());
		System.out.println("How many training cycles would you like?");
		int tC = Integer.parseInt(in.nextLine());
		styleNetwork sNet = new styleNetwork(depth, fS, tC, fN, "");
		BufferedImage imgT = null;
		try {
			imgT = ImageIO.read(new File("images\\"+fN));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse filename");
		}
		potImg pi = sNet.baseScan(imgT);
		pi.printSize();
		MainyMain.JP.updateImg(pi.toBI());
		MainyMain.JF.pack();
	}
	
	public static void weights(Scanner in)
	{
		System.out.println("Which image would you like to use as a source?");
		String fN = in.nextLine();
		System.out.println("What frameSize would you like?");
		int fS = Integer.parseInt(in.nextLine());
		System.out.println("Which weight file would you like to use?");
		String wF = in.nextLine();
		styleNetwork sNet = new styleNetwork(depth, fS, fN, wF, "");
		System.out.println("Which image would you like to use as a target?");
		String input = in.nextLine();
		BufferedImage imgT = null;
		try {
			imgT = ImageIO.read(new File("images\\"+input));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse filename");
		}
		potImg pi = sNet.scan(imgT);
		pi.printSize();
		MainyMain.JP.updateImg(pi.toBI());
		MainyMain.JF.pack();
	}
	
	public static void weightedBase(Scanner in)
	{
		System.out.println("Which image would you like to use as a source?");
		String fN = in.nextLine();
		System.out.println("What frameSize would you like?");
		int fS = Integer.parseInt(in.nextLine());
		System.out.println("Which weight file would you like to use?");
		String wF = in.nextLine();
		styleNetwork sNet = new styleNetwork(depth, fS, fN, wF, "");
		System.out.println("Which image would you like to use as a target?");
		String input = in.nextLine();
		BufferedImage imgT = null;
		try {
			imgT = ImageIO.read(new File("images\\"+input));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse filename");
		}
		potImg pi = sNet.scan(imgT);
		pi.printSize();
		MainyMain.JP.updateImg(pi.toBI());
		MainyMain.JF.pack();
	}
	
	public static void multiScale(Scanner in)
	{
		System.out.println("How many layers?");
		int d = Integer.parseInt(in.nextLine());
		System.out.println("Which image would you like to use as a source?");
		String fN = in.nextLine();
		System.out.println("What frameSize would you like?");
		int fS = Integer.parseInt(in.nextLine());
		System.out.println("Which weight file would you like to use?");
		String wF = in.nextLine();
		styleNetwork sNet1 = new styleNetwork(depth, fS, fN, wF, "");
		System.out.println("Which image would you like to use as a target?");
		String input = in.nextLine();
		BufferedImage imgT = null;
		try {
			imgT = ImageIO.read(new File("images\\"+input));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse filename");
		}
		potImg pI1 = sNet1.scan(imgT);
		System.out.println("");
		for(int i = 2; i < d+1; i++)
		{
			pI1.flattenPix();
			MainyMain.JP.updateImg(pI1.toBI());
			MainyMain.JF.pack();
			System.out.println("frameSize "+i+"?");
			int fS2 = Integer.parseInt(in.nextLine());
			System.out.println("weight file "+i+"?");
			String wF2 = in.nextLine();
			styleNetwork sNet2 = new styleNetwork(depth, fS2, fN, wF2, "");
			//if(i == d)
			//	sNet2.compBaseScan(imgT, pI1, fS);
			//else
				sNet2.compScan(imgT, pI1, fS);
			System.out.println("");
		}
		pI1.flattenPix();
		MainyMain.JP.updateImg(pI1.toBI());
		MainyMain.JF.pack();
	}
	
	public static void retrain(Scanner in)
	{
		System.out.println("Which image would you like to use as a source?");
		String fN = in.nextLine();
		System.out.println("What frameSize would you like?");
		int fS = Integer.parseInt(in.nextLine());
		System.out.println("How many training cycles would you like?");
		int tC = Integer.parseInt(in.nextLine());
		System.out.println("Which weight file would you like to use?");
		String wF = in.nextLine();
		styleNetwork sNet = new styleNetwork(depth, fS, tC, fN, wF, "L");
		System.out.println("Which image would you like to use as a target?");
		String input = in.nextLine();
		BufferedImage imgT = null;
		try {
			imgT = ImageIO.read(new File("images\\"+input));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse filename");
		}
		potImg pi = sNet.scan(imgT);
		pi.printSize();
		MainyMain.JP.updateImg(pi.toBI());
		MainyMain.JF.pack();
	}

	public static void lap(Scanner in)
	{
		// TODO Auto-generated method stub
		System.out.println("Which image would you like to use as a source?");
		String fN = in.nextLine();
		System.out.println("What frameSize would you like?");
		int fS = Integer.parseInt(in.nextLine());
		System.out.println("Which weight file would you like to use?");
		String wF = in.nextLine();
		styleNetwork sNet = new styleNetwork(depth, fS, fN, wF, "L");
		System.out.println("Which image would you like to use as a target?");
		String input = in.nextLine();
		BufferedImage imgT = null;
		try {
			imgT = ImageIO.read(new File("images\\"+input));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to parse filename");
		}
		potImg pi = sNet.scan(imgT);
		pi.printSize();
		MainyMain.JP.updateImg(pi.toBI());
		MainyMain.JF.pack();
	}
}
