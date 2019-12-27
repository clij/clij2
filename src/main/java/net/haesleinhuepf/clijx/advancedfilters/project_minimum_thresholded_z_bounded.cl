
__kernel void project_minimum_thresholded_z_bounded(
    DTYPE_IMAGE_OUT_2D dst_min,
    DTYPE_IMAGE_IN_3D src,
    float threshold_intensity,
    int min_z,
    int max_z
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  float min = 0;

  int start = 0;
  if (min_z > start) {
    start = min_z;
  }

  int end = GET_IMAGE_IN_DEPTH(src) - 1;
  if (max_z < end) {
    end = max_z;
  }

  bool initialized = 0;
  for(int z = start; z <= end; z++)
  {
    float value = READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
    if ((threshold_intensity < value) && (value < min || initialized == 0)) {
      min = value;
      initialized = 1;
    }
  }
  WRITE_IMAGE_2D(dst_min,(int2)(x,y), CONVERT_DTYPE_OUT(min));
}