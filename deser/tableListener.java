import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class tableListener implements ActionListener{
	public tableListener(double classTurnPoint,double minenergy,double minX){		//This file creates the table when the "Table" button is pressed
		RCTP = classTurnPoint;
		minE = minenergy;
		minR = minX;
	}
	private void createComponents(Container window){					//Here we make a simple table
		JLabel text = new JLabel("Table");
		String[] columnnames = {"Attribute (Approximately)","Value (AU)"};
		Object[][] data = {
			{"R_(CTP)",new Double(RCTP)},
			{"De",new Double(minE)},
			{"Re",new Double(minR)}
		};
		JTable table = new JTable(data,columnnames);
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
	public void actionPerformed(ActionEvent event){						//Here we make a window
		frame = new JFrame("Table");
		int sizeX=1024;
		int sizeY=256;
		frame.setPreferredSize(new Dimension(sizeX,sizeY));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		createComponents(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	private Double RCTP;
	private Double minE;
	private Double minR;
	private JFrame frame;
}
