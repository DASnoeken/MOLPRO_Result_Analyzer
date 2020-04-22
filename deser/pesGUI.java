import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class pesGUI{
	/*This class produces the GUI where we can plot the PES in.*/
	public pesGUI(ArrayList<String> k, HashMap<String,Double> hm, double[][] g){	//ctor
		allKeys=k;
		NOF=allKeys.size();
		sortKeys();
		potMap=hm;
		geoms=g;
		sortGeoms();
		la = new lagrange(geoms,sortedKeys,potMap);
		la.setMinimum();
		frame = new JFrame("PES");
		sizeX=1000;
		sizeY=800;
		drawPanel = new plot();
		frame.setPreferredSize(new Dimension(sizeX,sizeY));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		createComponents(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
		deriv = null;
	}

	private void createComponents(Container window){
		JLabel text = new JLabel("PES Plot");							//Here we build the GUI
		JPanel topPanel = new JPanel();
		JPanel botPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JLabel sl1 = new JLabel("Scale X : ");
		JLabel sl2 = new JLabel("Scale Y : ");
		JLabel sl3 = new JLabel("Move X : ");
		JLabel sl4 = new JLabel("Move Y : ");
		JButton closeButton = new JButton("Close");
		JButton helpButton = new JButton("HELP");
		JButton derButton = new JButton("Derivative");
		JButton tabButton = new JButton("Table");
		JButton datButton = new JButton("Data");
		JSlider scaleX = new JSlider(JSlider.HORIZONTAL,0,100,25);		//Slider buttons
		JSlider scaleY = new JSlider(JSlider.VERTICAL,500,2000,sizeY);
		JSlider moveYslid = new JSlider(JSlider.VERTICAL,-500,100,0);
		JSlider moveXslid = new JSlider(JSlider.HORIZONTAL,-100,300,0);
		facY=sizeY;
		facX=25;
		moveX=0;
		moveY=0;
		scaleX.setMajorTickSpacing(25);									//Adapting sliders, this part repeats for other sliders
		scaleX.setMinorTickSpacing(5);
		scaleX.setPaintTicks(true);
		scaleX.setPaintLabels(true);

		scaleX.addChangeListener(new ChangeListener() {					//Adding a listener for the slider, this part also repeats for other sliders
		public void stateChanged(ChangeEvent e){
			JSlider source = (JSlider)e.getSource();
			if(!source.getValueIsAdjusting()){
				facX=(double)source.getValue();
				drawPanel.removeAll();									//Repainting after change is found
				drawPanel.repaint();
				drawPanel.revalidate();
				drawPES();
			}
		}});

		scaleY.setMajorTickSpacing(500);
		scaleY.setMinorTickSpacing(100);
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

		moveXslid.setMajorTickSpacing(60);
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
		if(sortedKeys==null){
			System.out.println("sortedKeys is null");
		}
		derListener dL = new derListener(sortedKeys,potMap,NOF,geoms);
		derButton.addActionListener(dL);
		helpListener hL = new helpListener("HELP");
		helpButton.addActionListener(hL);
		okListener okL = new okListener(frame);							//In an earlier version, the "close" button was called "ok" so I called it okListener. But the word close seemed a better name for it
		analyzeData dat = new analyzeData(geoms,sortedKeys,potMap);
		Double RCTP = dat.getRCTP();
		tableListener tbL = new tableListener(RCTP,la.getMinY(),la.getMinX());
		tabButton.addActionListener(tbL);
		dataListener daL = new dataListener(geoms,sortedKeys,potMap);
		datButton.addActionListener(daL);
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
		cardY.add(cardSY);												//I can also put boxes inside boxes
		cardY.add(cardMY);
		
		closeButton.addActionListener(okL);
		topPanel.add(text);												//Filling up the panels the way I want
		topPanel.add(closeButton);
		topPanel.add(derButton);
		topPanel.add(datButton);
		botPanel.add(cardSX);
		botPanel.add(cardMX);
		rightPanel.add(cardY);
		botPanel.add(helpButton);
		topPanel.add(tabButton);
		window.add(topPanel, BorderLayout.PAGE_START);					//Putting panels in the window
		window.add(botPanel,BorderLayout.PAGE_END);
		window.add(rightPanel,BorderLayout.LINE_END);
		window.add(drawPanel,BorderLayout.CENTER);
	}
	
	public void drawPES(){												//Here I set up to start drawing, or possibly redraw
		sortKeys();
		sortGeoms();	
		double[] d = new double[NOF];
		double[] g = new double[NOF];
		for(int i=0; i<NOF; i++){
			d[i]=potMap.get(sortedKeys.get(i));
			d[i] = d[i]*facY+facY+moveY;
			g[i]=geoms[i][0];
			g[i]=g[i]*facX+moveX;
		}
		
		drawPanel.setX(g);
		drawPanel.setY(d);
		frame.getContentPane().add(drawPanel);
	}
	
	//Accessor methods
	public JFrame getFrame(){
		return frame;
	}
	
	//Modifier methods
	public void setAllKeys(ArrayList<String> ALS){
		allKeys=ALS;
		NOF=allKeys.size();
		
	}
	public void setPotMap(HashMap<String,Double> HMSD){
		potMap=HMSD;
	}
	public void setGeoms(double[][] d){
		geoms=d;
	}
	
	//Couple of sorting functions
	public void sortKeys(){
		String tmp = new String();
		int lK;
		sortedKeys = new ArrayList<String>(NOF);
		for(int i=1;i<=NOF;i++){
			lK = allKeys.indexOf("R"+Integer.toString(i));
			tmp = allKeys.get(i-1);
			sortedKeys.add(allKeys.get(lK));
		}
	}
	
	public void sortGeoms(){
		double tmp;
		for(int i=0;i<NOF;i++){
			for(int j=0;j<i;j++){
				if(geoms[j][0]>geoms[i][0]){
					tmp = geoms[i][0];
					geoms[i][0]=geoms[j][0];
					geoms[j][0]=tmp;
				}
			}
		}
	}
	
	public pesder getDeriv(){
		return deriv;
	}
	
	//private stuff
	private plot drawPanel;
	private JFrame frame;
	private ArrayList<String> allKeys;
	private ArrayList<String> sortedKeys;
	private HashMap<String,Double> potMap;
	private double[][] geoms;
	private int NOF;
	private int sizeX,sizeY;
	private double facX,facY,moveY,moveX;
	private pesder deriv;
	private lagrange la;
}
