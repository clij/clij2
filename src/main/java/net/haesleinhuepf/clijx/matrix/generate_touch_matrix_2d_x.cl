
__kernel void generate_touch_matrix_2d(
    IMAGE_dst_matrix_TYPE dst_matrix,
    IMAGE_src_label_map_TYPE src_label_map
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);

  if (x > GET_IMAGE_WIDTH(src_label_map) - 2) {
    return;
  }
  if (y > GET_IMAGE_HEIGHT(src_label_map) - 2) {
    return;
  }

  int2 pos = (int2){x, y};
  float label = READ_src_label_map_IMAGE(src_label_map, sampler, pos).x;
  float labelx = READ_src_label_map_IMAGE(src_label_map, sampler, (pos + (int2)(1, 0))).x;
  float labely = READ_src_label_map_IMAGE(src_label_map, sampler, (pos + (int2)(0, 1))).x;

  if (label < labelx) {
    WRITE_dst_matrix_IMAGE(dst_matrix, ((int2)(label, labelx)), CONVERT_dst_matrix_PIXEL_TYPE(1));
  } else if (label > labelx) {
    WRITE_dst_matrix_IMAGE(dst_matrix, ((int2)(labelx, label)), CONVERT_dst_matrix_PIXEL_TYPE(1));
  }
  if (label < labely) {
    WRITE_dst_matrix_IMAGE(dst_matrix, ((int2)(label, labely)), CONVERT_dst_matrix_PIXEL_TYPE(1));
  } else if (label > labely) {
    WRITE_dst_matrix_IMAGE(dst_matrix, ((int2)(labely, label)), CONVERT_dst_matrix_PIXEL_TYPE(1));
  }
}
