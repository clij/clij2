## meanClosestSpotDistance
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Takes two binary images A and B with marked spots and determines for each spot in image A the closest spot in image B. Afterwards, it saves the average shortest distances from image A to image B as 'mean_closest_spot_distance_A_B' and from image B to image A as 'mean_closest_spot_distance_B_A' to the results table. The distance between B and A is only determined if the `bidirectional` checkbox is checked.

### Usage in ImageJ macro
```
Ext.CLIJx_meanClosestSpotDistance(Image spotsA, Image spotsB, Boolean bidirectional);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer spotsA = clij2.push(spotsAImagePlus);
ClearCLBuffer spotsB = clij2.push(spotsBImagePlus);
boolean bidirectional = true;
```

```
// Execute operation on GPU
double[] resultMeanClosestSpotDistance = clij2.meanClosestSpotDistance(clij, spotsA, spotsB, bidirectional);
```

```
//show result
System.out.println(resultMeanClosestSpotDistance);

// cleanup memory on GPU
clij2.release(spotsA);
clij2.release(spotsB);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
