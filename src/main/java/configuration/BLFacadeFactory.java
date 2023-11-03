package configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;

public class BLFacadeFactory {

	ConfigXML c;
	
	public BLFacadeFactory() {
		c=ConfigXML.getInstance();
		System.out.println(c.getLocale());
		Locale.setDefault(new Locale(c.getLocale()));
		System.out.println("Locale: "+Locale.getDefault());
	}
	
	public BLFacade get() throws MalformedURLException {
		if (c.isBusinessLogicLocal()) {
			//In this option the DataAccess is created by FacadeImplementationWS
			//appFacadeInterface=new BLFacadeImplementation();

			//In this option, you can parameterize the DataAccess (e.g. a Mock DataAccess object)

			DataAccess da= new DataAccess(c.getDataBaseOpenMode().equals("initialize"));
			 return new BLFacadeImplementation(da);
		}
		else { 
			//If remote
			 String serviceName= "http://"+c.getBusinessLogicNode() +":"+ c.getBusinessLogicPort()+"/ws/"+c.getBusinessLogicName()+"?wsdl";
			URL url = new URL(serviceName);

	 
	        //1st argument refers to wsdl document above
			//2nd argument is service name, refer to wsdl document above
	        QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
	 
	        Service service = Service.create(url, qname);

	        return service.getPort(BLFacade.class);
		}
	}
	
}
