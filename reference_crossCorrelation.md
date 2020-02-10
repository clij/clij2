## crossCorrelation
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Performs cross correlation analysis between two images. The second image is shifted by deltaPos in the given dimension. The cross correlation coefficient is calculated for each pixel in a range around the given pixel with given radius in the given dimension. Together with the original images it is recommended to hand over mean filtered images using the same radius.  

### Usage in ImageJ macro
```
Ext.CLIJx_crossCorrelation(Image input1, Image meanInput1, Image input2, Image meanInput2, Image destination, Number radius, Number deltaPos, Number dimension);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer input1 = clij2.push(input1ImagePlus);
ClearCLBuffer meanInput1 = clij2.push(meanInput1ImagePlus);
ClearCLBuffer input2 = clij2.push(input2ImagePlus);
ClearCLBuffer meanInput2 = clij2.push(meanInput2ImagePlus);
destination = clij.create(input1);
int radius = 10;
int deltaPos = 20;
int dimension = 30;
```

```
// Execute operation on GPU
clij2.crossCorrelation(clij, input1, meanInput1, input2, meanInput2, destination, radius, deltaPos, dimension);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(input1);
clij2.release(meanInput1);
clij2.release(input2);
clij2.release(meanInput2);
clij2.release(destination);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
