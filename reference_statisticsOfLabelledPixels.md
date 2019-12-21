## statisticsOfLabelledPixels
![Image](images/mini_clijx_logo.png)

Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.

### Usage in ImageJ macro
```
Ext.CLIJx_statisticsOfLabelledPixels(Image input, Image labelmap);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
ClearCLBuffer arg2 = clijx.push(arg2ImagePlus);
```

```
// Execute operation on GPU
double[][] resultStatisticsOfLabelledPixels = clijx.statisticsOfLabelledPixels(clij, arg1, arg2, arg3, arg4);
```

```
//show result
System.out.println(resultStatisticsOfLabelledPixels);

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [measure_statistics.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measure_statistics.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [particle_analysis.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/particle_analysis.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
