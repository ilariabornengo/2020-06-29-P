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
	public int massimo;
	
	public Model()
	{
		this.dao=new PremierLeagueDAO();
	}
	
	public void creaGrafo(int mese, int min)
	{
		this.idMap=new HashMap<Integer,Match>();
		this.grafo=new SimpleWeightedGraph<Match,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		this.dao.getVertcici(idMap, mese);
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

	public List<Match> vertici()
	{
		List<Match> vertici=new ArrayList<Match>(this.grafo.vertexSet());
		return vertici;
	}
	public List<Adiacenza> migliori(Integer mese, Integer min)
	{
		Integer minBest=0;
		List<Adiacenza> best=new ArrayList<Adiacenza>();
		for(Adiacenza a:this.dao.getAdiacenze(idMap, mese, min))
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
	
	public List<Match> getBest(Match partenza, Match arrivo)
	{
		this.listaBest=new ArrayList<Match>();
		List<Match> parziale=new ArrayList<Match>();
		parziale.add(partenza);
		this.massimo=0;
		ricorsione(parziale,arrivo);
		return this.listaBest;
	}
	private void ricorsione(List<Match> parziale, Match arrivo) {
		Match ultimo=parziale.get(parziale.size()-1);
		//caso terminale
		if(ultimo.equals(arrivo))
		{
			int pesoParz=calcolaPeso(parziale);
			if(pesoParz>this.massimo)
			{
				this.massimo=pesoParz;
				this.listaBest=new ArrayList<Match>(parziale);
				return;
			}
			
		}
		//al di fuori del caso terminale
		for(Match m:Graphs.neighborListOf(this.grafo, ultimo))
		{
			if((m.getTeamAwayID().equals(ultimo.getTeamHomeID()) && m.getTeamHomeID().equals(ultimo.getTeamAwayID())) || ((m.getTeamAwayID().equals(ultimo.getTeamAwayID()))&& m.getTeamHomeID().equals(ultimo.getTeamHomeID())))
			{
			}
			else
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
		int pesoFin=0;
		for(int i=1; i<parziale.size();i++)
		{
			Match m1=parziale.get(i-1);
			Match m2=parziale.get(i);
			int pesoA=(int) this.grafo.getEdgeWeight(this.grafo.getEdge(m1, m2));
			pesoFin+=pesoA;
		}
		return pesoFin;
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
