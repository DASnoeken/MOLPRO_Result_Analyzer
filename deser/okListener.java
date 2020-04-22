import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class okListener implements ActionListener{			//Listens to closing buttons
	public okListener(JFrame f){
		frame = f;
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		frame.dispose();							//Get rid of frame
	}
	
	private JFrame frame;
}
