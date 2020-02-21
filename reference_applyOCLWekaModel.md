## applyOCLWekaModel
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

By Robert Haase (rhaase@mpi-cbg.de, based on work by 
 Verena Kaynig (verena.kaynig@inf.ethz.ch), 
 Ignacio Arganda-Carreras (iarganda@mit.edu),
 Albert Cardona (acardona@ini.phys.ethz.ch))

Applies a Weka model which was saved as OpenCL file. Train your model with trainWekaModel to save it as OpenCL file.
It takes a 3D feature stack (e.g. first plane original image, second plane blurred, third plane edge image)and applies a pre-trained a Weka model. Take care that the feature stack has been generated in the sameway as for training the model!

### Usage in ImageJ macro
```
Ext.CLIJx_applyOCLWekaModel(Image featureStack3D, Image prediction2D_destination, String loadModelFilename);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer featureStack3D = clij2.push(featureStack3DImagePlus);
prediction2D_destination = clij.create(featureStack3D);
```

```
// Execute operation on GPU
clij2.applyOCLWekaModel(clij, featureStack3D, prediction2D_destination, loadModelFilename);
```

```
//show result
prediction2D_destinationImagePlus = clij2.pull(prediction2D_destination);
prediction2D_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(featureStack3D);
clij2.release(prediction2D_destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [weka.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/weka.ijm)  




### License terms
Parts of the code of CLIJ Weka were copied over from the Trainable_Segmentation repository (link above). Thus,  
 this code is licensed GPL2 as well.  
  
  License: GPL  
  
  This program is free software; you can redistribute it and/or  
  modify it under the terms of the GNU General Public License 2  
  as published by the Free Software Foundation.  
  
  This program is distributed in the hope that it will be useful,  
  but WITHOUT ANY WARRANTY; without even the implied warranty of  
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  
  GNU General Public License for more details.  
  
  You should have received a copy of the GNU General Public License  
  along with this program; if not, write to the Free Software  
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.  
  Authors: Verena Kaynig (verena.kaynig@inf.ethz.ch), Ignacio Arganda-Carreras (iarganda@mit.edu)  
           Albert Cardona (acardona@ini.phys.ethz.ch)

[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
