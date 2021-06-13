# CLIJ2: GPU-accelerated image processing for everyone

## Introduction
CLIJ2 is a GPU-accelerated image processing library for [ImageJ/Fiji](https://fiji.sc/), 
[Icy](http://icy.bioimageanalysis.org/), 
Matlab and Java. It comes with hundreds of operations for 
[filtering](https://clij.github.io/clij2-docs/reference__filter), 
[binarizing](https://clij.github.io/clij2-docs/reference__binary),
[labeling](https://clij.github.io/clij2-docs/reference__label),
[measuring](https://clij.github.io/clij2-docs/reference__measurement) in images,
[projections](https://clij.github.io/clij2-docs/reference__project),
[transformations](https://clij.github.io/clij2-docs/reference__transform) and 
[mathematical operations](https://clij.github.io/clij2-docs/reference__math) for images. 
While most of these are classical image processing operations, CLIJ2 also allows performing operations on 
[matrices](https://clij.github.io/clij2-docs/reference__matrix) potentially representing
[neighborhood relationships](https://clij.github.io/clij2-docs/reference__neighbor) between [cells](https://clij.github.io/clij2-docs/md/neighbors_of_neighbors) and pixels.

<img src="./images/clij25.gif" width="500">

Under the hood it uses [OpenCL](https://www.khronos.org/opencl/) but users don't have to learn a new programming language such as OpenCL, they can just use it transparently. 
Entry-evel coding skills are sufficient! 
Increased efforts were put on documentation, code examples, user-convenience, interoperability, and extensibility.
CLIJ is based on 
[ClearCL](http://github.com/ClearControl/ClearCL), 
[JOCL](http://github.com/gpu/JOCL), 
[Imglib2](https://github.com/imglib), 
[ImageJ](http://imagej.net) and 
[SciJava](https://github.com/SciJava).

**CLIJ2 is build on CLIJ. If you use it, please cite it:**

* Robert Haase, Loic Alain Royer, Peter Steinbach, Deborah Schmidt, 
Alexandr Dibrov, Uwe Schmidt, Martin Weigert, Nicola Maghelli, Pavel Tomancak, 
Florian Jug, Eugene W Myers. 
*CLIJ: GPU-accelerated image processing for everyone*. [Nat Methods 17, 5-6 (2020) doi:10.1038/s41592-019-0650-1](https://doi.org/10.1038/s41592-019-0650-1)

* Daniela Vorkel, Robert Haase. 
*GPU-accelerating ImageJ Macro image processing workflows using CLIJ*.
[arXiv preprint](https://arxiv.org/abs/2008.11799)

* Robert Haase, Akanksha Jain, St&eacute;phane Rigaud, Daniela Vorkel, Pradeep Rajasekhar, Theresa Suckert, Talley J. Lambert, Juan Nunez-Iglesias, Daniel P. Poole, Pavel Tomancak, Eugene W. Myers.
*Interactive design of GPU-accelerated Image Data Flow Graphs and cross-platform deployment using multi-lingual code generation*.
[bioRxiv preprint](https://www.biorxiv.org/content/10.1101/2020.11.19.386565v1)

If you search for support, please open a thread on the [image.sc](https://image.sc) forum.

[![Image.sc forum](https://img.shields.io/badge/dynamic/json.svg?label=forum&url=https%3A%2F%2Fforum.image.sc%2Ftag%2Fclij.json&query=%24.topic_list.tags.0.topic_count&colorB=brightgreen&suffix=%20topics&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAABPklEQVR42m3SyyqFURTA8Y2BER0TDyExZ+aSPIKUlPIITFzKeQWXwhBlQrmFgUzMMFLKZeguBu5y+//17dP3nc5vuPdee6299gohUYYaDGOyyACq4JmQVoFujOMR77hNfOAGM+hBOQqB9TjHD36xhAa04RCuuXeKOvwHVWIKL9jCK2bRiV284QgL8MwEjAneeo9VNOEaBhzALGtoRy02cIcWhE34jj5YxgW+E5Z4iTPkMYpPLCNY3hdOYEfNbKYdmNngZ1jyEzw7h7AIb3fRTQ95OAZ6yQpGYHMMtOTgouktYwxuXsHgWLLl+4x++Kx1FJrjLTagA77bTPvYgw1rRqY56e+w7GNYsqX6JfPwi7aR+Y5SA+BXtKIRfkfJAYgj14tpOF6+I46c4/cAM3UhM3JxyKsxiOIhH0IO6SH/A1Kb1WBeUjbkAAAAAElFTkSuQmCC)](https://forum.image.sc/tag/clij)

## Overview
![Image](images/installation_ok.png)

* [Introduction to the graphical user interface clij2-assistant](https://clij.github.io/assistant/getting_started)
* Installation
  * [Fiji](https://clij.github.io/clij2-docs/installationInFiji)
  * [Icy](https://github.com/clij/clicy)
  * [ImageJ](https://clij.github.io/clij2-imagej1/)
  * [Matlab](https://github.com/clij/clatlab)
  * [Maven/Java](https://clij.github.io/clij2-docs/dependingViaMaven)
* [Cheat sheets](https://clij.github.io/clij2-docs/CLIJ2-cheatsheet_V3.pdf)
* [Reference](https://clij.github.io/clij2-docs/reference)
* [Frequently asked questions](https://clij.github.io/clij2-docs/faq)
* [Release notes](https://github.com/clij/clij2/releases)
* [Community guidelines](https://clij.github.io/clij2-docs/community_guidelines)
* [Release cycle](https://clij.github.io/clij2-docs/release_cycle)    
* Future perspectives
  * [CLIJx-Assistant](https://clij.github.io/clijx-assistant)
  * [clEsperanto](http://clesperanto.net)
  * [The big dictionary](https://clij.github.io/clij2-docs/dictionary_clesperanto)
* [Support](https://image.sc)

## Tutorials
### The assistant - graphical user interface
* [Building workflows](https://clij.github.io/assistant/getting_started)
* [Saving and loading workflows](https://clij.github.io/assistant/save_and_load)
* [Undo parameter changes](https://clij.github.io/assistant/undo)

### Scripting CLIJ  
* [Basics of GPU-accelerated image processing and memory management](https://clij.github.io/clij2-docs/md/basics/)
* [Basic image processing workflows](https://clij.github.io/clij2-docs/md/basic_image_processing/)
* [Binary images, label images and parametric images](https://clij.github.io/clij2-docs/md/image_types/)
* [Introduction for ImageJ Macro users](https://clij.github.io/clij2-docs/macro_intro)
* [Processing multi-channel time-lapse data](https://clij.github.io/clij2-docs/md/process_multichannel_timelapse/)
* Advanced clij programming
  * [Introduction for Java developers](https://clij.github.io/clij2-docs/api_intro)
  * [CLIJ2 development](https://clij.github.io/clij2-docs/development)
  * [CLIJ2 plugin template](https://github.com/clij/clij2-plugin-template)

### Filtering images
* [Image filtering using the assitant](https://clij.github.io/assistant/filtering)
* [Gamma correction using the assitant](https://clij.github.io/assistant/gamma_correction)
* [Gaussian blur](https://clij.github.io/clij2-docs/md/blur/)
* [Drosophila embryo cell counting](https://clij.github.io/clij2-docs/md/drosophila_max_cylinder_projection/)

### Transforming images
* [Crop, Pan & zoom using the assitant](https://clij.github.io/assistant/crop_pan_zoom)
* [Cylinder projection using the assitant](https://clij.github.io/assistant/cylinder_projection)
* [Sphere projection using the assitant](https://clij.github.io/assistant/sphere_projection)
* [Crop and paste images](https://clij.github.io/clij2-docs/md/crop_and_paste/)
* [Maximum projections](https://clij.github.io/clij2-docs/md/maximumProjection/)
* [Warp images](https://clij.github.io/clij2-docs/md/applyVectorFieldMD/)

### Segmentation and labelling
* [Spot detection](https://clij.github.io/clij2-docs/md/spot_detection/)
* [Nuclei segmentation using the assitant](https://clij.github.io/assistant/segmentation_nuclei)
* [Cell segmentation based on membranes using the assitant](https://clij.github.io/assistant/segmentation_cells)
* [Optimize parameters for binarization using the assitant](https://clij.github.io/assistant/parameter_optimization)
* [Labeling](https://clij.github.io/clij2-docs/md/labeling/)
* [3D Image Segmentation](https://clij.github.io/clij2-docs/md/image_segmentation_3d/)
* [Binary images](https://clij.github.io/clij2-docs/md/binary_processing/)
* [Working with regions of interest (ROIs)](https://clij.github.io/clij2-docs/md/working_with_rois/)
* [Adding label outlines and numbers as overlay](https://clij.github.io/clij2-docs/md/outlines_numbers_overlay/)  
* [Voronoi diagrams](https://clij.github.io/clij2-docs/md/voronoi/)
* [Voronoi Otsu labeling](https://clij.github.io/clij2-docs/md/voronoi_otsu_labeling/)  
* [Classic watershed (MorpholibJ extension)](https://clij.github.io/clij2-docs/md/morpholibj_classic_watershed/)  
* [Label maps and voronoi diagrams](https://clij.github.io/clij2-docs/md/labelmap_voronoi)
* [Custom clij functions for image segmentation](https://clij.github.io/clij2-docs/md/custom_clij_macro_functions/)

### Working with matrices and graphs
* [Multiply vectors and matrices](https://clij.github.io/clij2-docs/md/multiply_vectors_matrices)
* [Matrix multiplication](https://clij.github.io/clij2-docs/md/matrix_multiply/)
* [Spots, pointlists, matrices and tables](https://clij.github.io/clij2-docs/md/spots_pointlists_matrices_tables/)
* [Filtering in graphs](https://clij.github.io/clij2-docs/md/filtering_in_graphs/)
* [Filtering between touching neighbors](https://clij.github.io/clij2-docs/md/mean_of_touching_neighbors)
* [Neighbors of neighbors](https://clij.github.io/clij2-docs/md/neighbors_of_neighbors/)
* [Neighborhood definitions](https://clij.github.io/clij2-docs/md/neighborhood_definitions/)  
* [Tribolium embryo morphometry](https://clij.github.io/clij2-docs/md/tribolium_morphometry/)
* [Superpixel segmentation](https://clij.github.io/clij2-docs/md/superpixel_segmentation/)
  
### Statistics and measurements
* [Images statistics](https://clij.github.io/clij2-docs/md/image_statistics/)
* [Pixel statistics on labeled images](https://clij.github.io/clij2-docs/md/measure_statistics/)
* [Parametric images](https://clij.github.io/clij2-docs/md/parametric_images/)
* [Count neighbors](https://clij.github.io/clij2-docs/md/count_neighbors/)
* [Mean of touching neighbors](https://clij.github.io/clij2-docs/md/mean_of_touching_neighbors/)  
* [Working with tables](https://clij.github.io/clij2-docs/md/tables/)
* [Colocalisation measurements using Jaccard index and Sorensen/Dice coefficient](https://clij.github.io/clij2-docs/md/measure_overlap/)
* [Measuring presence of objects between channels](https://clij.github.io/clij2-docs/md/count_overlap_between_channels/)

### Benchmarking
* [Measure speedup](https://clij.github.io/clij2-docs/md/benchmarking/)
* [Detailed time tracing](https://clij.github.io/clij2-docs/md/time_tracing/)
* [Comparing and benchmarking workflows](https://clij.github.io/clij2-docs/md/compare_workflows/)
* [Comparing image rotation ImageJ vs. CLIJ](https://clij.github.io/clij2-docs/md/rotate_comparison_IJ_CLIJ/)

### Reproducibility / interoperability using the assistant
* [Export workflows as ImageJ Script](https://clij.github.io/assistant/macro_export)
* [Export human readable protocols and ImageJ Macro Markdown notebooks](https://clij.github.io/assistant/supplementary_methods_section_generator)
* [Export as Icy Protocol](https://clij.github.io/assistant/icy_protocol_export)

## Media (external resources)
* [CLIJx-Assistant: My favourite image analysis tool, by Neubias members, by Elnaz Fazeli](https://analyticalscience.wiley.com/do/10.1002/was.0004000132)
* [Euro-BioImaging Virtual Pub: Robert Haase, TU Dresden, on "GPU-accelerated image analysis in Fiji and Napari"](https://www.youtube.com/watch?v=MERVnf5_QkI)
* [Imaging ONE World: GPU-accelerated 3D image processing for everyone](https://www.youtube.com/watch?v=meQvA4jlJSU)
* [I2K2020: Designing GPU-accelerated Image Data Flow Graphs for CLIJ2 and clEsperanto](https://www.youtube.com/watch?v=EYsfJ9W6gHw)
* [GPU-accelerated image processing for the life-sciences, guest lecture at Computer Science Faculty, TU Dresden](https://www.youtube.com/watch?v=_2yBn8MfP-I)
* [How CLIJ2 can make your bio-image analysis workflows incredibly fast, FocalPlane](https://focalplane.biologists.com/2020/07/14/how-clij-can-make-your-image-analysis-incredibly-fast/)
* [YouTube NEUBIAS Academy @home](https://youtu.be/uMj0OS1TiQE)
* [NEUBIAS Symposium 2020, Invited talk, Bordeaux](https://git.mpi-cbg.de/rhaase/clij_neubias_2020)
* [NEUBIAS Training School TS14, Teaching session, Bordeaux](https://git.mpi-cbg.de/rhaase/neubias_ts14)
* [MTZ Image Processing Seminar, Teaching session, TU Dresden](https://git.mpi-cbg.de/rhaase/clij_mtz_2020)
* [Quantitative BioImaging Conference 2020, Selected talk, Oxford](slides/2020-01-QBI_SmartMicroscopy_Haase_V2.pdf)
* [Fast, Faster, CLIJ, News, Center for Systems Biology Dresden](https://www.csbdresden.de/news-events/news/article/2019/11/18/fast-faster-clij/)
* [CLIJ: GPU-accelerated image processing for everyone, Article, Nature Methods](https://www.nature.com/articles/s41592-019-0650-1)
* [NEUBIAS Training School TS13, Teaching session, Porto](https://git.mpi-cbg.de/rhaase/neubias_ts14)

## Example code
* [ImageJ macro](https://github.com/clij/clij2-docs/tree/master/src/main/macro)
* [Icy javascript](https://github.com/clij/clicy/tree/master/src/main/javascript)
* [Icy protocols](https://github.com/clij/clicy/tree/master/src/main/protocols)
* [Matlab](https://github.com/clij/clatlab/tree/master/src/main/matlab)
* [Java](https://github.com/clij/clij2-docs/tree/master/src/main/java/net/haesleinhuepf/cli2/examples)
* [ImageJ Beanshell](https://github.com/clij/clij2-docs/tree/master/src/main/beanshell)
* [ImageJ Groovy](https://github.com/clij/clij2-docs/tree/master/src/main/groovy)
* [ImageJ Javascript](https://github.com/clij/clij2-docs/tree/master/src/main/javascript)
* [ImageJ Jython](https://github.com/clij/clij2-docs/tree/master/src/main/jython)

## Further reading (external resources)
* [GPU Image Processing using OpenCL, Harald Scheidl, TowardsDataScience](https://towardsdatascience.com/get-started-with-gpu-image-processing-15e34b787480)
* [OpenCL SGEMM tuning for Kepler, Cedric Nugteren, SURFsara](https://cnugteren.github.io/tutorial/pages/page1.html)
* [OpenCL: A Hands-on Introduction, Tim Mattson, Intel](https://www.nersc.gov/assets/pubs_presos/MattsonTutorialSC14.pdf)
* [OpenCL 1.2 Quick Reference, Khronos Group](https://www.khronos.org/files/opencl-1-2-quick-reference-card.pdf)
* [OpenCL 1.2 Specifications, Khronos Group](https://www.khronos.org/registry/OpenCL/specs/opencl-1.2.pdf)

## FAQ / support
* [CLIJ2 - CLIJ2.5 transition guide](https://clij.github.io/clij2-docs/clij25_transition_notes)
* CLIJ versus CLIJ2
  * [Combining CLIJ and CLIJ2](https://clij.github.io/clij2-docs/md/clij1_clij2_combination/)
  * [CLIJ documentation (archived)](https://clij.github.io/clij-docs)
  * [CLIJ BioRxiv preprint (archived)](https://doi.org/10.1101/660704)
  * [What's different between CLIJ1, CLIJ2 and CLIJx?](https://clij.github.io/clij2-docs/clij12xAPIcomparison)
  * [CLIJ - CLIJ2 transition guide (under construction)](https://clij.github.io/clij2-docs/clij2_transition_notes)
* [Troubleshooting](https://clij.github.io/clij2-docs/troubleshooting)
* [Support](https://image.sc)
* [Imprint](https://clij.github.io/imprint)

## Acknowledgements
Development of CLIJ is a community effort. We would like to thank everybody who helped developing and testing. In particular thanks goes to
Alex Herbert (University of Sussex),
Bert Nitzsche (PoL TU Dresden),
Bertrand Vernay (IGBMC, Strasbourg)
Bram van den Broek (Netherlands Cancer Institute),
Brenton Cavanagh (RCSI),
Brian Northan (True North Intelligent Algorithms),
Bruno C. Vellutini (MPI CBG),
Christian Tischer (EMBL Heidelberg),
Curtis Rueden (UW-Madison LOCI),
Damir Krunic (DKFZ),
Daniela Vorkel (MPI CBG),
Daniel J. White (GE),
Eduardo Conde-Sousa (University of Porto),
Elnaz Fazeli (BIU, University of Helsinki),
Erick Ratamero (The Jackson Laboratory),
Eugene W. Myers (MPI CBG Dresden)
Florian Jug (MPI CBG Dresden),
Gaby G. Martins (IGC),
Gayathri Nadar (MPI CBG Dresden),
Guillaume Witz (Bern University),
Giovanni Cardone (MPI Biochem),
Irene Seijo Barandiaran (MPI CBG Dresden),
Jan Brocher (Biovoxxel), 
Jean-Yves Tinevez (Institute Pasteur),
Jim Rowe (University of Cambridge),
Johannes Girstmair (MPI CBG),
Juergen Gluch (Fraunhofer IKTS),
Kisha Sivanathan (Harvard Medical School Boston),
Kota Miura,
Laurent Thomas (Acquifer),
Lior Pytowski (University of Oxford),
Marion Louveaux (Institut Pasteur Paris),
Matthew Foley (University of Sydney),
Matthias Arzt (MPI-CBG),
Nico Stuurman (UCSF),
Nik Cordes (Los Alamos National Laboratory),
Noreen Walker (MPI CBG Dresden),
Ofra Golani (Weizmann Institute of Science),
Pavel Tomancak (MPI CBG Dresden),
Pradeep Rajasekhar (Monash University Melbourne),
Patrick Dummer, 
Peter Haub,
Pete Bankhead (University of Edinburgh),
Pit Kludig,
Pradeep Rajasekhar (Monash University),
Rita Fernandes (University of Porto),
Romain Guiet (EPFL Lausanne),
Ruth Whelan-Jeans,
Sebastian Munck (VIB Leuven),
Si&acirc;n Culley (LMCB MRC),
Stein R&oslash;rvik,
St&eacute;phane Dallongeville (Institut Pasteur)
St&eacute;phane~Rigaud (Institut Pasteur Paris),
Tanner Fadero (UNC-Chapel Hill),
Theresa Suckert (OncoRay, TU Dresden),
Thomas Irmer (Zeiss),
Tobias Pietzsch (MPI-CBG),
Wilson Adams (VU Biophotonics)

R.H. was supported by the German Federal Ministry of
Research and Education (BMBF) under the code 031L0044
(Sysbio II) and D.S. received support from the German
Research Foundation (DFG) under the code JU3110/1-1.
P.T. was supported by the European Regional
Development Fund in the IT4Innovations national
supercomputing center-path to exascale project,
project number CZ.02.1.01/0.0/0.0/16_013/0001791
within the Operational Programme Research, Development
and Education.
We also acknowledge the support by the Deutsche 
Forschungsgemeinschaft (DFG, German Research Foundation) 
under Germanys Excellence Strategy EXC2068 - Cluster of 
Excellence Physics of Life of TU Dresden.

[Imprint](https://clij.github.io/imprint)
  


