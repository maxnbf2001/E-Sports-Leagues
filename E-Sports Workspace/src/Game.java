
public class Game 
{
	private Team team1;
	private Team team2;
	private int t1G;
	private int t2G;
	public Game ()
	{
		team1 = new Team();
		team2 = new Team();
		t1G = 0;
		t2G = 0;
	}
	
	public Game (Team t1, Team t2)
	{
		team1 = new Team(t1.getName(),t1.getWins(),t1.getLosses(),t1.getDraws(),t1.getGF(),t1.getGA());
		team2 = new Team(t2.getName(),t2.getWins(),t2.getLosses(),t2.getDraws(),t2.getGF(),t2.getGA());
		t1G = -1;
		t2G = -1;
	}
	
	public Game (Team t1, Team t2, int g1, int g2)
	{
		team1 = new Team(t1.getName(),t1.getWins(),t1.getLosses(),t1.getDraws(),t1.getGF(),t1.getGA());
		team2 = new Team(t2.getName(),t2.getWins(),t2.getLosses(),t2.getDraws(),t2.getGF(),t2.getGA());
		t1G = g1;
		t2G = g2;
	}
	
	public void setScore (int g1, int g2)
	{
		t1G = g1;
		t2G = g2;
	}
	
	public int getT1G ()
	{
		return t1G;
	}
	
	public int getT2G ()
	{
		return t2G;
	}
}
