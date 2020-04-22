import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class derListener implements ActionListener{						//Listens to the derivative button, Also, very similar to pesGUI.java but for the derivative instead
	
	public derListener(ArrayList<String> ALS,HashMap<String,Double> HMSD,int n, double[][] g){												//ctor
		sortedKeys=ALS;
		potMap=HMSD;
		NOF=n;
		geoms=g;
	}
	
	private void createComponents(Container window){
		JLabel text = new JLabel("PES Derivative");
		JPanel topPanel = new JPanel();
		JPanel botPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JLabel sl1 = new JLabel("Scale X : ");
		JLabel sl2 = new JLabel("Scale Y : ");
		JLabel sl3 = new JLabel("Move X : ");
		JLabel sl4 = new JLabel("Move Y : ");
		JButton closeButton = new JButton("Close");
		JButton tabButton = new JButton("Table");
		JSlider scaleX = new JSlider(JSlider.HORIZONTAL,0,100,25);
		JSlider scaleY = new JSlider(JSlider.VERTICAL,20,500,50);
		JSlider moveYslid = new JSlider(JSlider.VERTICAL,-400,100,0);
		JSlider moveXslid = new JSlider(JSlider.HORIZONTAL,-50,250,0);
		moveX=0;
		moveY=0;
		scaleX.setMajorTickSpacing(25);
		scaleX.setMinorTickSpacing(5);
		scaleX.setPaintTicks(true);
		scaleX.setPaintLabels(true);
		
		scaleX.addChangeListener(new ChangeListener() {					//Adding a listener for the slider This part repeats for the other sliders.
		public void stateChanged(ChangeEvent e){
			JSlider source = (JSlider)e.getSource();
			if(!source.getValueIsAdjusting()){
				facX=(double)source.getValue();
				drawPanel.removeAll();
				drawPanel.repaint();
				drawPanel.revalidate();
				drawPES();
			}
		}});		
		
		scaleY.setMajorTickSpacing(50);
		scaleY.setMinorTickSpacing(10);
		scaleY.setPaintTicks(true);
		scaleY.setPaintLabels(true);
		
		scaleY.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e){
			JSlider source = (JSlider)e.getSource();
			if(!source.getValueIsAdjusting()){
				facY=(double)source.getValue();
				drawPanel.removeAll();
				drawPanel.repaint();
				drawPanel.revalidate();
				drawPES();
			}
		}});
		
		moveXslid.setMajorTickSpacing(50);
		moveXslid.setMinorTickSpacing(20);
		moveXslid.setPaintTicks(true);
		moveXslid.setPaintLabels(true);
		
		moveXslid.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e){
				JSlider source = (JSlider)e.getSource();
				if(!source.getValueIsAdjusting()){
					moveX=(double)source.getValue();
					drawPanel.removeAll();
					drawPanel.repaint();
					drawPanel.revalidate();
					drawPES();
				}
			}
		});
		
		moveYslid.setMajorTickSpacing(100);
		moveYslid.setMinorTickSpacing(20);
		moveYslid.setPaintTicks(true);
		moveYslid.setPaintLabels(true);
		
		moveYslid.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e){
				JSlider source = (JSlider)e.getSource();
				if(!source.getValueIsAdjusting()){
					moveY=(double)source.getValue();
					drawPanel.removeAll();
					drawPanel.repaint();
					drawPanel.revalidate();
					drawPES();
				}
			}
		});
		okListener okL = new okListener(frame);
		tableListener tbl = new tableListener(0.0/0.0,0.0/0.0,getRe());
		tabButton.addActionListener(tbl);
		Box cardSX = Box.createVerticalBox();							//I can use boxes to organize my layout better
		cardSX.add(sl1);
		cardSX.add(scaleX);
		Box cardMX = Box.createVerticalBox();
		cardMX.add(sl3);
		cardMX.add(moveXslid);
		Box cardY = Box.createVerticalBox();
		Box cardMY = Box.createHorizontalBox();
		cardMY.add(sl4);
		cardMY.add(moveYslid);
		Box cardSY = Box.createHorizontalBox();
		cardSY.add(sl2);
		cardSY.add(scaleY);
		cardY.add(cardSY);
		cardY.add(cardMY);
		
		closeButton.addActionListener(okL);
		topPanel.add(text);												//Filling up the panels the way I want
		topPanel.add(closeButton);
		topPanel.add(tabButton);
		botPanel.add(cardSX);
		botPanel.add(cardMX);
		rightPanel.add(cardY);
		window.add(topPanel, BorderLayout.PAGE_START);					//Putting panels in the window
		window.add(botPanel,BorderLayout.PAGE_END);
		window.add(rightPanel,BorderLayout.LINE_END);
		window.add(drawPanel,BorderLayout.CENTER);
	}
	
	public void drawPES(){			//draws the derivative
		double[] xvar = new double[NOF-1];
		double[] yvar = new double[NOF-1];
		for(int i=0; i<NOF-1;i++){
			xvar[i]=deriv.getXp(i);
			yvar[i]=deriv.getYp(i);
			xvar[i]=xvar[i]*facX+moveX;
			yvar[i]=yvar[i]*facY+facY/1.2+moveY;
		}
		drawPanel.setX(xvar);
		drawPanel.setY(yvar);
		frame.getContentPane().add(drawPanel);
	}
	
	public double getRe(){
		//Function gets the Re value using linear interpolation of the derivative.
		double[] xvar = new double[NOF-1];
		double[] yvar = new double[NOF-1];
		for(int i=0; i<NOF-1;i++){
			xvar[i]=deriv.getXp(i);
			yvar[i]=deriv.getYp(i);
		}
		//values represent equation y=ax+b
		double x=0.0;
		double y=0.0;
		double a = 0.0;
		double b = 0.0;
		int index = 0;
		for(int i=0;i<NOF-1;i++){
			y=yvar[i];
			x=xvar[i];
			if(y>0){
				index = i;
				break;
			}
		}
		if(index>0){
			//solve a,b for y=ax+b
			a = (y-yvar[index-1])/(xvar[index-1]-x);
			b = (x*yvar[index-1]-xvar[index-1]*y)/(xvar[index-1]-x);
		}
		//solution for x where y=0 is -b/a
		double ans = -b/a;
		return ans;
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		sizeX=1000;
		sizeY=800;
		deriv = new pesder(NOF);
		deriv.setDergeoms(geoms);
		deriv.setDersortedkeys(sortedKeys);
		deriv.setDerpotmap(potMap);
		deriv.Derive();
		facX=abs(deriv.maxXp());
		facY=10.0/abs(deriv.maxYp());
		System.out.println("Scaling factor X for derivative set to "+Double.toString(facX));
		System.out.println("Scaling factor Y for derivative set to "+Double.toString(facY));
		frame = new JFrame("PES Derivative");
		drawPanel = new plot();
		frame.setPreferredSize(new Dimension(sizeX,sizeY));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		createComponents(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
		drawPES();
	}
	
	public void setSortedKeys(ArrayList<String> ALS){
		sortedKeys=ALS;
	}
	public void setPotMap(HashMap<String,Double> HMSD){
		potMap=HMSD;
	}
	public void setGeoms(double[][] d){
		geoms=d;
	}
	public void setNOF(int n){
		NOF=n;
	}
	public double abs(double d){
		if(d<0.0){
			return -1*d;
		}else{
			return d;
		}
		
	}
	private JFrame frame;
	private plot drawPanel;
	private int sizeX, sizeY,NOF;
	private double facX,facY,moveX,moveY;
	private double[][] geoms;
	private ArrayList<String> sortedKeys;
	private HashMap<String,Double> potMap;
	private pesder deriv;
}
