import java.util.*;

/**
 * Data structure that holds a set of events. 
 * @author eau
 *
 */
public class Game {
	ArrayList<Event> events;	//ArrayList of the events that make up this game
	int startSequence;			//sequence # of the first event	, -1 if not found yet				
	int endSequence;			//sequence # of the last event , -1 if not found yet
	int winner;					//1 if home team won, 0 if away team won, -1 if has not been processed yet
	int gameId;					//gameId, -1 if not found yet 
	int maxDiff;				//saves largest scoreDiff, for more efficient searching later
	int minDiff;				//saves smallest scoreDiff, default = 0
	int numPeriods;				//number of periods covered
	
	/**
	 * Constructor, sets all values to their defaults
	 */
	public Game(){
		events = new ArrayList<Event>();
		startSequence = -1;
		endSequence = -1;
		winner = -1;
		gameId = -1;
		maxDiff = 0;
		minDiff = 0;
	}
	
	/**
	 * Method for adding a new event to our data structure
	 * @param newEvent - Event object to be added into the game
	 */
	public void addEvent(Event newEvent){
		if(events.isEmpty()){		//the list is empty, nothing has been added yet
			startSequence = newEvent.sequenceId;
			endSequence = newEvent.sequenceId;
			if(newEvent.scoreDiff != 0 && newEvent.secLeft == 0){		//end of game, no extra time needed
				if(newEvent.scoreDiff > 0){
					winner = 1;
				}
				else{
					winner = 0;
				}
			}
			gameId = newEvent.gameId;
			if(maxDiff < newEvent.scoreDiff){
				maxDiff = newEvent.scoreDiff;
			}
			if(minDiff > newEvent.scoreDiff){
				minDiff = newEvent.scoreDiff;
			}
			events.add(newEvent);
		}
		else{		//list is not empty
			if(newEvent.gameId != gameId){		//this is bad. why did this event get added to this game??
				System.out.println("NEWEVENT GAME ID DOES NOT MATCH GAME ID. NOT ADDED TO GAME");		//quick error checking
			}
			else{			//phew good.
				if(startSequence > newEvent.sequenceId){	//questionable...it shouldnt be...
					startSequence = newEvent.sequenceId;	//I don't like this.
				}
				if(endSequence < newEvent.sequenceId){		//this one is expected
					endSequence = newEvent.sequenceId;
				}
				if(newEvent.homeScore == -1){				//adding the score, taking from prev data if necessary
					newEvent.homeScore = events.get(events.size()-1).homeScore;
					newEvent.awayScore = events.get(events.size()-1).awayScore;
					newEvent.scoreDiff = newEvent.homeScore - newEvent.awayScore;
				}
				if(newEvent.scoreDiff != 0 && newEvent.secLeft == 0){		//end of game, no extra time needed
					if(newEvent.scoreDiff > 0){
						winner = 1;
					}
					else{
						winner = 0;
					}
				}
				if(maxDiff < newEvent.scoreDiff){
					maxDiff = newEvent.scoreDiff;
				}
				if(minDiff > newEvent.scoreDiff){
					minDiff = newEvent.scoreDiff;
				}
				events.add(newEvent);
			}
		}
	}

}

