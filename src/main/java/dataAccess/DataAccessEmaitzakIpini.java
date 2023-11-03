package dataAccess;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import domain.ApustuAnitza;
import domain.Apustua;
import domain.Question;
import domain.Quote;
import domain.Registered;
import domain.Transaction;
import exceptions.EventNotFinished;

public class DataAccessEmaitzakIpini {

	protected static EntityManager  db;
	protected static EntityManagerFactory emf;
	
	ConfigXML c=ConfigXML.getInstance();

    public DataAccessEmaitzakIpini(boolean initializeMode)  {

		System.out.println("Creating DataAccess instance => isDatabaseLocal: "+c.isDatabaseLocal()+" getDatabBaseOpenMode: "+c.getDataBaseOpenMode());

		open(initializeMode);

	}

	public DataAccessEmaitzakIpini()  {	
		 this(false);
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
	
	public Collection<Quote> findQuote(Question question){
		TypedQuery<Quote> Qquery = db.createQuery("SELECT q FROM Quote q WHERE q.getQuestion() =?1 ",Quote.class);
		Qquery.setParameter(1, question);
		return Qquery.getResultList();
	}
	
	public void EmaitzakIpini(Quote quote) throws EventNotFinished{
		
		Quote q = db.find(Quote.class, quote); 
		String result = q.getForecast();
		
		if(new Date().compareTo(q.getQuestion().getEvent().getEventDate())<0)
			throw new EventNotFinished();

		Vector<Apustua> listApustuak = q.getApustuak();
		db.getTransaction().begin();
		Question que = q.getQuestion(); 
		Question question = db.find(Question.class, que); 
		question.setResult(result);
		markatuKuotak(question);
		db.getTransaction().commit();
		markatuIrabaziak(listApustuak);
	}

	private void markatuKuotak(Question question) {
		for(Quote quo: question.getQuotes()) {
			for(Apustua apu: quo.getApustuak()) {
				
				Boolean b=apu.galdutaMarkatu(quo);
				if(b) {
					apu.getApustuAnitza().setEgoera("galduta");
				}else {
					apu.setEgoera("irabazita");
				}
			}
		}
	}

	private void markatuIrabaziak(Vector<Apustua> listApustuak) {
		for(Apustua a : listApustuak) {
			db.getTransaction().begin();
			Boolean bool=a.getApustuAnitza().irabazitaMarkatu();
			db.getTransaction().commit();
			if(bool) {
				this.ApustuaIrabazi(a.getApustuAnitza());
			}
		}
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
	
	public void close(){
		db.close();
		System.out.println("DataBase closed");
	}
}
