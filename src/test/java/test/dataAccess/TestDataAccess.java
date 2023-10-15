package test.dataAccess;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import domain.Event;
import domain.Question;
import domain.Quote;

public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestDataAccess()  {
		
		System.out.println("Creating TestDataAccess instance");

		open();
		
	}

	
	
	public boolean removeQuestion(Question q) {
		System.out.println(">> DataAccessTest: removeEvent");
		Question qu = db.find(Question.class, q.getQuestionNumber());
		if (qu!=null) {
			db.getTransaction().begin();
			db.remove(qu);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
	
	public void open(){
		
		System.out.println("Opening TestDataAccess instance ");

		String fileName=c.getDbFilename();
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
		
	}
	public void close(){
		db.close();
		System.out.println("DataBase closed");
	}

	public boolean removeEvent(Event ev) {
		System.out.println(">> DataAccessTest: removeEvent");
		Event e = db.find(Event.class, ev.getEventNumber());
		if (e!=null) {
			db.getTransaction().begin();
			db.remove(e);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
	
	
	
	public boolean removeEventsWithDescDate(String desc, Date d) {
		System.out.println(">> DataAccessTest: removeEventWithDescDate");
		boolean rem = false;
		TypedQuery<Event> Equery = db.createQuery("SELECT e FROM Event e WHERE e.getEventDate() =?1 AND e.getDescription() = ?2",Event.class);
		Equery.setParameter(1, d);
		Equery.setParameter(2, desc);
		for(Event ev: Equery.getResultList()) {
			removeEvent(ev);
			rem = true;
		}
		return rem;
		
	}
		
		public Event addEventWithQuestion(String desc, Date d, String question, float qty) {
			System.out.println(">> DataAccessTest: addEvent");
			Event ev=null;
				db.getTransaction().begin();
				try {
				    ev=new Event(null, desc,d, null, null);
				    ev.addQuestion(question, qty);
					db.persist(ev);
					db.getTransaction().commit();
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return ev;
	    }
		public boolean existQuestion(Event ev,Question q) {
			System.out.println(">> DataAccessTest: existQuestion");
			Event e = db.find(Event.class, ev.getEventNumber());
			if (e!=null) {
				return e.DoesQuestionExists(q.getQuestion());
			} else 
			return false;
			
		}
		
		public boolean removeQuote(Quote q) {
			System.out.println(">> DataAccessTest: removeEvent");
			Quote quo = db.find(Quote.class, q.getQuoteNumber());
			if (quo!=null) {
				db.getTransaction().begin();
				db.remove(quo);
				db.getTransaction().commit();
				return true;
			} else 
			return false;
	    }
		
		
		public Event addEvent(Event ev) {
			System.out.println(">> DataAccessTest: addEvent");
				db.getTransaction().begin();
				try {
				   
					db.persist(ev);
					db.getTransaction().commit();
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return ev;
	    }
		
			
			public Event addEventWithNoQuestion(String desc, Date d) {
				System.out.println(">> DataAccessTest: addEvent");
				Event ev=null;
					db.getTransaction().begin();
					try {
					    ev=new Event(null, desc,d, null, null);
						db.persist(ev);
						db.getTransaction().commit();
					}
					catch (Exception e){
						e.printStackTrace();
					}
					return ev;
		    }
			
			public Event addEventWithQuestionAndResult(String desc, Date d, String question, float qty, String res) {
				System.out.println(">> DataAccessTest: addEvent");
				Event ev=null;
					db.getTransaction().begin();
					try {
					    ev=new Event(null, desc,d, null, null);
					    Question q = new Question("¿Quién ganará?", 1, ev);
					    q.setResult(res);
					    ev.listaraGehitu(q);;
						db.persist(ev);
						db.getTransaction().commit();
					}
					catch (Exception e){
						e.printStackTrace();
					}
					return ev;
		    }
			
			public Event addEventWithQuestionResultAndQuote(int n, String desc, Date d, String question, float qty, String res) {
				System.out.println(">> DataAccessTest: addEvent");
				Event ev=null;
					db.getTransaction().begin();
					try {
					    ev=new Event(n, desc,d, null, null);
					    Question q = ev.addQuestion("¿Quién ganará?", 1);
					    q.setResult(res);
					    Quote quo = q.addQuote(3.0, "prueba", q);				   
					    db.persist(q);				    
						db.persist(ev);
						db.persist(quo);
						db.getTransaction().commit();
					}
					catch (Exception e){
						e.printStackTrace();
					}
					return ev;
		    }
			
}

