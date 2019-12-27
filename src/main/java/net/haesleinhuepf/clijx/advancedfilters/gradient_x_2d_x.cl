__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;


__kernel void gradientX_2d
(
  IMAGE_dst_TYPE dst, 
  IMAGE_src_TYPE src
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2){i,j};
  const int2 coordA = (int2){i-1,j};
  const int2 coordB = (int2){i+1,j};

  float valueA = READ_src_IMAGE(src, sampler, coordA).x;
  float valueB = READ_src_IMAGE(src, sampler, coordB).x;
  IMAGE_dst_PIXEL_TYPE res = CONVERT_dst_PIXEL_TYPE(valueB - valueA);

  WRITE_dst_IMAGE(dst, coord, res);
}


__kernel void gradientY_2d
(
  IMAGE_dst_TYPE dst, IMAGE_src_TYPE src
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2){i,j};
  const int2 coordA = (int2){i,j-1};
  const int2 coordB = (int2){i,j+1};

  float valueA = READ_src_IMAGE(src, sampler, coordA).x;
  float valueB = READ_src_IMAGE(src, sampler, coordB).x;
  IMAGE_dst_PIXEL_TYPE res = CONVERT_dst_PIXEL_TYPE(valueB - valueA);

  WRITE_dst_IMAGE(dst, coord, res);
}

__kernel void gradientX_3d
(
  IMAGE_dst_TYPE dst, IMAGE_src_TYPE src
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);
  const int k = get_global_id(2);
  const int4 coord  = (int4){i, j, k, 0};
  const int4 coordA = (int4){i-1, j, k, 0};
  const int4 coordB = (int4){i+1, j, k, 0};

  float valueA = READ_src_IMAGE(src, sampler, coordA).x;
  float valueB = READ_src_IMAGE(src, sampler, coordB).x;
  IMAGE_dst_PIXEL_TYPE res = CONVERT_dst_PIXEL_TYPE(valueB - valueA);

  WRITE_dst_IMAGE(dst, coord, res);
}

__kernel void gradientY_3d
(
  IMAGE_dst_TYPE dst, IMAGE_src_TYPE src
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);
  const int k = get_global_id(2);
  const int4 coord  = (int4){i, j, k, 0};
  const int4 coordA = (int4){i, j-1, k, 0};
  const int4 coordB = (int4){i, j+1, k, 0};

  float valueA = READ_src_IMAGE(src, sampler, coordA).x;
  float valueB = READ_src_IMAGE(src, sampler, coordB).x;
  IMAGE_dst_PIXEL_TYPE res = CONVERT_dst_PIXEL_TYPE(valueB - valueA);

  WRITE_dst_IMAGE(dst, coord, res);
}

__kernel void gradientZ_3d
(
  IMAGE_dst_TYPE dst, IMAGE_src_TYPE src
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);
  const int k = get_global_id(2);
  const int4 coord  = (int4){i, j, k, 0};
  const int4 coordA = (int4){i, j, k-1, 0};
  const int4 coordB = (int4){i, j, k+1, 0};

  float valueA = READ_src_IMAGE(src, sampler, coordA).x;
  float valueB = READ_src_IMAGE(src, sampler, coordB).x;
  IMAGE_dst_PIXEL_TYPE res = CONVERT_dst_PIXEL_TYPE(valueB - valueA);

  WRITE_dst_IMAGE(dst, coord, res);
}

