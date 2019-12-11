import java.text.DecimalFormat;

public class NBATeam
{
	private DecimalFormat fmt = new DecimalFormat("#.###");
	private String teamName, pct, lastFive;
	private int rank, wins, losses, gb, strk;

	private NBATeam ()
	{
		teamName = "";
		rank = 0;
		wins = 0;
		losses = 0;
		pct = "0.000";
		gb = 0;
		strk = 0;
		lastFive = "";
	}
	
	private NBATeam (String name)
	{
		teamName = name;
		rank = 0;
		wins = 0;
		losses = 0;
		pct = "0.000";
		gb = 0;
		strk = 0;
		lastFive = "";
	}
	
	
	private NBATeam (String name, int r, int w, int l)
	{
		teamName = name;
		rank = r;
		wins = w;
		losses = l;
		pct = "0.000";
		gb = 0;
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
		
		lastFive = "W"+lastFive.substring(0,4);
	}
	
	public void addLoss()
	{
		losses+=1;
		updatePCT();
		if (strk > 0)
			strk = -1;
		else
			strk-=1;
		
		lastFive = "L"+lastFive.substring(0,4);
	}
	
	public void updatePCT()
	{
		if (wins == 0 && losses == 0)
			pct = "0.000";
		else if (wins > 0 && losses == 0)
			pct = "1.000";
		else 
			pct = fmt.format((double)wins/(double)losses);
	}
}
