import java.text.DecimalFormat;

public class NHLTeam {
	
	private DecimalFormat fmt = new DecimalFormat("#.###");
	private String teamName, pct, lastFive;
	private int wins, losses, strk, gf, ga;

	public NHLTeam ()
	{
		teamName = "";
		wins = 0;
		losses = 0;
		pct = "0.000";
		strk = 0;
		lastFive = "";
		ga = 0;
		gf = 0;
	}
	
	public NHLTeam (String name)
	{
		teamName = name;
		wins = 0;
		losses = 0;
		pct = "0.000";
		strk = 0;
		lastFive = "";
		gf = 0;
		ga = 0;
	}
	
	
	public NHLTeam (String name, int w, int l)
	{
		teamName = name;
		wins = w;
		losses = l;
		pct = "0.000";
		strk = 0;
		lastFive = "";
		gf = 0;
		ga = 0;
	}
	
	public void addWin (int GF, int GA)
	{
		wins +=1; 
		updatePCT();
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
	public void addLoss(int GF, int GA)
	{
		losses+=1;
		updatePCT();
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
	}
	
	public void updatePCT()
	{
		if (wins == 0 && losses == 0)
			pct = "0.000";
		else if (wins > 0 && losses == 0)
			pct = "1.000";
		else 
			pct = fmt.format((double)wins/(double)(losses+wins));
		if (pct.equals("0"))
			pct = "0.000";
		while (pct.length() < 5)
			pct = pct + "0";
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
	
	public String getPCT()
	{
		return pct;
	}

	public String getLastFive()
	{
		return lastFive;
	}
	
	public int getStrk()
	{
		return strk;
	}
	


}
