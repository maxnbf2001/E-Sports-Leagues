import java.util.ArrayList;

public class Team 
{
	private String teamName;
	private int wins, losses, draws, points, GD, GF, GA, clean, strk, bwstreak, blstreak;
	
	public Team ()
	{
	   teamName = "";
	   wins = 0;
	   losses = 0; 
	   draws = 0;
	   points = 0;
	   GD = 0;
       GF = 0; 
       GA = 0;
       clean = 0;
       strk = 0;
	}
	
	public Team (String name)
	{
	   teamName = name;
	   wins = 0;
	   losses = 0; 
	   draws = 0;
	   points = 0;
	   GD = 0;
       GF = 0; 
       GA = 0;
       clean = 0;
       strk = 0;
	}
	
	
	public Team (String tn, int w, int l, int d, int gf, int ga)
	{
		teamName = tn;
		wins = w;
		losses = l;
		draws = d;
		points = w*3 + d;
		GF = gf;
		GA = ga;
		GD = gf - ga;
		strk = 0;
		if (ga == 0)
			clean = 1;
		else
			clean = 0;
	}
	
	public void addWin (int gf, int ga)
	{
		wins+=1;
		points+=3;
		GD = GD + gf - ga;
		GA += ga;
		GF += gf;
		
		if (ga == 0)
			clean +=1;
		
		if (strk < 0)
			strk = 1;
		else 
			strk+=1; 
		
		if (strk > bwstreak)
			bwstreak = strk;
	}
	
	public int getBW ()
	{
		return bwstreak;
	}
	
	public void addLoss (int gf, int ga)
	{	
		losses+=1;
		GD = GD + gf - ga;
		GA += ga;
		GF += gf;
		
		if (strk > 0)
			strk = -1;
		else
			strk-=1;
		
		if (strk < blstreak)
			blstreak = strk;
	}
	
	public int getBL()
	{
		return blstreak;
	}
	
	public void addDraw (int gf, int ga)
	{
		draws+=1;
		points+=1;
		GD = GD + gf - ga;
		GA += ga;
		GF += gf;
		
		if (ga == 0)
			clean +=1;
	}
	
	public String getName()
	{
		return teamName;
	}
	
	public int getWins ()
	{
		return wins;
	}
	
	
	public int getLosses ()
	{
		return losses;
	}
	
	
	public int getDraws ()
	{
		return draws;
	}
	
	
	public int getGD ()
	{
		return GD;
	}
	
	public int getGF ()
	{
		return GF;
	}
	
	public int getGA ()
	{
		return GA;
	}
	
	public int getPoints ()
	{
		return points;
	}
	

	
	public int getClean ()
	{
		return clean;
	}
	public String toString ()
	{
		return teamName + "\t" + (wins+losses+draws) + "\t" + wins
				+ "\t" + draws + "\t" + losses + "\t" + GF + "\t" + GA + "\t" + GD + "\t" + points;
	}
	
}
