import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class Driver {

	public static void main(String[] args) throws IOException
	{
		Scanner scan = new Scanner (new File ("games.txt"));
		final int NUM_TEAMS = 10;
		JSONObject allData = new JSONObject();
		JSONArray leagues = new JSONArray();
		
		//bulds the data for both the gold and blue league
		for (int l = 0; l < 2; l++)
		{
			String league = scan.nextLine();
			
			// creates a list of NUM_TEAMS teams with no information
			Team[] teams = new Team[NUM_TEAMS];
			for (int i = 0; i < teams.length; i++)
			{
				String teamName = scan.nextLine();
				teams[i] = new Team (teamName);
			}
			
			
			JSONArray fixtures = new JSONArray();
			JSONArray splitFixtures = new JSONArray ();
			
			// creates a list of (NUM_TEAMS-1)*2 gameweeks with no information (consider making a Seasons class)
			GameWeek[] season = new GameWeek[(NUM_TEAMS-1)*2];
			
			scan.nextLine();
			
			// loops through the entire season and reads in the games for each week
			for (int i = 0; i < season.length; i++)
			{
				String week = scan.nextLine();
				season[i] = new GameWeek(i+1);
				JSONArray weeklyGames = new JSONArray();
				
				// reads in the games for each week
				for (int j = 0; j < NUM_TEAMS/2; j++)
				{
					
					String g = scan.nextLine();
					weeklyGames.add(g);
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

				splitFixtures.add(weeklyGames);
				if (splitFixtures.size() == 6)
				{
					fixtures.add(splitFixtures);
					splitFixtures = new JSONArray ();
				}
				scan.nextLine();
			}
			fixtures.add(splitFixtures);
			

			order(teams);

			JSONArray teamList = new JSONArray();
			JSONObject lginfo = new JSONObject ();
			for (int i = 0; i < teams.length; i++)
			{
				JSONObject team = new JSONObject();
				team.put("rank", (i+1));
				team.put("played", (teams[i].getWins() + teams[i].getDraws() + teams[i].getLosses()));
				team.put("name", teams[i].getName());
				team.put("wins", teams[i].getWins());
				team.put("draws", teams[i].getDraws());
				team.put("losses", teams[i].getLosses());
				team.put("gf", teams[i].getGF());
				team.put("ga", teams[i].getGA());
				team.put("gd", teams[i].getGD());
				team.put("points", teams[i].getPoints());
				//System.out.println(team.toString());
				teamList.add(team);
			}
			lginfo.put("teams", teamList);
			lginfo.put("fixtures", fixtures);
			allData.put(league, lginfo);
			
		}
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		String lastUpdated = dateFormat.format(date);
		allData.put("date", lastUpdated);
		
		try(FileWriter file = new FileWriter("C:\\Users\\maxnb\\OneDrive\\Documents\\GitHub\\E-Sports-Leagues\\E-Sports_HTML\\info.js"))
		{
			file.write("var websiteData = " + allData.toString() + ";");
			file.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		if (scan.hasNext())
			scan.nextLine();


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
