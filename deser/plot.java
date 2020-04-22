import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.geom.*;

public class plot extends JPanel{										//This is where we actually paint the potential
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int height = getHeight();
		AffineTransform tform = AffineTransform.getTranslateInstance(0,height);		//Translates origin
		double yTrans = ((double)height)/2.0;
		AffineTransform tform2 = AffineTransform.getTranslateInstance(0.0,yTrans);	//Translates origin
		tform.scale(1,-1);
		Graphics2D g2 = (Graphics2D) g;	
		g2.setTransform(tform);							//Gives more logical output. Java plots everything upside down normally (Why would anyone want to draw upside down? It makes no sense)
		g2.transform(tform2);
		g2.setStroke(new BasicStroke(2.0f));
		Line2D lin = null;
		nlines=X.length;
		for(int i=0;i<nlines-1;i++){
			lin = new Line2D.Double(X[i],Y[i],X[i+1],Y[i+1]);			//Drawing lines between points is basically the same thing as plotting data. So here it is.
			g2.draw(lin);
		}
	}
	
	//Modifier functions
	public void setX(double[] d){
		X = d;
	}
	public void setY(double[] d){
		Y = d;
	}

	private int nlines;
	private double[] X;
	private double[] Y;
}
