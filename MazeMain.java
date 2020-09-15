
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
//MazeMain Class
//This class handles user input, storing of the initial maze, as well as all outputs
//<List Of Identifiers>
//let SolutionMap = 2d static char array, used to store the 8x12 maze solution <type char>
//let CheeseorExit = be a static character variable used to track if the program looks for the cheese or the exit<type char>
//*****************************************************************************************************************************

import java.io.*;
import java.util.*;

public class MazeMain{//Start of main method 

	static char SolutionMap[][] = new char[8][12];
	static char CheeseorExit;
	
	
	
	/**
	 * Constructor for public class MazeMain  

	 * Functions to instantly set the value of char CheeseorExit to 'C', forcing the program to look for the cheese first
	 *Also calls output method OutputGreeting to instantly display a greeting message detailing program information
	
	 */	
	public MazeMain(){
		CheeseorExit = 'C';//char CheeseorExit initially set to 'C' to allow the program search for the cheese  
		OutputGreeting();//calls output method OutputGreeting to instantly displace greeting message 
	}//end of constructor MazeMain
	
	
	/**main method:
	 * This procedural method is called automatically, used to organize the calling of other methods in the class
	 * 
	 * List of Local Variables
	 * Start - object used to get access to non-static methods in the class MazeMain
	 * Position - 1D array used to store the x and y values of the start position <type int>
	 * alist -  Arraylist used to store the current path to the cheese being checked  <type int>
	 * blist -  Arraylist used to store a possible shortest path to the cheese from the current path being checked (alist) <type int>
	 * clist -  Arraylist used to store the current path to the exit being checked  <type int>
	 * dlist -  Arraylist used to store a possible shortest path to the exit from the current path being checked (clist) <type int>
	 * Map   -  A 2d character array used to store the map from the file read <type char>
	 * int a -  Integer set to 0, used to store the y location of the start position <type int>
	 * int b -  Integer set to 0, used to store the x location of the start position <type int>
	 *
	 * @param args <type String>
	 * @throws IO Exception	
	 * @return void
	 */
	public static void main(String[] args) throws IOException {

		MazeMain Start=new MazeMain();
		int[] Position=new int[2];
		ArrayList<Integer> alist = new ArrayList<Integer>();
		ArrayList<Integer> blist = new ArrayList<Integer>();
		ArrayList<Integer> clist = new ArrayList<Integer>();
		ArrayList<Integer> dlist = new ArrayList<Integer>();
		char Map[][] = new char[8][12];
		int a=0;
		int b=0;
		
		Map=FileReader(Map); //calls the FileReader method, which returns the maze from the user's file-Map array is set to equal the return
		
		

		Position=Start.StartLocater(Map);//calls the StartLocater method and sends it the Map array, 1D array Position is set to equal its return
		a=Position[0];//Sets int a (y value) equal to index 0 of the array Position
		b=Position[1];//Sets int b (x value) equal to index 1 of the array Position
		blist = MazeProcessing.CheesePath(Map, a, b, alist, blist);/*calls the CheesePath method in the MazeProcessing class, and sends it the Map array, int a, int b, alist, and blist
																	blist is set to the methods return value, which is the path to the cheese*/
		OutputStart(a, b);//Calls output method OutputStart, sending it int a and int b- this will print the start position
		OutputSolvedMaze();//Calls output method OutputSolvedMaze, this will output the path found from the start to the cheese
		OutputPathsMoves(blist);//Calls output method OutputPathsMoves, sending blist as an argument; this will output a coordinate solution and the total moves from the start to the cheese
		Reset(Map, blist, a, b);//Calls reset method and sends Map,blist, int a, and int b as arguments; this will reset some attribute values in the program
		dlist = MazeProcessing.CheesePath(Map, blist.get(blist.size() - 2), blist.get(blist.size() - 1), clist, dlist);
		/*Calls CheesePath method in the MazeProcessing class, sending it Map, the last 2 elements in the b list as integers, clist, and dlist
		 the last 2 blist elements are sent as they are the y and x values of the last path location, which is the cheese; this is the new starting position as the program looks for the exit
		 dlist is set to equal the methods return value, which is the path to the exit*/
		OutputStart(blist.get(blist.size() - 2), blist.get(blist.size() - 1));/*Calls output method OutputStart, sending it the last 2 array elements of blist
						this will print the start position as the cheese location since the mouse is now looking for the exit after reaching the cheese*/
		OutputSolvedMaze();//Calls output method OutputSolvedMaze, this will output the path found from the cheese to the exit
		OutputPathsMoves(dlist);//Calls output method OutputPathsMoves, sending dlist as an argument; this will output a coordinate solution and the total moves from the cheese to the exit

	}//end of main method
	
	
	/**InputFile method:
	 * This functional input method gets user input for the file directory  
	 * List of local variables:
	 * br - a BufferedReader object used for keyboard input <type BufferedReader>
	 * 
	 * @param String FileName, a string used to store user input 
	 * @throws IO Exception	
	 * @return the FileName defined by user input 
	
	 */	
	public static String InputFile(String FileName) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		FileName=br.readLine();
		return FileName;
	}//end of InputFile method 
	
	
	/**FileReader method:
	 * This functional method processes and stores a user inputed maze file 
	 * List of local variables:
	 * fileName - String used to store user inputed file directory, set to null initially <type String>
	 * character - Integer variable set to 0, used to check if there are still characters to read in the file <type int>
	 * fileReader - object used to get access to non-static methods in the class FileReader
	 * bufferedReader - object used to get access to non-static methods in the class BufferedReader
	 * 
	 * @param char Map [][] a 2d character array used to store the map from the file read <type char>
	 * @throws IO Exception	
	 * @return the Map read from the file  
	
	 */	
	public static char[][] FileReader(char[][] Map) throws IOException{
		
		String fileName = null;//Sets value of String fileName to null
		fileName=InputFile(fileName); //Calls InputFile input method to assign user input to String fileName 
		int character = 0;//Sets value of int character to 0
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			for (int i = 0; i < 8; i++) {//nested for loop to fill the 8x12 2d character array Map
				for (int x = 0; x < 12; x++) {
					if ((character = bufferedReader.read()) != -1) {//checks if there are still characters in the file to store (no character when bufferedReader.read returns -1)
						Map[i][x] = (char) character;//sets current 2d map array element to char value of int character 

						while (Map[i][x] == 0x0A || Map[i][x] == 0x0D || Map[i][x] == '\t' || Map[i][x] == ' ') {/* checks if the current element is set as a newline, 	
															carriage return, tab or a space- this would mean it is not a true value in the maze and must be reread */
							
								if ((character = bufferedReader.read()) != -1) {/*checks if there are still characters in the file to store 
																				(no character when bufferedReader.read returns -1)*/
								Map[i][x] = (char) character;/*resets current map array element to another bufferedReader.read call 
																	so that its value is changed to an actual maze value- ex. 'B' or '.'*/
							}

						}
					}
				}
			}

			bufferedReader.close();// closes bufferedReader stream
		} catch (FileNotFoundException e) {
			//prints an error if user entered file is not found and prompts the user to rerun the program 
			System.out.println("File not found, make sure the maze file is in the directory entered");
			System.out.println("Please rerun the program and enter a correct file directory");
			System.exit(0);//terminates the program
		}
	
		return Map;//returns the map attained from reading the user's file 
	}//end of FileReader method 
	
	
	/**StartLocater method:
	 * This functional method processes and returns the mouse's start position in the map, moving the mouse within the maze if its location is on a border
	 * 
	 * 
	 * List of local variables:
	 * position - 1D array of length 2, stores the x and y values of the start position <int>
	 * GetPosition - object used to get access to non-static methods in the class MazeProcessing
	 * 
	 * @param char Map [][] a 2d chracter array storing the map from the file read <type char>
	 * @return the 1D array position, which holds the x and y values of the start position 
	 */	
	public int[] StartLocater(char[][] Map) {
		MazeProcessing GetPosition=new MazeProcessing();
		int[] Position=new int[2];
		for (int i = 0; i < 8; i++) {
			for (int x = 0; x < 12; x++) {
				if (Map[i][x] == 'R'||Map[i][x]=='M') {/*checks if the current array element has the char value of R or M, indicating 
												that it is the start position*/
					GetPosition.setStarta(i);//calls mutator setStartA, sends it int i to change y position to the current indicated position
					GetPosition.setStartb(x);//calls mutator setStartb, sends it int x to change x position to the current indicated position
					if (GetPosition.getStarta()==0) {/*checks if current y position is 0 (if it is on the top border) by calling Accessor method getStarta
														in class MazeProcessing*/
						
							Map[GetPosition.getStarta()][GetPosition.getStartb()]='B'; //changes current Map array element to a wall ('B')
							Map[GetPosition.getStarta()+1][GetPosition.getStartb()]='R';//changes current start position to one Y value down (+1) to avoid array out of bounds error
							GetPosition.setStarta(GetPosition.getStarta()+1);/*calls mutator getStartA, sends it one value more than the current y
																		position(one row down) to change start position (on top border) to the same position one row down */
					
					}
					else if (GetPosition.getStartb()==11) {/*checks if current x position is 11 (if it is on the rightmost border) by calling Accessor method getStartb
														in class MazeProcessing*/
						
							Map[GetPosition.getStarta()][GetPosition.getStartb()]='B';//changes current Map array element to a wall ('B')
							Map[GetPosition.getStarta()][GetPosition.getStartb()-1]='R';//changes current start position to one x value left (-1) to avoid array out of bounds error
							GetPosition.setStartb(GetPosition.getStartb()-1);/*calls mutator getStartb, sends it one value less than the current x
																		position(one column	 left) to change start position (on right border) to the same position one column left */
						
					}
					else if (GetPosition.getStarta()==7) {/*checks if current y position is 7 (if it is on the bottom border) by calling Accessor method getStarta
														in class MazeProcessing*/
						
							Map[GetPosition.getStarta()][GetPosition.getStartb()]='B';//changes current Map array element to a wall ('B')
							Map[GetPosition.getStarta()-1][GetPosition.getStartb()]='R';//changes current start position to one y value up (-1) to avoid array out of bounds error
							GetPosition.setStarta(GetPosition.getStarta()-1);/*calls mutator getStarta, sends it one value less than the current 
							 											y position(one row up) to change start position (on bottom border) to the same position one row up */
							
					}
					else if (GetPosition.getStartb()==0) {/*checks if current x position is 0 (if it is on the leftmost border) by calling Accessor method getStartb
														in class MazeProcessing*/
								
						Map[GetPosition.getStarta()][GetPosition.getStartb()]='B';//changes current Map array element to a wall ('B')
						Map[GetPosition.getStarta()][GetPosition.getStartb()+1]='R';//changes current start position to one x value right (+1) to avoid array out of bounds error
						GetPosition.setStartb(GetPosition.getStartb()+1);/*calls mutator getStartb, sends it one value more than the current 
						 											x position(one column right) to change start position (on leftmost border) to the same position one column right */
					}
				}
			}
		}
		OutputMaze(Map);//Calls output method OutputMaze, sends Map array as an argument; this will print the map with a new starting location within the maze (not on border)
						/*The map array does not have to be specially updated or sent/received to output the result in which the start location is changed
						 changing its values in this method also change the values in the original array sent (in the main method)*/
		Position[0]=GetPosition.getStarta();//Sets index 0 of 1d array Position to equal the y value start position attained from calling accessor GetStarta
		Position[1]=GetPosition.getStartb();//Sets index 1 of 1d array Position to equal the x value start position attained from calling accessor GetStartb
		return Position;//returns 1d array Position
	}//end of StartLocater method 
	
	/**Reset method:
	 * This procedural method resets and changes values to switch the program to search for the exit 
	 * 
	 * @param char Map [][] - a 2d character array storing the map from the file read  <type char>
	 * 		  blist - Arraylist used to store the shortest possible path to the cheese 
	 * 		  int a - stores the y location of the start position <type int>
	 * 		  int b - stores the x location of the start position <type int>
	 * @return void
	 */	
	public static void Reset(char[][] Map, ArrayList<Integer> blist, int a, int b) {
		MazeProcessing.count = 0;// resets count to 0
		MazeProcessing.smallest = 999;// resets smallest to 999
		CheeseorExit = 'X';//changes static char variable CheeseorExit to 'X', allowing the program to search for the exit
		Map[blist.get(blist.size() - 2)][blist.get(blist.size() - 1)] = 'R';// sets cheese location (last 2 elements of blist) as new start location ('R')
		Map[a][b] = '.';// sets old start location as another point on the map
		
		
	}//end of Reset method 
	
	
	/**OutputMaze method:
	 * This procedural output method outputs the initial maze entered, as well as Y-Axis and X-Axis labels for a friendly user interface
	 * 
	 * @param char Map [][] - a 2d character array storing the map from the file read <type char>
	 * 		  
	 * @return void
	 */	
	public static void OutputMaze(char[][] Map) {

		System.out.println("Maze Inputed:");
		for (int i = 0; i < 8; i++) {
			for (int x = 0; x < 12; x++) {//nested for loop prints the 8x12 maze
				System.out.print(Map[i][x]);
				if(i==0&&x==11){
					System.out.print(" 0");//if current loop is at top right most location, prints 0 indicating the row has a y value of 0
				}
				if(i==3&&x==11) {
					System.out.print(" Y-Axis, 0-7");/*prints Y axis label, indicating y axis begins at the top row with a value of 0
													 and ends on the bottom with a value of 7*/
				}
				if(i==7&&x==11){
					System.out.print(" 7");//if current loop is at bottom right most location, prints 7 indicating the row has a y value of 7
				}
			}
			System.out.println("");
		}
		System.out.println("0         11");//prints 0 and 11 at opposite horizontal ends of maze to indicate that leftmost side is x=0 and rightmost side is x=11
		System.out.println("X-Axis, 0-11");//prints X-axis label to indicate the range of the axis 
		System.out.println("");
	
		
	}//end of OutputMaze method 
	
	
	/**OutputStart method:
	 * This procedural output method outputs the start position
	 * 
	 * @param int a -  the y location of the start position <type int>
	 * 		  int b -  the x location of the start position <type int>
	 * @return void
	 */	
	public static void OutputStart(int a, int b) {
		System.out.println("Start Position= (" + b + ", " + a + ")");
		
	}//end of Output Start method
	
	
	/**OutputSolvedMaze method:
	 * This procedural output method outputs the solved maze
	 * 
	 * @param none
	 * @return void
	 */	
	public static void OutputSolvedMaze() {

		for (int i = 0; i < 8; i++) {
			for (int x = 0; x < 12; x++) {//nested loop outputs the static 2d char array that holds the 8x12 solution to the maze
				System.out.print(SolutionMap[i][x]);
				
			}
			System.out.println("");
		}//end of OutputSolvedMaze
		
		
		
	}
	
	
	/**OutputPathsMoves method:
	 * This procedural output method outputs the solution path as well as the total moves 
	 * 
	 * @param SolutionList - Arraylist used to store the shortest possible path to the cheese or the shortest path from the cheese to the exit <type int>
	 *   
	 * @return void
	 */	
	public static void OutputPathsMoves(ArrayList<Integer> Solutionlist) {
		System.out.println("Shortest Path:");

		for (int i=0;i<Solutionlist.size();i+=2) {//for loop counts to total size of array in increments of 2 since 2 coordinates is equal to one move
			System.out.print("("+Solutionlist.get(i+1)+","+Solutionlist.get(i)+") ");/*outputs the array list with the larger index in front so 
																			that an x,y coordinate path is achieved ( array list stores y then x initially)*/
		}
		System.out.println();
		System.out.println("Moves:" + Solutionlist.size() / 2);//outputs half the array list size since 2 coordinates is equal to one move
		System.out.println();
	}//end of OutputPathsMoves 
	
	
	/**OutputGreetings method:
	 * This procedural output method outputs the program function and user input information when the program first starts 
	 * 
	 * @param none 
	 *   
	 * @return void
	 */	
	public static void OutputGreeting() {
		System.out.println("This program, given a maze with a cheese and exit, attempts to reach the cheese then the exit in the shortest path.");//Outputs information about the program
		System.out.println("In the maze (8x12), ‘B’ represents a barrier, ‘C’ the cheese, and ‘X’ an exit. ‘R’ or ‘M’ represents the Rat or Mouse.");//Outputs information about the program
		System.out.println("");//Outputs blank line for spacing
		System.out.println("Please enter the complete directory for your maze's filename");//Outputs a prompt for the user to enter their maze file's directory 
		System.out.println("Ex. For maze file 'test.txt', please enter 'H:\\Test\\test.txt' ");//Outputs a user friendly tip for directory input 
	}//end of OutputGreeting method 

}//end of MazeMain class