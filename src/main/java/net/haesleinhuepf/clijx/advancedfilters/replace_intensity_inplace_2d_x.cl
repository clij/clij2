
const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void replace_intensity_inplace_2d
(
  IMAGE_src_TYPE src,
  IMAGE_dst_TYPE dst,
  float to_be_replaced,
  float replacement
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);

  float current = (float)(READ_src_IMAGE(src,sampler,(int2)(i,j)).x);
  if (current == to_be_replaced) {
    WRITE_dst_IMAGE(dst, (int2)(i,j), CONVERT_dst_PIXEL_TYPE(replacement));
  }
}

