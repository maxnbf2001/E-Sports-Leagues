import java.text.DecimalFormat;

public class NHLTeam {
	
	private DecimalFormat fmt = new DecimalFormat("#.###");
	private String teamName, lastFive;
	private int wins, losses, otlosses, points, strk, gf, ga, bwstreak, blstreak;

	public NHLTeam ()
	{
		teamName = "";
		wins = 0;
		losses = 0;
		strk = 0;
		lastFive = "";
		ga = 0;
		gf = 0;
		otlosses = 0;
		points = 0;
	}
	
	public NHLTeam (String name)
	{
		teamName = name;
		wins = 0;
		losses = 0;
		strk = 0;
		lastFive = "";
		gf = 0;
		ga = 0;
		otlosses = 0;
		points = 0;
	}
	
	
	public NHLTeam (String name, int w, int l)
	{
		teamName = name;
		wins = w;
		losses = l;
		strk = 0;
		lastFive = "";
		gf = 0;
		ga = 0;
		otlosses = 0;
		points = 0;
	}
	
	public void addWin (int GF, int GA)
	{
		wins +=1; 
		if (strk < 0)
			strk = 1;
		else 
			strk+=1; 
		if (lastFive.length() == 5)
			lastFive = "W"+lastFive.substring(0,4);
		else 
			lastFive = "W"+lastFive;
		
		gf += GF;
		ga += GA;
		
		points+=2;
		
		if (strk > bwstreak)
			bwstreak = strk;
	}
	
	public int getBW ()
	{
		return bwstreak;
	}
	
	public int getGF ()
	{
		return gf;
	}
	
	public int getGA ()
	{
		return ga;
	}
	
	public int getGD ()
	{
		return gf - ga;
	}
	public void addLoss(int GF, int GA, boolean OT)
	{
		if (OT)
		{
			otlosses+=1;
			points+=1;
		}
		else
			losses+=1;
		
		if (strk > 0)
			strk = -1;
		else
			strk-=1;
		if (lastFive.length() == 5)
			lastFive = "L"+lastFive.substring(0,4);
		else 
			lastFive = "L"+lastFive;
		
		gf += GF;
		ga += GA;
		
		if (strk < blstreak)
			blstreak = strk;
		
	}
	
	public int getBL()
	{
		return blstreak;
	}
	public String getName()
	{
		return teamName;
	}
	
	public int getWins()
	{
		return wins;
	}
	
	public int getLosses()
	{
		return losses;
	}
	
	public String getLastFive()
	{
		return lastFive;
	}
	
	public int getStrk()
	{
		return strk;
	}
	
	public int getOTL()
	{
		return otlosses;
	}
	
	public int getPoints()
	{
		return points;
	}


}
