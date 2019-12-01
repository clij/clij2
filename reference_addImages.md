## addImages
![Image](images/mini_clij1_logo.png)

Calculates the sum of pairs of pixels x and y of two images X and Y.

<pre>f(x, y) = x + y</pre>

### Usage in ImageJ macro
```
Ext.CLIJ_addImages(Image summand1, Image summand2, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer summand1 = clijx.push(summand1ImagePlus);
ClearCLBuffer summand2 = clijx.push(summand2ImagePlus);
destination = clij.create(summand1);
```

```
// Execute operation on GPU
clijx.addImages(clij, summand1, summand2, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
summand1.close();
summand2.close();
destination.close();
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [addImages.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/addImages.ijm)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [backgroundSubtraction.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/backgroundSubtraction.ijm)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [addImages.py](https://github.com/clij/clij-docs/blob/master/src/main/jython/addImages.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
