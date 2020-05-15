
__kernel void depth_projection(
    IMAGE_dst_depth_TYPE dst_depth,
    IMAGE_src_TYPE src,
    IMAGE_lut_TYPE lut,
    float min_display_intensity,
    float max_display_intensity
) {
  const sampler_t sampler =  CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP | CLK_FILTER_LINEAR;
  //CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  float max = 0;
  float max_z = 0;

  for(float z = 0; z < GET_IMAGE_DEPTH(src); z += GET_IMAGE_DEPTH(src) / 255.0 )
  {
    float value = READ_IMAGE(src,sampler,(float4)(x, y,z,0)).x;
    if (value > max || z == 0) {
      max = value;
      max_z = z;
    }
  }

  float intensity = (max - min_display_intensity) / (max_display_intensity - min_display_intensity);
  float relative_z = max_z / (GET_IMAGE_DEPTH(src)-1);

  if (intensity < 0) {
    intensity = 0;
  }
  if (intensity > 1) {
    intensity = 1;
  }
  if (relative_z < 0) {
    relative_z = 0;
  }
  if (relative_z > 1) {
    relative_z = 1;
  }

  int lookup_index = 255.0 * relative_z;
  //if (x == 255 && y == 255) {
  //  printf("gello %f \n", intensity );
  //  printf("gello %f \n", relative_z );
  //  printf("gello %d \n", lookup_index );
  //}

  float r = READ_IMAGE(lut, sampler, (int4)(lookup_index, 0, 0, 0)).x * intensity;
  float g = READ_IMAGE(lut, sampler, (int4)(lookup_index, 0, 1, 0)).x * intensity;
  float b = READ_IMAGE(lut, sampler, (int4)(lookup_index, 0, 2, 0)).x * intensity;

  WRITE_IMAGE(dst_depth,POS_dst_depth_INSTANCE(x, y, 0, 0), CONVERT_dst_depth_PIXEL_TYPE(r));
  WRITE_IMAGE(dst_depth,POS_dst_depth_INSTANCE(x, y, 1, 0), CONVERT_dst_depth_PIXEL_TYPE(g));
  WRITE_IMAGE(dst_depth,POS_dst_depth_INSTANCE(x, y, 2, 0), CONVERT_dst_depth_PIXEL_TYPE(b));
}