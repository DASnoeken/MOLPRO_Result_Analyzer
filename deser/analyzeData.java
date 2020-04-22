import java.io.*;
import java.util.*;

public class analyzeData{
	public analyzeData(double[][] g, ArrayList<String> ALS, HashMap<String,Double> HMSD){
		setGeoms(g);
		setSortedkeys(ALS);
		setPotmap(HMSD);
	}
	public Double getRCTP(){		//This function gets the classical turning point (CTP) via an "educated guess", either using the last R point or the actual root of the curve.
		Double x=0.0;
		Double y=0.0;
		Double a = 0.0;
		Double b = 0.0;
		int index = 0;
		for(int i=0;i<sortedKeys.size();i++){
			y=potMap.get(sortedKeys.get(i));			//Get all the energies and R points
			x=geoms[i][0];
			if(y<0){									//This should find the root
				index = i;
				break;
			}
		}
		if(index>0){		//Use this if we actually found the root
			a = (y-potMap.get(sortedKeys.get(index-1)))/(geoms[index-1][0]-x);		//y=ax+b solution to a and b for 2 known points
			b = (x*potMap.get(sortedKeys.get(index-1))-geoms[index-1][0]*y)/(geoms[index-1][0]-x);
		}else if(index==0){				//In case the whole potential was negative and we have no root. Probably because you forgot to do some BSSE correction at the ab-initio level.
			Double ctpE = potMap.get(sortedKeys.get(sortedKeys.size()-1));		//Correction term.
			System.out.println("Correction added to potential: "+Double.toString(ctpE)+" hartree.");
			Double[] yp = new Double[sortedKeys.size()];
			for(int i=0;i<sortedKeys.size();i++){
				yp[i] = potMap.get(sortedKeys.get(i))+abs(ctpE);		//Add the correction to the potential
			}
			for(int i=0;i<sortedKeys.size();i++){
				x=geoms[i][0];											//Find the new root
				if(yp[i]<0){
					index = i;
					break;
				}
			}
			if(index==0){
				System.out.println("Error! No root found for potential. Either fully repulsive or you made a mistake.");
				return 0.0/0.0;		//Returns NaN.
			}
			y=yp[index];
			a = (y-(potMap.get(sortedKeys.get(index-1))+abs(ctpE)))/(x-geoms[index-1][0]);
			b = (x*(potMap.get(sortedKeys.get(index-1))+abs(ctpE))-geoms[index-1][0]*y)/(x-geoms[index-1][0]);
		}
		//solve linear equation y=ax+b
		Double ans = -b/a;	//solution to x for y=0
		return ans;
	}
	public Double getRe(){							//Get approximate equilibrium distance
		Double[] yp = new Double[sortedKeys.size()];
		for(int i=0;i<sortedKeys.size();i++){
			yp[i] = potMap.get(sortedKeys.get(i));
		}
		Double min = yp[0];
		for(int i=1; i<yp.length;i++){
			if(yp[i]<min){
				min=yp[i];
				minG = new Double(geoms[i][0]);
			}
		}
		return minG;
	}
	public Double getDe(){							//Get approximate well depth
		Double[] yp = new Double[sortedKeys.size()];
		for(int i=0;i<sortedKeys.size();i++){
			yp[i] = potMap.get(sortedKeys.get(i));
		}
		Double min = yp[0];
		for(int i=1; i<yp.length;i++){
			if(yp[i]<min){
				min=yp[i];
			}
		}
		return min;
	}
	public Double abs(Double d){	//Absolute value
		if(d<0){
			return -d;
		}else{
			return d;
		}
	}
	//modifiers
	public void setGeoms(double[][] g){
		geoms=g;
	}
	public void setSortedkeys(ArrayList<String> ALS){
		sortedKeys=ALS;
	}
	public void setPotmap(HashMap<String,Double> HMSD){
		potMap=HMSD;
	}
	//variables
	private Double RCTP;
	private Double De;
	private Double Re;
	private Double minG;
	private HashMap<String,Double> potMap;
	private ArrayList<String> sortedKeys;
	private double[][] geoms;
}
