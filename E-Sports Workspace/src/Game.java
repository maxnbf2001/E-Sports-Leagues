
public class Game 
{
	private String home;
	private String away;
	private int HG;
	private int AG;
	public Game ()
	{
		home = "";
		away = "";
		HG = 0;
		AG = 0;
	}
	
	public Game (String t1, String t2)
	{
		home = t1;
		away = t2;
		HG = -1;
		AG = -1;
	}
	
	public Game (String t1, String t2, int g1, int g2)
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
	
	public String getHome()
	{
		return home;
	}
	
	public String getAway()
	{
		return away;
	}
}
