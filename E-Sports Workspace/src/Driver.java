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
		
		int numFIFATeams = 7;
		int numNBATeams = 7;
		int numNHLTeams = 8;

		JSONObject FIFAData = new JSONObject();
		JSONObject NBAData = new JSONObject();
		JSONObject NHLData = new JSONObject();
		JSONObject allData = new JSONObject();

		weekDate startDate; 

		// -------------------------------------------------------------------------
		//  READS IN THE FIFA DATA
		// -------------------------------------------------------------------------

		//bulds the data for both the gold and blue league
		Scanner scan = new Scanner (new File ("fifagames.txt"));
		for (int l = 0; l < 2; l++)
		{
			startDate = new weekDate (8, 1, 2020);
			String league = scan.nextLine();

			Team[] teams = new Team[numFIFATeams];
			for (int i = 0; i < teams.length; i++)
			{
				String teamName = scan.nextLine();
				teams[i] = new Team (teamName);
			}
			if (teams.length%2 !=0)
				scan.nextLine();


			JSONArray fixtures = new JSONArray();
			JSONArray splitFixtures = new JSONArray ();

			
			int seasonLength = 0;
			if (teams.length%2 != 0)
				seasonLength = teams.length;
			else
				seasonLength = teams.length-1;

			GameWeek[] season = new GameWeek[seasonLength];
			scan.nextLine();

			// loops through the entire season and reads in the games for each week
			for (int i = 0; i < season.length; i++)
			{
				String week = scan.nextLine();
				season[i] = new GameWeek();
				JSONObject weeklyFixtures = new JSONObject();
				JSONArray weeklyGames = new JSONArray();
		
				// reads in the games for each week
				int numGames = 0;
				if (teams.length%2 == 0)
					numGames = teams.length;
				else 
					numGames = teams.length+1;

				for (int j = 0; j < numGames; j++)
				{
					JSONObject gameObject = new JSONObject();
					String g = scan.nextLine();
					if (g.indexOf("nogame") == -1)
					{
					
						if (g.indexOf("-") != -1) //if the game has been played
						{
							String homeT = g.substring(0, g.indexOf("-") -2);
							String awayT = g.substring(g.indexOf("-") +3, g.length());

							int homeG = Integer.parseInt(g.substring(g.indexOf("-")-1, g.indexOf("-")));
							int awayG = Integer.parseInt(g.substring(g.indexOf("-")+1, g.indexOf("-")+2));

							//finds the team with the given name and assigns them a win, draw, or loss
							Team HT = findTeam (homeT, teams);
							Team AT = findTeam (awayT, teams);

							season[i].addGame(new Game(homeT, awayT, homeG, awayG));
							gameObject.put("homeT", homeT);
							gameObject.put("awayT", awayT);
							gameObject.put("score", homeG + " - " + awayG);
							weeklyGames.add(gameObject);
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
						else
						{
							String homeT = g.substring(0, g.indexOf("vs") -1);
							String awayT = g.substring(g.indexOf("vs") +2, g.length());
							gameObject.put("homeT", homeT);
							gameObject.put("awayT", awayT);
							gameObject.put("score", "-1 - -1");
							weeklyGames.add(gameObject);
						}
					}

				}
				String now = startDate.toString();
				startDate.nextWeek();
				weeklyFixtures.put("dates", now + " - " + startDate.toString());
				weeklyFixtures.put("weeklyGames", weeklyGames);
				splitFixtures.add(weeklyFixtures);
				//makes sub arrays of the fixtures for formatting purposes on the html
				if (splitFixtures.size() == 4)
				{
					fixtures.add(splitFixtures);
					splitFixtures = new JSONArray ();
				}
				scan.nextLine();
			}
			fixtures.add(splitFixtures);


			//sorts the teams by the given conditions
			orderFIFA(teams);

			//adds all of the teams and their data into the league info
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

			//sorts the team by clean sheets and adds them into the league info
			mostClean(teams);
			JSONArray cleanList = new JSONArray();
			for (int i = 0; i < teams.length; i++)
			{
				JSONObject team = new JSONObject();
				team.put("name", teams[i].getName());
				team.put("clean", teams[i].getClean());
				cleanList.add(team);
			}
			lginfo.put("cleanList", cleanList);


			//sorts the teams by goals scored and adds them into the league info
			mostScored(teams);
			JSONArray scoredList = new JSONArray();
			for (int i = 0; i < teams.length; i++)
			{
				JSONObject team = new JSONObject();
				team.put("name", teams[i].getName());
				team.put("scored", teams[i].getGF());
				scoredList.add(team);
			}
			lginfo.put("scoredList", scoredList);


			//sorts the teams by goals conceded and adds them into the league info
			mostConceded(teams);
			JSONArray concededList = new JSONArray();
			for (int i = 0; i < teams.length; i++)
			{
				JSONObject team = new JSONObject();
				team.put("name", teams[i].getName());
				team.put("conceded", teams[i].getGA());
				concededList.add(team);
			}
			lginfo.put("concededList", concededList);


			//adds the games with the biggest victory margin to the league information
			JSONArray bvarray = new JSONArray();
			ArrayList<String> biggestVictory = getBiggestVictory(season);
			for (int i = 0; i < biggestVictory.size(); i++)
			{
				bvarray.add(biggestVictory.get(i));
			}
			lginfo.put("biggestVictory", bvarray);

			//adds the games with the most amount of goals to the league information

			JSONArray hsarray = new JSONArray();
			ArrayList<String> highestScoring = getHighestScoring(season);
			for (int i = 0; i < highestScoring.size(); i++)
			{
				hsarray.add(highestScoring.get(i));
			}
			lginfo.put("highestScoring", hsarray);


			JSONArray winStreakTeams = new JSONArray();
			ArrayList<Team> winStreak = biggestWinStreak(teams);
			for (int i = 0; i < winStreak.size(); i++)
			{
				winStreakTeams.add(winStreak.get(i).getName());
			}
			JSONObject win = new JSONObject();
			win.put("teams", winStreakTeams);
			win.put("streak", winStreak.get(0).getBW());
			lginfo.put("winstreak", win);

			JSONArray lossStreakTeams = new JSONArray();
			ArrayList<Team> lossStreak = biggestLossStreak(teams);
			for (int i = 0; i < lossStreak.size(); i++)
			{
				lossStreakTeams.add(lossStreak.get(i).getName());
			}
			JSONObject loss = new JSONObject();
			loss.put("teams", lossStreakTeams);
			loss.put("streak", Math.abs(lossStreak.get(0).getBL()));

			lginfo.put("losingStreak", loss);


			FIFAData.put(league, lginfo);
		}




		// -------------------------------------------------------------------------
		//  READS IN THE NBA DATA
		// -------------------------------------------------------------------------

		startDate = new weekDate (8, 1, 2020);
		scan = new Scanner (new File ("nbagames.txt"));
		for (int m = 0; m < 2; m++)
		{
	
			String league = scan.nextLine();
			startDate = new weekDate (8, 1, 2020);
			NBATeam[] NBAteams = new NBATeam[numNBATeams];
			for (int i = 0; i < NBAteams.length; i++)
			{
				String teamName = scan.nextLine();
				NBAteams[i] = new NBATeam(teamName);
			}
			scan.nextLine();
			if (NBAteams.length%2 !=0)
				scan.nextLine();


			JSONArray fixtures = new JSONArray();
			JSONArray splitFixtures = new JSONArray ();

			int seasonLength = 0;
			if (NBAteams.length%2 != 0)
				seasonLength = NBAteams.length;
			else
				seasonLength = NBAteams.length-1;

			GameWeek[] season = new GameWeek[seasonLength];
			//loops through all of the weeks
			for (int i = 0; i < season.length; i++)
			{
				String week = scan.nextLine();
				
				JSONObject weeklyFixtures = new JSONObject();
				JSONArray weeklyGames = new JSONArray();
				season[i] = new GameWeek();

				int numGames = 0;
				if (NBAteams.length%2 == 0)
					numGames = NBAteams.length;
				else 
					numGames = NBAteams.length+1;

				//loops through all of the games in a week
				for (int j = 0; j < numGames; j++)
				{
					String game = scan.nextLine();
					JSONObject gameObject = new JSONObject();
					if (game.indexOf("nogame") == -1)
					{

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

							season[i].addGame(new Game (homeT, awayT, homePoints, awayPoints));

							gameObject.put("homeT", homeT);
							gameObject.put("awayT", awayT);
							gameObject.put("score", homePoints + " - " + awayPoints);
							weeklyGames.add(gameObject);
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
						else
						{
							String homeT = game.substring(0, game.indexOf("vs")-1);
							String awayT = game.substring(game.indexOf("vs")+2, game.length());
							gameObject.put("homeT", homeT);
							gameObject.put("awayT", awayT);
							gameObject.put("score", "-1 - -1");
							weeklyGames.add(gameObject);
						}
					}
				}

				String now = startDate.toString();
				startDate.nextWeek();
				weeklyFixtures.put("dates", now + " - " + startDate.toString());
				weeklyFixtures.put("weeklyGames", weeklyGames);
				splitFixtures.add(weeklyFixtures);
				//makes sub arrays of the fixtures for formatting purposes on the html
				if (splitFixtures.size() == 4)
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
			JSONObject lginfo = new JSONObject ();
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

			//sorts the teams by goals scored and adds them into the league info
			mostScored(NBAteams);
			JSONArray scoredList = new JSONArray();
			for (int i = 0; i < NBAteams.length; i++)
			{
				JSONObject team = new JSONObject();
				team.put("name", NBAteams[i].getName());
				team.put("scored", NBAteams[i].getPF());
				scoredList.add(team);
			}
			lginfo.put("scoredList", scoredList);


			//sorts the teams by goals conceded and adds them into the league info
			mostConceded(NBAteams);
			JSONArray concededList = new JSONArray();
			for (int i = 0; i < NBAteams.length; i++)
			{
				JSONObject team = new JSONObject();
				team.put("name", NBAteams[i].getName());
				team.put("conceded", NBAteams[i].getPA());
				concededList.add(team);
			}
			lginfo.put("concededList", concededList);



			//adds the games with the biggest victory margin to the league information
			JSONArray bvarray = new JSONArray();
			ArrayList<String> biggestVictory = getBiggestVictory(season);
			for (int i = 0; i < biggestVictory.size(); i++)
			{
				bvarray.add(biggestVictory.get(i));
			}
			lginfo.put("biggestVictory", bvarray);

			//adds the games with the most amount of goals to the league information

			JSONArray hsarray = new JSONArray();
			ArrayList<String> highestScoring = getHighestScoring(season);
			for (int i = 0; i < highestScoring.size(); i++)
			{
				hsarray.add(highestScoring.get(i));
			}
			lginfo.put("highestScoring", hsarray);


			JSONArray winStreakTeams = new JSONArray();
			ArrayList<NBATeam> winStreak = biggestWinStreak(NBAteams);
			for (int i = 0; i < winStreak.size(); i++)
			{
				winStreakTeams.add(winStreak.get(i).getName());
			}
			JSONObject win = new JSONObject();
			win.put("teams", winStreakTeams);
			win.put("streak", winStreak.get(0).getBW());
			lginfo.put("winstreak", win);

			JSONArray lossStreakTeams = new JSONArray();
			ArrayList<NBATeam> lossStreak = biggestLossStreak(NBAteams);
			for (int i = 0; i < lossStreak.size(); i++)
			{
				lossStreakTeams.add(lossStreak.get(i).getName());
			}
			JSONObject loss = new JSONObject();
			loss.put("teams", lossStreakTeams);
			loss.put("streak", Math.abs(lossStreak.get(0).getBL()));
			lginfo.put("lossstreak", loss);

			lginfo.put("teams", teamList);
			lginfo.put("fixtures", fixtures);
			
			NBAData.put(league, lginfo);
			numNBATeams-=1; //this accounts for the fact that one league is of size 7, and the second is of size 6;
		}


		// -------------------------------------------------------------------------
		//  READS IN THE NHL DATA
		// -------------------------------------------------------------------------

		scan = new Scanner (new File ("nhlgames.txt"));
		startDate = new weekDate (8, 1, 2020);
		String league = scan.nextLine();
		// creates a list of NUM_TEAMS teams with no information
		NHLTeam[] NHLteams = new NHLTeam[numNHLTeams];
		for (int i = 0; i < NHLteams.length; i++)
		{
			String teamName = scan.nextLine();

			NHLteams[i] = new NHLTeam(teamName);
		}
		scan.nextLine();
		JSONArray fixtures = new JSONArray();
		JSONArray splitFixtures = new JSONArray ();

		int numWeeks = (numNHLTeams-1);
		GameWeek[] season = new GameWeek[numWeeks];
		//loops through all of the weeks
		for (int i = 0; i < numWeeks; i++)
		{
			season[i] = new GameWeek();
			String week = scan.nextLine();
			JSONObject weeklyFixtures = new JSONObject();
			JSONArray weeklyGames = new JSONArray();

			//loops through all of the games in a week
			for (int j = 0; j < numNHLTeams; j++)
			{
				String game = scan.nextLine();
				JSONObject gameObject = new JSONObject ();
				//handles the game if it has been played
				if (game.indexOf("-") != -1)
				{
					boolean ot = false;
					if (game.indexOf("OT") != -1)
						ot = true;
					
					String homeT = game.substring(0, game.indexOf("-") -2);
					String awayT = "";
					if (ot)
						awayT = game.substring(game.indexOf("-") +3, game.length()-3);
					else
						awayT = game.substring(game.indexOf("-") +3, game.length());

					int homeG = Integer.parseInt(game.substring(game.indexOf("-")-1, game.indexOf("-")));
					int awayG = Integer.parseInt(game.substring(game.indexOf("-")+1, game.indexOf("-")+2));


					//finds the team with the given name and assigns them a win, draw, or loss
					NHLTeam HT = findNHLTeam (homeT, NHLteams);
					NHLTeam AT = findNHLTeam (awayT, NHLteams);
				
					season[i].addGame(new Game (homeT, awayT, homeG, awayG));
					

					gameObject.put("homeT", homeT);
					gameObject.put("awayT", awayT);
					gameObject.put("score", homeG + " - " + awayG);
					weeklyGames.add(gameObject);
					
					if (homeG > awayG)
					{
						HT.addWin(homeG, awayG);
						AT.addLoss(awayG, homeG, ot);
					}
					else if (awayG > homeG)
					{
						HT.addLoss(homeG, awayG, ot);
						AT.addWin(awayG, homeG);
					}
				}
				else
				{
					String homeT = game.substring(0, game.indexOf("vs")-1);
					String awayT = game.substring(game.indexOf("vs")+2, game.length());
					gameObject.put("homeT", homeT);
					gameObject.put("awayT", awayT);
					gameObject.put("score", -1 + " - " + -1);
					weeklyGames.add(gameObject);
					
				}
			}

			String now = startDate.toString();
			startDate.nextWeek();
			weeklyFixtures.put("dates", now + " - " + startDate.toString());
			weeklyFixtures.put("weeklyGames", weeklyGames);
			splitFixtures.add(weeklyFixtures);
			//makes sub arrays of the fixtures for formatting purposes on the html
			if (splitFixtures.size() == 4)
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
		JSONArray teamList = new JSONArray();
		for (int i = 0; i < NHLteams.length; i++)
		{
			JSONObject team = new JSONObject();
			team.put("rank", (i+1));
			team.put("name", NHLteams[i].getName());
			team.put("played", NHLteams[i].getWins() + NHLteams[i].getLosses() + NHLteams[i].getOTL());
			team.put("wins", NHLteams[i].getWins());
			team.put("losses", NHLteams[i].getLosses());
			team.put("otl", NHLteams[i].getOTL());
			team.put("points", NHLteams[i].getPoints());
			team.put("strk", NHLteams[i].getStrk());
			team.put("lastFive", NHLteams[i].getLastFive());
			team.put("gf", NHLteams[i].getGF());
			team.put("ga", NHLteams[i].getGA());
			team.put("gd", NHLteams[i].getGF() - NHLteams[i].getGA());
			teamList.add(team);
		}

		//sorts the teams by goals scored and adds them into the league info
		mostScored(NHLteams);
		JSONArray scoredList = new JSONArray();
		for (int i = 0; i < NHLteams.length; i++)
		{
			JSONObject team = new JSONObject();
			team.put("name", NHLteams[i].getName());
			team.put("scored", NHLteams[i].getGF());
			scoredList.add(team);
		}
		NHLData.put("scoredList", scoredList);

		//sorts the teams by goals conceded and adds them into the league info
		mostConceded(NHLteams);
		JSONArray concededList = new JSONArray();
		for (int i = 0; i < NHLteams.length; i++)
		{
			JSONObject team = new JSONObject();
			team.put("name", NHLteams[i].getName());
			team.put("conceded", NHLteams[i].getGA());
			concededList.add(team);
		}
		NHLData.put("concededList", concededList);

		//adds the games with the biggest victory margin to the league information
		JSONArray bvarray = new JSONArray();
		ArrayList<String> biggestVictory = getBiggestVictory(season);
		for (int i = 0; i < biggestVictory.size(); i++)
		{
			bvarray.add(biggestVictory.get(i));
		}
		NHLData.put("biggestVictory", bvarray);

		//adds the games with the most amount of goals to the league information

		JSONArray hsarray = new JSONArray();
		ArrayList<String> highestScoring = getHighestScoring(season);
		for (int i = 0; i < highestScoring.size(); i++)
		{
			hsarray.add(highestScoring.get(i));
		}
		NHLData.put("highestScoring", hsarray);


		JSONArray winStreakTeams = new JSONArray();
		ArrayList<NHLTeam> winStreak = biggestWinStreak(NHLteams);
		for (int i = 0; i < winStreak.size(); i++)
		{
			winStreakTeams.add(winStreak.get(i).getName());
		}
		JSONObject win = new JSONObject();
		win.put("teams", winStreakTeams);
		win.put("streak", winStreak.get(0).getBW());
		NHLData.put("winstreak", win);

		JSONArray lossStreakTeams = new JSONArray();
		ArrayList<NHLTeam> lossStreak = biggestLossStreak(NHLteams);
		for (int i = 0; i < lossStreak.size(); i++)
		{
			lossStreakTeams.add(lossStreak.get(i).getName());
		}
		JSONObject loss = new JSONObject();
		loss.put("teams", lossStreakTeams);
		loss.put("streak", Math.abs(lossStreak.get(0).getBL()));
		
		NHLData.put("lossstreak", loss);
		
		
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

	public static ArrayList<String> getBiggestVictory (GameWeek[] s)
	{
		int bv = 0;
		ArrayList<String> biggestGames = new ArrayList<String>();

		for (int i = 0; i < s.length; i++)
		{
			ArrayList<Game> games = s[i].getGames();
			for (int j = 0; j < games.size(); j++) 
			{
				if (Math.abs(games.get(j).getHG() - games.get(j).getAG()) > bv)
				{
					biggestGames = new ArrayList<String>();
					biggestGames.add(games.get(j).getHome() + " " + games.get(j).getHG() + "-" + 
							games.get(j).getAG() + " " + games.get(j).getAway());
					bv = Math.abs(games.get(j).getHG() - games.get(j).getAG());
				}
				else if (Math.abs(games.get(j).getHG() - games.get(j).getAG()) == bv)
				{
					biggestGames.add(games.get(j).getHome() + " " + games.get(j).getHG() + "-" + 
							games.get(j).getAG() + " " + games.get(j).getAway());
				}
			}
		}

		return biggestGames;
	}

	public static ArrayList<String> getHighestScoring (GameWeek[] s)
	{
		int hs = 0;
		ArrayList<String> highestScoring = new ArrayList<String>();

		for (int i = 0; i < s.length; i++)
		{
			ArrayList<Game> games = s[i].getGames();
			for (int j = 0; j < games.size(); j++) 
			{
				if (games.get(j).getHG() + games.get(j).getAG() > hs)
				{
					highestScoring = new ArrayList<String>();
					highestScoring.add(games.get(j).getHome() + " " + games.get(j).getHG() + "-" + 
							games.get(j).getAG() + " " + games.get(j).getAway());
					hs = games.get(j).getHG() + games.get(j).getAG();
				}
				else if (games.get(j).getHG() + games.get(j).getAG() == hs)
				{
					highestScoring.add(games.get(j).getHome() + " " + games.get(j).getHG() + "-" + 
							games.get(j).getAG() + " " + games.get(j).getAway());
				}
			}
		}

		return highestScoring;
	}
	public static ArrayList<NHLTeam> biggestWinStreak (NHLTeam[] arr)
	{
		ArrayList<NHLTeam> biggestStreak = new ArrayList<NHLTeam>();
		int bs = 0;

		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].getBW() > bs)
			{
				biggestStreak = new ArrayList<NHLTeam>();
				biggestStreak.add(arr[i]);
				bs = arr[i].getBW();
			}
			else if (arr[i].getBW() == bs)
				biggestStreak.add(arr[i]);
		}

		return biggestStreak;

	}

	public static ArrayList<NHLTeam> biggestLossStreak (NHLTeam[] arr)
	{
		ArrayList<NHLTeam> biggestStreak = new ArrayList<NHLTeam>();
		int bs = 0;

		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].getBL() < bs)
			{
				biggestStreak = new ArrayList<NHLTeam>();
				biggestStreak.add(arr[i]);
				bs = arr[i].getBL();
			}
			else if (arr[i].getBL() == bs)
				biggestStreak.add(arr[i]);
		}

		return biggestStreak;

	}
	public static ArrayList<NBATeam> biggestWinStreak (NBATeam[] arr)
	{
		ArrayList<NBATeam> biggestStreak = new ArrayList<NBATeam>();
		int bs = 0;

		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].getBW() > bs)
			{
				biggestStreak = new ArrayList<NBATeam>();
				biggestStreak.add(arr[i]);
				bs = arr[i].getBW();
			}
			else if (arr[i].getBW() == bs)
				biggestStreak.add(arr[i]);
		}

		return biggestStreak;

	}

	public static ArrayList<NBATeam> biggestLossStreak (NBATeam[] arr)
	{
		ArrayList<NBATeam> biggestStreak = new ArrayList<NBATeam>();
		int bs = 0;

		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].getBL() < bs)
			{
				biggestStreak = new ArrayList<NBATeam>();
				biggestStreak.add(arr[i]);
				bs = arr[i].getBL();
			}
			else if (arr[i].getBL() == bs)
				biggestStreak.add(arr[i]);
		}

		return biggestStreak;

	}


	public static ArrayList<Team> biggestWinStreak (Team[] arr)
	{
		ArrayList<Team> biggestStreak = new ArrayList<Team>();
		int bs = 0;

		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].getBW() > bs)
			{
				biggestStreak = new ArrayList<Team>();
				biggestStreak.add(arr[i]);
				bs = arr[i].getBW();
			}
			else if (arr[i].getBW() == bs)
				biggestStreak.add(arr[i]);
		}

		return biggestStreak;

	}

	public static ArrayList<Team> biggestLossStreak (Team[] arr)
	{
		ArrayList<Team> biggestStreak = new ArrayList<Team>();
		int bs = 0;

		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].getBL() < bs)
			{
				biggestStreak = new ArrayList<Team>();
				biggestStreak.add(arr[i]);
				bs = arr[i].getBL();
			}
			else if (arr[i].getBL() == bs)
				biggestStreak.add(arr[i]);
		}

		return biggestStreak;

	}
	public static void mostClean (Team[] array)
	{
		ArrayList<Team> arrTemp = new ArrayList<Team>();
		for (int i = 0; i < array.length; i++)
		{
			arrTemp.add(array[i]);
		}
		Collections.sort(arrTemp, new Comparator() {

			public int compare(Object o1, Object o2) {

				Integer x5 = ((Team) o1).getClean();
				Integer x6 = ((Team) o2).getClean();
				return x6.compareTo(x5);
			}});

		for (int i = 0; i < array.length; i++)
		{
			array[i] = arrTemp.get(i);
		}
	}

	public static void mostScored (Team[] array)
	{
		ArrayList<Team> arrTemp = new ArrayList<Team>();
		for (int i = 0; i < array.length; i++)
		{
			arrTemp.add(array[i]);
		}
		Collections.sort(arrTemp, new Comparator() {

			public int compare(Object o1, Object o2) {

				Integer x5 = ((Team) o1).getGF();
				Integer x6 = ((Team) o2).getGF();
				return x6.compareTo(x5);
			}});

		for (int i = 0; i < array.length; i++)
		{
			array[i] = arrTemp.get(i);
		}
	}


	public static void mostConceded (Team[] array)
	{
		ArrayList<Team> arrTemp = new ArrayList<Team>();
		for (int i = 0; i < array.length; i++)
		{
			arrTemp.add(array[i]);
		}
		Collections.sort(arrTemp, new Comparator() {

			public int compare(Object o1, Object o2) {

				Integer x5 = ((Team) o1).getGA();
				Integer x6 = ((Team) o2).getGA();
				return x6.compareTo(x5);
			}});

		for (int i = 0; i < array.length; i++)
		{
			array[i] = arrTemp.get(i);
		}
	}

	public static void mostScored (NBATeam[] array)
	{
		ArrayList<NBATeam> arrTemp = new ArrayList<NBATeam>();
		for (int i = 0; i < array.length; i++)
		{
			arrTemp.add(array[i]);
		}
		Collections.sort(arrTemp, new Comparator() {

			public int compare(Object o1, Object o2) {

				Integer x5 = ((NBATeam) o1).getPF();
				Integer x6 = ((NBATeam) o2).getPF();
				return x6.compareTo(x5);
			}});

		for (int i = 0; i < array.length; i++)
		{
			array[i] = arrTemp.get(i);
		}
	}


	public static void mostConceded (NBATeam[] array)
	{
		ArrayList<NBATeam> arrTemp = new ArrayList<NBATeam>();
		for (int i = 0; i < array.length; i++)
		{
			arrTemp.add(array[i]);
		}
		Collections.sort(arrTemp, new Comparator() {

			public int compare(Object o1, Object o2) {

				Integer x5 = ((NBATeam) o1).getPA();
				Integer x6 = ((NBATeam) o2).getPA();
				return x6.compareTo(x5);
			}});

		for (int i = 0; i < array.length; i++)
		{
			array[i] = arrTemp.get(i);
		}
	}
	
	public static void mostScored (NHLTeam[] array)
	{
		ArrayList<NHLTeam> arrTemp = new ArrayList<NHLTeam>();
		for (int i = 0; i < array.length; i++)
		{
			arrTemp.add(array[i]);
		}
		Collections.sort(arrTemp, new Comparator() {

			public int compare(Object o1, Object o2) {

				Integer x5 = ((NHLTeam) o1).getGF();
				Integer x6 = ((NHLTeam) o2).getGF();
				return x6.compareTo(x5);
			}});

		for (int i = 0; i < array.length; i++)
		{
			array[i] = arrTemp.get(i);
		}
	}


	public static void mostConceded (NHLTeam[] array)
	{
		ArrayList<NHLTeam> arrTemp = new ArrayList<NHLTeam>();
		for (int i = 0; i < array.length; i++)
		{
			arrTemp.add(array[i]);
		}
		Collections.sort(arrTemp, new Comparator() {

			public int compare(Object o1, Object o2) {

				Integer x5 = ((NHLTeam) o1).getGA();
				Integer x6 = ((NHLTeam) o2).getGA();
				return x6.compareTo(x5);
			}});

		for (int i = 0; i < array.length; i++)
		{
			array[i] = arrTemp.get(i);
		}
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

				Integer x1 = ((NHLTeam) o1).getPoints();
				Integer x2 = ((NHLTeam) o2).getPoints();
				int sComp = x2.compareTo(x1);

				if (sComp != 0) {
					return sComp;
				} 


				Integer x3 = ((NHLTeam) o1).getGD();
				Integer x4 = ((NHLTeam) o2).getGD();
				sComp =  x4.compareTo(x3);

				if (sComp != 0) {
					return sComp;
				} 

				Integer x5 = ((NHLTeam) o1).getWins();
				Integer x6 = ((NHLTeam) o2).getWins();

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
			if (name.equals(lot[i].getName().substring(0, lot[i].getName().indexOf("(") - 1)))
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
			if (name.equals(lot[i].getName().substring(0, lot[i].getName().indexOf("(") - 1)))
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
			if (name.equals(lot[i].getName().substring(0, lot[i].getName().indexOf("(") - 1)))
			{
				return lot[i];
			}
		}
		return new NHLTeam();
	}

}
