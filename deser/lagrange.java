import java.io.*;
import java.util.*;

//This class uses lagrange interpolation to do a harmonic approximation of the R-dependent potential.
public class lagrange{
	public lagrange(double[][] g, ArrayList<String> ALS, HashMap<String,Double> HMSD){
		setGeoms(g);
		setSortedkeys(ALS);
		setPotmap(HMSD);
	}
	
	public void setMinimum(){
		//need 3 points for interpolation with quadratic polynomial, where a<b<c and f(a)>f(b)<f(c).
		x = new double[sortedKeys.size()];
		y = new double[sortedKeys.size()];
		double[] yp = new double[sortedKeys.size()];
		double[] xp = new double[sortedKeys.size()];
		//fill up these arrays first
		for(int k=0;k<sortedKeys.size();k++){
			x[k] = geoms[k][0];
			y[k] = potMap.get(sortedKeys.get(k));
			yp[k] = y[k];
			xp[k] = x[k];
		}
		//sort them low to high
		double min = y[0];
		for(int i=0;i<sortedKeys.size()-1;i++){
			for(int j=i;j<sortedKeys.size();j++){
				if(y[j]<y[i]){
					double tmp = y[i];
					y[i] = y[j];
					y[j] = tmp;
					double tmpx = x[i];
					x[i] = x[j];
					x[j] = tmpx;
				}
			}
		}
		//take the lowest three points according to the rule above
		double fb = y[0];
		double b = x[0];
		//find a,c and fa,fc
		int indexA=0;
		int indexB=0;
		int indexC=0;
		for(int i=0;i<sortedKeys.size();i++){
			if(geoms[i][0]==b){
				indexB=i;
				indexA=i-1;
				indexC=i+1;
			}
		}
		double fa = yp[indexA];		//f(a)
		double a = xp[indexA];
		double fc = yp[indexC];		//f(c)
		double c = xp[indexC];
		int interpolationLength = 1000;		//Number of points on the interpolation grid
		double[] xpp = new double[interpolationLength];
		double[] q = new double[interpolationLength];
		xpp[0] = a;
		//System.out.println(xpp[0]);
		double delta = ((c-a+1)/interpolationLength);
		for(int i=1;i<interpolationLength;i++){
			xpp[i]=xpp[i-1]+delta;
			//System.out.println(xpp[i]);
		}
		//System.out.println("\n");
		
		for(int i=0;i<xpp.length;i++){
			q[i] = fa*((xpp[i]-b)*(xpp[i]-c)/((a-b)*(a-c)))+fb*((xpp[i]-a)*(xpp[i]-c)/((b-c)*(b-a)))+fc*((xpp[i]-a)*(xpp[i]-b)/((c-a)*(c-b)));	//This is the polynomial accoring to Lagrange. Fully tested in scilab.
			//System.out.println(q[i]);
		}
		//minX = b+(1/2)*((fa-fb)*(c-b)*(c-b)-(fc-fb)*(b-a)*(b-a))/((fa-fb)*(c-b)+(fc-fb)*(b-a)));
		//Now we find the minimum of q, which can be shown by algebra by solving q'=0
		double[] qp  = new double[interpolationLength-1];
		for(int i=0;i<interpolationLength-1;i++){
			qp[i] = abs(fa*(((xpp[i]-b)+(xpp[i]-c))/((a-b)*(a-c)))+fb*(((xpp[i]-a)+(xpp[i]-c))/((b-c)*(b-a)))+fc*(((xpp[i]-a)+(xpp[i]-b))/((c-a)*(c-b))));		//derivative of q and made it into absolute value
			//System.out.println(qp[i]);
		}		
		
		//find minimum of q
		int indY = 0;
		for(int i=1; i<qp.length;i++){
			if(qp[i]<qp[indY]){
				indY=i;
			}
		}
		minY=q[indY];
		minX=xpp[indY];
	}
	
	public double abs(double d){	//Absolute value
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
	
	//Accessors
	public double getMinX(){
		return minX;
	}
	public double getMinY(){
		return minY;
	}
	
	//variables
	private double minX;
	private double minY;
	private HashMap<String,Double> potMap;
	private ArrayList<String> sortedKeys;
	private double[][] geoms;
	private double[] x;
	private double[] y;
}
