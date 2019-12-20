import java.util.ArrayList;

public class GameWeek
{
	private ArrayList<Game> gameWeek;
	private int weekNum;

	
	public GameWeek ()
	{
		gameWeek = new ArrayList<Game>();
		weekNum = 0;
	}
	

	public void addGame (Game g)
	{
		gameWeek.add(g);
	}
	
	public ArrayList<Game> getGames ()
	{
		return gameWeek;
	}
	
}
