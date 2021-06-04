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
	List<Match> percorsoBest;
	
	public Model()
	{
		this.dao=new PremierLeagueDAO();
	}
	
	public void creaGrafo(Integer mese,Integer tempoMin)
	{
		this.idMap=new HashMap<Integer,Match>();
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		this.dao.getVerices(idMap, mese);
		Graphs.addAllVertices(this.grafo, this.idMap.values());
		
		//aggiungo gli archi
		for(Adiacenza a:this.dao.getAdiacenze(idMap, tempoMin,mese))
		{
			if(this.grafo.vertexSet().contains(a.getM1())&& this.grafo.vertexSet().contains(a.getM2()))
			{
				Graphs.addEdge(this.grafo, a.getM1(), a.getM2(), a.getPeso());
			}
		}
	}
	
	public List<Adiacenza> getBest()
	{
		Double peso=Double.MIN_VALUE;
		List<Adiacenza> bestv=new ArrayList<Adiacenza>();
		Adiacenza best=null;
		for(DefaultWeightedEdge d:this.grafo.edgeSet())
		{
			if(best==null)
			{
				best=new Adiacenza(this.grafo.getEdgeSource(d),this.grafo.getEdgeTarget(d),this.grafo.getEdgeWeight(d));
				peso=this.grafo.getEdgeWeight(d);
				bestv.add(best);
			}
			else
			{
				if(this.grafo.getEdgeWeight(d)>peso)
				{
					bestv.clear();
					best=new Adiacenza(this.grafo.getEdgeSource(d),this.grafo.getEdgeTarget(d),this.grafo.getEdgeWeight(d));
					peso=this.grafo.getEdgeWeight(d);
					bestv.add(best);
				}else if(this.grafo.getEdgeWeight(d)==peso)
				{
					best=new Adiacenza(this.grafo.getEdgeSource(d),this.grafo.getEdgeTarget(d),this.grafo.getEdgeWeight(d));
					peso=this.grafo.getEdgeWeight(d);
					bestv.add(best);
				}
			}
		}
		return bestv;
	}
	
	public List<Match> lista(Match partenza,Match arrivo)
	{
		this.percorsoBest=null;
		List<Match> parziale=new ArrayList<Match>();
		parziale.add(partenza);
		ricorsione(parziale,1,arrivo);
		return percorsoBest;
	}
	
	public void ricorsione(List<Match> parziale,int livello,Match arrivo)
	{
		Match ultimo=parziale.get(parziale.size()-1);
		//caso limite
		if(ultimo.equals(arrivo))
		{
			if(percorsoBest==null)
			{
				this.percorsoBest=new ArrayList<Match>(parziale);
				return;
			}
			else if(parziale.size()>percorsoBest.size())
			{
				this.percorsoBest=new ArrayList<Match>(parziale);
				return;
			}
			else
			{
				return;
			}
		}
		for(Match m:Graphs.neighborListOf(this.grafo, ultimo))
		{
			if(!parziale.contains(m))
			{
				if((m.teamAwayID.equals(ultimo.teamHomeID) && m.teamHomeID.equals(ultimo.teamAwayID)) || (m.teamAwayID.equals(ultimo.teamAwayID) && m.teamHomeID.equals(ultimo.teamHomeID)))
				{
					//niente
				}else
				{   parziale.add(m);
					ricorsione(parziale,livello+1,arrivo);
					parziale.remove(m);
				}
			}
		}
	}
	
	public double getPesoCamminoMax()
	{
		double peso=0.0;
		List<Match> parziale=new ArrayList<Match>();
		for(Match m:this.percorsoBest)
		{
			if(parziale.size()==0)
			{
				parziale.add(m);
			}
			else
			{
				Match ultimo=parziale.get(parziale.size()-1);
				double pesoA=this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, m));
				peso+=pesoA;
				parziale.add(m);
			}
			
		}
		return peso;
	}
	
	public List<Match> getMatches()
	{
		List<Match> partite=new ArrayList<Match>();
		for(Match m:this.grafo.vertexSet())
		{
			partite.add(m);
		}
		Collections.sort(partite, new ComparatorIDMatches());
		return partite;
	}
	public int getNArchi()
	{
		return this.grafo.edgeSet().size();
	}
	public int getNVertici()
	{
		return this.grafo.vertexSet().size();
	}
}
