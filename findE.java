import java.io.*;
import java.util.*;

public class findE implements Serializable{
	//public stuff
	public findE(String jobn,int i,int j){								//ctor
		fileNum=-1;
		setJobname(jobn);
		state = i;
		col=j;
		File file = null;
		FileReader freader = null;
		BufferedReader reader = null;
		try{
			String line;
			file = new File("ls.ls");									//The ls.ls file lists all the files in the potential. I want to read this first
			freader = new FileReader(file);
			reader = new BufferedReader(freader);
			fnAL = new ArrayList<String>();								//Arraylist of filenames to keep track of where we are in the potential
			allKeys = new ArrayList<String>();							//Arraylist of keys relating to the index of the potential.
			potMap = new HashMap<String,Double>();
			NOF=0;
			while((line=reader.readLine())!=null){
				fnAL.add(line);											//Check all the files we need to read
				genKeys(line);
				NOF++;													//count number of files
			}
			findPESDim(allKeys.get(0));									//Finding the dimensionality of the PES
			geoms = new double[NOF][dim];								//Geometries
		}catch(FileNotFoundException fnfe){
			System.out.println("The file ls.ls was not found.");
			System.out.println("Please run the command: \"ls *.out > ls.ls\" in bash first.");
			fnfe.printStackTrace();
		}catch(IOException ioe){
			System.out.println("Caught IO exception. Something went wrong reading ls.ls.");
			ioe.printStackTrace();
		}finally{														//Closing some files here
			if(reader!=null){
				try{
					reader.close();
				}catch(IOException ioe){
					System.out.println("Could not close reader.");
					ioe.printStackTrace();
				}
			}
			if(freader!=null){
				try{
					freader.close();
				}catch(IOException ioe){
					System.out.println("Could not close freader.");
					ioe.printStackTrace();
				}
			}
		}
	}																	//End of ctor
	
	public String newFile(){											//Get the next MOLPRO outputfile
		fileNum++;
		if(fileNum<fnAL.size()){										//Check to see if we are at the end of the arraylist
			return fnAL.get(fileNum);
		}else{
			return null;
		}
	}
	
	public void read() throws mpErrException{							//Reading the MOLPRO outputfile
		File mpFile=null;												//mpFile = MolPro File
		FileReader mpfReader=null;
		BufferedReader reader=null;
		energies = new String[NOF];
		try{
			String line;
			mpfName = newFile();										//Define mpfName first to make sure is not null
			while(fileNum<NOF && mpfName!=null){						//mpfName will return null if the fileNum variable exceeds the size of fnAL arraylist (see: newFile method)
				mpFile=new File(mpfName);
				mpfReader=new FileReader(mpFile);
				reader=new BufferedReader(mpfReader);
				while((line=reader.readLine())!=null){						//Read until EOF
					if(line.contains("TABLE") && !line.contains("text")){	//Read until we find the table
						for(int q=1;q<=4;q++){line=reader.readLine();}		//We always need to skip 4 lines to get to the actual table. This is just built into MOLPRO itself
						String[] Es = line.split(" ");						//We turn the line into an array of strings.
						int j=0;
						outputs = new String[Es.length];
						for(int i=0;i<Es.length;i++){					//Need to do this trick, because the standard MOLPRO output uses a lot of empty spaces in the tables. 
							if(Es[i].length()>0){						//This way we avoid the empty spaces, i.e. the length needs to be greater than 0.
								outputs[j] = Es[i];
								j++;
							}
						}
						geometry(outputs);
						Energy = outputs[col-1];						//Set the energy for this file
						energies[fileNum]=Energy;
					}else if(line.contains("ERROR")||line.contains("error")||line.contains("Error")){
						throw new mpErrException("WARNING! Errors found in MOLPRO outputfile!");
					}
				}
				mpfName = newFile();									//mpfName = MolPro File Name
			}
		}catch(FileNotFoundException fnfe){
			System.out.println("File "+fnAL.get(fileNum)+" not found!");
			fnfe.printStackTrace();
		}catch(IOException ioe){
			System.out.println("Something went wrong reading file: "+fnAL.get(fileNum));
			ioe.printStackTrace();
		}finally{
			if(reader!=null){
				try{reader.close();}catch(IOException ioe){ioe.printStackTrace();}
			}
			if(mpfReader!=null){
				try{mpfReader.close();}catch(IOException ioe){ioe.printStackTrace();}
			}
		}
	}
	
