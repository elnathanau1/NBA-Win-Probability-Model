import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

/**
 * Command line. No real comments in here, should be self-explanatory
 * @author eau
 *
 */
public class CommandLine {
	public static ArrayList<Game> games;
	
	/**
	 * Runs the command line
	 * @param newGames - ArrayList of Games that the following commands will use as base data
	 */
	public static void run(ArrayList<Game> newGames){
		games = newGames;
		System.out.println("The command line is now running. Type \"help\" for instructions.");
		Scanner input = new Scanner(System.in);
		boolean running = true;
		while(running){							//optional knob to turn off the commandLine. Because nothing occurs after this closes, I opted for System.exit(0)
			System.out.print(": ");
			String line = input.nextLine();
			String[] args = line.split(" ");			//commands split by " ", then processed like standard string array
			
			//Check line for the first word
			if(args[0].toLowerCase().equals("instructions") || args[0].toLowerCase().equals("help")){
				printInstructions(args);
			}
			else if(args[0].toLowerCase().equals("displaygraph") || args[0].toLowerCase().equals("dg")){
				if(args.length == 1){
					System.out.println("Please provide a valid game_id");
				}
				else if(args.length == 2){
					displayGraph(args);
				}
				else{
					System.out.println("Please only provide one parameter for dG");
				}
			}
			else if(args[0].toLowerCase().equals("makeconfidenceinterval") || args[0].toLowerCase().equals("mci")){
				makeConfidenceInterval(args);
			}
			else if(args[0].toLowerCase().equals("shouldfoul") || args[0].toLowerCase().equals("sf")){
				shouldFoul(args);
			}
			else if(args[0].toLowerCase().equals("shouldfoul2") || args[0].toLowerCase().equals("sf2")){
				shouldFoul2(args);
			}
			else if(args[0].toLowerCase().equals("printgame") || args[0].toLowerCase().equals("pg")){
				printGame(args);
			}
			else if(args[0].toLowerCase().equals("exit")){
				System.out.println("Program closing.");
				System.exit(0);
			}
			else{
				System.out.println("Command not recognized. Type \"help\" for instructions");
			}
		}
	}

	/**
	 * Parses the argument, then prints the proper help instructions
	 * @param args - String array of arguments that are user-inputed
	 */
	private static void printInstructions(String[] args){
		switch(args.length){
		case 1:
			System.out.println("Recognized commands: displayGraph(dG), makeConfidenceInterval(mci), shouldFoul(sf), shouldFoul2(sf2), printGame(pg), exit");
			System.out.println("Type \"help\" with command to see further details. Ex: help dG");
			break;
		case 2:
			if(args[1].toLowerCase().equals("displaygraph") || args[1].toLowerCase().equals("dg")){
				System.out.println("Help page for: Display Graph");
				System.out.println("dG [gameId]");
				System.out.println("Displays the graph of confidence intervals for gameId");
				System.out.println("For this data, gameId's range from " + games.get(0).gameId + " to " + games.get(games.size()-1).gameId);
			}
			else if(args[1].toLowerCase().equals("makeconfidenceinterval") || args[1].toLowerCase().equals("mci")){
				System.out.println("Help page for: Make Confidence Interval");
				System.out.println("mci [scoreDiff] [clock/secLeft]");
				System.out.println("Creates a confidence interval for the proportion of games won by teams with scoreDiff and secLeft");
			}
			else if(args[1].toLowerCase().equals("shouldfoul") || args[1].toLowerCase().equals("sf")){
				System.out.println("Help page for: Should foul");
				System.out.println("sf [scoreDiff] [clock/secLeft]");
				System.out.println("Prints confidence intervals to help coach decide whether or not to foul at end of games");
			}
			else if(args[1].toLowerCase().equals("shouldfoul2") || args[1].toLowerCase().equals("sf2")){
				System.out.println("Help page for: Should foul 2");
				System.out.println("sf2 [scoreDiff] [clock/secLeft]");
				System.out.println("NOTE: THIS IS NOT THE PREFERRED METHOD.");
				System.out.println("Prints confidence intervals to help coach decide whether or not to foul at end of games");
			}
			else if(args[1].toLowerCase().equals("printgame") || args[1].toLowerCase().equals("pg")){
				System.out.println("Help page for: Print Game");
				System.out.println("pg [gameId]");
				System.out.println("Prints the game info for the given game");
			}
			else if(args[1].toLowerCase().equals("exit")){
				System.out.println("Help page for: Exit Program");
				System.out.println("exit");
				System.out.println("Exits the program");
			}
			break;
		default:
			System.out.println("Help page not found. Try again");
		}
	}

