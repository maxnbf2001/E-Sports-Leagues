import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class Driver {

	public static void main(String[] args) throws IOException
	{
		Scanner scan = new Scanner (new File ("games.txt"));
		final int NUM_TEAMS = 11;

		// creates a list of NUM_TEAMS teams with no information (consider making a Standings class)
		Team[] teams = new Team[NUM_TEAMS];
		for (int i = 0; i < teams.length; i++)
		{
			String teamName = scan.nextLine();
			teams[i] = new Team (teamName);
		}

		// creates a list of (NUM_TEAMS-1)*2 gameweeks with no information (consider making a Seasons class)
		GameWeek[] season = new GameWeek[(NUM_TEAMS-1)*2];
		scan.nextLine();
		for (int i = 0; i < season.length; i++)
		{
			String week = scan.nextLine();
			season[i] = new GameWeek(i+1);

			for (int j = 0; j < NUM_TEAMS/2; j++)
			{
				String g = scan.nextLine();
				if (g.indexOf("-") == -1) //if the game has not been played
				{
					String homeT = g.substring(0, g.indexOf("vs") -1);
					String awayT = g.substring(g.indexOf("vs") +2, g.length());
					Game game = new Game(findTeam(homeT, teams), findTeam(awayT, teams));
					season[i].addGame(game, j);
				}
				else //if the game has been played
				{
					String homeT = g.substring(0, g.indexOf("-") -2);
					String awayT = g.substring(g.indexOf("-") +3, g.length());
					int homeG = Integer.parseInt(g.substring(g.indexOf("-")-1, g.indexOf("-")));
					int awayG = Integer.parseInt(g.substring(g.indexOf("-")+1, g.indexOf("-")+2));
					Game game = new Game(findTeam (homeT, teams), findTeam (awayT, teams), homeG, awayG);
					season[i].addGame(game, j);

					Team HT = findTeam (homeT, teams);
					Team AT = findTeam (awayT, teams);
					if (homeG > awayG)
					{
						HT.addWin(homeG, awayG);
						AT.addLoss(awayG, homeG);
					}
					else if (homeG == awayG)
					{
						HT.addDraw(homeG, awayG);
						AT.addDraw(awayG, homeG);
					}
					else if (awayG > homeG)
					{
						HT.addLoss(homeG, awayG);
						AT.addWin(awayG, homeG);
					}

				}
			}
			scan.nextLine();
		}
		
		order(teams);
		System.out.println("Team\tPlayed\tWins\tDraws\tLoss\tGF\tGA\tGD\tPoints");
		for (int i = 0; i < teams.length; i++)
		{
			System.out.println(teams[i]);
		}

		// creates JSON file
		JSONObject allData = new JSONObject();
		JSONArray teamList = new JSONArray();
		
		for (int i = 0; i < teams.length; i++)
		{
			JSONObject team = new JSONObject();
			team.put("Rank", (i+1));
			team.put("Games Played", (teams[i].getWins() + teams[i].getDraws() + teams[i].getLosses()));
			team.put("Name", teams[i].getName());
			team.put("Wins", teams[i].getWins());
			team.put("Draws", teams[i].getDraws());
			team.put("Losses", teams[i].getLosses());
			team.put("GF", teams[i].getGF());
			team.put("GA", teams[i].getGA());
			team.put("GD", teams[i].getGD());
			team.put("Points", teams[i].getPoints());
			System.out.println(team.toString());
			teamList.add(team);
		}
		allData.put("standings", teamList);
		try(FileWriter file = new FileWriter("C:\\Users\\maxnb\\OneDrive\\Documents\\GitHub\\E-Sports-Leagues\\E-Sports_HTML\\info.js"))
		{
			file.write("var list = " + allData.toString().substring(13, allData.toString().length()-1) + ";");
			file.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		System.out.println(allData);
		

	}
	private static void order(Team[] array) {

		ArrayList<Team> arrTemp = new ArrayList<Team>();
		for (int i = 0; i < array.length; i++)
		{
			arrTemp.add(array[i]);
		}
	    Collections.sort(arrTemp, new Comparator() {

	        public int compare(Object o1, Object o2) {

	            Integer x1 = ((Team) o1).getPoints();
	            Integer x2 = ((Team) o2).getPoints();
	            int sComp = x2.compareTo(x1);

	            if (sComp != 0) {
	               return sComp;
	            } 
	            

	            Integer x3 = ((Team) o1).getGD();
	            Integer x4 = ((Team) o2).getGD();
	            sComp = x4.compareTo(x3);
	            
	            if (sComp != 0) {
	            	return sComp;
	            }
	            
	            Integer x5 = ((Team) o1).getGF();
	            Integer x6 = ((Team) o2).getGF();
	            return x6.compareTo(x5);
	    }});
	    
	    for (int i = 0; i < array.length; i++)
	    {
	    	array[i] = arrTemp.get(i);
	    }
	}

	public static Team findTeam (String name, Team[] lot)
	{
		for (int i = 0; i < lot.length; i++)
		{
			if (name.equals(lot[i].getName()))
			{
				return lot[i];
			}
		}
		return new Team();
	}

}
