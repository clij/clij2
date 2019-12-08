## sorensenDiceCoefficient
![Image](images/mini_clijx_logo.png)

Determines the overlap of two binary images using the Sorensen-Dice coefficent.
A value of 0 suggests no overlap, 1 means perfect overlap.
The Sorensen-Dice coefficient is saved in the colum 'Sorensen_Dice_coefficient'.
Note that the Sorensen-Dice coefficient s can be calculated from the Jaccard index j using this formula:
<pre>s = f(j) = 2 j / (j + 1)</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_sorensenDiceCoefficient(Image source1, Image source2);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source1 = clijx.push(source1ImagePlus);
ClearCLBuffer source2 = clijx.push(source2ImagePlus);
```

```
// Execute operation on GPU
double resultSorensenDiceCoefficient = clijx.sorensenDiceCoefficient(clij, source1, source2);
```

```
//show result
System.out.println(resultSorensenDiceCoefficient);

// cleanup memory on GPU
source1.close();
source2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [jaccardIndex.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/jaccardIndex.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
