__kernel void extrema (    DTYPE_IMAGE_IN_3D  src1,
                           DTYPE_IMAGE_IN_3D  src2,
                           DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const sampler_t intsampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int width = get_global_size(0);
  const int height = get_global_size(1);
  const int depth = get_global_size(2);

  const int4 pos = (int4)(x, y, z,0);

  DTYPE_IN value = READ_IMAGE_3D(src1, intsampler, pos).x;
  const DTYPE_IN value2 = READ_IMAGE_3D(src2, intsampler, pos).x;

  if (fabs((float)value2) > fabs((float)value)) {
    value = value2;
  }

  WRITE_IMAGE_3D (dst, (int4)(x,y,z,0), (DTYPE_OUT)value);
}
