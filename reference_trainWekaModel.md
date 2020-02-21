## trainWekaModel
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

By Robert Haase (rhaase@mpi-cbg.de, based on work by 
 Verena Kaynig (verena.kaynig@inf.ethz.ch), 
 Ignacio Arganda-Carreras (iarganda@mit.edu),
 Albert Cardona (acardona@ini.phys.ethz.ch))

Trains a Weka model using functionality of Fijis Trainable Weka Segmentation plugin.
It takes a 3D feature stack (e.g. first plane original image, second plane blurred, third plane edge image)and trains a Weka model. This model will be saved to disc.
The given groundTruth image is supposed to be a label map where pixels with value 1 represent class 1, pixels with value 2 represent class 2 and so on. Pixels with value 0 will be ignored for training.

### Usage in ImageJ macro
```
Ext.CLIJx_trainWekaModel(Image featureStack3D, Image groundTruth2D, String saveModelFilename);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer featureStack3D = clij2.push(featureStack3DImagePlus);
ClearCLBuffer groundTruth2D = clij2.push(groundTruth2DImagePlus);
```

```
// Execute operation on GPU
clij2.trainWekaModel(clij, featureStack3D, groundTruth2D, saveModelFilename);
```

```
//show result

// cleanup memory on GPU
clij2.release(featureStack3D);
clij2.release(groundTruth2D);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [weka.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/weka.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [weka_segmentation.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/weka_segmentation.ijm)  




### License terms
Parts of the code CLIJ Weka were copied over from the Trainable_Segmentation repository (link above). Thus,  
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
