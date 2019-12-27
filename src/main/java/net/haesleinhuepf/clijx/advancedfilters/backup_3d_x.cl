__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void binary_or_3d(
    IMAGE_src1_TYPE  src1,
    IMAGE_src2_TYPE  src2,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  IMAGE_dst_PIXEL_TYPE value1 = CONVERT_dst_PIXEL_TYPE(READ_src1_IMAGE(src1, sampler, pos).x);
  IMAGE_dst_PIXEL_TYPE value2 = CONVERT_dst_PIXEL_TYPE(READ_src2_IMAGE(src2, sampler, pos).x);
  if ( value1 > 0 || value2 > 0 ) {
    value1 = 1;
  } else {
    value1 = 0;
  }
  WRITE_dst_IMAGE (dst, pos, value1);
}

__kernel void binary_and_3d(
    IMAGE_src1_TYPE  src1,
    IMAGE_src2_TYPE  src2,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  IMAGE_dst_PIXEL_TYPE value1 = CONVERT_dst_PIXEL_TYPE(READ_src1_IMAGE(src1, sampler, pos).x);
  IMAGE_dst_PIXEL_TYPE value2 = CONVERT_dst_PIXEL_TYPE(READ_src2_IMAGE(src2, sampler, pos).x);
  if ( value1 > 0 && value2 > 0 ) {
    value1 = 1;
  } else {
    value1 = 0;
  }
  WRITE_dst_IMAGE (dst, pos, value1);
}

__kernel void binary_xor_3d(
    IMAGE_src1_TYPE  src1,
    IMAGE_src2_TYPE  src2,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  IMAGE_dst_PIXEL_TYPE value1 = CONVERT_dst_PIXEL_TYPE(READ_src1_IMAGE(src1, sampler, pos).x);
  IMAGE_dst_PIXEL_TYPE value2 = CONVERT_dst_PIXEL_TYPE(READ_src2_IMAGE(src2, sampler, pos).x);
  if ( (value1 > 0 && value2 == 0) || (value1 == 0 && value2 > 0)) {
    value1 = 1;
  } else {
    value1 = 0;
  }
  WRITE_dst_IMAGE (dst, pos, value1);
}

__kernel void binary_not_3d(
    IMAGE_src1_TYPE  src1,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  IMAGE_dst_PIXEL_TYPE value1 = CONVERT_dst_PIXEL_TYPE(READ_src1_IMAGE(src1, sampler, pos).x);
  if ( value1 > 0) {
    value1 = 0;
  } else {
    value1 = 1;
  }
  WRITE_dst_IMAGE (dst, pos, value1);
}

__kernel void erode_box_neighborhood_3d(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value != 0) {
    for (int ax = -1; ax <= 1; ax++) {
      for (int ay = -1; ay <= 1; ay++) {
        for (int az = -1; az <= 1; az++) {
          value = READ_src_IMAGE(src, sampler, (pos + (int4){ax, ay, az, 0})).x;
          if (value == 0) {
            break;
          }
        }
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

__kernel void erode_box_neighborhood_slice_by_slice(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value != 0) {
    for (int ax = -1; ax <= 1; ax++) {
      for (int ay = -1; ay <= 1; ay++) {
        value = READ_src_IMAGE(src, sampler, (pos + (int4){ax, ay, 0, 0})).x;
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

__kernel void erode_diamond_neighborhood_3d(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value != 0) {
    value = READ_src_IMAGE(src, sampler, (pos + (int4){1, 0, 0, 0})).x;
    if (value != 0) {
      value = READ_src_IMAGE(src, sampler, (pos + (int4){-1, 0, 0, 0})).x;
      if (value != 0) {
        value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 1, 0, 0})).x;
        if (value != 0) {
          value = READ_src_IMAGE(src, sampler, (pos + (int4){0, -1, 0, 0})).x;
        }
      }
    }
  }
  if (value != 0) {
    value = 1;
  }

  WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(value));
}

__kernel void erode_diamond_neighborhood_slice_by_slice(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value != 0) {
    value = READ_src_IMAGE(src, sampler, (pos + (int4){1, 0, 0, 0})).x;
    if (value != 0) {
      value = READ_src_IMAGE(src, sampler, (pos + (int4){-1, 0, 0, 0})).x;
      if (value != 0) {
        value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 1, 0, 0})).x;
        if (value != 0) {
          value = READ_src_IMAGE(src, sampler, (pos + (int4){0, -1, 0, 0})).x;
        }
      }
    }
  }
  if (value != 0) {
    value = 1;
  }

  WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(value));
}

__kernel void dilate_box_neighborhood_3d(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value < 1) {
    for (int ax = -1; ax <= 1; ax++) {
      for (int ay = -1; ay <= 1; ay++) {
        for (int az = -1; az <= 1; az++) {
          value = READ_src_IMAGE(src, sampler, (pos + (int4){ax, ay, az, 0})).x;
          if (value != 0) {
            break;
          }
        }
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

__kernel void dilate_box_neighborhood_slice_by_slice(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value == 0) {
    for (int ax = -1; ax <= 1; ax++) {
      for (int ay = -1; ay <= 1; ay++) {
        value = READ_src_IMAGE(src, sampler, (pos + (int4){ax, ay, 0, 0})).x;
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

__kernel void dilate_diamond_neighborhood_3d(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value == 0) {

    value = READ_src_IMAGE(src, sampler, (pos + (int4){1, 0, 0, 0})).x;
    if (value == 0) {
      value = READ_src_IMAGE(src, sampler, (pos + (int4){-1, 0, 0, 0})).x;
      if (value == 0) {
        value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 1, 0, 0})).x;
        if (value == 0) {
          value = READ_src_IMAGE(src, sampler, (pos + (int4){0, -1, 0, 0})).x;
          if (value == 0) {
            value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 0, 1, 0})).x;
            if (value == 0) {
              value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 0, -1, 0})).x;
            }
          }
        }
      }
    }
  }
  if (value != 0) {
    value = 1;
  }

  WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(value));
}

__kernel void dilate_diamond_neighborhood_slice_by_slice(
    IMAGE_src_TYPE  src,
    IMAGE_dst_TYPE  dst
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  float value = READ_src_IMAGE(src, sampler, pos).x;
  if (value == 0) {

    value = READ_src_IMAGE(src, sampler, (pos + (int4){1, 0, 0, 0})).x;
    if (value == 0) {
      value = READ_src_IMAGE(src, sampler, (pos + (int4){-1, 0, 0, 0})).x;
      if (value == 0) {
        value = READ_src_IMAGE(src, sampler, (pos + (int4){0, 1, 0, 0})).x;
        if (value == 0) {
          value = READ_src_IMAGE(src, sampler, (pos + (int4){0, -1, 0, 0})).x;
        }
      }
    }
  }
  if (value != 0) {
    value = 1;
  }

  WRITE_dst_IMAGE (dst, pos, CONVERT_dst_PIXEL_TYPE(value));
}
