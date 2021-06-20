package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	
	public void getVertcici(Map<Integer,Match> idMap, int mese){
		String sql = "SELECT m.MatchID AS id, m.TeamHomeID AS homeID, m.TeamAwayID AS awayID, m.TeamHomeFormation AS fH, m.TeamAwayFormation AS fA, m.ResultOfTeamHome AS res, m.Date AS data, t1.Name AS nomeH, t2.Name AS nomeA "
				+ "FROM matches m, teams t1, teams t2 "
				+ "WHERE month(m.Date)=? "
				+ "AND m.TeamHomeID=t1.TeamID "
				+ "AND m.TeamAwayID=t2.TeamID " ;
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet res = st.executeQuery();
			while (res.next()) {
			if(!idMap.containsKey(res.getInt("id")))
			{
				
				Match match = new Match(res.getInt("id"), res.getInt("homeID"), res.getInt("awayID"), res.getInt("fH"), 
							res.getInt("fA"),res.getInt("res"), res.getTimestamp("data").toLocalDateTime(), res.getString("nomeH"),res.getString("nomeA"));
				
				idMap.put(match.getMatchID(), match);
			}
			
			conn.close();
			
			}} catch (SQLException e) {
			e.printStackTrace();
	
			
	}
}
	
	public List<Adiacenza> getAdiacenze(Map<Integer,Match> idMap, Integer mese, Integer min){
		String sql = "SELECT a1.MatchID AS id1, a2.MatchID AS id2, COUNT( distinct a1.PlayerID) AS peso "
				+ "FROM matches m1, matches m2, actions a1,actions a2 "
				+ "WHERE m1.MatchID=a1.MatchID AND m2.MatchID=a2.MatchID "
				+ "AND month(m1.Date)=MONTH(m2.Date) "
				+ "AND month(m1.Date)=? "
				+ "AND a1.PlayerID=a2.PlayerID "
				+ "AND a1.MatchID> a2.MatchID "
				+ "AND a1.TimePlayed>=? AND a2.TimePlayed>=? "
				+ "GROUP BY a1.MatchID, a2.MatchID "
				+ "";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setInt(2, min);
			st.setInt(3, min);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("id1")) && idMap.containsKey(res.getInt("id2")))
				{
					Adiacenza a=new Adiacenza(idMap.get(res.getInt("id1")),idMap.get(res.getInt("id2")),res.getInt("peso"));
					result.add(a);
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
		
