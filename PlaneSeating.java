import java.util.*;

/*          
	The algorithm works by first calculating the set of seats in each category and their starting passenger number.
	After the above step, we itearate through each seat from from front row to back row and fill it if its quota is not exhausted
	The algorithm terminates once it has filled all the seats or there are no more passengers left to seat
	It then prints the seating map with passenger number and the seat type row wise.
	Unoccupied seat is marked with 'X'
	
	Time Complexity :
	Assuming k grids each with row and column in O(n) 
	We iterate the entire grid twice ,once for calculation and once for arrangement 
	   ==> k * n * n = O(k * n^2)
*/

class PlaneSeating  {
  
	public static void main(String[] args) {

		PlaneSeating ps = new PlaneSeating();

		System.out.println("Enter the Number of Grids");
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[][] seatMap = new int[n][2];
		int r = 0, c = 0;

		for(int i=0;i<n;i++){
			System.out.println("Enter Number of Rows in Grid - "+(i+1));
			r = sc.nextInt();
			System.out.println("Enter Number of Columns in Grid - " + (i+1));
			c = sc.nextInt();
			seatMap[i] = new int[2];
			seatMap[i][0] = r;
			seatMap[i][1] = c;
		}

		System.out.println("Enter Number of passengers");
		n = sc.nextInt();
		ps.arrangeSeating(seatMap, n);

	}

	public void arrangeSeating(int[][] seatMap, int passCount) {

		int maxRows = 0;
		int totalSeats = 0;
		int winSeats = 0, aisleSeats = 0, centerSeats = 0;
		int numGrids = seatMap.length;

		//output list
		List<List<List<String>>> arrangement = new ArrayList<List<List<String>>>();


		// Calculate count of each category seats
		for (int i = 0; i < numGrids; i++) {

			int currRowCount = seatMap[i][0];
			int currColCount = seatMap[i][1];

			maxRows = Math.max(maxRows, currRowCount);
			totalSeats += (currRowCount * currColCount);

			if (i == 0 || i == numGrids - 1) {
				winSeats += currRowCount;
				aisleSeats += (currColCount > 1 ? currRowCount : 0);  // consider  single col seats
			} else {
				aisleSeats += ( currColCount > 1 ? (2 * currRowCount) : currRowCount);   // consider  single col seats
			}

			// Add Grid to the final Layout
			addGridToLayout(currRowCount, currColCount, arrangement);

		}

		centerSeats = totalSeats - (winSeats + aisleSeats);


		int aisleStart = 0;
		int windowStart = aisleSeats;
		int centerStart = aisleSeats + winSeats;

		int aisleEnd = Math.min(windowStart, passCount);
		int windowEnd = Math.min(centerStart, passCount);
		int centerEnd = Math.min(centerStart + centerSeats, passCount);

		int currRow = 0, passRem = passCount;
		while (currRow < maxRows && passRem > 0) {
			// System.out.println("Seating in Row "+ (currRow+1) );
			for (int i = 0; i < numGrids; i++) {
				int[] currGrid = seatMap[i];
				if (currGrid[0] > currRow) {
					for (int j = 0; j < currGrid[1]; j++) {
						if (j == 0 || j == currGrid[1] - 1) {
							if (((i == 0  && j == 0) || (i == numGrids - 1 && j == currGrid[1] - 1))) {
								// Window Seat
								if (windowStart < windowEnd) {
									arrangement.get(i).get(currRow).set(j, String.valueOf(windowStart + 1).concat("W"));
									windowStart++;
									passRem--;
								}
							} else if (aisleStart < aisleEnd) {
								// Aisle Seat
								arrangement.get(i).get(currRow).set(j, String.valueOf(aisleStart + 1).concat("A"));
								aisleStart++;
								passRem--;
							}
						} else if (centerStart < centerEnd) {
							// Center Seat
							arrangement.get(i).get(currRow).set(j, String.valueOf(centerStart + 1).concat("C"));
							centerStart++;
							passRem--;
						}
					}
				}
			}
			currRow++;
		}

		if (passRem > 0) {
			System.out.println(passRem + (passRem > 1 ? " passengers" : " passenger") + " not seated");
		} else {
			System.out.println("All passengers seated");
		}

		printLayout(arrangement, maxRows, passCount);

	}

	/* Prints the seating arrangement */
	public void printLayout(List<List<List<String>>> list, int maxRows, int peopleCount) {

		String emptySpace = "  ";

		int currRow = 0;
		while (currRow < maxRows) {
			for (int i = 0; i < list.size(); i++) {
				List<List<String>> grid = list.get(i);
				int numRows = grid.size();
				StringBuilder sb = new StringBuilder();
				if (currRow < numRows) {
					List<String> row = grid.get(currRow);
					for (String str : row) {
						sb.append(str);
						sb.append(emptySpace);
					}
				} 
				System.out.print(sb.toString());
			}
			currRow++;
			System.out.println("");
		}

	}

	/* This method add a grid to the layout */
	public void addGridToLayout(int currRowCount, int currColCount, List<List<List<String>>> arrangement) {
		List<List<String>> list = new ArrayList<List<String>>();
		for (int j = 0; j < currRowCount; j++) {
			List<String> row = new ArrayList<>();
			for (int k = 0; k < currColCount; k++) {
				row.add("X");
			}
			list.add(row);
		}
		arrangement.add(list);
	}



}