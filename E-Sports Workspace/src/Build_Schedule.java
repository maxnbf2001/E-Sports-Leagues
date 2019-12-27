import java.io.IOException;
import java.io.PrintWriter;
public class Build_Schedule {

	public static void main(String[] args) throws IOException
	{
		String[] blue = {"Rowan", "Wolpert", "Haber", "Dean", "Glazer", "Messer", "Beef"};
		String[] gold = {"Lippman", "Karasik", "Assaf", "Golbert", "Geller", "Zang", "Steinberg"};
		String[] nbablue = {"Dean", "Rasch", "Zang", "Haber", "Rowan", "Glazer", "Assaf"};
		String[] nbagold = {"Novo", "Hassan", "Greenwald", "Lucier", "Wolpert", "Steinberg"};
		String[] nhl  = {"Hassan", "Dean", "Lippman", "Zabib", "Kasimor", "Broder", "Rowan", "Wolpert"};
		PrintWriter writer = new PrintWriter ("games.txt", "UTF-8");
		writer.println("fifablue");
		makeSched (blue, writer);
		writer.println("fifagold");
		makeSched (gold, writer);
		writer.println("nbablue");
		makeSched (nbablue, writer);
		writer.println("nbagold");
		makeSched (nbagold, writer);
		writer.println("nhl");
		makeSched (nhl, writer);

		writer.close();
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
