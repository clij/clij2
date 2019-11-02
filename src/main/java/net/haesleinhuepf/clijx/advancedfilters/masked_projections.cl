

__kernel void mean_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst,
    DTYPE_IMAGE_IN_3D src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  DTYPE_IN sum = 0;
  int count = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    sum = sum + READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
    count++;
  }
  WRITE_IMAGE_2D(dst,(int2)(x,y), CONVERT_DTYPE_OUT(sum / count));
}


__kernel void min_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst_min,
    DTYPE_IMAGE_OUT_2D dst_mask,
    DTYPE_IMAGE_IN_3D mask,
    DTYPE_IMAGE_IN_3D src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  DTYPE_IN min = 0;
  float mask_value = 0;
  bool initial = true;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    DTYPE_IN binary = READ_IMAGE_3D(mask,sampler,(int4)(x,y,z,0)).x;

    if (binary != 0) {
        mask_value = 1;
        DTYPE_IN value = READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
        if (value < min || initial) {
          min = value;
          initial = false;
        }
    }
  }
  WRITE_IMAGE_2D(dst_min,(int2)(x,y), CONVERT_DTYPE_OUT(min));
  WRITE_IMAGE_2D(dst_mask,(int2)(x,y), CONVERT_DTYPE_OUT(mask_value));
}


__kernel void max_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst_max,
    DTYPE_IMAGE_OUT_2D dst_mask,
    DTYPE_IMAGE_IN_3D mask,
    DTYPE_IMAGE_IN_3D src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  DTYPE_IN max = 0;
  float mask_value = 0;
  bool initial = true;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    DTYPE_IN binary = READ_IMAGE_3D(mask,sampler,(int4)(x,y,z,0)).x;

    if (binary != 0) {
        mask_value = 1;
        DTYPE_IN value = READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
        if (value > max || initial) {
          max = value;
          initial = false;
        }
    }
  }
  WRITE_IMAGE_2D(dst_max,(int2)(x,y), CONVERT_DTYPE_OUT(max));
  WRITE_IMAGE_2D(dst_mask,(int2)(x,y), CONVERT_DTYPE_OUT(mask_value));
}