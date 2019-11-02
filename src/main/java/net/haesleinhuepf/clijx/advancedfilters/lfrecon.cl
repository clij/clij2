__kernel void reorder_views (    DTYPE_IMAGE_IN_2D  src,
                           DTYPE_IMAGE_OUT_3D  dst,
                           const int umax,
                           const int vmax
                     )
{
  const sampler_t intsampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int u = z % umax;
  const int v = z / umax;

  const width = get_global_size(0);
  const height = get_global_size(1);

  const int2 pos = (int2)(u + umax * x, v + vmax * y);

  DTYPE_IN value = READ_IMAGE_2D(src, intsampler, pos).x;

  WRITE_IMAGE_3D (dst, (int4)(x,y,z,0), (DTYPE_OUT)value);
}
