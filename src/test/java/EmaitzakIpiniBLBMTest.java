import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import org.mockito.*;

import businessLogic.*;
import exceptions.*;
import dataAccess.*;

import domain.Event;
import domain.Question;
import domain.Quote;
import domain.Team;

public class EmaitzakIpiniBLBMTest {
	Calendar calendar;
	Quote q;
	
	@InjectMocks
	BLFacadeImplementation sut;
	
	@Mock
    DataAccess da;
    
    @Before
    public void antesDeCada() {
    	calendar = Calendar.getInstance();
    	da = Mockito.mock(DataAccess.class);
    	sut = new BLFacadeImplementation(da);
    }
    
    @Test
	public void test1() throws EventNotFinished {
		//La cuota no existe en la BD
    	q = new Quote(245.0, "2sedrf");
    	Mockito.doThrow(new NullPointerException()).when(da).EmaitzakIpini(q);
    	try {
			da.EmaitzakIpini(q);
			fail("No deberia de ir bien");
		} catch (EventNotFinished | NullPointerException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void test2() throws EventNotFinished {
		//La fecha de ese evento es posterior a la fecha actual
		calendar.set(2023, Calendar.NOVEMBER, 01, 0, 0, 0);
		q = new Quote(100.0, "2", new Question(15, "Who will win the match?", 1.0, new Event(21, "Atletico-Athletic", calendar.getTime(), new Team("Atletico"), new Team("Athletic"))));
		Mockito.doThrow(new EventNotFinished()).when(da).EmaitzakIpini(q);
		try {
			da.EmaitzakIpini(q);
			fail("No deberia de ir bien");
		} catch (EventNotFinished e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void test3_1() {
		//Una cuota sin apuestas
		calendar.set(2023, Calendar.SEPTEMBER, 21, 0, 0, 0);
		q = new Quote(5.0, "1", new Question(19, "Emaitza?", 1.0, new Event(21, "Real Madrid-Barcelona", calendar.getTime(), new Team("Real Madrid"), new Team("Barcelona"))));
		
		try {
			da.EmaitzakIpini(q);
			Mockito.verify(da, Mockito.times(1)).EmaitzakIpini(Mockito.any(Quote.class));
		} catch (EventNotFinished e) {
			fail("Todo deber�a ir bien");
		}
	}
	
	@Test
	public void test3_2() {
		//Cuota con apuestas y sera una puesta "perdida"
		calendar.set(2023, Calendar.SEPTEMBER, 21, 0, 0, 0);
		q = new Quote(2.5, "2", new Question(19, "Emaitza?", 1.0, new Event(21, "Real Madrid-Barcelona", calendar.getTime(), new Team("Real Madrid"), new Team("Barcelona"))));
		try {
			da.EmaitzakIpini(q);
			Mockito.verify(da, Mockito.times(1)).EmaitzakIpini(Mockito.any(Quote.class));
		} catch (EventNotFinished e) {
			fail("Todo deber�a ir bien");
		}
	}
	
	@Test
	public void test3_3() {
		//Cuota con apuestas y sera una puesta "premiada"
		calendar.set(2023, Calendar.SEPTEMBER, 21, 0, 0, 0);
		q = new Quote(2.5, "2", new Question(19, "Emaitza?", 1.0, new Event(21, "Real Madrid-Barcelona", calendar.getTime(), new Team("Real Madrid"), new Team("Barcelona"))));
		try {
			da.EmaitzakIpini(q);
			Mockito.verify(da, Mockito.times(1)).EmaitzakIpini(Mockito.any(Quote.class));
		} catch (EventNotFinished e) {
			fail("Todo deber�a ir bien");
		}
	} 
}
