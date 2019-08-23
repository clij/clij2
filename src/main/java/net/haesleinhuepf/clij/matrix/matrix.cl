
__kernel void multiply_matrix(DTYPE_IMAGE_OUT_2D dst_matrix, DTYPE_IMAGE_IN_2D src1,  DTYPE_IMAGE_IN_2D src2) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);

  int n_y = GET_IMAGE_IN_HEIGHT(src1);
  int n_x = GET_IMAGE_IN_WIDTH(src2);

  for (int j = 0; j < n_x; j ++) {
      float sum = 0;
      for (int i = 0; i < n_y; i ++) {
          int2 pos1 = (int2){i, j};
          int2 pos2 = (int2){j, i};
          sum = sum + READ_IMAGE_2D(src1, sampler, pos1).x * READ_IMAGE_2D(src2, sampler, pos2).x;
      }
      float out = sqrt(sum);
      int2 pos = (int2){get_global_id(0), j};
      WRITE_IMAGE_2D(dst_matrix, pos, CONVERT_DTYPE_OUT(out));
  }
}