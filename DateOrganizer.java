package dateorganizer;

/**
 * A testbed for a binary heap implementation of a priority queue using 
 * various comparators to sort Gregorian dates
 * @author Duncan, Johnny Williams III
 * @see Date, PQueueAPI, PQueue
 * <pre>
 * Date: 09-20-2023
 * Course: csc 3102
 * File: DateOrganizer.java
 * Instructor: Dr. Duncan
 * </pre>
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.util.Comparator;

public class DateOrganizer {
	/**
	 * Gives the integer value equivalent to the day of the week of the specified
	 * date
	 * 
	 * @param d a date on the Gregorian Calendar
	 * @return 0->Sunday, 1->Monday, 2->Tuesday, 3->Wednesday, 4->Thursday,
	 *         5->Friday, 6->Saturday; otherwise, -1
	 */
	public static int getDayNum(Date d) {
		int month;
		switch (d.getDayOfWeek()) {

		case "Sunday":
			month = 0;
			break;
		case "Monday":
			month = 1;
			break;
		case "Tuesday":
			month = 2;
			break;
		case "Wednesday":
			month = 3;
			break;
		case "Thursday":
			month = 4;
			break;
		case "Friday":
			month = 5;
		case "Saturday":
			month = 6;
			break;
		default:
			month = -1;
			break;
		}

		return month;
	}

	public static void main(String[] args) throws IOException, PQueueException {
		String usage = "DateOrganizer <date-file-name> <sort-code>%n";
		usage += "sort-code: -2 -month-day-year%n";
		usage += "           -1 -year-month-day%n";
		usage += "            0 +weekDayNumber+monthName+day+year%n";
		usage += "            1 +year+month+day%n";
		usage += "            2 +month+day+year";
		if (args.length != 2) {
			System.out.println("Invalid number of command line arguments");
			System.out.printf(usage + "%n%");
			System.exit(1);
		}
		String datefile = args[0];
		int sort = Integer.parseInt(args[1]);
		Comparator<Date> datecomp = null;

		if (sort == -2) {
			datecomp = (date1, date2) -> {
				int compmonth = Integer.compare(date2.getMonth(), date1.getMonth());
				if (compmonth != 0) {
					return compmonth;
				}
				int compday = Integer.compare(date2.getDay(), date1.getDay());
				if (compday != 0) {
					return compday;
				}
				return Integer.compare(date2.getYear(), date1.getYear());
			};
		} else if (sort == -1) {
			datecomp = (date1, date2) -> {
				int compyear = Integer.compare(date2.getYear(), date1.getYear());
				if (compyear != 0) {
					return compyear;
				}
				int compmonth = Integer.compare(date2.getMonth(), date1.getMonth());
				if (compmonth != 0) {
					return compmonth;
				}
				return Integer.compare(date2.getDay(), date1.getDay());
			};
		} else if (sort == 0) {
			datecomp = (date1, date2) -> {
				int compday = Integer.compare(getDayNum(date1), getDayNum(date2));
				if (compday != 0) {
					return compday;
				}
				int compmonth = Integer.compare(date1.getMonth(), date2.getMonth());
				if (compmonth != 0) {
					return compmonth;
				}
				return Integer.compare(date1.getYear(), date2.getYear());
			};
		} else if (sort == 1) {
			datecomp = (date1, date2) -> {
				int compyear = Integer.compare(date1.getYear(), date2.getYear());
				if (compyear != 0) {
					return compyear;
				}
				int compmonth = Integer.compare(date1.getMonth(), date2.getMonth());
				if (compmonth != 0) {
					return compmonth;
				}
				return Integer.compare(date1.getDay(), date2.getDay());
			};
		} else if (sort == 2) {
			datecomp = (date1, date2) -> {
				int compmonth = Integer.compare(date1.getMonth(), date2.getMonth());
				if (compmonth != 0) {
					return compmonth;
				}
				int compday = Integer.compare(date1.getDay(), date2.getDay());
				if (compday != 0) {
					return compday;
				}
				return Integer.compare(date1.getYear(), date2.getYear());
			};
		}

		PQueue<Date> Dates = new PQueue<>(datecomp);

		try (Scanner in = new Scanner(new FileReader(datefile))) {
			while (in.hasNextLine()) {
				String datetostring = in.nextLine();
				String[] splitdate = datetostring.split("/");
				if (splitdate.length == 3) {
					int year = Integer.parseInt(splitdate[2]);
					int month = Integer.parseInt(splitdate[0]);
					int day = Integer.parseInt(splitdate[1]);
					Date date = new Date(month, day, year);
					Dates.insert(date);
				}
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}

		while (!Dates.isEmpty()) {
			Date date = Dates.remove();
			String day = date.getDayOfWeek();
			String month = date.getMonthName();
			int daynum = date.getDay();
			int year = date.getYear();
			System.out.printf("%s, %s %d, %d%n", day, month, daynum, year);
		}
	}
}
