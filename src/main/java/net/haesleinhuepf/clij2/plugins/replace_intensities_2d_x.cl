
const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void replace_intensities_2d
(
  IMAGE_dst_TYPE dst, IMAGE_src_TYPE src,
  IMAGE_map_TYPE map
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);

  const int w = GET_IMAGE_WIDTH(src);
  const int h = GET_IMAGE_HEIGHT(src);

  int index = (int)(READ_src_IMAGE(src,sampler,(int2)(i,j)).x);
  int replacement = 0;
  if (index > 0) {
    replacement = (int)(READ_map_IMAGE(map,sampler,(int4)(index,0,0,0)).x);
  }
  WRITE_dst_IMAGE(dst, (int2)(i,j), CONVERT_dst_PIXEL_TYPE(replacement));
}

