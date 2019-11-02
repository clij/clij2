
__kernel void generate_touch_matrix_3d(DTYPE_IMAGE_OUT_2D dst_matrix,
  DTYPE_IMAGE_IN_3D src_label_map) {
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
  float label = READ_IMAGE_3D(src_label_map, sampler, pos).x;
  float labelx = READ_IMAGE_3D(src_label_map, sampler, (pos + (int4)(1, 0, 0, 0))).x;
  float labely = READ_IMAGE_3D(src_label_map, sampler, (pos + (int4)(0, 1, 0, 0))).x;
  float labelz = READ_IMAGE_3D(src_label_map, sampler, (pos + (int4)(0, 0, 1, 0))).x;

  if (label < labelx) {
    WRITE_IMAGE_2D(dst_matrix, ((int2)(label, labelx)), CONVERT_DTYPE_OUT(1));
  } else if (label > labelx) {
    WRITE_IMAGE_2D(dst_matrix, ((int2)(labelx, label)), CONVERT_DTYPE_OUT(1));
  }
  if (label < labely) {
    WRITE_IMAGE_2D(dst_matrix, ((int2)(label, labely)), CONVERT_DTYPE_OUT(1));
  } else if (label > labely) {
    WRITE_IMAGE_2D(dst_matrix, ((int2)(labely, label)), CONVERT_DTYPE_OUT(1));
  }
  if (label < labelz) {
    WRITE_IMAGE_2D(dst_matrix, ((int2)(label, labelz)), CONVERT_DTYPE_OUT(1));
  } else if (label > labelz) {
    WRITE_IMAGE_2D(dst_matrix, ((int2)(labelz, label)), CONVERT_DTYPE_OUT(1));
  }
}

__kernel void generate_touch_matrix_2d(DTYPE_IMAGE_OUT_2D dst_matrix,
  DTYPE_IMAGE_IN_2D src_label_map) {
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
  float label = READ_IMAGE_2D(src_label_map, sampler, pos).x;
  float labelx = READ_IMAGE_2D(src_label_map, sampler, (pos + (int2)(1, 0))).x;
  float labely = READ_IMAGE_2D(src_label_map, sampler, (pos + (int2)(0, 1))).x;

  if (label < labelx) {
    WRITE_IMAGE_2D(dst_matrix, ((int2)(label, labelx)), CONVERT_DTYPE_OUT(1));
  } else if (label > labelx) {
    WRITE_IMAGE_2D(dst_matrix, ((int2)(labelx, label)), CONVERT_DTYPE_OUT(1));
  }
  if (label < labely) {
    WRITE_IMAGE_2D(dst_matrix, ((int2)(label, labely)), CONVERT_DTYPE_OUT(1));
  } else if (label > labely) {
    WRITE_IMAGE_2D(dst_matrix, ((int2)(labely, label)), CONVERT_DTYPE_OUT(1));
  }
}
