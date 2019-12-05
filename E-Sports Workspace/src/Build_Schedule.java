import java.io.IOException;
import java.io.PrintWriter;
public class Build_Schedule {

	public static void main(String[] args) throws IOException
	{
		String[] blue = {"Team 1", "Team 2", "Team 3", "Team 4", "Team 5", "Team 6", "Team 7", "Team 8", "Team 9", "Team 10"};
		String[] gold = {"Team 11", "Team 12", "Team 13", "Team 14", "Team 15", "Team 16", "Team 17", "Team 18", "Team 19", "Team 20"};
		PrintWriter writer = new PrintWriter ("games.txt", "UTF-8");
		writer.println("Blue League");
		makeSched (blue, writer);
		writer.println("Gold League");
		makeSched (gold, writer);

		writer.close();
	}

	public static void makeSched (String[] arr, PrintWriter writer)
	{
		
		for (int i = 0; i < arr.length; i++)
		{
			writer.println(arr[i]);
		}
		writer.println();
		for (int i = 0; i < (arr.length-1)*2; i++)
		{
			writer.println("Week " + (i+1));
			if ((i+1)%2 == 0)
				for (int j = 0; j < arr.length/2; j++)
					writer.println(arr[j] + " vs " +arr[arr.length-1-j]);
			else
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