	public void write(){												//Function writes to scilab compatible dump file. This way the data can be plotted and used there as well. Also serializes some stuff
		System.out.println("Found "+Integer.toString(dim)+"D potential energy surface.");
		Writer writer = null;											//initialize writers and streams
		FileOutputStream fileOut = null;
		ObjectOutputStream outs = null;
		FileOutputStream fileOutKey = null;
		ObjectOutputStream outsKey = null;
		FileOutputStream fileOutGeoms = null;
		ObjectOutputStream outsGeoms = null;
		try{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("out.sce"),"utf-8"));
			for(int i=0;i<NOF;i++){
				char[] localKey = allKeys.get(i).toCharArray();			//Char[] would be easier to handle here than a string, because I want to loop over all the characters
				writer.write("E(");
				for(int j=0;j<localKey.length;j++){
					int lK = (int)localKey[j];							//lK is the ascii converted version for a char from localKey, makes it easier to compare in my opinion
					if(lK>=48 && lK<=57){								//ASCII values for numbers 0-9
						writer.write(Integer.toString(lK-48));			//lK-48 leaves the numerical value from the ascii value
					}else if(lK==45){									//ASCII value for '-' character, this would be encountered in higher dimensional PESs
						writer.write(",");
					}
				}
				writer.write(") = "+energies[i]+";\n");					//Write all the energies and finish off with a semicolon
				double dEnergy = Double.parseDouble(energies[i]);
				String key = allKeys.get(i);
				potMap.put(key,dEnergy);								//Here I save the potential map
			}
			writer.write("\n");
			for(int d=0;d<dim;d++){
				for(int i=0;i<NOF;i++){
					char[] localKey = allKeys.get(i).toCharArray();
					writer.write(Character.toString(dimLabels[d])+"(");
					for(int j=0;j<localKey.length;j++){
						int lK = (int)localKey[j];
						if(lK==45){
							break;
						}else if(localKey[j]==dimLabels[d]){
							continue;
						}else if(lK>=48 && lK<=57){
							writer.write(Integer.toString(lK-48));
						}
					}
					writer.write(") = "+Double.toString(geoms[i][d])+";\n");
				}
			}
			try{														//Serialization happens in this block
				fileOut = new FileOutputStream("./deser/addressPotMap.ser");
				outs = new ObjectOutputStream(fileOut);
				outs.writeObject(potMap);								//Serialize potMap
				fileOutKey = new FileOutputStream("./deser/addressKey.ser");
				outsKey = new ObjectOutputStream(fileOutKey);
				outsKey.writeObject(allKeys);							//Serialize allKeys
				fileOutGeoms = new FileOutputStream("./deser/addressGeoms.ser");
				outsGeoms = new ObjectOutputStream(fileOutGeoms);
				outsGeoms.writeObject(geoms);							//Serialize geoms
			}catch(IOException ioe){
				ioe.printStackTrace();
			}finally{
				if(outs!=null){											//Closing streams
					outs.close();
				}
				if(fileOut!=null){
					fileOut.close();
				}
				if(fileOutKey!=null){
					fileOutKey.close();
				}
				if(outsKey!=null){
					outsKey.close();
				}
				if(fileOutGeoms!=null){
					fileOutGeoms.close();
				}
				if(outsGeoms!=null){
					outsGeoms.close();
				}
				System.out.println("Serialized map stored in: ./deser/addressPotMap.ser");
				System.out.println("Serialized keys stored in: ./deser/addressKey.ser");
				System.out.println("Serialized geometries stored in: ./deser/addressGeoms.ser");
			}
		}catch(IOException ioe){
			System.out.println("IOException");
			ioe.printStackTrace();
		}finally{
			if(writer!=null){try{writer.close();}catch(IOException ioe){ioe.printStackTrace();}}		//Closing writer
		}
	}
	
	public void genKeys(String str){									//Function that generates the keys for the hashmap and puts them in an arraylist
		int startIndex = jobNameLength+1;
		int endIndex = str.indexOf(".");
		String key = str.substring(startIndex,endIndex);
		allKeys.add(key);
	}
	
	public void findPESDim(String k){									//Function gets the dimensionality of the PES
		char[] localKey = k.toCharArray();
		int d=1;
		for(int i=0;i<localKey.length;i++){
			int lK=(int)localKey[i];
			if(lK==45){
				d++;
			}
		}
		setDim(d);
		dimLabels = new char[d];
		for(int j=0;j<localKey.length;j++){
			int lK=(int)localKey[j];
			if((lK>=65 && lK<=90) || (lK>=97 && lK<=122)){
				for(int l=0;l<dim;l++){
					dimLabels[l]=localKey[j];
				}
			}
		}
	}
	
	public void geometry(String[] str){									//Gets all the geometric parameters for a certain file
		for(int i=0;i<dim;i++){
			geoms[fileNum][i]=Double.parseDouble(str[i]);
		}
	}
	
	//Accessor methods
	public int getState(){
		return state;
	}
	public int getCol(){
		return col;
	}
	public double getE(String s){
		return potMap.get(s);
	}
	//Modifier methods
	public void setState(int i){
		state=i;
	}
	public void setCol(int i){
		col=i;
	}
	public void setJobname(String jn){
		jobname=jn;
		jobNameLength = jobname.length();
	}
	public void setDim(int d){
		dim=d;
	}
	
	//privatize everything
	private int state;													//State selector
	private int col;													//Collumn selector
	private String jobname;												//Name of molpro job
	private int jobNameLength;											//length of jobname String
	private ArrayList<String> fnAL;										//Arraylist of filename strings
	private int fileNum;												//number corresponding to index in fnAL
	private int dim;													//Dimensionality of the PES
	private String[] outputs;											//Array that contains the contents of one output row
	private int NOF;													//total number of files
	private String Energy;												//String that holds the value for Energy
	private String mpfName;												//mpfName = MolPro File Name
	private String[] energies;											//String array of energies
	private ArrayList<String> allKeys;									//List of keys for the map
	private HashMap<String,Double> potMap;								//the map for the potential, I used HashMap because it turned out easier than regular Map.
	private double[][] geoms;											//2D String array to store all the geometries
	private char[] dimLabels;											//Labels for all the dimensions
}
