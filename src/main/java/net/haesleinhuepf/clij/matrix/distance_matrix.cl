
__kernel void generate_distance_matrix(DTYPE_IMAGE_OUT_2D dst_matrix, DTYPE_IMAGE_IN_2D src_point_list1,  DTYPE_IMAGE_IN_2D src_point_list2) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);

  int n_dimensions = GET_IMAGE_IN_HEIGHT(src_point_list1);
  int n_points = GET_IMAGE_IN_WIDTH(src_point_list2);

  float positions[10];
  for (int i = 0; i < n_dimensions; i ++) {
      int2 pos = (int2){dx, i};
      positions[i] = READ_IMAGE_2D(src_point_list1, sampler, pos).x;
  }

  for (int j = 0; j < GET_IMAGE_IN_WIDTH(src_point_list2); j ++) {
      float sum = 0;
      for (int i = 0; i < n_dimensions; i ++) {
          int2 pos = (int2){j, i};
          sum = sum + pow(positions[i] - READ_IMAGE_2D(src_point_list2, sampler, pos).x, 2);
      }
      float out = sqrt(sum);
      int2 pos = (int2){get_global_id(0), j};
      WRITE_IMAGE_2D(dst_matrix, pos, CONVERT_DTYPE_OUT(out));
  }
}
