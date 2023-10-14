import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;

import dataAccess.*;
import domain.*;
import exceptions.*;	

public class EmaitzakIpiniDAWTest {

	DataAccess sut;
	Calendar calendar;
	Quote q, q2;
	
	@Before
	public void antesDeCada() {
		sut = new DataAccess();
		calendar = Calendar.getInstance();
	}
	
	@Test
	public void testCamino1() {
		//La fecha de ese evento es posterior a la fecha actual
		calendar.set(2023, Calendar.NOVEMBER, 01, 0, 0, 0);
		Collection<Quote> quote = sut.findQuote(new Question(15, "Who will win the match?", 1.0, new Event(21, "Atletico-Athletic", calendar.getTime(), new Team("Atletico"), new Team("Athletic"))));
		for (Quote qt: quote)
		{
			if (qt.getForecast().equals("2")) q=qt;
		}
		assertThrows(EventNotFinished.class, () -> {sut.EmaitzakIpini(q);});
	}
	
	
	@Test
	public void testCamino3()  {
		//Una cuota sin apuestas
		calendar.set(2023, Calendar.SEPTEMBER, 21, 0, 0, 0);
		Collection<Quote> quote = sut.findQuote(new Question(19, "Emaitza?", 1.0, new Event(21, "Real Madrid-Barcelona", calendar.getTime(), new Team("Real Madrid"), new Team("Barcelona"))));
		for (Quote qt: quote)
		{
			if (qt.getForecast().equals("1")) q=qt;
		}
		try {
			sut.EmaitzakIpini(q);
			Vector<Quote>quotes = q.getQuestion().getQuotes();
			assertEquals("jokoan", quotes.get(0).getApustuak().get(0).getApustuAnitza().getEgoera());
		} catch (EventNotFinished e) {
			fail("Todo debería ir bien");
		}
	}
	
	@Test
	public void testCamino5() {
		//Cuota con apuestas y sera una puesta "perdida"
		calendar.set(2023, Calendar.SEPTEMBER, 21, 0, 0, 0);
		Collection<Quote> quote = sut.findQuote(new Question(19, "Emaitza?", 1.0, new Event(21, "Real Madrid-Barcelona", calendar.getTime(), new Team("Real Madrid"), new Team("Barcelona"))));
		for (Quote qt: quote)
		{
			if (qt.getForecast().equals("2")) q=qt;
		}
		try {
			sut.EmaitzakIpini(q);
			Vector<Quote>quotes = q.getQuestion().getQuotes();
			//para esta cuota se deberia de cambiar su valor en la BD y asi veremos que si funciona
			assertEquals("galduta", quotes.get(0).getApustuak().get(2).getApustuAnitza().getEgoera());		
		} catch (EventNotFinished e) {
			fail("Todo debería ir bien");
		}
	}
	
	@Test
	public void testCamino6() {
		//Cuota con apuestas y sera una puesta "premiada"
		calendar.set(2023, Calendar.SEPTEMBER, 21, 0, 0, 0);
		Collection<Quote> quote = sut.findQuote(new Question(19, "Emaitza?", 1.0, new Event(21, "Real Madrid-Barcelona", calendar.getTime(), new Team("Real Madrid"), new Team("Barcelona"))));
		for (Quote qt: quote)
		{
			if (qt.getForecast().equals("2")) q=qt;
		}
		try {
			sut.EmaitzakIpini(q);
			Vector<Quote>quotes = q.getQuestion().getQuotes();
			assertEquals("irabazita", quotes.get(0).getApustuak().get(1).getApustuAnitza().getEgoera());		
		} catch (EventNotFinished e) {
			fail("Todo debería ir bien");
		}
	}
}
