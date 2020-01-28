It is a implementation of Box Plot. Java Programming Language was used to implement it.
There is an example dataset inside of this folder. It Dataset.txt. Dataset is about with 
amount of road and spending fuel to travel the road. For example load-bearing trucks.<br />

The lines are some line of the dataset<br />
<b>
Id DateOfTraveling(hh:mm) AmountOfRoad(km) LiterOfFuel(lt)<br />
1 "06:00" 100 70<br />
2 "06:00" 100 27<br />
3 "06:00" 100 28<br />
</b>
These means that travel Id with 1 started to travel at 06.00 o'clock and it traveled 100km and it spent 70lt of fuel.
Datasets have to be like that. First line of the dataset is description of the dataset.You can seperate value with [, ?.@] or more than one with them.
<br />
<br />
Point Anomalies occures if a truck spends too much or too few fuel to travel road.<br />

Group Anomalies occures if some trucks travel road more than upper bound, or travel road less 
than lower bound of the road. And also some trucks have to be together with sequence.
<br /><br />
Conceptual Anomalies occures if a truck spends too few fuel or too much fuel to travel road in 
rush hours. I suppose in the rush hours, trucks spend 1.5 times liter more fuel.<br />

<br /><a href = "https://github.com/alihaydarkurban/CSE454-Data-Mining/blob/master/HW3/Outputs.pdf"> Result of the implementation was included.</a>
