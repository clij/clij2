
__kernel void maximum_z_projection_bounded(
    IMAGE_dst_max_TYPE dst_max,
    IMAGE_src_TYPE src,
    int min_z,
    int max_z
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  float max = 0;

  int start = 0;
  if (min_z > start) {
    start = min_z;
  }

  int end = GET_IMAGE_DEPTH(src) - 1;
  if (max_z < end) {
    end = max_z;
  }

  for(int z = start; z <= end; z++)
  {
    float value = READ_src_IMAGE(src,sampler,(int4)(x,y,z,0)).x;
    if (value > max || z == start) {
      max = value;
    }
  }
  WRITE_dst_max_IMAGE(dst_max,(int2)(x,y), CONVERT_dst_max_TYPE(max));
}