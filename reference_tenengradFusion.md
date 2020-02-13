## tenengradFusion
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Fuses #n# image stacks using Tenengrads algorithm.

### Usage in ImageJ macro
```
Ext.CLIJx_tenengradFusion(Image input, Image destination, Number number_of_substacks, Number sigmaX, Number sigmaY, Number sigmaZ, Number exponent);
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
int number_of_substacks = 10;
float sigmaX = 1.0;
float sigmaY = 2.0;
float sigmaZ = 3.0;
float exponent = 4.0;
```

```
// Execute operation on GPU
clij2.tenengradFusion(clij, input, destination, number_of_substacks, sigmaX, sigmaY, sigmaZ, exponent);
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
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [fusion.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/fusion.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [fusion_x.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/fusion_x.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
