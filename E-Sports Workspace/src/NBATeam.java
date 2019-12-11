import java.text.DecimalFormat;

public class NBATeam
{
	private DecimalFormat fmt = new DecimalFormat("#.###");
	private String teamName, pct, lastFive;
	private int wins, losses, strk;
	private String gb;

	public NBATeam ()
	{
		teamName = "";
		wins = 0;
		losses = 0;
		pct = "0.000";
		gb = "";
		strk = 0;
		lastFive = "";
	}
	
	public NBATeam (String name)
	{
		teamName = name;
		wins = 0;
		losses = 0;
		pct = "0.000";
		gb = "";
		strk = 0;
		lastFive = "";
	}
	
	
	public NBATeam (String name, int w, int l)
	{
		teamName = name;
		wins = w;
		losses = l;
		pct = "0.000";
		gb = "";
		strk = 0;
		lastFive = "";
	}
	
	public void addWin ()
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
	}
	
	public void addLoss()
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
	
	public String getGB()
	{
		return gb;
	}
	
	public String getLastFive()
	{
		return lastFive;
	}
	
	public int getStrk()
	{
		return strk;
	}
	
	public void setGB(String gamesBehind)
	{
		gb = gamesBehind;
	}
}
