__kernel void squared_sum_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst,
    DTYPE_IMAGE_IN_3D src,
    float mean_intensity
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  DTYPE_IN sum = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    float value = READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
    sum = sum + pow(value - mean_intensity, 2);
  }
  WRITE_IMAGE_2D(dst,(int2)(x,y), CONVERT_DTYPE_OUT(sum));
}

__kernel void masked_squared_sum_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst,
    DTYPE_IMAGE_IN_3D src,
    DTYPE_IMAGE_IN_3D src_mask,
    float mean_intensity
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  DTYPE_IN sum = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    if (READ_IMAGE_3D(src_mask,sampler,(int4)(x,y,z,0)).x > 0) {
        float value = READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
        sum = sum + pow(value - mean_intensity, 2);
    }
  }
  WRITE_IMAGE_2D(dst,(int2)(x,y), CONVERT_DTYPE_OUT(sum));
}