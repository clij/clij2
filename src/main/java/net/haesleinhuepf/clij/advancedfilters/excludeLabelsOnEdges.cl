__kernel void exclude_on_edges_x_3d (    DTYPE_IMAGE_IN_3D  src,
                           DTYPE_IMAGE_OUT_3D  label_index_dst
                     )
{
  const sampler_t intsampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int width = GET_IMAGE_WIDTH(src);

  x = 0;
  int4 pos = (int4)(x, y, z,0);
  int index = READ_IMAGE_3D(src, intsampler, pos).x;
  if (index > 0) {
    WRITE_IMAGE_3D (label_index_dst, (int4)(index,0,0,0), 0);
  }

  x = width - 1;
  pos = (int4)(x, y, z,0);
  index = READ_IMAGE_3D(src, intsampler, pos).x;
  if (index > 0) {
    WRITE_IMAGE_3D (label_index_dst, (int4)(index,0,0,0), 0);
  }
}

__kernel void exclude_on_edges_y_3d (    DTYPE_IMAGE_IN_3D  src,
                           DTYPE_IMAGE_OUT_3D  label_index_dst
                     )
{
  const sampler_t intsampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  int y = get_global_id(1);
  const int z = get_global_id(2);

  const int height = GET_IMAGE_HEIGHT(src);

  y = 0;
  int4 pos = (int4)(x, y, z,0);
  int index = READ_IMAGE_3D(src, intsampler, pos).x;
  if (index > 0) {
    WRITE_IMAGE_3D (label_index_dst, (int4)(index,0,0,0), 0);
  }

  y = height - 1;
  pos = (int4)(x, y, z,0);
  index = READ_IMAGE_3D(src, intsampler, pos).x;
  if (index > 0) {
    WRITE_IMAGE_3D (label_index_dst, (int4)(index,0,0,0), 0);
  }
}


__kernel void exclude_on_edges_z_3d (    DTYPE_IMAGE_IN_3D  src,
                           DTYPE_IMAGE_OUT_3D  label_index_dst
                     )
{
  const sampler_t intsampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  int z = get_global_id(2);

  const int depth = GET_IMAGE_DEPTH(src);

  z = 0;
  int4 pos = (int4)(x, y, z,0);
  int index = READ_IMAGE_3D(src, intsampler, pos).x;
  if (index > 0) {
    WRITE_IMAGE_3D (label_index_dst, (int4)(index,0,0,0), 0);
  }

  z = depth - 1;
  pos = (int4)(x, y, z,0);
  index = READ_IMAGE_3D(src, intsampler, pos).x;
  if (index > 0) {
    WRITE_IMAGE_3D (label_index_dst, (int4)(index,0,0,0), 0);
  }
}

