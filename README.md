# Voronoi

Constructs Voronoi diagrams. Some samples:

| Input                                                                                      	| Output                                                                                  	|
|--------------------------------------------------------------------------------------------	|-----------------------------------------------------------------------------------------	|
| ![space](https://github.com/j-roskopf/Voronoi/blob/master/input/space.jpg?raw=true)        	| ![space](https://github.com/j-roskopf/Voronoi/blob/master/out/space.png?raw=true)       	|
| ![sf](https://github.com/j-roskopf/Voronoi/blob/master/input/sf.jpg?raw=true)              	| ![sf](https://github.com/j-roskopf/Voronoi/blob/master/out/sf.png?raw=true)             	|
| ![portrait](https://github.com/j-roskopf/Voronoi/blob/master/input/portrait.jpeg?raw=true) 	| ![portrait](https://github.com/j-roskopf/Voronoi/blob/master/out/portrait.png?raw=true) 	|


Each can do a variable amount of cells to generate.

BruteMain.kt - Brute force implementation. Very slow! Takes an image as input and generates Voronoi diagram over image.
KdMain.Kt - Uses k-d tree for nearest neighbor lookup. Faster! Can do ~50,000 points in ~5 seconds. Takes an image as input and generates Voronoi diagram over image.
VoronoiGuiMain.kt - Generates Voronoi diagram with a GUI for seeing it generated real time
VoronoiImageMain.kt - Generates plain image of Voronoi diagram