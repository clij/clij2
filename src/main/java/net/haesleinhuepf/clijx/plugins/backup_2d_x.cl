__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void binary_or_2d(
    IMAGE_src1_TYPE  src1,
    IMAGE_src2_TYPE  src2,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  IMAGE_dst_PIXEL_TYPE value1 = CONVERT_dst_PIXEL_TYPE(READ_src1_IMAGE(src1, sampler, pos).x);
  IMAGE_dst_PIXEL_TYPE value2 = CONVERT_dst_PIXEL_TYPE(READ_src2_IMAGE(src2, sampler, pos).x);
  if ( value1 > 0 || value2 > 0 ) {
    value1 = 1;
  } else {
    value1 = 0;
  }
  WRITE_dst_IMAGE (dst, pos, value1);
}

__kernel void binary_and_2d(
    IMAGE_src1_TYPE  src1,
    IMAGE_src2_TYPE  src2,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  IMAGE_dst_PIXEL_TYPE value1 = CONVERT_dst_PIXEL_TYPE(READ_src1_IMAGE(src1, sampler, pos).x);
  IMAGE_dst_PIXEL_TYPE value2 = CONVERT_dst_PIXEL_TYPE(READ_src2_IMAGE(src2, sampler, pos).x);
  if ( value1 > 0 && value2 > 0 ) {
    value1 = 1;
  } else {
    value1 = 0;
  }
  WRITE_dst_IMAGE (dst, pos, value1);
}

__kernel void binary_xor_2d(
    IMAGE_src1_TYPE  src1,
    IMAGE_src2_TYPE  src2,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  IMAGE_dst_PIXEL_TYPE value1 = CONVERT_dst_PIXEL_TYPE(READ_src1_IMAGE(src1, sampler, pos).x);
  IMAGE_dst_PIXEL_TYPE value2 = CONVERT_dst_PIXEL_TYPE(READ_src2_IMAGE(src2, sampler, pos).x);
  if ( (value1 > 0 && value2 == 0) || (value1 == 0 && value2 > 0)) {
    value1 = 1;
  } else {
    value1 = 0;
  }
  WRITE_dst_IMAGE (dst, pos, value1);
}

__kernel void binary_not_2d(
    IMAGE_src1_TYPE  src1,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  IMAGE_dst_PIXEL_TYPE value1 = CONVERT_dst_PIXEL_TYPE(READ_src1_IMAGE(src1, sampler, pos).x);
  if ( value1 > 0) {
    value1 = 0;
  } else {
    value1 = 1;
  }
  WRITE_dst_IMAGE (dst, pos, value1);
}


__kernel void erode_box_neighborhood_2d(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value != 0) {
    for (int ax = -1; ax <= 1; ax++) {
      for (int ay = -1; ay <= 1; ay++) {
        value = READ_src_IMAGE(src, sampler, (pos + (int2){ax, ay})).x;
        if (value == 0) {
          break;
        }
      }
      if (value == 0) {
        break;
      }
    }
  }
  if (value != 0) {
    value = 1;
  }

  WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(value));
}


__kernel void erode_diamond_neighborhood_2d(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value != 0) {
    value = READ_src_IMAGE(src, sampler, (pos + (int2){1, 0})).x;
    if (value != 0) {
      value = READ_src_IMAGE(src, sampler, (pos + (int2){-1, 0})).x;
      if (value != 0) {
        value = READ_src_IMAGE(src, sampler, (pos + (int2){0, 1})).x;
        if (value != 0) {
          value = READ_src_IMAGE(src, sampler, (pos + (int2){0, -1})).x;
        }
      }
    }
  }
  if (value != 0) {
    value = 1;
  }

  WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(value));
}


__kernel void dilate_box_neighborhood_2d(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value == 0) {
    for (int ax = -1; ax <= 1; ax++) {
      for (int ay = -1; ay <= 1; ay++) {
        value = READ_src_IMAGE(src, sampler, (pos + (int2){ax, ay})).x;
        if (value != 0) {
          break;
        }
      }
      if (value != 0) {
        break;
      }
    }
  }
  if (value != 0) {
    value = 1;
  }

  WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(value));
}

__kernel void dilate_diamond_neighborhood_2d(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value == 0) {
    value = READ_src_IMAGE(src, sampler, (pos + (int2){1, 0})).x;
    if (value == 0) {
      value = READ_src_IMAGE(src, sampler, (pos + (int2){-1, 0})).x;
      if (value == 0) {
        value = READ_src_IMAGE(src, sampler, (pos + (int2){0, 1})).x;
        if (value == 0) {
          value = READ_src_IMAGE(src, sampler, (pos + (int2){0, -1})).x;
        }
      }
    }
  }
  if (value != 0) {
    value = 1;
  }

  WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(value));
}
