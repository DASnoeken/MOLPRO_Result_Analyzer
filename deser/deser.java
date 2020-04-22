import java.io.*;
import java.util.*;

public class deser implements Serializable{
	public static void main(String[] args){
		ArrayList<String> allKeys = null;
		HashMap<String,Double> potMap = null;
		double[][] geoms = null;
		FileInputStream fisKeys = null;
		ObjectInputStream oisKeys = null;
		FileInputStream fisMap = null;
		ObjectInputStream oisMap = null;
		ObjectInputStream oisGeom = null;
		FileInputStream fisGeom = null;
		try{															//Deserialization happens in this big try block
			fisKeys = new FileInputStream("addressKey.ser");
			oisKeys = new ObjectInputStream(fisKeys);
			fisMap = new FileInputStream("addressPotMap.ser");
			oisMap = new ObjectInputStream(fisMap);
			fisGeom = new FileInputStream("addressGeoms.ser");
			oisGeom = new ObjectInputStream(fisGeom);
			try{
				allKeys = (ArrayList<String>) oisKeys.readObject();		//Reading all the objects
				potMap = (HashMap<String,Double>) oisMap.readObject();
				geoms = (double[][]) oisGeom.readObject();
			}catch(ClassNotFoundException cnfe){
				cnfe.printStackTrace();
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}finally{
			if(fisKeys!=null){
				try{fisKeys.close();}catch(IOException ioe){ioe.printStackTrace();}
			}
			if(oisKeys!=null){
				try{oisKeys.close();}catch(IOException ioe){ioe.printStackTrace();}
			}
			if(fisMap!=null){
				try{fisMap.close();}catch(IOException ioe){ioe.printStackTrace();}
			}
			if(oisMap!=null){
				try{oisMap.close();}catch(IOException ioe){ioe.printStackTrace();}
			}
			if(fisGeom!=null){
				try{fisGeom.close();}catch(IOException ioe){ioe.printStackTrace();}
			}
			if(oisGeom!=null){
				try{oisGeom.close();}catch(IOException ioe){ioe.printStackTrace();}
			}
		}
		pesGUI pes = new pesGUI(allKeys,potMap,geoms);										//Make instance of the gui class
		pes.setAllKeys(allKeys);
		pes.setGeoms(geoms);
		pes.setPotMap(potMap);
		pes.drawPES();
	}
}
