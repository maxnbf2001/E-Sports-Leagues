import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
		final int NUM_TEAMS = 8;
		JSONObject FIFAData = new JSONObject();
		JSONObject NBAData = new JSONObject();
		JSONObject NHLData = new JSONObject();
		JSONObject allData = new JSONObject();

		// -------------------------------------------------------------------------
		//  READS IN THE FIFA DATA
		// -------------------------------------------------------------------------

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
			GameWeek[] season = new GameWeek[(NUM_TEAMS-1)];

			scan.nextLine();

			// loops through the entire season and reads in the games for each week
			for (int i = 0; i < season.length; i++)
			{
				String week = scan.nextLine();
				season[i] = new GameWeek(i+1);
				JSONArray weeklyGames = new JSONArray();

				// reads in the games for each week
				for (int j = 0; j < NUM_TEAMS; j++)
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

						//finds the team with the given name and assigns them a win, draw, or loss
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
				//makes sub arrays of the fixtures for formatting purposes on the html
				if (splitFixtures.size() == 6)
				{
					fixtures.add(splitFixtures);
					splitFixtures = new JSONArray ();
				}
				scan.nextLine();
			}
			fixtures.add(splitFixtures);

			//sorts the teams by the given conditions
			orderFIFA(teams);
			
			//makes every team into a JSON object and adds it into a JSONArray of teams
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
			FIFAData.put(league, lginfo);

		}



		// -------------------------------------------------------------------------
		//  READS IN THE NBA DATA
		// -------------------------------------------------------------------------

		String league = scan.nextLine();
		final int numNBATeams = 10;
		// creates a list of NUM_TEAMS teams with no information
		NBATeam[] NBAteams = new NBATeam[numNBATeams];
		for (int i = 0; i < NBAteams.length; i++)
		{
			String teamName = scan.nextLine();
			NBAteams[i] = new NBATeam(teamName);
		}
		scan.nextLine();
		JSONArray fixtures = new JSONArray();
		JSONArray splitFixtures = new JSONArray ();

		int numWeeks = (numNBATeams-1);
		
		//loops through all of the weeks
		for (int i = 0; i < numWeeks; i++)
		{
			String week = scan.nextLine();
			JSONArray weeklyGames = new JSONArray();
			
			//loops through all of the games in a week
			for (int j = 0; j < numNBATeams; j++)
			{
				String game = scan.nextLine();
				weeklyGames.add(game);
				
				//handles the game if it has been played
				if (game.indexOf("-") != -1)
				{
					//potential home points
					String PHP = game.substring(game.indexOf("-")-3, game.indexOf("-"));
					//potential away points
					String PAP = game.substring(game.indexOf("-")+1, game.indexOf("-")+4);
					int awayPoints = 0, homePoints = 0;

					// determines if the home/away points are 3 digits or 2 digits and assigns them accordingly
					if (Character.isDigit(PHP.charAt(0)))
						homePoints = Integer.parseInt(PHP);
					else
						homePoints = Integer.parseInt(PHP.substring(1));

					if (Character.isDigit(PAP.charAt(2)))
						awayPoints = Integer.parseInt(PAP);
					else
						awayPoints = Integer.parseInt(PAP.substring(0,2));

					String homeT = "";
					String awayT = "";

					// determines the name of the home and away team
					if (homePoints < 100)
						homeT = game.substring(0, game.indexOf("-") -3);
					else
						homeT = game.substring(0,game.indexOf("-") - 4);

					if (awayPoints < 100)
						awayT = game.substring(game.indexOf("-") +4, game.length());
					else
						awayT = game.substring(game.indexOf("-") +5, game.length());

					// find the team with the given name, and assigns them a win or loss
					NBATeam HT = findNBATeam (homeT, NBAteams);
					NBATeam AT = findNBATeam (awayT, NBAteams);
					if (homePoints > awayPoints)
					{
						HT.addWin(homePoints, awayPoints);
						AT.addLoss(awayPoints, homePoints);
					}
					else if (awayPoints > homePoints)
					{
						HT.addLoss(homePoints, awayPoints);
						AT.addWin(awayPoints, homePoints);
					}

				}
			}
			
			splitFixtures.add(weeklyGames);
			
			//makes sub arrays of the fixtures for formatting purposes on the html
			if (splitFixtures.size() == 6)
			{
				fixtures.add(splitFixtures);
				splitFixtures = new JSONArray ();
			}
			scan.nextLine();
		}
		fixtures.add(splitFixtures);
		
		//sorts the nba teams based on the desired conditions
		orderNBA (NBAteams);
		
		//calculates how many games behind the firt team each other team is
		gamesBehind (NBAteams);

		//makes a json array of all of the teams data
		JSONArray teamList = new JSONArray();
		for (int i = 0; i < NBAteams.length; i++)
		{
			JSONObject team = new JSONObject();
			team.put("rank", (i+1));
			team.put("name", NBAteams[i].getName());
			team.put("wins", NBAteams[i].getWins());
			team.put("losses", NBAteams[i].getLosses());
			team.put("pct", NBAteams[i].getPCT());
			team.put("gb", NBAteams[i].getGB());
			team.put("strk", NBAteams[i].getStrk());
			team.put("lastFive", NBAteams[i].getLastFive());
			team.put("pf", NBAteams[i].getPF());
			team.put("pa", NBAteams[i].getPA());
			team.put("pd", NBAteams[i].getPF() - NBAteams[i].getPA());
			teamList.add(team);
		}

		NBAData.put("teams", teamList);
		NBAData.put("fixtures", fixtures);
		
		// -------------------------------------------------------------------------
		//  READS IN THE NHL DATA
		// -------------------------------------------------------------------------

		league = scan.nextLine();
		final int numNHLTeams = 8;
		// creates a list of NUM_TEAMS teams with no information
		NHLTeam[] NHLteams = new NHLTeam[numNHLTeams];
		for (int i = 0; i < NHLteams.length; i++)
		{
			String teamName = scan.nextLine();

			NHLteams[i] = new NHLTeam(teamName);
		}
		scan.nextLine();
		fixtures = new JSONArray();
		splitFixtures = new JSONArray ();

		numWeeks = (numNHLTeams-1);
		
		//loops through all of the weeks
		for (int i = 0; i < numWeeks; i++)
		{
			String week = scan.nextLine();
			JSONArray weeklyGames = new JSONArray();
			
			//loops through all of the games in a week
			for (int j = 0; j < numNHLTeams; j++)
			{
				String game = scan.nextLine();
				weeklyGames.add(game);
				
				//handles the game if it has been played
				if (game.indexOf("-") != -1)
				{
					String homeT = game.substring(0, game.indexOf("-") -2);
					String awayT = game.substring(game.indexOf("-") +3, game.length());

					int homeG = Integer.parseInt(game.substring(game.indexOf("-")-1, game.indexOf("-")));
					int awayG = Integer.parseInt(game.substring(game.indexOf("-")+1, game.indexOf("-")+2));

					
					//finds the team with the given name and assigns them a win, draw, or loss
					NHLTeam HT = findNHLTeam (homeT, NHLteams);
					NHLTeam AT = findNHLTeam (awayT, NHLteams);
					if (homeG > awayG)
					{
						HT.addWin(homeG, awayG);
						AT.addLoss(awayG, homeG);
					}
					else if (awayG > homeG)
					{
						HT.addLoss(homeG, awayG);
						AT.addWin(awayG, homeG);
					}
				}
			}

			splitFixtures.add(weeklyGames);
			
			//makes sub arrays of the fixtures for formatting purposes on the html
			if (splitFixtures.size() == 6)
			{
				fixtures.add(splitFixtures);
				splitFixtures = new JSONArray ();
			}
			scan.nextLine();
		}
		fixtures.add(splitFixtures);
		
		//sorts the nba teams based on the desired conditions
		orderNHL (NHLteams);
	
		//makes a json array of all of the teams data
		teamList = new JSONArray();
		for (int i = 0; i < NHLteams.length; i++)
		{
			JSONObject team = new JSONObject();
			team.put("rank", (i+1));
			team.put("name", NHLteams[i].getName());
			team.put("wins", NHLteams[i].getWins());
			team.put("losses", NHLteams[i].getLosses());
			team.put("pct", NHLteams[i].getPCT());
			team.put("strk", NHLteams[i].getStrk());
			team.put("lastFive", NHLteams[i].getLastFive());
			team.put("gf", NHLteams[i].getGF());
			team.put("ga", NHLteams[i].getGA());
			team.put("gd", NHLteams[i].getGF() - NHLteams[i].getGA());
			teamList.add(team);
		}

		NHLData.put("teams", teamList);
		NHLData.put("fixtures", fixtures);

		
		
		
		
		
		//date of the most recent build
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		String lastUpdated = dateFormat.format(date);
		
		//puts all of the data in the final json object
		allData.put("fifaData", FIFAData);
		allData.put("nbaData", NBAData);
		allData.put("nhlData", NHLData);
		allData.put("date", lastUpdated);

		//writes the data as a variable to a js file 
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
	private static void gamesBehind (NBATeam[] arr)
	{
		for (int i = 1; i < arr.length; i++)
			arr[i].setGB(((arr[0].getWins() - arr[0].getLosses()) - (arr[i].getWins() - arr[i].getLosses()))/2.0 +"");

	}
	private static void orderFIFA(Team[] array) 
	{

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

	private static void orderNBA (NBATeam[] array)
	{

		ArrayList<NBATeam> arrTemp = new ArrayList<NBATeam>();
		for (int i = 0; i < array.length; i++)
		{
			arrTemp.add(array[i]);
		}
		Collections.sort(arrTemp, new Comparator() {

			public int compare(Object o1, Object o2) {

				Integer x1 = (int) (1000*Double.parseDouble(((NBATeam) o1).getPCT()));
				Integer x2 = (int) (1000*Double.parseDouble(((NBATeam) o2).getPCT()));
				int sComp = x2.compareTo(x1);

				if (sComp != 0) {
					return sComp;
				} 


				Integer x3 = ((NBATeam) o1).getLosses();
				Integer x4 = ((NBATeam) o2).getLosses();
				sComp =  x3.compareTo(x4);

				if (sComp != 0) {
					return sComp;
				} 

				Integer x5 = ((NBATeam) o1).getWins();
				Integer x6 = ((NBATeam) o2).getWins();
				
				if (sComp != 0) {
					return sComp;
				} 
				
				Integer x7 = ((NBATeam) o1).getPD();
				Integer x8 = ((NBATeam) o2).getPD();
				
				return x8.compareTo(x7);
			}});

		for (int i = 0; i < array.length; i++)
		{
			array[i] = arrTemp.get(i);
		}
	}

	private static void orderNHL (NHLTeam[] array)
	{

		ArrayList<NHLTeam> arrTemp = new ArrayList<NHLTeam>();
		for (int i = 0; i < array.length; i++)
		{
			arrTemp.add(array[i]);
		}
		Collections.sort(arrTemp, new Comparator() {

			public int compare(Object o1, Object o2) {

				Integer x1 = (int) (1000*Double.parseDouble(((NHLTeam) o1).getPCT()));
				Integer x2 = (int) (1000*Double.parseDouble(((NHLTeam) o2).getPCT()));
				int sComp = x2.compareTo(x1);

				if (sComp != 0) {
					return sComp;
				} 


				Integer x3 = ((NHLTeam) o1).getLosses();
				Integer x4 = ((NHLTeam) o2).getLosses();
				sComp =  x3.compareTo(x4);

				if (sComp != 0) {
					return sComp;
				} 

				Integer x5 = ((NHLTeam) o1).getWins();
				Integer x6 = ((NHLTeam) o2).getWins();
				
				if (sComp != 0) {
					return sComp;
				} 
				
				Integer x7 = ((NHLTeam) o1).getGD();
				Integer x8 = ((NHLTeam) o2).getGD();
				
				return x8.compareTo(x7);
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

	public static NBATeam findNBATeam (String name, NBATeam[] lot)
	{
		for (int i = 0; i < lot.length; i++)
		{
			if (name.equals(lot[i].getName()))
			{
				return lot[i];
			}
		}
		return new NBATeam();
	}

	public static NHLTeam findNHLTeam (String name, NHLTeam[] lot)
	{
		for (int i = 0; i < lot.length; i++)
		{
			if (name.equals(lot[i].getName()))
			{
				return lot[i];
			}
		}
		return new NHLTeam();
	}

}