	/**
	 * Parses the arguments, verifies the input, then sets up the display for the graph
	 * @param args
	 */
	private static void displayGraph(String[] args){
		try{
			int gameId = Integer.parseInt(args[1]);
			try{
				Game tempGame = Methods.getGameFromId(games, gameId);
				double[][] data = Stats.getDataPoints(games, tempGame);

				System.out.println("Displaying Graph " + gameId);
				System.out.println("Press Left or Right to move selected point. Click anywhere in the graph to see game clock");
				System.out.println("Quitting graph display will quit command line. ");

				int width = 800;				//Try to keep width = height (but not necessary, optimized for 600 x 600)
				int height = width;

				Graph graph = new Graph(tempGame, data, width * 4/5, height / 2);	// 4/5 and 1/2 are from Display.java

				JFrame f = new JFrame();									//set the frame which the Display sits in
				f.setSize(width, height);
				Display display = new Display(width, height, graph);		//create a new display
				f.setLayout(null);
				f.setTitle("NBA Win Probability Model");
				f.addMouseListener(display.mListener);
				f.addKeyListener(display.kListener);
				f.add(display);
				f.setVisible(true);
			}
			catch(ArrayIndexOutOfBoundsException e){						//safety checks
				System.out.println(gameId + " is not a valid game_id");
			}
			catch(NullPointerException e){
				System.out.println(gameId + " is not a valid game_id");
			}
		}
		catch(NumberFormatException e){
			System.out.println(args[1] + " is not a valid game_id");
		}
	}

	/**
	 * Parses the argument, checks if inputs are correct, then creates a confidence interval and prints it.
	 * @param args - String array of arguments that are user-inputed
	 */
	private static void makeConfidenceInterval(String[] args){
		if(args.length == 3){
			try{
				int scoreDiff = Integer.parseInt(args[1]);
				int secLeft = 720;
				String[] time = args[2].split(":");
				if(time.length == 1){
					secLeft = Integer.parseInt(args[2]);
				}
				else if(time.length == 2){
					secLeft = Methods.convertPlayClock(args[2]);
				}
				ArrayList<ArrayList<Integer>> sample = Stats.gatherSample(games, scoreDiff, secLeft);

				double[] interval = Stats.makeConfidenceInterval(sample);
				Print.printConclusion(interval, scoreDiff, secLeft);
			}
			catch(NumberFormatException e){
				System.out.println("Please use the proper format");
			}
		}
		else{
			System.out.println("Please provide scoreDiff and secLeft");
		}

	}

	/**
	 * Parses the argument, checks if inputs are correct, gathers sample, then runs analyzeFoulSample()
	 * @param args - String array of arguments that are user-inputed
	 */
	private static void shouldFoul2(String[] args){
		if(args.length == 3){
			try{
				int scoreDiff = Integer.parseInt(args[1]);
				int secLeft = 720;
				String[] time = args[2].split(":");
				if(time.length == 1){
					secLeft = Integer.parseInt(args[2]);
				}
				else if(time.length == 2){
					secLeft = Methods.convertPlayClock(args[2]);
				}
				ArrayList<ArrayList<Integer>> sample = Stats.gatherFoulSample(games, scoreDiff, secLeft);
				Stats.analyzeFoulSample(sample, scoreDiff, secLeft);
			}
			catch(NumberFormatException e){
				System.out.println("Please use the proper format");
			}
		}
		else{
			System.out.println("Please provide scoreDiff and secLeft");
		}
	}
	
	/**
	 * Parses the argument, checks if inputs are correct, then runs decideFoul()
	 * @param args - String array of arguments that are user-inputed
	 */
	private static void shouldFoul(String[] args){
		if(args.length == 3){
			try{
				int scoreDiff = Integer.parseInt(args[1]);		
				int secLeft = 720;
				String[] time = args[2].split(":");
				if(time.length == 1){
					secLeft = Integer.parseInt(args[2]);
				}
				else if(time.length == 2){
					secLeft = Methods.convertPlayClock(args[2]);
				}
				Stats.decideFoul(games, scoreDiff, secLeft);
			}
			catch(NumberFormatException e){
				System.out.println("Please use the proper format");
			}
		}
		else{
			System.out.println("Please provide scoreDiff and secLeft");
		}
	}

	/**
	 * Parses the argument, checks if inputs are correct, then prints the information gathered about the game
	 * @param args - String array of arguments that are user-inputed
	 */
	private static void printGame(String[] args){
		try{
			int gameId = Integer.parseInt(args[1]);
			try{
				Print.printGame(Methods.getGameFromId(games, gameId));
			}
			catch(ArrayIndexOutOfBoundsException e){
				System.out.println(gameId + " is not a valid game_id");
			}
			catch(NullPointerException e){
				System.out.println(gameId + " is not a valid game_id");
			}
		}
		catch(NumberFormatException e){
			System.out.println(args[1] + " is not a valid game_id");
		}
	}
}
