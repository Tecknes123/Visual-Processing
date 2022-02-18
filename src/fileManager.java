import java.io.*;
import java.util.ArrayList;

public class fileManager
{
	public static void readFrom(String fileName, Network n)
	{
        // This will reference one line at a time
        String line = null;
        ArrayList<String> text = new ArrayList<String>();
        try{
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null)
            {
            	text.add(line);
                //System.out.println(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }catch(FileNotFoundException ex){
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }catch(IOException ex){
            System.out.println("Error reading file '" + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        int layer = 0;
        for(int c = 0; c < text.size(); c++)
        {
        	for(int c2 = 0; c2 < text.get(c).length(); c2++)
        	{
        		char Q = text.get(c).charAt(c2);
        		
        		if(Q == 'T')
        		{
        			String sub = "";
        			c2++;
        			while(Q != ';')
    	    		{
        				Q = text.get(c).charAt(c2);
        				sub = sub + Q;
        				c2++;
        				Q = text.get(c).charAt(c2);
    	    		}
        			if(!n.init)
        				n.l = new layer[Integer.parseInt(sub)];
        			System.out.println("numLayers: "+n.l.length);
        		}
	    		
        		else if(Q == 'L')
        		{
        			String sub = "";
        			c2++;
        			while(Q != ';')
    	    		{
        				Q = text.get(c).charAt(c2);
        				sub = sub + Q;
        				c2++;
        				Q = text.get(c).charAt(c2);
    	    		}
        			layer = Integer.parseInt(sub);
        			if(!n.init)
        				n.l[layer] = new layer();
        		}
        		
        		else if(Q == 'W')
        		{
        			c2++;
        			c2++;
        			String sub = "";
        			while(Q != '*')
    	    		{
        				Q = text.get(c).charAt(c2);
        				sub = sub + Q;
        				c2++;
        				Q = text.get(c).charAt(c2);
    	    		}
        			/*if(!n.init)
        				n.l[layer].n = new double[Integer.parseInt(sub)][];*/
        			c2++;
        			int wNum = 0;
        			while(Q != ')')
        			{
        				sub = "";
        				while(Q != ':')
        	    		{
            				Q = text.get(c).charAt(c2);
            				sub = sub + Q;
            				c2++;
            				Q = text.get(c).charAt(c2);
        	    		}
        				if(!n.init)
        					n.l[layer].n.add(new Neuron());
            			c2++;
            			int arrCount = 0;
	        			while(Q != '|')
	        			{
	        				sub = "";
	            			while(Q != ',')
	        	    		{
	            				Q = text.get(c).charAt(c2);
	            				sub = sub + Q;
	            				c2++;
	            				Q = text.get(c).charAt(c2);
	        	    		}
	            			if(arrCount == 37)
	            				System.out.println("ohgoodness - l: "+layer+", w: "+wNum);
	            			n.l[layer].n.get(wNum).w.set(arrCount, Double.parseDouble(sub));
	            			arrCount++;
	            			c2++;
	            			Q = text.get(c).charAt(c2);
	        			}
	        			wNum++;
	        			c2++;
	        			Q = text.get(c).charAt(c2);
	        			//System.out.println("Q: "+Q);
        			}
        			c2++;
        			n.l[layer].printWeights(layer);
        		}
        		
        		else if(Q == 'V')
        		{
        			c2++;
        			c2++;
        			String sub = "";
        			while(Q != ':')
    	    		{
        				Q = text.get(c).charAt(c2);
        				sub = sub + Q;
        				c2++;
        				Q = text.get(c).charAt(c2);
    	    		}
        			System.out.println("Layer: "+layer+", Sub: "+sub);
        			//if(!n.init)
        				//n.l[layer].l = new double[Integer.parseInt(sub)];
        			c2++;
        			int arrCount = 0;
        			while(Q != ')')
        			{
        				sub = "";
            			while(Q != ',')
        	    		{
            				Q = text.get(c).charAt(c2);
            				sub = sub + Q;
            				c2++;
            				Q = text.get(c).charAt(c2);
        	    		}
            			n.l[layer].n.get(arrCount).val = Double.parseDouble(sub);
            			arrCount++;
            			c2++;
            			Q = text.get(c).charAt(c2);
        			}
        			n.l[layer].printValues(layer);
        		}
        	}
        }
        if(!n.init)
        	n.reverseInit();
    }
	
	public static void readFrom(String fileName, styleNetwork n)
	{
        // This will reference one line at a time
        String line = null;
        ArrayList<String> text = new ArrayList<String>();
        try{
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null)
            {
            	text.add(line);
                //System.out.println(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }catch(FileNotFoundException ex){
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }catch(IOException ex){
            System.out.println("Error reading file '" + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        int layer = 0;
        for(int c = 0; c < text.size(); c++)
        {
        	for(int c2 = 0; c2 < text.get(c).length(); c2++)
        	{
        		char Q = text.get(c).charAt(c2);
        		
        		if(Q == 'T')
        		{
        			String sub = "";
        			c2++;
        			while(Q != ';')
    	    		{
        				Q = text.get(c).charAt(c2);
        				sub = sub + Q;
        				c2++;
        				Q = text.get(c).charAt(c2);
    	    		}
        			if(!n.init)
        				n.l = new layer[Integer.parseInt(sub)];
        			System.out.println("numLayers: "+n.l.length);
        		}
	    		
        		else if(Q == 'L')
        		{
        			String sub = "";
        			c2++;
        			while(Q != ';')
    	    		{
        				Q = text.get(c).charAt(c2);
        				sub = sub + Q;
        				c2++;
        				Q = text.get(c).charAt(c2);
    	    		}
        			layer = Integer.parseInt(sub);
        			if(!n.init)
        				n.l[layer] = new layer();
        		}
        		
        		else if(Q == 'W')
        		{
        			c2++;
        			c2++;
        			String sub = "";
        			while(Q != '*')
    	    		{
        				Q = text.get(c).charAt(c2);
        				sub = sub + Q;
        				c2++;
        				Q = text.get(c).charAt(c2);
    	    		}
        			/*if(!n.init)
        				n.l[layer].n = new double[Integer.parseInt(sub)][];*/
        			c2++;
        			int wNum = 0;
        			while(Q != ')')
        			{
        				sub = "";
        				while(Q != ':')
        	    		{
            				Q = text.get(c).charAt(c2);
            				sub = sub + Q;
            				c2++;
            				Q = text.get(c).charAt(c2);
        	    		}
        				if(!n.init)
        					n.l[layer].n.add(new Neuron());
            			c2++;
            			int arrCount = 0;
	        			while(Q != '|')
	        			{
	        				sub = "";
	            			while(Q != ',')
	        	    		{
	            				Q = text.get(c).charAt(c2);
	            				sub = sub + Q;
	            				c2++;
	            				Q = text.get(c).charAt(c2);
	        	    		}
	            			if(arrCount == 37)
	            				System.out.println("ohgoodness - l: "+layer+", w: "+wNum);
	            			n.l[layer].n.get(wNum).w.set(arrCount, Double.parseDouble(sub));
	            			arrCount++;
	            			c2++;
	            			Q = text.get(c).charAt(c2);
	        			}
	        			wNum++;
	        			c2++;
	        			Q = text.get(c).charAt(c2);
	        			//System.out.println("Q: "+Q);
        			}
        			c2++;
        			n.l[layer].printWeights(layer);
        		}
        		
        		else if(Q == 'V')
        		{
        			c2++;
        			c2++;
        			String sub = "";
        			while(Q != ':')
    	    		{
        				Q = text.get(c).charAt(c2);
        				sub = sub + Q;
        				c2++;
        				Q = text.get(c).charAt(c2);
    	    		}
        			System.out.println("Layer: "+layer+", Sub: "+sub);
        			//if(!n.init)
        				//n.l[layer].l = new double[Integer.parseInt(sub)];
        			c2++;
        			int arrCount = 0;
        			while(Q != ')')
        			{
        				sub = "";
            			while(Q != ',')
        	    		{
            				Q = text.get(c).charAt(c2);
            				sub = sub + Q;
            				c2++;
            				Q = text.get(c).charAt(c2);
        	    		}
            			n.l[layer].n.get(arrCount).val = Double.parseDouble(sub);
            			arrCount++;
            			c2++;
            			Q = text.get(c).charAt(c2);
        			}
        			n.l[layer].printValues(layer);
        		}
        	}
        }
        if(!n.init)
        	n.reverseInit();
    }
	
	public static void writeTo(String fileName, Network n)
	{
		try {
            // Assume default encoding.
            FileWriter fileWriter =
                new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bW =
                new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            System.out.println(n.l.length);
            bW.write("T"+n.l.length+";");
            bW.newLine();
            for(int c = 0; c < n.l.length; c++)
            {
            	bW.write("L"+c+";");
            	bW.newLine();
            	bW.write("V("+n.l[c].n.size()+":");
            	for(int c2 = 0; c2 < n.l[c].n.size(); c2++)
            	{
            		bW.write(n.l[c].n.get(c2).val+",");
            	}
            	bW.write(")");
            	bW.newLine();
            	if(c != n.l.length-1)
            	{
	            	bW.write("W("+n.l[c].n.size()+"*");
	            	for(int c2 = 0; c2 < n.l[c].n.size(); c2++)
	            	{
	            		bW.write(n.l[c].n.get(c2).w.size()+":");
	            		for(int c3 = 0; c3 < n.l[c].n.get(c2).w.size(); c3++)
	            		{
	            			bW.write(n.l[c].n.get(c2).w.get(c3) + ",");
	            		}
	            		bW.write("|");
	            	}
	            	bW.write(")");
	            	bW.newLine();
            	}
            }
            
            // Always close files.
            bW.close();
        }catch(IOException ex){
            System.out.println("Error writing to file '" + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
	}
	
	public static void writeTo(String fileName, styleNetwork n)
	{
		try {
            // Assume default encoding.
            FileWriter fileWriter =
                new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bW =
                new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            System.out.println(n.l.length);
            bW.write("T"+n.l.length+";");
            bW.newLine();
            for(int c = 0; c < n.l.length; c++)
            {
            	bW.write("L"+c+";");
            	bW.newLine();
            	bW.write("V("+n.l[c].n.size()+":");
            	for(int c2 = 0; c2 < n.l[c].n.size(); c2++)
            	{
            		bW.write(n.l[c].n.get(c2).val+",");
            	}
            	bW.write(")");
            	bW.newLine();
            	if(c != n.l.length-1)
            	{
	            	bW.write("W("+n.l[c].n.size()+"*");
	            	for(int c2 = 0; c2 < n.l[c].n.size(); c2++)
	            	{
	            		bW.write(n.l[c].n.get(c2).w.size()+":");
	            		for(int c3 = 0; c3 < n.l[c].n.get(c2).w.size(); c3++)
	            		{
	            			bW.write(n.l[c].n.get(c2).w.get(c3) + ",");
	            		}
	            		bW.write("|");
	            	}
	            	bW.write(")");
	            	bW.newLine();
            	}
            }
            
            // Always close files.
            bW.close();
        }catch(IOException ex){
            System.out.println("Error writing to file '" + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
	}
}
