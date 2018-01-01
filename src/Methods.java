
import java.util.ArrayList;
import java.io.*;

/**
 * Class holding miscellaneous methods
 * @author eau
 *
 */
public class Methods {

	/**
	 * Processes the file, creating the events, placing them into the respective games, then placing games into the array. returns the array
	 * @param fileName String that holds the file path of the input data
	 * @return ArrayList of Games that the data represents
	 */
	public static ArrayList<Game> processFile(String fileName){
		ArrayList<Game> games = new ArrayList<Game>();			//arraylist to hold the games (which hold the events)
		BufferedReader reader = null;							//used to read text files (including out csv file)
		String line = "";
		int readLines = 0;										//workaround for the header line
		try{
			reader = new BufferedReader(new FileReader(fileName));
			while((line = reader.readLine()) != null){			//while there is a line to read
				if(readLines == 0){
					readLines ++;
				}
				else{
					Event tempEvent = new Event(line);				//temporary used to create the event before it gets placed into game
					int gameIndex = getGameIndex(games, tempEvent.gameId);
					if(gameIndex == -1){							//this game hasnt been used before
						Game newGame = new Game();					//create a new Game for arraylist
						newGame.addEvent(tempEvent);
						games.add(newGame);
					}
					else{	
						games.get(gameIndex).addEvent(tempEvent);
					}
				}
			}
		}
		catch(FileNotFoundException e){			//safety 
			e.printStackTrace();
		}
		catch (IOException e) {					//safety
			e.printStackTrace();
		}

		if(reader != null){						//close the reader if it still exists
			try{
				reader.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}

		return games;
	}

	/**
	 * Converts the playClock String to the secLeft int. Left out error-checking, so don't be dumb with this method
	 * @param clock String that represents the game clock (ex: "11:45")
	 * @return int secLeft which holds the equivalent number of seconds left in the game
	 */
	public static int convertPlayClock(String clock){
		String[] time = clock.split(":");
		int min = Integer.parseInt(time[0]);
		int sec = Integer.parseInt(time[1]);
		return (min*60 + sec);

	}

	/**
	 * After removing playClock from event, using this to convert when I need the string
	 * @param secLeft integer than represents the number of seconds left in the game
	 * @return String that holds the game clock equivalent of secLeft (ex: 126 -> "02:06")
	 */
	public static String convertSecLeft(int secLeft){
		int min = secLeft / 60;
		int sec = secLeft % 60;
		String output = String.format("%02d:%02d", min, sec);
		return output;
	}

	/**
	 * Converts two scores to a string. Ex: "89 - 66" (Note: in data, scores are represented awayScore - homeScore)
	 * @param homeScore - integer that holds the home team's score
	 * @param awayScore - integer that holds the away team's score
	 * @return String that holds the score in the proper format
	 */
	public static String convertScores(int homeScore, int awayScore){
		String output = String.format("%d - %d", awayScore, homeScore);
		return output;
	}

	/**
	 * Finds the index of the game w/ gameId. returns index if found, -1 if not in arraylist. Implementing BinarySearch for efficiency
	 * @param games	- ArrayList of Games to search for the Game w/ gameId from
	 * @param gameId - integer holding the game_id of the game to be found
	 * @return integer that holds the index in games that the Game w/ gameId is held
	 */
	public static int getGameIndex(ArrayList<Game> games, int gameId){
		int size = games.size();
		if(size == 0){		//arraylist is empty, no point in searching for gameId
			return -1;
		}
		else{
			int start = 0;
			int end = size - 1;
			while(start <= end){
				int mid = (start + end)/2;
				int foundId = games.get(mid).gameId;
				if(gameId == foundId){			//found
					return mid;
				}
				else if(gameId < foundId){		//search first half
					end = mid - 1;
				}
				else{							//search second half
					start = mid + 1;
				}
			}
			return -1;
		}
	}

	/**
	 * Returns the Game object given a game_id. Returns null if not found. Uses getGameIndex()
	 * @param games - ArrayList of Games to search for the Game w/ gameId from
	 * @param gameId - integer holding the game_id of the game to be found
	 * @return Game in games w/ gameId. If not found, null
	 */
	public static Game getGameFromId(ArrayList<Game> games, int gameId){
		try{
			return games.get(getGameIndex(games, gameId));
		} 
		catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Game not found");
			return null;
		}

	}

	/**
	 * returns the index of the event which has the score at the time of secLeft
	 * @param game - Game which the sequence is held
	 * @param secLeft - integer holding the number of seconds left in the game
	 * @return integer holding the index in game that holds the Event 
	 */
	public static int getEventIndex(Game game, int secLeft){
		if(secLeft == 0){										//bug fix for when time is 0
			return game.events.size()-1;
		}
		for(int i = 0; i < game.events.size()-1; i++){
			int t1 = game.events.get(i).secLeft;				//secLeft for the first event
			int t2 = game.events.get(i+1).secLeft;				//secLeft for the next event
			if(secLeft == t1 && secLeft != t2){					//if this event is the one we're looking for, or if it is in the range
				return i;
			}
			else if(secLeft <= t1 && secLeft > t2){
				return i;
			}
		}
		return -1;					//SHOULD NEVER HAPPEN
	}

}

