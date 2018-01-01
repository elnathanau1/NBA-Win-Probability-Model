/* Note: Variables and methods in this program are intentionally left public for easy modification. 
 * 
 */

//IMPORTANT: MAKE SURE TO REPLACE FILE STRING WITH PROPER FILE PATH

import java.util.ArrayList;

/**
 * Main class, main method is located here
 * @author eau
 *
 */
public class Main {
	public static void main(String[] args){
		String file = "/Users/eau/Documents/OKC Internship Files/pbp.csv";	//Replace w/ proper file path

		ArrayList<Game> games = Methods.processFile(file);
		System.out.println("Done Processing file: " + file);
		
		CommandLine.run(games);
	}
}
