import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * Display class. Holds code for the GUI to display graph + data
 * @author eau
 *
 */
public class Display extends JComponent{
	private final int DISPLAY_WIDTH;   					//Width of window
	private final int DISPLAY_HEIGHT;					//Height of window
	private final int GRAPH_WIDTH;						//Width of graph
	private final int GRAPH_HEIGHT;						//height of graph
	private final int graphX0;							//x-value of top left corner of graph
	private final int graphY0;							//y-value of top left corner of graph
	private final int graphY1;							//bottom of graph
	private Graph graph;								//graph being displayed
	private int mouseX;									//mouse x position
	private int mouseY;									//mouse y position
	public int selectedPoint;							//index in graph.points that is being displayed
	public KeyControl kListener = new KeyControl();
	public MouseControl mListener = new MouseControl();	

	/**
	 * Constructor for Display
	 * @param width - int holding width of window
	 * @param height - int holding height of window
	 * @param newGraph - Graph to be displayed in this window
	 */
	public Display(int width, int height, Graph newGraph){
		graph = newGraph;
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;
		GRAPH_WIDTH = width * 4/5;						// 4/5 used to scale GRAPH_WIDTH w/ respect to DISPLAY_WIDTH
		GRAPH_HEIGHT = height/2;						// 1/2 used to scale GRAPH_HEIGHT w/ respect to DISPLAY_HEIGHT

		int a = 10;				//divider for width NOTE: this one has to be 10 to stay in center (dependent on GRAPH_HEIGHT)
		int b = 13;				//divider for height (the lower the number, the lower the graph REMEMBER THIS IS A FRACTION)
		graphX0 = DISPLAY_WIDTH/a;
		graphY0 = DISPLAY_HEIGHT/b;
		graphY1 = graphY0 + GRAPH_HEIGHT;

		mouseX = 0;
		mouseY = 0;

		selectedPoint = 0;

		setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
	}

