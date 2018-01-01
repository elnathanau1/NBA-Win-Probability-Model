
public class Graph {
	public Game game;					//whichever game this graph holds
	public double[][] dataPoints;		//the data to be represented, generated from Stats.getDataPoints()
	public int graphWidth;				//how wide the graph is
	public int graphHeight;				//how tall the graph is
	public double[][] points; 			//points[0] = x, points[1] = yMid, points[2] = yHigh, points[3] = yLow
	public int totalSec;

	/**
	 * 
	 * @param newGame
	 * @param data
	 * @param newWidth
	 * @param newHeight
	 */
	public Graph(Game newGame, double[][] data, int newWidth, int newHeight){
		game = newGame;
		dataPoints = data;
		graphWidth = newWidth;
		graphHeight = newHeight;
		points = new double[dataPoints.length][4];
		totalSec = 720;
		generatePoints();
	}

	private void generatePoints(){
		/** FOR OVERTIME
		int numPeriods = 0;
		int tempPeriod = 0;
		for(int i = 0; i < game.events.size(); i++){
			if(game.events.get(i).period != tempPeriod){
				tempPeriod = game.events.get(i).period;
				numPeriods++;
			}
		}
		 **/

		for(int i = 0; i < dataPoints.length; i++){
			Event tempEvent = game.events.get((int) dataPoints[i][5]);
			int tempSecLeft = tempEvent.secLeft;
			double x = (double) (totalSec - tempSecLeft)/(double) totalSec*graphWidth;

			double dataYLow = dataPoints[i][0];
			double dataYMid = dataPoints[i][1];
			double dataYHigh = dataPoints[i][2];
			if(dataYLow < 0){
				dataYLow = 0;
			}
			if(dataYMid < 0){
				dataYMid = 0;
			}
			if(dataYHigh < 0){
				dataYHigh = 0;
			}
			if(dataYHigh > 1){
				dataYHigh = 1;
			}
			if(dataYMid > 1){
				dataYMid = 1;
			}
			if(dataYLow > 1){
				dataYLow = 1;
			}

			double yLow = dataYLow * graphHeight; 			// remember to flip when graphing
			double yHigh = dataYHigh * graphHeight;
			double yMid = dataYMid * graphHeight;
			double[] point = {x, yLow, yHigh, yMid};
			points[i] = point;
		}


	}

}
