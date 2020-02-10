## jaccardIndex
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Determines the overlap of two binary images using the Jaccard index.
A value of 0 suggests no overlap, 1 means perfect overlap.
The resulting Jaccard index is saved to the results table in the 'Jaccard_Index' column.
Note that the Sorensen-Dice coefficient can be calculated from the Jaccard index j using this formula:
<pre>s = f(j) = 2 j / (j + 1)</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_jaccardIndex(Image source1, Image source2);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer source1 = clij2.push(source1ImagePlus);
ClearCLBuffer source2 = clij2.push(source2ImagePlus);
```

```
// Execute operation on GPU
double resultJaccardIndex = clij2.jaccardIndex(clij, source1, source2);
```

```
//show result
System.out.println(resultJaccardIndex);

// cleanup memory on GPU
clij2.release(source1);
clij2.release(source2);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [measureOverlap.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/measureOverlap.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [jaccardIndex.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/jaccardIndex.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
