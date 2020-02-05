

__kernel void power_images_2d(DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src1,  DTYPE_IMAGE_IN_2D src2) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int2 pos = (int2){x, y};

  float a = READ_IMAGE_2D(src1, sampler, pos).x;
  float b = READ_IMAGE_2D(src2, sampler, pos).x;
  float result = pow(a, b);

  float out = result;
  WRITE_IMAGE_2D(dst, pos, CONVERT_DTYPE_OUT(out));

}

__kernel void power_images_3d(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src1,  DTYPE_IMAGE_IN_3D src2) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);
  const int4 pos = (int4){x, y, z, 0};

  float a = READ_IMAGE_3D(src1, sampler, pos).x;
  float b = READ_IMAGE_3D(src2, sampler, pos).x;
  float result = pow(a, b);

  float out = result;
  WRITE_IMAGE_3D(dst, pos, CONVERT_DTYPE_OUT(out));

}