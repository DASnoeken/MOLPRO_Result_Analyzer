import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class pesder{			//Derivative calculator
	public pesder(int n){
		NOF=n;
		xp=new double[NOF];
		yp=new double[NOF];
	}

	public void Derive(){
		for(int i=0;i<NOF-1;i++){
			xp[i]=geoms[i][0];
			yp[i]=(potMap.get(sortedKeys.get(i))-potMap.get(sortedKeys.get(i+1)))/(geoms[i][0]-geoms[i+1][0]);		//finite difference approximation
		}
	}

	//modifiers
	public void setDergeoms(double[][] g){
		geoms=g;
	}
	public void setDersortedkeys(ArrayList<String> ALS){
		sortedKeys=ALS;
	}
	public void setDerpotmap(HashMap<String,Double> HMSD){
		potMap=HMSD;
	}
	//accessors
	public double[] getXp(){
		return xp;
	}
	public double[] getYp(){
		return yp;
	}
	public double getXp(int i){
		return xp[i];
	}
	public double getYp(int i){
		return yp[i];
	}
	
	//Returning minimum and maximum
	public double minXp(){
		min = xp[0];
		for(int i=1;i<xp.length;i++){
			if(xp[i]<min){
				min=xp[i];
			}
		}
		return min;
	}
	public double maxXp() {
		max = xp[0];
		for(int i=1;i<xp.length;i++){
			if(xp[i]>max){
				max=xp[i];
			}
		}
		return max;
	}
	public double minYp(){
		min = yp[0];
		for(int i=1; i<yp.length;i++){
			if(yp[i]<min){
				min=yp[i];
			}
		}
		return min;
	}
	public double maxYp(){
		max = yp[0];
		for(int i=1; i<yp.length;i++){
			if(yp[i]>max){
				max=yp[i];
			}
		}
		return max;
	}
	private double[] xp;
	private double[] yp;
	private int NOF;
	private ArrayList<String> dersortedKeys;
	private ArrayList<String> sortedKeys;
	private HashMap<String,Double> derpotMap;
	private HashMap<String,Double> potMap;
	private double[][] dergeoms;
	private double[][] geoms;
	private double min,max;
}
