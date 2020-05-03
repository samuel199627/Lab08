package it.polito.tdp.extflightdelays.model;

//classe che tiene le connessioni unidirezionali tra gli aereoporti in quanto non sono stato in grado di creare una query unica per
//poter unire già le due direzioni

public class CoppiaAereoporti {
	
	private Airport p;
	private Airport a;
	private int numVoli;
	private int distanzaTotVoli;
	
	
	public CoppiaAereoporti(Airport p, Airport a, int numVoli, int distanzaTotVoli) {
		super();
		this.p = p;
		this.a = a;
		this.numVoli = numVoli;
		this.distanzaTotVoli = distanzaTotVoli;
	}


	public Airport getP() {
		return p;
	}


	public void setP(Airport p) {
		this.p = p;
	}


	public Airport getA() {
		return a;
	}


	public void setA(Airport a) {
		this.a = a;
	}


	public int getNumVoli() {
		return numVoli;
	}


	public void setNumVoli(int numVoli) {
		this.numVoli = numVoli;
	}


	public int getDistanzaTotVoli() {
		return distanzaTotVoli;
	}


	public void setDistanzaTotVoli(int distanzaTotVoli) {
		this.distanzaTotVoli = distanzaTotVoli;
	}
	
	
	
	
	

}
