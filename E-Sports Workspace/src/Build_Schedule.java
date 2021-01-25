import java.io.IOException;
import java.io.PrintWriter;
public class Build_Schedule {

	public static void main(String[] args) throws IOException
	{
		String[] fifa = {"Golbert (Real Madrid)", "Beef (Liverpool)", "Zabib (Barcelona)", "Lippman (Man City)", "Mitnick (Italy)", "Assaf (France)", "Antar (PSG)", "Omri (Bayern)", "Seb (Man United)"};
		String[] nba = {"Dean (Celtics)", "Zang (Bucks)", "Shane (Clippers)", "Assaf (Lakers)", "Beef (Nets)", "Golbert (Knicks)", "Wheeler (Nets)", "Hassan (Lakers)", "Wolpert (Trailblazers)", "Haber (Bucks)", "Weiss (Celtics)"};
		String[] nhl  = {"Zabib ()", "Lippman ()", "Kasimor ()", "Broder ()", "Rubin ()"};
		PrintWriter writer1 = new PrintWriter ("fifagames.txt", "UTF-8");
		PrintWriter writer2 = new PrintWriter ("nbagames.txt", "UTF-8");
		PrintWriter writer3 = new PrintWriter ("nhlgames.txt", "UTF-8");
		writer1.println("fifablue");
		makeSched (fifa, writer1);
		writer2.println("nbablue");
		makeSched (nba, writer2);
		writer3.println("nhl");
		makeSched (nhl, writer3);

		writer1.close();
		writer2.close();
		writer3.close();
		
	}

	public static void makeSched (String[] arr, PrintWriter writer)
	{
		String[] arr2 = new String[arr.length+1];
		if (arr.length%2 != 0)
		{
			for (int i = 0; i < arr.length; i++)
			{
				arr2[i] = arr[i];
			}
			arr2[arr.length] = "nogame";
			arr = arr2;
		}
		

		for (int i = 0; i < arr.length; i++)
		{
			writer.println(arr[i]);
		}
		writer.println();

		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].indexOf("(") != -1)
			{
				arr[i] = arr[i].substring(0, arr[i].indexOf("(") - 1);
			}
		}
	
		for (int i = 0; i < (arr.length-1); i++)
		{
			writer.println("Week " + (i+1));

			for (int j = 0; j < arr.length/2; j++)
			{
					writer.println(arr[j] + " vs " +arr[arr.length-1-j]);
					writer.println(arr[arr.length - 1 - j] + " vs " +arr[j]);
			}

			rotate (arr, arr.length);
			writer.println();
		}
	}
	public static void rotate (String[] arr, int n)
	{
		int i = 1;
		String temp; 
		temp = arr[n-1]; 
		for (i = n-1; i > 1; i--) 
			arr[i] = arr[i - 1]; 
		arr[i] = temp; 

	}

}