	public void paintComponent(Graphics g){
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		drawText(g);
		drawGraph(g);

		Timer timer = new Timer(200, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}

		});
		timer.start();

	}

	void drawGraph(Graphics g){
		g.setColor(Color.BLACK);
		//rectangle

		g.drawRect(graphX0, graphY0, GRAPH_WIDTH, GRAPH_HEIGHT);

		//middle line
		g.drawLine(graphX0, graphY0 + GRAPH_HEIGHT/2, graphX0 + GRAPH_WIDTH, graphY0 + GRAPH_HEIGHT/2);

		//Text
		g.setFont(new Font("Courier", Font.PLAIN, 15));
		int c = 50;
		int d = 100;
		g.drawString("1", graphX0 - DISPLAY_WIDTH/c, graphY0 + DISPLAY_HEIGHT/d);
		g.drawString("0.5", graphX0 - 3 * DISPLAY_WIDTH/c, graphY0 + GRAPH_HEIGHT/2 + DISPLAY_HEIGHT/d);
		g.drawString("0", graphX0 - DISPLAY_WIDTH/c, graphY0 + GRAPH_HEIGHT + DISPLAY_HEIGHT/d);
		g.drawString("Home team probability of win", graphX0 + GRAPH_WIDTH/4, graphY0 + GRAPH_HEIGHT + 3 * DISPLAY_HEIGHT/d);

		//Graph
		for(int i = 0; i < graph.points.length-1; i++){
			double[] data1 = graph.points[i];
			double[] data2 = graph.points[i+1];

			g.setColor(Color.BLUE);				//for low
			g.drawLine(graphX0 + (int) data1[0], graphY0 + (int) (GRAPH_HEIGHT - data1[1]), graphX0 + (int) data2[0], graphY0 + (int) (GRAPH_HEIGHT - data2[1]));
			g.setColor(Color.RED);				//for mid
			g.drawLine(graphX0 + (int) data1[0], graphY0 + (int) (GRAPH_HEIGHT - data1[2]), graphX0 + (int) data2[0], graphY0 + (int) (GRAPH_HEIGHT - data2[2]));
			g.setColor(Color.MAGENTA);			//for high
			g.drawLine(graphX0 + (int) data1[0], graphY0 + (int) (GRAPH_HEIGHT - data1[3]), graphX0 + (int) data2[0], graphY0 + (int) (GRAPH_HEIGHT - data2[3]));
			if(i == selectedPoint){
				Event event = graph.game.events.get((int) graph.dataPoints[selectedPoint][5]);
				int radius = 6;
				g.setColor(Color.BLUE);				//for low
				g.fillOval(graphX0 + (int) data1[0] - radius/2, graphY0 + (int) (GRAPH_HEIGHT - data1[1]) - radius/2, radius, radius);

				g.setColor(Color.RED);				//for mid
				g.fillOval(graphX0 + (int) data1[0] - radius/2, graphY0 + (int) (GRAPH_HEIGHT - data1[2]) - radius/2, radius, radius);

				g.setColor(Color.MAGENTA);			//for high
				g.fillOval(graphX0 + (int) data1[0] - radius/2, graphY0 + (int) (GRAPH_HEIGHT - data1[3]) - radius/2, radius, radius);

				g.setColor(Color.YELLOW);
				g.drawLine(graphX0 + (int) data1[0], graphY0, graphX0 + (int) data1[0], graphY1);
				g.setColor(Color.BLACK);
				g.drawString(Methods.convertSecLeft(event.secLeft), graphX0 + (int) data1[0], graphY0 - 7);
			}
		}
	}

	void drawText(Graphics g){
		g.setColor(Color.BLACK);
		//Title
		g.drawString("Game_id: " + graph.game.gameId, graphX0, graphY0 / 2);
		Event event = graph.game.events.get((int) graph.dataPoints[selectedPoint][5]);
		String hD = event.homeDescription;
		String aD = event.awayDescription;
		String score = Methods.convertScores(event.homeScore, event.awayScore);
		String time = Methods.convertSecLeft(event.secLeft);

		//write the text where it belongs (keeping everything relative to display and graph sizes)
		g.drawString("Sequence_id: " + event.sequenceId, graphX0 + (int) (GRAPH_WIDTH * (2.0/3.0)), graphY0 / 2);
		g.drawString("Upper Bound: " + graph.dataPoints[selectedPoint][1], graphX0, (int) ((DISPLAY_HEIGHT - graphY1) * (2.0/7.0) + graphY1));
		g.drawString("Lower Bound: " + graph.dataPoints[selectedPoint][0], graphX0, (int) ((DISPLAY_HEIGHT - graphY1) * (3.0/7.0) + graphY1));
		g.drawString("p: " + graph.dataPoints[selectedPoint][2], graphX0 + (int) (GRAPH_WIDTH * (2.0/3.0)), (int) ((DISPLAY_HEIGHT - graphY1) * (2.0/7.0) + graphY1));
		g.drawString("n: " + graph.dataPoints[selectedPoint][3], graphX0 + (int) (GRAPH_WIDTH * (2.0/3.0)), (int) ((DISPLAY_HEIGHT - graphY1) * (3.0/7.0) + graphY1));

		g.drawString("Score: " + score, graphX0, (int) ((DISPLAY_HEIGHT - graphY1) * (4.0/7.0) + graphY1));
		g.drawString("Time: " + time, graphX0 + (int) (GRAPH_WIDTH * (2.0/3.0)), (int) ((DISPLAY_HEIGHT - graphY1) * (4.0/7.0) + graphY1));
		g.drawString("Home: " + hD, graphX0, (int) ((DISPLAY_HEIGHT - graphY1) * (5.0/7.0) + graphY1));
		g.drawString("Away: " + aD, graphX0, (int) ((DISPLAY_HEIGHT - graphY1) * (6.0/7.0) + graphY1));
		//Time
		if(mouseX >= graphX0 && mouseX < graphX0 + GRAPH_WIDTH && mouseY >= graphY0 && mouseY <= graphY1){		//mouse clicked inside graph
			int mouseSecLeft = 720 - (int) ((double) (mouseX - graphX0)/(double) GRAPH_WIDTH * 720);
			g.drawString(Methods.convertSecLeft(mouseSecLeft), mouseX, mouseY);
			g.setColor(Color.CYAN);
			g.drawLine(mouseX, graphY0, mouseX, graphY1);
		}

	}

	/**
	 * Holds my mouse controls
	 * @author eau
	 *
	 */
	private class MouseControl implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent arg0) {
			mouseX = arg0.getX();
			mouseY = arg0.getY();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * Holds my keyboard controls
	 * @author eau
	 *
	 */
	private class KeyControl implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
			//move the selected point depending on which key was pressed
			switch(e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				if(selectedPoint < graph.dataPoints.length-2){
					selectedPoint++;
				}
				break;
			case KeyEvent.VK_LEFT:
				if(selectedPoint > 0){
					selectedPoint--;
				}
				break;
			}
		}

		public void keyTyped(KeyEvent e) {
		}
	}
}


