import java.io.IOException;
import java.io.PrintWriter;
public class Build_Schedule {

	public static void main(String[] args) throws IOException
	{
		String[] blue = {"Beef", "Zang", "Ripps", "Zabib", "Wolpert", "Platzman", "Steinberg", "Assaf"};
		String[] gold = {"Vernoff", "Rowan", "Zingler", "Israel", "Weiss", "Hodys", "Wheeler", "Demian"};
		String[] nba  = {"Ripps", "Zabib", "Wolpert", "Platzman", "Weiss", "Israel", "Wheeler", "Demian", "Zingler", "Vernoff"};
		PrintWriter writer = new PrintWriter ("games.txt", "UTF-8");
		writer.println("blue");
		makeSched (blue, writer);
		writer.println("gold");
		makeSched (gold, writer);
		writer.println("nba");
		makeSched (nba, writer);

		writer.close();
	}

	public static void makeSched (String[] arr, PrintWriter writer)
	{

		for (int i = 0; i < arr.length; i++)
		{
			writer.println(arr[i]);
		}
		writer.println();
		for (int i = 0; i < (arr.length-1); i++)
		{
			writer.println("Week " + (i+1));
			for (int j = 0; j < arr.length/2; j++)
				writer.println(arr[j] + " vs " +arr[arr.length-1-j]);

			for (int j = 0; j < arr.length/2; j++)
				writer.println(arr[arr.length - 1 - j] + " vs " +arr[j]);

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
