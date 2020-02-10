## addImagesWeighted
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Calculates the sum of pairs of pixels x and y from images X and Y weighted with factors a and b.

<pre>f(x, y, a, b) = x * a + y * b</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_addImagesWeighted(Image summand1, Image summand2, Image destination, Number factor1, Number factor2);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer summand1 = clij2.push(summand1ImagePlus);
ClearCLBuffer summand2 = clij2.push(summand2ImagePlus);
destination = clij.create(summand1);
float factor1 = 1.0;
float factor2 = 2.0;
```

```
// Execute operation on GPU
clij2.addImagesWeighted(clij, summand1, summand2, destination, factor1, factor2);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(summand1);
clij2.release(summand2);
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [addImages3D.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/addImages3D.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [addImages.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/addImages.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [backgroundSubtraction.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/backgroundSubtraction.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [clij1_clij2_combination.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/clij1_clij2_combination.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
