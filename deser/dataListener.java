import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

public class dataListener implements ActionListener{	//This class makes a table that lists all the input data.
	public dataListener(double[][] g, ArrayList<String> ALS, HashMap<String,Double> HMSD){
		setGeoms(g);
		setSortedkeys(ALS);
		setPotmap(HMSD);
		setData();
	}
	
	public void setData(){	//set R and E properly for usage later
		R = new double[sortedKeys.size()];
		E = new double[sortedKeys.size()];
		for(int i=0;i<sortedKeys.size();i++){
			R[i] = geoms[i][0];
			E[i] = potMap.get(sortedKeys.get(i));
		}
	}
	
	private void createComponents(Container window){		//Here we make the table
		JLabel text = new JLabel("Table");
		DefaultTableModel model = new DefaultTableModel(); 
		JTable table = new JTable(model);
		model.addColumn("R (bohr)"); 
		model.addColumn("E (hartree)");
		for(int i=0;i<R.length;i++){
			model.addRow(new Object[]{R[i],E[i]});		//adding all the rows
		}
		table.setPreferredScrollableViewportSize(new Dimension(500,70));
		JPanel botPanel = new JPanel();
		JButton okButton = new JButton("OK");
		okListener okl = new okListener(frame);
		okButton.addActionListener(okl);
		botPanel.add(okButton);
		window.setLayout(new BorderLayout());
		window.add(table.getTableHeader(),BorderLayout.PAGE_START);
		window.add(table,BorderLayout.CENTER);
		window.add(botPanel,BorderLayout.PAGE_END);
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		frame = new JFrame("Data");
		int sizeX=512;
		int sizeY=1024;
		frame.setPreferredSize(new Dimension(sizeX,sizeY));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		createComponents(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
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
	
	private JFrame frame;
	private HashMap<String,Double> potMap;
	private ArrayList<String> sortedKeys;
	private double[][] geoms;
	private double[] R;
	private double[] E;
}
