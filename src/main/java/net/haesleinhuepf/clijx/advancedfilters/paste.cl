__kernel void paste_3d(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src, int destination_x, int destination_y, int destination_z) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0) + destination_x;
  const int dy = get_global_id(1) + destination_y;
  const int dz = get_global_id(2) + destination_z;


  const int sx = get_global_id(0);
  const int sy = get_global_id(1);
  const int sz = get_global_id(2);

  const int4 dpos = (int4){dx,dy,dz,0};
  const int4 spos = (int4){sx,sy,sz,0};

  const DTYPE_IN out = READ_IMAGE_3D(src,sampler,spos).x;
  WRITE_IMAGE_3D(dst,dpos,(DTYPE_OUT)out);
}


__kernel void paste_2d(DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src, int destination_x, int destination_y) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0) + destination_x;
  const int dy = get_global_id(1) + destination_y;

  const int sx = get_global_id(0);
  const int sy = get_global_id(1);

  const int2 dpos = (int2){dx,dy};
  const int2 spos = (int2){sx,sy};

  const DTYPE_IN out = READ_IMAGE_2D(src,sampler,spos).x;
  WRITE_IMAGE_2D(dst,dpos,(DTYPE_OUT)out);
}
