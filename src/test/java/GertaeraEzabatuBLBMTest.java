import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;
import org.junit.*;
import org.mockito.*;

import businessLogic.*;
import configuration.UtilDate;
import exceptions.*;
import dataAccess.*;
import domain.Event;
import domain.Question;
import domain.Team;

public class GertaeraEzabatuBLBMTest {
	
	@InjectMocks
	BLFacadeImplementation sut;
	
	@Mock
    DataAccess da;
    
	int month;
	int year; 
	Calendar today;
	boolean result;
	boolean expected;
	
    @Before
    public void setUp() {
    	da = Mockito.mock(DataAccess.class);
    	sut = new BLFacadeImplementation(da);
    	today = Calendar.getInstance();   
		month=today.get(Calendar.MONTH);
		month+=1;
		year=today.get(Calendar.YEAR);
		if (month==12) { month=0; year+=1;}
    }
	@Test
	public void test1() {
		 Event ev = new Event("Raimon-Royal Academy", UtilDate.newDate(year, month, 01), new Team("Raimon"), new Team("Royal academy"));
		 Mockito.doReturn(false).when(da).gertaeraEzabatu(ev);
		 boolean result;
		 boolean expected = false;
		 
		 try {
			result = sut.gertaeraEzabatu(ev);
			assertEquals(result, expected);

		 } catch(Exception e) {
			 fail("Algo ha ido mal.");
		 }
	}
	


	@Test
	public void test2() {
		 Event ev = new Event("Raimon-Royal Academy", UtilDate.newDate(year-1, month, 01), new Team("Raimon"), new Team("Royal academy"));
		 Question q = ev.addQuestion("¿Quién gana?", 1.5);
		 q.setResult("Raimon");
		 Mockito.doReturn(true).when(da).gertaeraEzabatu(ev);
		 boolean result;
		 boolean expected = true;
		 
		 try {
			result = sut.gertaeraEzabatu(ev);
			assertEquals(result, expected);

		 } catch(Exception e) {
			 fail("Algo ha ido mal.");
		 }
	}
	
	@Test
	public void test3() {
		 Event ev = new Event("Raimon-Royal Academy", UtilDate.newDate(year, month, 01), new Team("Raimon"), new Team("Royal academy"));
		 Mockito.doReturn(true).when(da).gertaeraEzabatu(ev);
		 Question q = ev.addQuestion("¿Quién gana?", 1.5);
		 q.setResult("Raimon");
		 boolean result;
		 boolean expected = true;
		 
		 try {
			result = sut.gertaeraEzabatu(ev);
			assertEquals(result, expected);

		 } catch(Exception e) {
			 fail("Algo ha ido mal.");
		 }
	}
	
	@Test
	public void test4() {
		 Event ev = new Event("Raimon-Royal Academy", UtilDate.newDate(year, month, 30), new Team("Raimon"), new Team("Royal academy"));
		 Mockito.doReturn(true).when(da).gertaeraEzabatu(ev);
		 Question q = ev.addQuestion("¿Quién gana?", 1.5);
		 q.setResult("Raimon");
		 boolean result;
		 boolean expected = true;
		 
		 try {
			result = sut.gertaeraEzabatu(ev);
			assertEquals(result, expected);

		 } catch(Exception e) {
			 fail("Algo ha ido mal.");
		 }
	}
	
	@Test
	public void test5() {
		 Event ev = new Event("Raimon-Royal Academy", UtilDate.newDate(year, month, 30), new Team("Raimon"), new Team("Royal academy"));
		 Mockito.doReturn(true).when(da).gertaeraEzabatu(ev);
		 Question q = ev.addQuestion("¿Quién gana?", 1.5);
		 q.setResult("Raimon");
		 boolean result;
		 boolean expected = true;
		 
		 try {
			result = sut.gertaeraEzabatu(ev);
			assertEquals(result, expected);

		 } catch(Exception e) {
			 fail("Algo ha ido mal.");
		 }
	}
	
}
