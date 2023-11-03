import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Collection;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import dataAccess.DataAccessEmaitzakIpini;
import domain.Event;
import domain.Question;
import domain.Quote;
import domain.Team;
import exceptions.EventNotFinished;

public class EmaitzakIpiniDAB2Test {

	DataAccessEmaitzakIpini sut;
	Calendar calendar;
	Quote q, q2;
	
	@Before
	public void antesDeCada() {
		sut = new DataAccessEmaitzakIpini();
		calendar = Calendar.getInstance();
	}
	
	@Test
	public void test1() {
		//La cuota no existe en la BD
		q = new Quote(2.0, "232");
		assertThrows(NullPointerException.class,() -> {sut.EmaitzakIpini(q);});
	}
	
	@Test
	public void test2() {
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
	public void test3_1() {
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
	public void test3_2() {
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
	public void test3_3() {
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
