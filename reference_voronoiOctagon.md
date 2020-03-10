## voronoiOctagon
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Takes a binary image and dilates the regions using a octagon shape until the touch. The pixels where  the regions touched are afterwards returned as binary image which cooresponds to the Voronoi diagram.

### Usage in ImageJ macro
```
Ext.CLIJx_voronoiOctagon(Image input, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input = clij2.push(inputImagePlus);
destination = clij.create(input);
```

```
// Execute operation on GPU
clij2.voronoiOctagon(clij, input, destination);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(input);
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [benchmarkVoronoi.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/benchmarkVoronoi.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [voronoi.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/voronoi.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [voronoi3D.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/voronoi3D.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
