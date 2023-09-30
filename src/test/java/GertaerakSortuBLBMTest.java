import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import org.mockito.*;
import businessLogic.*;
import exceptions.*;
import dataAccess.*;

public class GertaerakSortuBLBMTest {

	@InjectMocks
	BLFacadeImplementation sut;
	
	@Mock
	DataAccess da;
	
	@Before
	public void antesDeCada() {
		da = Mockito.mock(DataAccess.class);
		sut = new BLFacadeImplementation(da);
	}
	
	@Test
	public void test1() {
		// Intentamos insertar evento cuyo deporte no existe en la bd
		
		String sport = "Pilla-pilla";
		String description = "Juan-Nerea";
		Calendar cal = Calendar.getInstance();
		cal.set(2050, 10, 12); // 12 de octubre del 2050
		Date date = cal.getTime();
		
		// Mockeamos
		Mockito.doReturn(false).when(da).gertaerakSortu(description, date, sport);
		
		// Lo que esperamos
		boolean expected = false;
		boolean actual;
		try {
			actual = sut.gertaerakSortu(description, date, sport);
			assertEquals(expected,actual);

		} catch (EventFinished e) {
			// Si salta la excepcion algo ha ido mal
			fail("Algo ha ido mal, no deberia de haber saltado excepcion");
		}
		
	}
	
	@Test
	public void test2() {
		// Intentamos insertar evento (valido) cuya fecha es anterior a ahora
		
		String sport = "Futbol";
		String description = "Madrid-Barcelona";
		Calendar cal = Calendar.getInstance();
		cal.set(1995, 10, 12); // 12 de octubre del 1995
		Date date = cal.getTime();
		
		// No hace falta mockear porq no tocamos la clase DataAccess (se supone!)
		
		try {
			sut.gertaerakSortu(description, date, sport);
			// Si no salta excepcion, algo ha ido mal
			fail("No ha saltado una excepcion al intentar insertar fecha anterior");
		} catch (EventFinished e) {
			// Bien, no ha dejado insertar el evento
			assertTrue(true);
		}
		
	}
	
	@Test
	public void test3() {
		// Intentamos insertar evento que ya está en db
		
		String sport = "Futbol";
		String description = "Madrid-Barcelona";
		Calendar cal = Calendar.getInstance();
		cal.set(2050, 10, 12); // 12 de octubre del 2050
		Date date = cal.getTime();
		
		// Mockeamos
		Mockito.doReturn(false).when(da).gertaerakSortu(description, date, sport);
		
		// Lo que esperamos
		boolean expected = false;
		boolean actual;
		try {
			actual = sut.gertaerakSortu(description, date, sport);
			assertEquals(expected,actual);

		} catch (EventFinished e) {
			// Si salta la excepcion algo ha ido mal
			fail("Algo ha ido mal, no deberia de haber saltado excepcion");
		}
	}
	
	@Test
	public void test4() {
		// Todo bien, insertamos evento que no esta en db y con deporte existente
		
		String sport = "Futbol";
		String description = "Madrid-Barcelona";
		Calendar cal = Calendar.getInstance();
		cal.set(2050, 10, 12); // 12 de octubre del 2050
		Date date = cal.getTime();
		
		// Mockeamos
		Mockito.doReturn(true).when(da).gertaerakSortu(description, date, sport);
		
		// Lo que esperamos
		boolean expected = true;
		boolean actual;
		try {
			actual = sut.gertaerakSortu(description, date, sport);
			assertEquals(expected,actual);

		} catch (EventFinished e) {
			// Si salta la excepcion algo ha ido mal
			fail("Algo ha ido mal, no deberia de haber saltado excepcion");
		}
	}
	
	
	
	
}
