
__kernel void generate_spotlist(DTYPE_IMAGE_IN_3D src, DTYPE_IMAGE_OUT_2D dst_point_list) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int sx = get_global_id(0);
  const int sy = get_global_id(1);
  const int sz = get_global_id(2);

  const int4 spos = (int4){sx, sy, sz, 0};
  const int index = ((int)READ_IMAGE_3D(src, sampler, spos).x) - 1;
  if (index < 0) { // background pixel
    return;
  }

  int n_dimensions = GET_IMAGE_IN_HEIGHT(dst_point_list);

  int2 pos = (int2){index, 0};
  WRITE_IMAGE_2D(dst_point_list, pos, (DTYPE_OUT)sx);

  if (n_dimensions > 1) {
    pos = (int2){index, 1};
    WRITE_IMAGE_2D(dst_point_list, pos, (DTYPE_OUT)sy);
  }
  if (n_dimensions > 2) {
    pos = (int2){index, 2};
    WRITE_IMAGE_2D(dst_point_list, pos, (DTYPE_OUT)sz);
  }
}