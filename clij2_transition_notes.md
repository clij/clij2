

breaking backwards compatibility:

maximiumSphere and minimumSphere, maximumSliceBySliceSphere, minimumSliceBySliceSphere meanSphere and meanSliceBySliceSphere, medianBox, medianSphere, MedianSliceBySliceBox and MedianSliceBySliceSphere had kernel sizes as parameter (java)
mean2DSphere, mean3DSphere, MeanSliceBySliceSphere3D,... have radii as parameters
for macro users everythin stays the same

meanIJ was removed. Use meanSphere2D or meanSphere3D instead