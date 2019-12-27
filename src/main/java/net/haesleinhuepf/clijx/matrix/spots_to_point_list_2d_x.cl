
__kernel void spots_to_point_list_2d (
    IMAGE_src_TYPE src,
    IMAGE_dst_point_list_TYPE dst_point_list
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int sx = get_global_id(0);
  const int sy = get_global_id(1);

  const int2 spos = (int2){sx, sy};
  const int index = ((int)READ_src_IMAGE(src, sampler, spos).x) - 1;
  if (index < 0) { // background pixel
    return;
  }

  int n_dimensions = GET_IMAGE_HEIGHT(dst_point_list);

  int2 pos = (int2){index, 0};
  WRITE_dst_point_list_IMAGE(dst_point_list, pos, CONVERT_dst_point_list_PIXEL_TYPE(sx));

  if (n_dimensions > 1) {
    pos = (int2){index, 1};
    WRITE_dst_point_list_IMAGE(dst_point_list, pos, CONVERT_dst_point_list_PIXEL_TYPE(sy));
  }
}