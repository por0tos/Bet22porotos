
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.*;

import dataAccess.DataAccess;
import domain.Event;
import test.dataAccess.TestDataAccess;

public class GertaerakSortuDABTest {

	DataAccess sut;
	TestDataAccess testDA;
	
	@Before
	public void antesDeCada() {
		sut = new DataAccess();
		testDA = new TestDataAccess();
	}
	
	@Test
	public void test1() {
		// Intentamos insertar evento cuyo deporte no existe en la bd
		
		String sport = "Pilla-pilla";
		String description = "Juan-Nerea";
		Calendar cal = Calendar.getInstance();
		cal.set(2050, 10, 12); // 12 de octubre del 2050
		Date date = cal.getTime();
		
		boolean expected = false;
		boolean actual = sut.gertaerakSortu(description, date, sport);
		
		assertEquals(expected,actual);
	}
	
	@Test
	public void test2() {
		// Intentamos insertar evento que ya está en db
		
		String sport = "Futbol";
		String description = "Madrid-Barcelona";
		Calendar cal = Calendar.getInstance();
		cal.set(2050, 10, 12); // 12 de octubre del 2050
		Date date = cal.getTime();
		
		// Insertamos el evento en bd
		Event insertado = testDA.addEventWithQuestion(description, date, "Pregunta", 1); // 1€ apuesta minima, nos da igual
		
		// probamos el metodo que nos interesa
		boolean expected = false;
		boolean actual = sut.gertaerakSortu(description, date, sport);
		
		assertEquals(expected,actual);
		// Limpiamos db para no contaminar
		testDA.removeEvent(insertado);
	}
	
	@Test
	public void test3() {
		// Todo bien, insertamos evento que no esta en db y con deporte existente
		
		String sport = "Futbol";
		String description = "Madrid-Barcelona";
		Calendar cal = Calendar.getInstance();
		cal.set(2050, 10, 12); // 12 de octubre del 2050
		Date date = cal.getTime();
		
		// probamos el metodo que nos interesa
		boolean expected = true;
		boolean actual = sut.gertaerakSortu(description, date, sport);
		
		assertEquals(expected,actual);
		
		// Limpiamos db para no contaminar
		testDA.removeEventsWithDescDate(description, date);
	}
	
}
