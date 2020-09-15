//=============================================================================================================================
// The Maze 
// Michael Qian
// Novemeber 3, 2019
// Eclipse Java EE IDE for Web Developers, 20180619-1200
//=============================================================================================================================
// Problem Definition 	-Given a starting point in the maze, find the cheese, then an exit point, printing the maze showing the shortest path found
//I - User must input the complete file directory of their maze 
//O - Outputs include the initial maze, updated mazes with paths to the cheese and exit, and a coordinate based solution to the maze
//P - Processes include recursive navigation from the start point to the cheese and from the cheese to the exit, 
//    as well as other functions processing relevant information, ex. number of moves, shortest path 
//=============================================================================================================================
//MazeProcessing Class
//This class handles the processing of maze solutions
//<List Of Identifiers>
//let count = static integer variable, used to track the movement of the mouse and total moves of each path <type int>
//let smallest = a static integer variable used to store the current shortest path and compare other possible shortest paths<type int>
//let int a = integer variable used to store the y value of the mouse's start location <type int>
//let int b = integer variable used to store the x value of the mouse's start location <type int>
//*****************************************************************************************************************************
import java.util.ArrayList;

public class MazeProcessing {
	static int count = 0;
	static int smallest = 999;
	private int a;
	private int b;
	
	/**CheesePath method:
	 * This functional method searches recursively for the cheese or exit given a starting position 
	 * 
	 * 
	 * 
	 * @param  Map - A 2d character array used to store the map from the file read <type char>
	 * 		   int a -  Integer used to store the y location of the start position <type int>
	 * 		   int b -  Integer used to store the x location of the start position <type int>
	 * 		   alist -  Arraylist used to store the current path to the cheese being checked  <type int>
	 * 		   blist -  Arraylist used to store a possible shortest path to the cheese from the current path being checked (alist)
	 * 	
	 * @return blist, which holds the solution path
	 */	
	public static ArrayList<Integer> CheesePath(char[][] Map, int a, int b, ArrayList<Integer> alist,ArrayList<Integer> blist) {

		if (Map[a][b] == '.' || Map[a][b] == 'R') {//checks if current array element is part of the path (is start position or '.')
			
			count++;//increases count since the current location is part of the path

			alist.add(a);//adds current y value from path to list since the current location is part of the path
			alist.add(b);//adds current x value from path to list since the current location is part of the path
			Map[a][b] = '+';//Marks current map element with '+' to indicate it as a visited location
			blist = CheesePath(Map, a - 1, b, alist, blist);/*makes a recursive call to method CheesePath with the same parameters, 
														except for the y value (a), being changed to 1 up (-1 moves location up 1 row) This 
														moves the mouse up to check if the location directly above it is a potential part of the path */
			blist = CheesePath(Map, a, b + 1, alist, blist);/*makes a recursive call to method CheesePath with the same parameters, 
														except for the x value (b), being changed to 1 right (+1 moves location right 1 column) This 
														moves the mouse right to check if the location directly right is a potential part of the path*/
			blist = CheesePath(Map, a + 1, b, alist, blist);/*makes a recursive call to method CheesePath with the same parameters, 
														except for the y value (a), being changed to 1 down (+1 moves location down 1 row) This 
														moves the mouse down to check if the location directly down  is a potential part of the path*/
			blist = CheesePath(Map, a, b - 1, alist, blist);/*makes a recursive call to method CheesePath with the same parameters, 
														except for the x value (b), being changed to 1 left (-1 moves location left 1 column) This 
														moves the mouse left to check if the location directly left is a potential part of the path*/
			

			if (alist.size() > 0) {
				alist.remove(alist.size() - 1); /*When the method reaches this line of code after it'pops back' from being sent to each of the locations around it, this indicates that
			it has reached a dead end, and that the path will not reach the cheese or exit; subsequently, they x and y values are removed from the list */
				alist.remove(alist.size() - 1);
				count--;//count is decreased since this the current location is not part of a path that will reach the cheese or exit 
			}
			Map[a][b] = '.';/*sets current location back to '.', removing the '+' that indicates it as part of the path; since this line only occurs upon the popping back of the method, 
			the recursion will not visit this removed path again as it is a path that has popped back, thus indicating it as an unvisited area will not prompt the method to revisit it*/
		}

		if (Map[a][b] ==  MazeMain.CheeseorExit) {/*checks if current location is equal to character CheeseorExit, which is the value 'C' or 'X' depending on if the program is looking
													for the cheese or the exit respectively*/
			
			
			//since this location (and thus this path) ends with the cheese being found, this path is a potential shortest route
			if (count <= smallest) {//checks if the count associated with this path is smaller than the previous path's count(smallest is initially set to 999 to ensure the first found path is made the shortest)*/
				blist = SolutionPrint(Map, alist, a, b);/*calls solution print method to save the current path as the new smallest path, sends Map array, alist, and a,b as arguments
															sets blist, which holds the shortest path, equal to the methods return*/
			}

			return blist;//returns blist upon 'pop back'
		}
		return blist;//returns blist upon 'pop back'
	}//end of CheesePath method 
	
	
	/**SolutionPrint method:
	 * This functional method saves the shortest path from the start to the cheese or the cheese to the exit into both a 2d Maze array and array list coordinate path
	 * 
	 * List of local variables:
	 * blist -  Arraylist used to store a shortest path to the cheese from the current path being checked (alist)
	 *
	 * 
	 * @param  Map - A 2d character array used to store the map from the file read <type char>
	 * 		   int a -  Integer used to store the y location of the start position <type int>
	 * 		   int b -  Integer used to store the x location of the start position <type int>
	 * 		   alist -  Arraylist used to store the current path to the cheese being checked  <type int>
	 * 	
	 * @return blist, which holds the solution path
	 */	
	public static ArrayList<Integer> SolutionPrint(char[][] Map, ArrayList<Integer> alist, int a, int b) {
		ArrayList<Integer> blist = new ArrayList<Integer>();
			for (int i = 0; i < count * 2; i++) {// for loop to add all elements in alist to final path blist- range of the for loop is count*2 since there are 2 x,y values for each move (count)
												
				blist.add(alist.get(i));//adds value of alist at index 'i' to blist 
			}

			blist.add(a);// adds "current" y location, which could be the cheese or the exit
			blist.add(b);// adds "current" x location, which could be the cheese or the exit
			smallest = count;//sets value of smallest to the amount of moves in the current solution path

			for (int i = 0; i < 8; i++) {
				for (int x = 0; x < 12; x++) {
					MazeMain.SolutionMap[i][x] = Map[i][x];/*Saves the current 8x12 2d Map array (with '+' that indicate its current solution path) to 2d array SolutionMap
					 before the method terminates and the Map array pops back (thereby deleting the '+'s that indicate the path) to look for other possible shortest paths*/
				}

			}
		
		return blist;//returns blist, which holds the coordinate solution
	}//end of SolutionPrint method
	
	
	/** Accessor method returns y (int a) value of start location
	 * 
	 * 
	 * @return a, the y value of start location
	 */	
	public  int getStarta(){
		return this.a;
	}//end of getStarta
	
	
	/** Mutator method returns changes y value (int a) of start location to argument 'i'
	 * 
	 * @param int i, the found y value for the start location 
	 * @return void
	 */	
	public void setStarta(int i) {  
		this.a = i;  
	}  //end of setStarta
	
	
	/** Accessor method returns x value (int b) of start location
	 * 
	 * 
	 * @return b, the x value of start location
	 */	
	public  int getStartb(){
		return this.b;
	}//end of getStartb
	
	
	/** Mutator method returns changes x value (int b) of start location to argument 'x'
	 * 
	 * @param int x, the found x value for the start location 
	 * @return void
	 */	
	public  void setStartb(int x) {  
		this.b = x;  
	} //end of Startb
	
	
	
}//end of class MazeProcessing
