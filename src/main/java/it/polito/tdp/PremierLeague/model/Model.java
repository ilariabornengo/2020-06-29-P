package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Map<Integer,Match> idMap;
	Graph<Match,DefaultWeightedEdge> grafo;
	List<Match> listaBest;
	public int pesoMax;
	
	public Model()
	{
		this.dao=new PremierLeagueDAO();
	}
	
	public void creaGrafo(Integer mese,Integer min)
	{
		this.idMap=new HashMap<Integer,Match>();
		this.grafo=new SimpleWeightedGraph<Match,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		this.dao.getVertici(idMap, mese);
		Graphs.addAllVertices(this.grafo, this.idMap.values());
		
		//aggiungo gli archi
		for(Adiacenza a:this.dao.getAdiacenze(idMap, mese, min))
		{
			if(this.grafo.vertexSet().contains(a.getM1()) && this.grafo.vertexSet().contains(a.getM2()))
			{
				Graphs.addEdge(this.grafo, a.getM1(), a.getM2(), a.getPeso());
			}
		}
	}
	
	public List<Adiacenza> migliori(Integer mese,Integer min)
	{
		List<Adiacenza> migliori=new ArrayList<Adiacenza>();
		int pesoMax=0;
		for(Adiacenza a:this.dao.getAdiacenze(idMap, mese, min))
		{
			if(a.getPeso()>pesoMax)
			{
				migliori.clear();
				pesoMax=a.getPeso();
				migliori.add(a);
			}
			else if(a.getPeso()==pesoMax)
			{
				migliori.add(a);
			}else
			{
				//
			}
		}
		return migliori;
	}
	public List<Match> getPartite()
	{
		List<Match> partite=new ArrayList<Match>(this.grafo.vertexSet());
		return partite;
	}
	public int getArchi()
	{
		return this.grafo.edgeSet().size();
	}
	public int getVertici()
	{
		return this.grafo.vertexSet().size();
	}
	
	public List<Match> getListaBest(Match partenza,Match arrivo)
	{
		this.listaBest=new ArrayList<Match>();
		List<Match> parziale=new ArrayList<Match>();
		this.pesoMax=0;
		parziale.add(partenza);
		ricorsione(parziale,arrivo);
		return this.listaBest;
	}

	private void ricorsione(List<Match> parziale, Match arrivo) {
		Match ultimo=parziale.get(parziale.size()-1);
		if(ultimo.equals(arrivo))
		{
			int pesoP=calcolaPeso(parziale);
			if(pesoP>this.pesoMax)
			{
				this.pesoMax=pesoP;
				this.listaBest=new ArrayList<Match>(parziale);
				return;
			}
		}
		//fuori dal caso terminale
		for(Match m:Graphs.neighborListOf(this.grafo, ultimo))
		{
			Integer ultimoID1=ultimo.getTeamHomeID();
			Integer ultimoID2=ultimo.getTeamAwayID();
			Integer mID1=m.getTeamHomeID();
			Integer mID2=m.getTeamAwayID();
			if(ultimoID1.equals(mID1) && ultimoID2.equals(mID2))
			{
				//non va bene
			}else if(ultimoID1.equals(mID2) && ultimoID2.equals(mID1))
			{
				//non va bene
			}else
			{
				if(!parziale.contains(m))
				{
					parziale.add(m);
					ricorsione(parziale,arrivo);
					parziale.remove(m);
				}
			}
		}
	}

	private int calcolaPeso(List<Match> parziale) {
		int pesoTot=0;
		for(int i=1;i<parziale.size();i++)
		{
			Match m1=parziale.get(i-1);
			Match m2=parziale.get(i);
			int pesoA=(int) this.grafo.getEdgeWeight(this.grafo.getEdge(m1, m2));
			pesoTot+=pesoA;
		}
		return pesoTot;
	}
}
