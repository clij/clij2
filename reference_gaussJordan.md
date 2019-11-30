## gaussJordan
![Image](images/mini_clijx_logo.png)

Gauss Jordan elimination algorithm for solving linear equation systems. Ent the equation coefficients as an n*n sized image A and an n*1 sized image B:
<pre>a(1,1)*x + a(2,1)*y + a(3,1)+z = b(1)
a(2,1)*x + a(2,2)*y + a(3,2)+z = b(2)
a(3,1)*x + a(3,2)*y + a(3,3)+z = b(3)
</pre>
The results will then be given in an n*1 image with values [x, y, z].

Adapted from: 
https://github.com/qbunia/rodinia/blob/master/opencl/gaussian/gaussianElim_kernels.cl
L.G. Szafaryn, K. Skadron and J. Saucerman. "Experiences Accelerating MATLAB Systems
//Biology Applications." in Workshop on Biomedicine in Computing (BiC) at the International
//Symposium on Computer Architecture (ISCA), June 2009.

### Usage in ImageJ macro
```
Ext.CLIJx_gaussJordan(Image A_matrix, Image B_result_vector, Image solution_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer A_matrix = clijx.push(A_matrixImagePlus);
ClearCLBuffer B_result_vector = clijx.push(B_result_vectorImagePlus);
solution_destination = clij.create(A_matrix);
```

```
// Execute operation on GPU
clijx.gaussJordan(clij, A_matrix, B_result_vector, solution_destination);
```

```
//show result
solution_destinationImagePlus = clij.pull(solution_destination);
solution_destinationImagePlus.show();

// cleanup memory on GPU
A_matrix.close();
B_result_vector.close();
solution_destination.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
