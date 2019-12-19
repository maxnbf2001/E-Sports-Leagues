
public class weekDate{
	private int day, month, year;

	
	public weekDate (int d, int m, int y)
	{
		day = d; 
		month = m;
		year = y;
	}
	
	
	public void nextWeek ()
	{
		day += 7; 
		
		if (month == 1 && day > 31) { //january
			month = 2;
			day = day - 31;
		}
		else if (month == 2 && day > 29) { //feburary for a leap year
			month = 3;
			day = day - 29;
		}
		else if (month == 3 && day > 31) { //march
			month = 4;
			day = day - 31;
		}
		else if (month == 4 && day > 30) { //april
			month = 5;
			day = day - 30;
		}
		else if (month == 5 && day > 31) { //may
			month = 6;
			day = day - 31;
		}
		else if (month == 6 && day > 30) { //june
			month = 7;
			day = day - 30;
		}
		else if (month == 7 && day > 31) { //july
			month = 8;
			day = day - 31;
		}
		else if (month == 8 && day > 31) { //august
			month = 9;
			day = day - 31;
		}
		else if (month == 9 && day > 30) { //september
			month = 10;
			day = day - 30;
		}
		else if (month == 10 && day > 31) { // october
			month = 11;
			day = day - 31;
		}
		else if (month == 11 && day > 30) { //november
			month = 12;
			day = day - 30;
		}
		else if (month == 12 && day > 31) { //december
			year +=1;
			month = 1;
			day = day - 31;
		}
		
	}
	
	public String toString()
	{
		return month + "/" + day + "/" + year;
	}
	

	
	
}
