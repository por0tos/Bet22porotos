package dataAccess;

import java.util.ArrayList;
//hello
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.ApustuAnitza;
import domain.Apustua;

import domain.Event;
import domain.Jarraitzailea;

import domain.Question;
import domain.Quote;
import domain.Registered;
import domain.Sport;
import domain.Team;
import domain.Transaction;
import exceptions.EventNotFinished;
import exceptions.QuestionAlreadyExist;
import exceptions.QuoteAlreadyExist;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccessGertaeraEzabatu  {
	protected static EntityManager  db;
	protected static EntityManagerFactory emf;


	ConfigXML c=ConfigXML.getInstance();

     public DataAccessGertaeraEzabatu(boolean initializeMode)  {
		
		System.out.println("Creating DataAccess instance => isDatabaseLocal: "+c.isDatabaseLocal()+" getDatabBaseOpenMode: "+c.getDataBaseOpenMode());

		open(initializeMode);
		
	}

	public DataAccessGertaeraEzabatu()  {	
		 this(false);
	}
	
	
	/**
	 * This is the data access method that initializes the database with some events and questions.
	 * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	
	
	/**
	 * This method creates a question for an event, with a question text and the minimum bet
	 * 
	 * @param event to which question is added
	 * @param question text of the question
	 * @param betMinimum minimum quantity of the bet
	 * @return the created question, or null, or an exception
 	 * @throws QuestionAlreadyExist if the same question already exists for the event
	 */
	public Question createQuestion(Event event, String question, float betMinimum) throws  QuestionAlreadyExist {
		System.out.println(">> DataAccess: createQuestion=> event= "+event+" question= "+question+" betMinimum="+betMinimum);
		
			Event ev = db.find(Event.class, event.getEventNumber());
			
			if (ev.DoesQuestionExists(question)) throw new QuestionAlreadyExist(ResourceBundle.getBundle("Etiquetas").getString("ErrorQueryAlreadyExist"));
			
			db.getTransaction().begin();
			Question q = ev.addQuestion(question, betMinimum);
			//db.persist(q);
			db.persist(ev); // db.persist(q) not required when CascadeType.PERSIST is added in questions property of Event class
							// @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
			db.getTransaction().commit();
			return q;
		
	}
	
	
public void open(boolean initializeMode){
		
		System.out.println("Opening DataAccess instance => isDatabaseLocal: "+c.isDatabaseLocal()+" getDatabBaseOpenMode: "+c.getDataBaseOpenMode());

		String fileName=c.getDbFilename();
		if (initializeMode) {
			fileName=fileName+";drop";
			System.out.println("Deleting the DataBase");
		}
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<>();
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
	
	public void apustuaEzabatu(Registered user1, ApustuAnitza ap) {
		Registered user = (Registered) db.find(Registered.class, user1.getUsername());
		ApustuAnitza apustuAnitza = db.find(ApustuAnitza.class, ap.getApustuAnitzaNumber());
		db.getTransaction().begin();
		user.updateDiruKontua(apustuAnitza.getBalioa());
		Transaction t = new Transaction(user, apustuAnitza.getBalioa(), new Date(), "ApustuaEzabatu");
		user.addTransaction(t);
		db.persist(t);
		user.removeApustua(apustuAnitza);
		int i;
		for(i=0; i<apustuAnitza.getApustuak().size(); i++) {
			apustuAnitza.getApustuak().get(i).getKuota().removeApustua(apustuAnitza.getApustuak().get(i));
			Sport spo =apustuAnitza.getApustuak().get(i).getKuota().getQuestion().getEvent().getSport();
			spo.setApustuKantitatea(spo.getApustuKantitatea()-1);
		}
		db.remove(apustuAnitza);
		db.getTransaction().commit();
	}

	
	public boolean gertaeraEzabatu(Event ev) {
		Event event  = db.find(Event.class, ev); 
		List<Question> listQ = event.getQuestions(); 
		
		for(Question q : listQ) {
			if(q.getResult() == null) {
				return false; 
			}
		}
		if(new Date().compareTo(event.getEventDate())<0) {
			TypedQuery<Quote> Qquery = db.createQuery("SELECT q FROM Quote q WHERE q.getQuestion().getEvent().getEventNumber() =?1", Quote.class);
			Qquery.setParameter(1, event.getEventNumber()); 
			List<Quote> listQUO = Qquery.getResultList();
			for(int j=0; j<listQUO.size(); j++) {
				Quote quo = db.find(Quote.class, listQUO.get(j));
				for(int i=0; i<quo.getApustuak().size(); i++) {
					ApustuAnitza apustuAnitza = quo.getApustuak().get(i).getApustuAnitza();
					ApustuAnitza ap1 = db.find(ApustuAnitza.class, apustuAnitza.getApustuAnitzaNumber());
					db.getTransaction().begin();
					ap1.removeApustua(quo.getApustuak().get(i));
					db.getTransaction().commit();
					if(ap1.getApustuak().isEmpty() && !ap1.getEgoera().equals("galduta")) {
						this.apustuaEzabatu(ap1.getUser(), ap1);
					}else if(!ap1.getApustuak().isEmpty() && ap1.irabazitaMarkatu()){
						this.ApustuaIrabazi(ap1);
					}
					db.getTransaction().begin();
					Sport spo =quo.getQuestion().getEvent().getSport();
					spo.setApustuKantitatea(spo.getApustuKantitatea()-1);
					db.getTransaction().commit();
				}
			}
			
		}
		db.getTransaction().begin();
		db.remove(event);
		db.getTransaction().commit();
		return true; 
	}
	public void ApustuaIrabazi(ApustuAnitza apustua) {
		ApustuAnitza apustuAnitza = db.find(ApustuAnitza.class, apustua.getApustuAnitzaNumber());
		Registered reg = (Registered) apustuAnitza.getUser();
		Registered r = (Registered) db.find(Registered.class, reg.getUsername());
		db.getTransaction().begin();
		apustuAnitza.setEgoera("irabazita");
		Double d=apustuAnitza.getBalioa();
		for(Apustua ap: apustuAnitza.getApustuak()) {
			d = d*ap.getKuota().getQuote();
		}
		r.updateDiruKontua(d);
		r.setIrabazitakoa(r.getIrabazitakoa()+d);
		r.setZenbat(r.getZenbat()+1);
		Transaction t = new Transaction(r, d, new Date(), "ApustuaIrabazi"); 
		db.persist(t);
		db.getTransaction().commit();
	}
	
}