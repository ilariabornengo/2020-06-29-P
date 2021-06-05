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
	int pesoMax=0;
	public Model()
	{
		this.dao=new PremierLeagueDAO();
	}
	
	public void creaGrafo(Integer mese,Integer min)
	{
		this.idMap=new HashMap<Integer,Match>();
		this.grafo=new SimpleWeightedGraph<Match,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//aggiungere i vertici
		this.dao.getVerices(idMap, mese);
		Graphs.addAllVertices(this.grafo, this.idMap.values());
		
		//aggiungere gli archi
		for(Adiacenza a:this.dao.listAdiacenze(idMap, mese, min))
		{
			if(this.grafo.vertexSet().contains(a.getM1())&& this.grafo.vertexSet().contains(a.getM2()))
			{
				Graphs.addEdge(this.grafo, a.getM1(), a.getM2(), a.getPeso());
			}
		}
	}
	
	public List<Adiacenza> getMigliori(Integer mese, Integer min)
	{
		Integer minBest=0;
		List<Adiacenza> best=new ArrayList<Adiacenza>();
		for(Adiacenza a:this.dao.listAdiacenze(idMap, mese, min))
		{
			if(a.getPeso()==minBest)
			{
				best.add(a);
			}
			else if(a.getPeso()>minBest)
			{
				best.clear();
				minBest=a.getPeso();
				best.add(a);
			}
		}
		return best;
	}
	
	public List<Match> listaBest(Match partenza,Match arrivo)
	{
		this.listaBest=new ArrayList<>();
		List<Match> parziale=new ArrayList<Match>();
		parziale.add(partenza);
		ricorsione(parziale,arrivo);
		pesoMax=0;
		return this.listaBest;
	}
	

	private void ricorsione(List<Match> parziale,Match arrivo) {
		Match ultimo=parziale.get(parziale.size()-1);
	
		//condizione di terminazione
		if(ultimo.equals(arrivo))
		{
			int peso=calcolaPeso(parziale);
			if(peso>pesoMax)
			{
				pesoMax=peso;
				this.listaBest=new ArrayList<Match>(parziale);
				return;
			}
			
		}
		//fuori dal caso di terminazione
		for(Match m:Graphs.neighborListOf(this.grafo, ultimo))
		{
			if((!m.getTeamAwayID().equals(ultimo.getTeamHomeID()) && !m.getTeamHomeID().equals(ultimo.getTeamAwayID())) && ((!m.getTeamAwayID().equals(ultimo.getTeamAwayID()))&& !m.getTeamHomeID().equals(ultimo.getTeamHomeID())))
			{
				if(!parziale.contains(m))
				{
				parziale.add(m);
				ricorsione(parziale,arrivo);
				parziale.remove(parziale.size()-1);
				}
			}
			
		}
		
	}

	private int calcolaPeso(List<Match> parziale) {
		Match m1;
		Match m2;
		Integer pesoTot=0;
		for(int i=0;i<parziale.size();i++)
		{
			m1=parziale.get(i);
			m2=parziale.get(i+1);
			pesoTot+=(int)this.grafo.getEdgeWeight(this.grafo.getEdge(m1, m2));
			
		}
		return pesoTot;
	}

	public List<Match> getListVertici()
	{
		List<Match> vertici=new ArrayList<Match>(this.grafo.vertexSet());
		return vertici;
	}
	
	public int getVertici()
	{
		return this.grafo.vertexSet().size();
	}
	
	public int getArchi()
	{
		return this.grafo.edgeSet().size();
	}
	
}
