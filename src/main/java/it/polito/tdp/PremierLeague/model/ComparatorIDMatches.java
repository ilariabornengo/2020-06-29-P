package it.polito.tdp.PremierLeague.model;

import java.util.Comparator;

public class ComparatorIDMatches implements Comparator<Match> {

	@Override
	public int compare(Match o1, Match o2) {
		// TODO Auto-generated method stub
		return o1.matchID.compareTo(o2.matchID);
	}

}
