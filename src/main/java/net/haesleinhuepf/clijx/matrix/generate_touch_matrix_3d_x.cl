
__kernel void generate_touch_matrix_3d(
    IMAGE_dst_matrix_TYPE dst_matrix,
    IMAGE_src_label_map_TYPE src_label_map
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  if (x > GET_IMAGE_WIDTH(src_label_map) - 2) {
    return;
  }
  if (y > GET_IMAGE_HEIGHT(src_label_map) - 2) {
    return;
  }
  if (z > GET_IMAGE_DEPTH(src_label_map) - 2) {
    return;
  }

  int4 pos = (int4){x, y, z, 0};
  float label = READ_src_label_map_IMAGE(src_label_map, sampler, pos).x;
  float labelx = READ_src_label_map_IMAGE(src_label_map, sampler, (pos + (int4)(1, 0, 0, 0))).x;
  float labely = READ_src_label_map_IMAGE(src_label_map, sampler, (pos + (int4)(0, 1, 0, 0))).x;
  float labelz = READ_src_label_map_IMAGE(src_label_map, sampler, (pos + (int4)(0, 0, 1, 0))).x;

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
  if (label < labelz) {
    WRITE_dst_matrix_IMAGE(dst_matrix, ((int2)(label, labelz)), CONVERT_dst_matrix_PIXEL_TYPE(1));
  } else if (label > labelz) {
    WRITE_dst_matrix_IMAGE(dst_matrix, ((int2)(labelz, label)), CONVERT_dst_matrix_PIXEL_TYPE(1));
  }
}
