
public class Game 
{
	private Team home;
	private Team away;
	private int HG;
	private int AG;
	public Game ()
	{
		home = new Team();
		away = new Team();
		HG = 0;
		AG = 0;
	}
	
	public Game (Team t1, Team t2)
	{
		home = t1;
		away = t2;
		HG = -1;
		AG = -1;
	}
	
	public Game (Team t1, Team t2, int g1, int g2)
	{
		home = t1;
		away = t2;
		HG = g1;
		AG = g2;
	}
	
	public void setScore (int g1, int g2)
	{
		HG = g1;
		AG = g2;
	}
	
	public int getHG ()
	{
		return HG;
	}
	
	public int getAG ()
	{
		return AG;
	}
	
	public Team getHome()
	{
		return home;
	}
	
	public Team getAway()
	{
		return away;
	}
}
