__kernel void transpose_xy (    DTYPE_IMAGE_IN_3D  src,
                           DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const sampler_t intsampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 spos = (int4)(y, x, z,0);
  const int4 tpos = (int4)(x, y, z,0);

  DTYPE_IN value = READ_IMAGE_3D(src, intsampler, spos).x;

  WRITE_IMAGE_3D (dst, tpos, (DTYPE_OUT)value);
}

__kernel void transpose_xz (    DTYPE_IMAGE_IN_3D  src,
                           DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const sampler_t intsampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 spos = (int4)(z, y, x,0);
  const int4 tpos = (int4)(x, y, z,0);

  DTYPE_IN value = READ_IMAGE_3D(src, intsampler, spos).x;

  WRITE_IMAGE_3D (dst, tpos, (DTYPE_OUT)value);
}

__kernel void transpose_yz (    DTYPE_IMAGE_IN_3D  src,
                           DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const sampler_t intsampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 spos = (int4)(x, z, y,0);
  const int4 tpos = (int4)(x, y, z,0);

  DTYPE_IN value = READ_IMAGE_3D(src, intsampler, spos).x;

  WRITE_IMAGE_3D (dst, tpos, (DTYPE_OUT)value);
}
