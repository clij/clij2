## addImages
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Calculates the sum of pairs of pixels x and y of two images X and Y.

<pre>f(x, y) = x + y</pre>

### Usage in ImageJ macro
```
Ext.CLIJx_addImages(Image summand1, Image summand2, Image destination);
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
```

```
// Execute operation on GPU
clij2.addImages(clij, summand1, summand2, destination);
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
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [virtual_ablation.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/virtual_ablation.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [addImages.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/addImages.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [backgroundSubtraction.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/backgroundSubtraction.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [clij1_clij2_combination.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/clij1_clij2_combination.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [rgb_invert_black_white.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/rgb_invert_black_white.ijm)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [addImages_.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/addImages_.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
