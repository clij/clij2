__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void equal_2d(DTYPE_IMAGE_IN_2D  src1,
                       float scalar,
                       DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const float input1 = (float)READ_IMAGE_2D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 == scalar) {
    value = 1;
  }
  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void equal_3d(DTYPE_IMAGE_IN_3D src1,
                       float scalar,
                     DTYPE_IMAGE_OUT_3D dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const float input1 = (float)READ_IMAGE_3D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 == scalar) {
    value = 1;
  }
  WRITE_IMAGE_3D (dst, pos, value);
}

// -----------------------------------------------------------------------

__kernel void not_equal_2d(DTYPE_IMAGE_IN_2D  src1,
                       float scalar,
                       DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const float input1 = (float)READ_IMAGE_2D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 != scalar) {
    value = 1;
  }
  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void not_equal_3d(DTYPE_IMAGE_IN_3D src1,
                       float scalar,
                     DTYPE_IMAGE_OUT_3D dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const float input1 = (float)READ_IMAGE_3D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 != scalar) {
    value = 1;
  }
  WRITE_IMAGE_3D (dst, pos, value);
}

// -----------------------------------------------------------------------

__kernel void greater_or_equal_2d(DTYPE_IMAGE_IN_2D  src1,
                       float scalar,
                       DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const float input1 = (float)READ_IMAGE_2D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 >= scalar) {
    value = 1;
  }
  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void greater_or_equal_3d(DTYPE_IMAGE_IN_3D src1,
                       float scalar,
                     DTYPE_IMAGE_OUT_3D dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const float input1 = (float)READ_IMAGE_3D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 >= scalar) {
    value = 1;
  }
  WRITE_IMAGE_3D (dst, pos, value);
}

// -----------------------------------------------------------------------


__kernel void greater_2d(DTYPE_IMAGE_IN_2D  src1,
                       float scalar,
                       DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const float input1 = (float)READ_IMAGE_2D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 > scalar) {
    value = 1;
  }
  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void greater_3d(DTYPE_IMAGE_IN_3D src1,
                       float scalar,
                     DTYPE_IMAGE_OUT_3D dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const float input1 = (float)READ_IMAGE_3D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 > scalar) {
    value = 1;
  }
  WRITE_IMAGE_3D (dst, pos, value);
}

// -----------------------------------------------------------------------
__kernel void smaller_or_equal_2d(DTYPE_IMAGE_IN_2D  src1,
                       float scalar,
                       DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const float input1 = (float)READ_IMAGE_2D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 <= scalar) {
    value = 1;
  }
  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void smaller_or_equal_3d(DTYPE_IMAGE_IN_3D src1,
                       float scalar,
                     DTYPE_IMAGE_OUT_3D dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const float input1 = (float)READ_IMAGE_3D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 <= scalar) {
    value = 1;
  }
  WRITE_IMAGE_3D (dst, pos, value);
}

// -----------------------------------------------------------------------

__kernel void smaller_2d(DTYPE_IMAGE_IN_2D  src1,
                       float scalar,
                       DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const float input1 = (float)READ_IMAGE_2D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 < scalar) {
    value = 1;
  }
  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void smaller_3d(DTYPE_IMAGE_IN_3D src1,
                       float scalar,
                     DTYPE_IMAGE_OUT_3D dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const float input1 = (float)READ_IMAGE_3D(src1, sampler, pos).x;

  DTYPE_OUT value = 0;
  if (input1 < scalar) {
    value = 1;
  }
  WRITE_IMAGE_3D (dst, pos, value);
}

// -----------------------------------------------------------------------

