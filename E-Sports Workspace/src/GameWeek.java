
public class GameWeek
{
	private Game[] game;
	private int weekNum;
	private final int numGames = 20;
	
	public GameWeek ()
	{
		game = new Game[numGames];
		weekNum = 0;
	}
	
	public GameWeek (int n)
	{
		game = new Game[numGames];
		weekNum = n;
	}
	
	public void addGame (Game g, int n)
	{
		game[n] = g;
	}
	
}
