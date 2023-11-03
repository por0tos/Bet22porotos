package test.businessLogic;

import java.util.*;
import businessLogic.*;
import configuration.*;
import domain.*;

public class TestEventIterator {
	public static void main(String[] args) {
		// Queremos objeto date del día 17 del proximo mes
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date nextMonth17thDate = calendar.getTime();
        
        
        try {
            BLFacade bl = new BLFacadeFactory().get();
        	ExtendedIterator<Event> i = bl.getEventsIterator(nextMonth17thDate);
        	Event e;
        	System.out.println("---------------");
        	System.out.println("hacia atras");
        	System.out.println("---------------");
        	i.goLast();
        	while (i.hasPrevious()) {
        		e = i.previous();
        		System.out.println(e.toString());
        	}
        	System.out.println("---------------");
        	System.out.println("hacia delante");
        	System.out.println("---------------");
        	i.goFirst();
        	while (i.hasNext()) {
        		e = i.next();
        		System.out.println(e.toString());
        	}
        	
        } catch(Exception e) {
        	System.out.println(e.getMessage());
        }
        
	}
}
