import java.util.ArrayList;

//class holding the different print methods (for output or testing)
public class Print {

	/**
	 * Prints the data from an event
	 * @param myEvent - Event to print data from
	 */
	public static void printEvent(Event myEvent){
		System.out.println("SequenceID: " + myEvent.sequenceId);
		System.out.println("gameId: " + myEvent.gameId);
		System.out.println("period: " + myEvent.period);
		System.out.println("playClock: " + Methods.convertSecLeft(myEvent.secLeft));
		System.out.println("secLeft: " + myEvent.secLeft);
		System.out.println("homeDescription: " + myEvent.homeDescription);
		System.out.println("awayDescription: " + myEvent.awayDescription);
		System.out.println("score: " + Methods.convertScores(myEvent.homeScore, myEvent.awayScore));
		System.out.println("homeScore: " + myEvent.homeScore);
		System.out.println("awayScore: " + myEvent.awayScore);
		System.out.println("player1ID: " + myEvent.player1ID);
		//System.out.println("player1Name: " + myEvent.player1Name);
		//System.out.println("player1Team: " + myEvent.player1Team);
		System.out.println("player2ID: " + myEvent.player2ID);
		//System.out.println("player2Name: " + myEvent.player2Name);
		//System.out.println("player2Team: " + myEvent.player2Team);
		System.out.println("player3ID: " + myEvent.player3ID);
		//System.out.println("player3Name: " + myEvent.player3Name);
		//System.out.println("player3Team: " + myEvent.player3Team);
		//System.out.println("eventType: " + myEvent.eventType);
		System.out.println("eventDescription: " + myEvent.eventDescription);
	}

	/**
	 * Prints the data from a game
	 * @param myGame - Game to print data from
	 */
	public static void printGame(Game myGame){
		System.out.println("gameId: "+ myGame.gameId);
		System.out.println("startSequence: "+ myGame.startSequence);
		System.out.println("endSequence: "+ myGame.endSequence);
		System.out.println("winner: "+ myGame.winner);
		System.out.println("maxDiff: "+ myGame.maxDiff);
		System.out.println("minDiff: "+ myGame.minDiff + "\n");	
	}
	
	/**
	 * Prints the data from the event of the end of the game
	 * @param myGame - Game to print the last sequence from
	 */
	public static void printGameEnd(Game myGame){
		printEvent(myGame.events.get(myGame.events.size()-1));		//prints last event, which should be the end of the game
	}
	
	/**
	 * Prints the data from the event of the start of the game
	 * @param myGame - Game to print the first sequence from
	 */
	public static void printGameStart(Game myGame){
		printEvent(myGame.events.get(0));							//prints first event, which should be the end of the game
	}
	
	/**
	 * Prints the data from all the games in the games arrayList
	 * @param games - ArrayList of Games to print from
	 */
	public static void printAllGames(ArrayList<Game> games){
		for(int i = 0; i < games.size(); i++){
			Print.printGame(games.get(i));
		}
	}

	/**
	 * Prints the confidence interval
	 * @param bounds - double[] which holds the info representing the confidence interval
	 */
	public static void printConfidenceInterval(double[] bounds){
		System.out.format("95 percent confidence interval is [%.5f, %.5f]\n", bounds[0], bounds[1]);
		System.out.format("Sample proportion p = %.5f, sample size n = %.1f\n", bounds[2], bounds[3]);
	}

	/**
	 * Prints a more presentable version of the data
	 * @param interval - double[] that holds confidence interval info
	 * @param scoreDiff - integer holding the score difference
	 * @param secLeft - integer holding the seconds left in the game
	 */
	public static void printConclusion(double[] interval, int scoreDiff, int secLeft){
		String clock = Methods.convertSecLeft(secLeft);
		if(scoreDiff > 0){
			System.out.println("Home team up " + scoreDiff + " points with " + clock + " left");
		}
		else if(scoreDiff < 0){
			System.out.println("Home team down " + -1 * scoreDiff + " points with " + clock + " left");
		}
		else{
			System.out.println("Both teams tied with " + clock + " left");
		}
		printConfidenceInterval(interval);
	}
}
