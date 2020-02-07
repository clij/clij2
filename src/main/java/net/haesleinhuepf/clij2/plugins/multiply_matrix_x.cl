
__kernel void multiply_matrix(
    IMAGE_dst_matrix_TYPE dst_matrix,
    IMAGE_src1_TYPE src1,
    IMAGE_src2_TYPE src2
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  int n_x = GET_IMAGE_WIDTH(src1);

  float sum = 0;
  for (int i = 0; i < n_x; i ++) {
      sum = sum + READ_src1_IMAGE(src1, sampler, POS_src1_INSTANCE(i, dy,0,0)).x * READ_src2_IMAGE(src2, sampler, POS_src2_INSTANCE(dx, i,0,0)).x;
  }
  float out = sum;
  WRITE_dst_matrix_IMAGE(dst_matrix, POS_dst_matrix_INSTANCE(dx, dy, 0, 0), CONVERT_dst_matrix_PIXEL_TYPE(out));

}

__kernel void multiply_matrix_fast_x(
    IMAGE_dst_matrix_TYPE dst_matrix,
    IMAGE_src1_TYPE src1,
    IMAGE_src2_TYPE src2
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dy = get_global_id(1);
  int n_x = GET_IMAGE_WIDTH(src1);

  float local_mem[MAX_ARRAY_SIZE];

  for (int i = 0; i < n_x; i ++) {
    if (i >= MAX_ARRAY_SIZE) {
      break;
    }
    local_mem[i] = READ_src1_IMAGE(src1, sampler, POS_src1_INSTANCE(i, dy,0,0)).x;
  }

  int max_width_output = GET_IMAGE_WIDTH(dst_matrix);

  for (int dx = 0; dx < max_width_output; dx++) {
      float sum = 0;
      for (int i = 0; i < n_x; i ++) {
        float value1 = 0;
        if (i < MAX_ARRAY_SIZE) {
          value1 = local_mem[i];
        } else {
          value1 = READ_src1_IMAGE(src1, sampler, POS_src1_INSTANCE(i, dy,0,0)).x;
        }
        if (value1 != 0) {
          sum = sum + value1 * READ_src2_IMAGE(src2, sampler, POS_src2_INSTANCE(dx, i,0,0)).x;
        }
      }
      float out = sum;
      WRITE_dst_matrix_IMAGE(dst_matrix, POS_dst_matrix_INSTANCE(dx, dy, 0, 0), CONVERT_dst_matrix_PIXEL_TYPE(out));
  }
}

__kernel void multiply_matrix_fast_y(
    IMAGE_dst_matrix_TYPE dst_matrix,
    IMAGE_src1_TYPE src1,
    IMAGE_src2_TYPE src2
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  int n_x = GET_IMAGE_WIDTH(src1);

  float local_mem[MAX_ARRAY_SIZE];

  for (int i = 0; i < n_x; i ++) {
    if (i >= MAX_ARRAY_SIZE) {
      break;
    }
    local_mem[i] = READ_src2_IMAGE(src2, sampler, POS_src2_INSTANCE(dx, i,0,0)).x;
  }

  int max_width_output = GET_IMAGE_WIDTH(dst_matrix);

  for (int dy = 0; dy < max_width_output; dy++) {
      float sum = 0;
      for (int i = 0; i < n_x; i ++) {
        float value1 = 0;
        if (i < MAX_ARRAY_SIZE) {
          value1 = local_mem[i];
        } else {
          value1 = READ_src2_IMAGE(src2, sampler, POS_src2_INSTANCE(dx, i,0,0)).x;
        }
        if (value1 != 0) {
          sum = sum + value1 * READ_src1_IMAGE(src1, sampler, POS_src1_INSTANCE(i, dy,0,0)).x;
        }
      }
      float out = sum;
      WRITE_dst_matrix_IMAGE(dst_matrix, POS_dst_matrix_INSTANCE(dx, dy, 0, 0), CONVERT_dst_matrix_PIXEL_TYPE(out));
  }
}
