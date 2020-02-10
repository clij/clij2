## replaceIntensities
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Replaces integer intensities specified in a vector image. The vector image must be 3D with size (m, 1, 1) where m corresponds to the maximum intensity in the original image. Assuming the vector image contains values (0, 1, 0, 2) means: 
 * All pixels with value 0 (first entry in the vector image) get value 0
 * All pixels with value 1 get value 1
 * All pixels with value 2 get value 0
 * All pixels with value 3 get value 2


### Usage in ImageJ macro
```
Ext.CLIJx_replaceIntensities(Image input, Image new_values_vector, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input = clij2.push(inputImagePlus);
ClearCLBuffer new_values_vector = clij2.push(new_values_vectorImagePlus);
destination = clij.create(input);
```

```
// Execute operation on GPU
clij2.replaceIntensities(clij, input, new_values_vector, destination);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(input);
clij2.release(new_values_vector);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
