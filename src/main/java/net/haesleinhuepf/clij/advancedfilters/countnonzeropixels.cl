__kernel void count_non_zero_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst,
    DTYPE_IMAGE_IN_3D src,
    float tolerance
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  DTYPE_OUT count = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    if (fabs(((float) READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x)) > tolerance) {
        count = count + 1;
    }
  }
  WRITE_IMAGE_2D(dst,(int2)(x,y),count);
}
