
__kernel void masked_squared_sum_2d_2d(
    DTYPE_IMAGE_OUT_2D dst,
    DTYPE_IMAGE_IN_2D src,
    DTYPE_IMAGE_IN_2D src_mask,
    float mean_intensity
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  float sum = 0;
  if (READ_IMAGE_2D(src_mask,sampler,(int2)(x,y)).x > 0) {
    float value = READ_IMAGE_2D(src,sampler,(int2)(x,y)).x;
    sum = pow(value - mean_intensity, 2);
  }
  WRITE_IMAGE_2D(dst,(int2)(x,y), CONVERT_DTYPE_OUT(sum));
}