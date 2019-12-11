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
		final int NUM_TEAMS = 10;
		JSONObject FIFAData = new JSONObject();
		JSONObject NBAData = new JSONObject();
		JSONObject allData = new JSONObject();
		JSONArray leagues = new JSONArray();

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


			orderFIFA(teams);

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

		for (int i = 0; i < (numNBATeams-1)*2; i++)
		{
			String week = scan.nextLine();
			JSONArray weeklyGames = new JSONArray();
			for (int j = 0; j < numNBATeams/2; j++)
			{
				String game = scan.nextLine();
				weeklyGames.add(game);
				if (game.indexOf("-") == -1) //the game has not been played
				{
					//Realizing i need to do nothing with the information at this point
				}
				else
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
					
					if (homePoints < 100)
						homeT = game.substring(0, game.indexOf("-") -3);
					else
						homeT = game.substring(0,game.indexOf("-") - 4);
					
					if (awayPoints < 100)
						awayT = game.substring(game.indexOf("-") +4, game.length());
					else
						awayT = game.substring(game.indexOf("-") +5, game.length());

					NBATeam HT = findNBATeam (homeT, NBAteams);
					NBATeam AT = findNBATeam (awayT, NBAteams);
					if (homePoints > awayPoints)
					{
						HT.addWin();
						AT.addLoss();
					}
					else if (awayPoints > homePoints)
					{
						HT.addLoss();
						AT.addWin();
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
		orderNBA (NBAteams);

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
			//System.out.println(team.toString());
			teamList.add(team);
		}
		
		NBAData.put("teams", teamList);
		NBAData.put("fixtures", fixtures);
		

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		String lastUpdated = dateFormat.format(date);
		allData.put("fifaData", FIFAData);
		allData.put("nbaData", NBAData);
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
	private static void orderFIFA(Team[] array) {

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
	
	private static void orderNBA (NBATeam[] array) {

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

}
