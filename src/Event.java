
/**
 * Data structure to make handling sequences easier
 * @author eau
 *
 */
public class Event {
	//variables
	public int sequenceId;
	public int gameId;
	public int period;				//for my purposes, will either be 4, 5, or 6 (7 is possible if there was a triple OT)
	
	public int secLeft; 			//converting play_clock from "12:00" to an int representing seconds left in period
									
	public String homeDescription;
	public String awayDescription;
	
	public int homeScore;			//converting score from a string to int. 
	public int awayScore;
	public int scoreDiff;			//homeScore - awayScore
	
	public int player1ID;			//default 0 if player not involved
	
	public int player2ID;
	
	public int player3ID;			
	
	public String eventDescription;	
	
	/**
	 * Constructor
	 * @param data - String representing the line in the csv file that describes the Event
	 */
	public Event(String data){
		String[] entry = data.split(",");				//csv file is comma separated, so creating string array to hold each thing
		int index = 0; 									//makes for easy copy/paste
		
		//Derrick Jones, Jr. was causing problems, since csv is comma separated. This fixes that
		int i = 0;
		while(i < entry.length){
			if(entry[i].startsWith(" Jr.")){
				entry[i-1].concat(entry[i]);
				int j = i+1;
				while(j < entry.length){
					entry[j-1] = entry[j];
					j++;
				}
			}
			i++;
		}
		//sequenceId
		if(entry[index] != null && !entry[index].isEmpty()) {	//data exists in this slot
			sequenceId = Integer.parseInt(entry[index]); 	//parse required to convert String to int. Will throw 
			//System.out.println(sequenceId);
		}
		
		//gameId
		index = 1;
		if(entry[index] != null && !entry[index].isEmpty()) {	//data exists in this slot
			gameId = Integer.parseInt(entry[index]); 	//parse required to convert String to int. Will throw 
		}
		
		//period
		index = 2;
		if(entry[index] != null && !entry[index].isEmpty()) {	//data exists in this slot
			period = Integer.parseInt(entry[index]); 	//parse required to convert String to int. Will throw 
		}
		
		//playClock
		index = 3;
		if(entry[index] != null && !entry[index].isEmpty()) {	//data exists in this slot
			//playClock = entry[index]; 	//parse required to convert String to int. Will throw 
			
			//secLeft
			secLeft = Methods.convertPlayClock(entry[index]);
		}
		
		//homeDescription
		index = 4;
		if(entry[index] != null && !entry[index].isEmpty()) {	//data exists in this slot
			homeDescription = entry[index]; 
		}
		else {
			homeDescription = "";				//the above may (but probably wont) take care of this. to be safe.
		}
		
		//awayDescription
		index = 5;
		if(entry[index] != null && !entry[index].isEmpty()) {
			awayDescription = entry[index]; 	
		}
		else {
			awayDescription = "";
		}
		
		//score / homeScore / awayScore / scoreDiff
		index = 6;
		if(entry[index] != null && !entry[index].isEmpty()) {		//score has changed in this event
			//score = entry[index]; 
			String[] scores = entry[index].split(" - "); 		
			homeScore = Integer.parseInt(scores[1]);
			awayScore = Integer.parseInt(scores[0]);
			scoreDiff = homeScore - awayScore;
			
		}
		else {
			//score = "";
			homeScore = -1;		//placeholder, will be updated when processing games
			awayScore = -1;
			scoreDiff = 0;		// could be problematic...
		}
		
		//player1ID
		index = 7;
		if(entry[index] != null && !entry[index].isEmpty()) {
			player1ID = Integer.parseInt(entry[index]);	
		}
		else {		//should always exist, even if its 0 or a huge number
			player1ID = 0;
		}
		//player2ID
		index = 10;
		if(entry[index] != null && !entry[index].isEmpty()) {
			player2ID = Integer.parseInt(entry[index]);	
		}
		else {		//should always exist, even if its 0 or a huge number
			player2ID = 0;
		}
		//player3ID
		index = 13;
		if(entry[index] != null && !entry[index].isEmpty()) {
			player3ID = Integer.parseInt(entry[index]);	
		}
		else {		//should always exist, even if its 0 or a huge number
			player3ID = 0;
		}
		//eventDescription
		index = 17;
		if(index < entry.length){			//this wont always exist, and in cases it doesn't the csv file wont have this extra slot
			if(entry[index] != null && !entry[index].isEmpty()) {	
				eventDescription = entry[index]; 	
			}
			else {
				eventDescription = "";
			}
		}
		else{
			eventDescription = "";
		}
	}

}
