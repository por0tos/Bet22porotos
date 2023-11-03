package gui;

import java.awt.Color;
import java.net.URL;
import java.util.Locale;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import configuration.BLFacadeFactory;
import configuration.ConfigXML;
import dataAccess.DataAccess;

public class ApplicationLauncher { 
	
	
	
	public static void main(String[] args) {
		
		MainGUI a=new MainGUI();
		a.setVisible(false);
		
		MainUserGUI b = new MainUserGUI(); 
		b.setVisible(true);


		try {
			
			BLFacade appFacadeInterface = new BLFacadeFactory().get();

			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			
			MainGUI.setBussinessLogic(appFacadeInterface);
	
			
		}catch (Exception e) {
			a.jLabelSelectOption.setText("Error: "+e.toString());
			a.jLabelSelectOption.setForeground(Color.RED);	
			
			System.out.println("Error in ApplicationLauncher: "+e.toString());
		}
		//a.pack();


	}

}
