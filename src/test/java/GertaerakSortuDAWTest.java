import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.*;

import dataAccess.*;
import domain.*;
import test.dataAccess.TestDataAccess;

public class GertaerakSortuDAWTest {
	
	DataAccess sut;
	TestDataAccess testDA;
	
	@Before
	public void antesDeCada() {
		sut = new DataAccess();
		testDA = new TestDataAccess();
	}
	
	@Test
	public void testCamino1() {
		// El deporte no existira en la db
		
		String desc = "Barsa-Madrid";
		Calendar cal = Calendar.getInstance();
		cal.set(2050, 10, 12); // 12 de octubre del 2050
		Date date = cal.getTime();
		String depo = "B4aloncest0";
		
		// Esperamos que devuelva falso (no se ha insertado porq el 
		// deporte no existe
		boolean expected = false;
				
		boolean actual = sut.gertaerakSortu(desc, date, depo);
		assertEquals(actual, expected);
	}
	
	@Test
	public void testCamino2() {
		// El evento SI existira en la bd, entonces NO se insertara
		
		String desc = "Barsa-Madrid";
		Calendar cal = Calendar.getInstance();
		cal.set(2050, 10, 12); // 12 de octubre del 2050
		Date date = cal.getTime();
		String depo = "Futbol";
		
		// Insertamos el evento en la db
		Event insertado = testDA.addEventWithQuestion(desc, date, "PreguntaGenerica", 1); // 1 euro minimum bet, nos da igual
		
		// Esperamos que devuelva falso (no se ha insertado porq el 
		// evento ya existe con esa descripcion en ese dia)
		boolean expected = false;
				
		boolean actual = sut.gertaerakSortu(desc, date, depo);
		assertEquals(actual, expected);
		
		// Quitamos el evento de la db para no contaminar
		testDA.removeEvent(insertado);
		
	}
	
	@Test
	public void testCamino3() {
		// Habrá eventos en la fecha consultada, pero no el nuestro. Entonces SI 
		// que se insertará
		
		String desc = "Barsa-Madrid";
		String desc2 = "Atletic-Real";
		Calendar cal = Calendar.getInstance();
		cal.set(2050, 10, 12); // 12 de octubre del 2050
		Date date = cal.getTime();
		String depo = "Futbol";
		
		// Insertamos el evento en la db
		Event insertado = testDA.addEventWithQuestion(desc2, date, "PreguntaGenerica", 1); // 1 euro minimum bet, nos da igual
		
		// Esperamos que devuelva true (SI se ha insertado porq el 
		// evento NO existe con esa descripcion en ese dia)
		boolean expected = true;
				
		boolean actual = sut.gertaerakSortu(desc, date, depo);
		assertEquals(actual, expected);
		
		// Quitamos los eventos de la db para no contaminar
		testDA.removeEvent(insertado);
		testDA.removeEventsWithDescDate(desc, date);
		
	}
	
	@Test
	public void testCamino4() {
		// No habrá nada en la fecha consultada. Entonces SI 
		// que se insertará
		
		String desc = "Barsa-Madrid";
		Calendar cal = Calendar.getInstance();
		cal.set(2050, 10, 12); // 12 de octubre del 2050
		Date date = cal.getTime();
		String depo = "Futbol";
		
		// Esperamos que devuelva true (SI se ha insertado porq no hay 
		// eventos ese dia)
		boolean expected = true;
				
		boolean actual = sut.gertaerakSortu(desc, date, depo);
		assertEquals(actual, expected);
		
		// Quitamos los eventos de la db para no contaminar
		testDA.removeEventsWithDescDate(desc, date);
		
	}
	

}
