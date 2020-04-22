import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class helpListener implements ActionListener{					//Listens to the help button
	
	public helpListener(String s){
		text=s;
	}
	
	private void createComponents(Container window){					//Display help window
		JLabel text = new JLabel("Help");
		JPanel topPanel = new JPanel();
		resultsArea = new JTextArea();
		JPanel botPanel = new JPanel();
		JButton okButton = new JButton("OK");
		okListener okl = new okListener(frame);
		okButton.addActionListener(okl);
		resultsArea.append("This GUI plots the potential energy surface (PES) made by MOLPRO in order to quickly check the shape of the potential.\n");
		resultsArea.append("The sliders that are given can be used to either scale or move the graph, as is indicated by the text.\n");
		resultsArea.append("The slider marked by \"Scale X\" will scale in the horizontal direction. \"Scale Y\" will scale vertically. \n");
		resultsArea.append("The slider marked by \"Move X\" will move the graph left and right and the slider \"Move Y\" will move the graph up or down.\n");
		resultsArea.append("The \"Derivative\" button will calculate and plot the first derivative of the PES.\n");
		resultsArea.append("The \"Data\" button shows all the input data.\n");
		resultsArea.append("The table button opens a table that shows some data on the PES cross section. The derivative only shows the value of Re. The one from original data is more reliable.\n");
		resultsArea.append("Note that the value reported by the table in the original data window is calculated by Lagrange interpolation with a\nsecond degree polynomial in combination with the harmonic approximation.\nThe derivative version is interpolated using linear interpolation.\n");
		resultsArea.append("The same scaling and moving buttons show up on the PES derivative screen and work identically to the ones on the PES screen.\n");
		resultsArea.append("The data reported as \"NaN\" in the derivative table is not possible to be calculated via the derivative. That's why it is reported as NaN.\n\n");
		resultsArea.append("Made by D. A. Snoeken. Version 2, March 2020.");
		resultsArea.setEditable(false);
		topPanel.add(text);
		botPanel.add(okButton);
		window.add(topPanel,BorderLayout.PAGE_START);
		window.add(resultsArea,BorderLayout.CENTER);
		window.add(botPanel,BorderLayout.PAGE_END);
	}

	@Override
	public void actionPerformed(ActionEvent event){
		frame = new JFrame(text);
		int sizeX=1200;
		int sizeY=512;
		frame.setPreferredSize(new Dimension(sizeX,sizeY));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		createComponents(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	private String text;
	private JFrame frame;
	private JTextArea resultsArea;
}
