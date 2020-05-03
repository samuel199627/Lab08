package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	//identity MAP
	private Map<Integer,Airline> compagnie;
	private Map<Integer,Airport> aereoporti;
	
	
	private List<Airline> compagnieLista;
	private List<Airport> aereoportiLista;
	private List<CoppiaAereoporti> connessioni;
	
	//per grafi pesati i lati sono quelli indicati qui sotto
	private Graph<Airport,DefaultWeightedEdge> grafo;
	
	private ExtFlightDelaysDAO flightDAO;
	
	public Model() {
		flightDAO = new ExtFlightDelaysDAO();
	}
	
	public Graph<Airport,DefaultWeightedEdge> creaGrafo(double distanzaMediaMinima) {
		//grafo semplice, non orientato, pesato
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		compagnie= new HashMap<>();
		aereoporti= new HashMap<>();
		
		compagnieLista=new ArrayList<>();
		aereoportiLista=new ArrayList<>();
		
		compagnieLista=this.loadAllAirlines();
		aereoportiLista=this.loadAllAirports();
		
		for(Airline a:compagnieLista) {
			compagnie.put(a.getId(), a);
		}
		for(Airport a: aereoportiLista) {
			aereoporti.put(a.getId(), a);
		}
		
		connessioni=new ArrayList<>();
		
		connessioni=this.loadAllConnectionsAirports(aereoporti);
		
		//ora ho tutto quello che mi serve per creare il grafo in quanto ho tutte le connessioni e i vertici
		//anche se le connessioni le ho ancora in entrambe le direzioni quindi dobbiamo completarle
		//System.out.println("IMPORTATO TUTTO: \n");
		//System.out.println("AEREOPORTI: "+aereoporti.size());
		//System.out.println("CONNESSIONI UNILATERALI: "+connessioni.size());
		
		//per prima cosa dobbiamo inserire tutti i vertici nel grafo
		//i vertici devono essere in una lista e non in una mappa
		Graphs.addAllVertices(grafo, aereoportiLista);
		//System.out.println("\n\nCREATO GRAFO CON I VERTICI: \n");
		//System.out.println("NUMERO VERTICI: "+grafo.vertexSet().size());
		
		//stampo il grafo con solo i vertici e abbiamo il toString dei singoli aereoporti
		//System.out.println("STAMPA GRAFO: "+grafo.toString());
		
		//faccio prove per aggiunta di lati
		/*
		System.out.println("PRIMA CONNESSIONE: \n");
		System.out.println("PARTENZA: "+connessioni.get(0).getP());
		System.out.println("ARRIVO: "+connessioni.get(0).getA());
		Graphs.addEdge(grafo, connessioni.get(0).getP(), connessioni.get(0).getA(), 1);
		//System.out.println("STAMPA GRAFO: "+grafo.toString());
		System.out.println("LATI: \n"+grafo.getEdgeWeight(grafo.getEdge(connessioni.get(0).getP(), connessioni.get(0).getA()))); //1.0
		System.out.println("CONTIENE: "+grafo.containsEdge(connessioni.get(0).getP(), connessioni.get(0).getA())); //true
		System.out.println("CONTIENE AL CONTRARIO: "+grafo.containsEdge(connessioni.get(0).getA(), connessioni.get(0).getP())); //true
		Graphs.addEdge(grafo, connessioni.get(1).getP(), connessioni.get(1).getA(), 4/3);
		*/
		
		//creo delle variabili globali per ambo le direzioni per la coppia
		//int contaExt=0;
		//int contaInt=0;
		int numVoliTot;
		int distanzaTotale;
		double distanzaMedia;
		for(CoppiaAereoporti c1: connessioni) {
			numVoliTot=0;
			distanzaTotale=0;
			//contaInt=0;
			//contaExt++;
			//controllo che non ci sia gia' un arco che unisce quei due vertici perche' significa che lo avrei gia' esaminato
			if(!grafo.containsEdge(c1.getP(), c1.getA())) {
				for(CoppiaAereoporti c2:connessioni) {
					//contaInt++;
					//controllo se sono con la coppia inversa
					if(c1.getP().equals(c2.getA())&&c1.getA().equals(c2.getP())) {
						numVoliTot=c1.getNumVoli()+c2.getNumVoli();
						distanzaTotale=c1.getDistanzaTotVoli()+c2.getDistanzaTotVoli();
						//System.out.println(""+contaExt+ " "+contaExt);
						distanzaMedia=((double) distanzaTotale)/numVoliTot;
						if(distanzaMedia>=distanzaMediaMinima) {
							Graphs.addEdge(grafo, c1.getP(), c1.getA(), distanzaMedia);
						}
						
					}
				}
			}
			
		}
		/*
		System.out.println("INFORMAZIONI GRAFO: \n");
		System.out.println("VERTICI: "+grafo.vertexSet().size());
		System.out.println("LATI: "+grafo.edgeSet().size());
		*/
		//System.out.println("STAMPA GRAFO: "+grafo.toString());
		//controllo un lato
		/*
		System.out.println("LATI: \n"+grafo.getEdgeWeight(grafo.getEdge(aereoporti.get(78), aereoporti.get(174)))); //1678.0
		System.out.println("LATI: \n"+grafo.getEdgeWeight(grafo.getEdge(aereoporti.get(174), aereoporti.get(78))));  //1678.0
		System.out.println("LATI: \n"+grafo.getEdgeWeight(grafo.getEdge(aereoporti.get(0), aereoporti.get(20)))); //692.0 
		*/
		//quando il lato non esiste si scatena un eccezione se provo ad estrarre il peso del lato
		//System.out.println("CONTIENE: "+grafo.containsEdge(aereoporti.get(20), aereoporti.get(1))); //false
		//System.out.println("LATI: \n"+grafo.getEdgeWeight(grafo.getEdge(aereoporti.get(20), aereoporti.get(1)))); 
		
		/*
		for(DefaultWeightedEdge e:grafo.edgeSet()) {
			System.out.println("Partenza: "+grafo.getEdgeSource(e)+" Arrivo:"+grafo.getEdgeTarget(e)+" Peso: "+grafo.getEdgeWeight(e));
		}
		*/
		
		return grafo;
	
	}
	
	
	public List<Airline> loadAllAirlines(){
		return flightDAO.loadAllAirlines();
	}
	
	public List<Airport> loadAllAirports(){
		return flightDAO.loadAllAirports();
	}
	
	public List<CoppiaAereoporti> loadAllConnectionsAirports(Map<Integer,Airport> aereoporti){
		return flightDAO.loadAllConnectionsAirports(aereoporti);
	}

	

}
