# NBA Win Probability Model

Given multiple season’s worth of data, created a win probability model by creating confidence intervals. Wrote a GUI to graph the confidence intervals, allow user interaction to follow the flow of the game.

## Getting Started

Import the folder into Eclipse as existing project. pbp.csv is the data set. Run Main.java, type "help" into console for more info. Look into CommandLine.java for specific commands.

## GUI

GUI loads if 'dG [gameID]' is typed into console. Ex:
```
: dG 21500002
```
Once GUI loads, can press the left/right keys to move through the sequences throughout the fourth quarter. The graph displays the confidence intervals of the win probability. Things that are displayed:
* Confidence Interval (Upper bound, lower bound)
* n (sample size)
* p (probability)
* Score, time left
* Sequence

## Structure
File is full of games, which are parsed into Game objects. Each Game consists of a set of Sequences (also called Events). Game keeps track of the gameId, startSequence, and endSequence. 

## Statistics
Currently using 95% confidence intervals. Analysis for decideFoul is included in the comments of Stats.java

## Bugs
* Overtime makes the graph double graph certain areas. Ex: game 21600005. This is caused because of the way I set secLeft when graphing. This doesn’t affect calculations, just the visualization. 

## Future Plans
* Parallel programming to decrease time waiting for file to process, and gathering samples.
* Slim down data structures, cutting down on data saved. Also maybe saving certain strings like event description as ints w/ a dictionary to save space. 
* Maybe find a way to represent who has possession. Doesn’t mater for most calculations, but important in clutch. 

## Notes
* Two seasons worth of data isn’t really enough to create very reliable confidence intervals at certain points. Preferably have more data for more reliability - but then heap memory error if I don’t manage data structures better. 

## Implementations

* Java Swing Library
* Data analysis

## Purpose
NBA Data Analytics Internship Application

## Author
Elnathan Au (eau3)

