import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import configuration.UtilDate;
import dataAccess.DataAccess;
import dataAccess.DataAccessGertaeraEzabatu;
import test.dataAccess.TestDataAccess;

import domain.*;

public class GertaeraEzabatuDAB2Test {

	DataAccessGertaeraEzabatu sut;
	TestDataAccess tda;
	EntityManager db;
	Calendar today;
	int month;
	int year; 
	boolean result;
	boolean expected;
	
	@Before
	public void setUp(){
		sut = new DataAccessGertaeraEzabatu();
		tda = new TestDataAccess();	
		today = Calendar.getInstance();   
		month=today.get(Calendar.MONTH);
		month+=1;
		year=today.get(Calendar.YEAR);
		if (month==12) { month=0; year+=1;}
	
	}
	
	@Test
	public void test1() {
			Event ev = tda.addEventWithQuestion("Raimon-Royal Academy", UtilDate.newDate(year, month, 30), "¿Quién ganará el partido?", 1);
			result = sut.gertaeraEzabatu(ev);
			expected = false;
			assertEquals(result, expected);
			tda.removeEvent(ev);
	}
	
	
	@Test
	public void test2() {
		Event ev = tda.addEventWithQuestionAndResult("Raimon-Royal Academy", UtilDate.newDate(year, month, 10), "¿Quién ganará?", 1, "Raimon");
		result = sut.gertaeraEzabatu(ev);
		expected = true;
		assertEquals(result, expected);
	}

}
