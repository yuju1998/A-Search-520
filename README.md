# A-Search-Data-Analysis-and-Visualization

##  Please refer to A-Search-Data_Analysis-and-Visualization/Data/Plots-and-Analysis/ to see the analysis and visualization.

Example-Algo-Heuristic-Bumps:NoBumps-Results.csv

Fields:
1. Probability
2. Solvable
3. Runtime
4. Path Length (Repeated A*)
5. Path Length (discovered)
6. Path Length (complete)
7. \# of Cells Processed
8. Runtime	Weight (EC)
9. Backtrack Steps (EC)

There are a lot of extra files for Questions 6, 7, and 9 because we ran the tests with all the different heuristics for completeness.
For these questions, it should only be necessary to process the tests that used the Manhattan distance metric, so you can ignore the others.
Also, for each file, only some of the fields actually hold relevant data; the rest have placeholder or garbage values.
Which fields are relevant to each file depends on the Question \# that file is intended for.

To make things simple, below is a table containing the relevant data files you need to process for each question,
as well as the relevant fields for each file/question.
Because we took some shortcuts when collecting the data, there are also some mislabeled fields;
the table contains some additional notes marked by asterisks addressing these.

| Question \# | Data Files | Relevant Fields for that file |
| ----------- | ---------- | ----------------------------- |
| **4** |`Q4-AStar-Manhattan-NoBumps-Results.csv`| Probability, Solvable |
| **5** |`Q5-AStar-Chebyshev-NoBumps-Results.csv`| Probability, Runtime, \# of Cells Processed |
|       |`Q5-AStar-Euclidean-NoBumps-Results.csv`| (same as above) |
|       |`Q5-AStar-Manhattan-NoBumps-Results.csv`| (same as above) |
| **6** |`Q6-AStar-Manhattan-NoBumps-Results.csv`| Probability, Path Length (Repeated A*), Path Length (discovered), Path Length (complete), \# of Cells Processed |
| **7** |`Q7-AStar-Manhattan-Bumps-Results.csv`  | Probability, Path Length (Repeated A*), Path Length (discovered), Path Length (complete), \# of Cells Processed |
| **8** |`Q8Dist=1-AStar-Manhattan-NoBumps-Results.csv`| Probability, Runtime, Path Length (Repeated A*), \# of Cells Processed |
|       |`Q8Dist=6-AStar-Manhattan-NoBumps-Results.csv`| (same as above) |
|       |`Q8Dist=11-AStar-Manhattan-NoBumps-Results.csv`| (same as above) |
|       |`Q8Dist=16-AStar-Manhattan-NoBumps-Results.csv`| (same as above) |
|       |`Q8Dist=21-AStar-Manhattan-NoBumps-Results.csv`| (same as above) |
|       |`Q8Dist=26-AStar-Manhattan-NoBumps-Results.csv`| (same as above) |
|       |`Q8Dist=31-AStar-Manhattan-NoBumps-Results.csv`| (same as above) |
|       |`Q8Dist=36-AStar-Manhattan-NoBumps-Results.csv`| (same as above) |
|       |`Q8Dist=41-AStar-Manhattan-NoBumps-Results.csv`| (same as above) |
|       |`Q8Dist=46-AStar-Manhattan-NoBumps-Results.csv`| (same as above) |
|       |`Q8Dist=51-AStar-Manhattan-NoBumps-Results.csv`| (same as above) |
| **9** |`Q9prob=15-AStar-WeightedManhattan-NoBumps-Results.csv`| Runtime, Path Length (Repeated A\*) **(\* see note 1 below)**, \# of Cells Processed, Weight |
|       |`Q9prob=25-AStar-WeightedManhattan-NoBumps-Results.csv`| (same as above) **(\* see note 1 below)** |
|       |`Q9prob=15-RepeatedAStar-WeightedManhattan-NoBumps-Results.csv`| Runtime, Path Length (Repeated A\*), \# of Cells Processed, Weight |
|       |`Q9prob=25-RepeatedAStar-WeightedManhattan-NoBumps-Results.csv`| (same as above) |
| **Extra Credit** |`Q6-BFS-NoBumps-Results.csv`| (same as for Qustion 6) **(\* see note 2 below)** |
|                  |`Q7-BFS-Bumps-Results.csv`| (same as for Question 7) **(\* see note 2 below)** |

### Note 1:
Even though the field name is "Path Length (Repeated A\*)", this data was run using regular A* on the complete gridworld,
and the path length in this field is for regular A*, not repeated A*.
The data collected using Repeated A* for question 9 are in the files named with "RepeatedAStar"
(eg `Q9prob=15-RepeatedAStar-WeightedManhattan-NoBumps-Results.csv`).

### Note 2:
Even though the field name is "Path Length (Repeated A\*)", it should be "Path Length (Repeated BFS)" because these tests were run with BFS
(as specified by the Extra Credit).

