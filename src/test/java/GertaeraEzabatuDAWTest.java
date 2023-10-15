import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import configuration.UtilDate;
import dataAccess.DataAccess;
import domain.Event;
import domain.Question;
import domain.Quote;
import domain.Team;
import exceptions.QuestionAlreadyExist;
import exceptions.QuoteAlreadyExist;
import test.dataAccess.TestDataAccess;

public class GertaeraEzabatuDAWTest {

	DataAccess sut;
	TestDataAccess tda;
	EntityManager db;
	Calendar today;
	int month;
	int year; 
	boolean result;
	boolean expected;
	
	@Before
	public void setUp(){
		sut = new DataAccess();
		tda = new TestDataAccess();	
		today = Calendar.getInstance();   
		month=today.get(Calendar.MONTH);
		month+=1;
		year=today.get(Calendar.YEAR);
		if (month==12) { month=0; year+=1;}
	}
	
	@Test
	public void testCamino1() {
		Event ev = tda.addEventWithNoQuestion("Raimon-Royal Academy", UtilDate.newDate(year, month, 10));
		result = sut.gertaeraEzabatu(ev);
		expected = true;
		assertEquals(result, expected);
	}

	@Test
	public void testCamino3() {
		Event ev = tda.addEventWithQuestionAndResult("Raimon-Royal Academy", UtilDate.newDate(year-1, month, 10), "¿Quién ganará?", 1, "Raimon");
		result = sut.gertaeraEzabatu(ev);
		expected = true;
		assertEquals(result, expected);
	}

	@Test
	public void testCamino4() {
		Event ev = tda.addEventWithQuestion("Raimon-Royal Academy", UtilDate.newDate(year, month, 10), "¿Quién ganará?", 1);
		result = sut.gertaeraEzabatu(ev);
		expected = false;
		assertEquals(result, expected);
	}
	
	@Test
	public void testCamino5() {
		Event ev = tda.addEventWithQuestionAndResult("Raimon-Royal Academy", new Date(), "¿Quién ganará?", 1, "Raimon");
		result = sut.gertaeraEzabatu(ev);
		expected = true;
		assertEquals(result, expected);
	}
	
}

