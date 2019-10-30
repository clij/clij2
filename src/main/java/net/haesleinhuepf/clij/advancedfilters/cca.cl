__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void min_sep_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_OUT_3D flag_dst, DTYPE_IMAGE_IN_3D src,
  const int dim, const int N, const float s
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);
  const int4 dir   = (int4)(dim==0,dim==1,dim==2,0);

  // center
  const int   c = (N-1)/2;

  float res = READ_IMAGE_3D(src,sampler,coord).x;
  if (res > 0.0) {
    float orig = res;
    for (int v = -c; v <= c; v++) {
      float value = (float)(READ_IMAGE_3D(src,sampler,  coord+v*dir).x);
      if (value > 0.0) {
        res = min(res, value);
      }
    }
    if (res != orig) {
      WRITE_IMAGE_3D(flag_dst,(int4)(0,0,0,0),1);
    }
  }
  WRITE_IMAGE_3D(dst,coord,(DTYPE_OUT)res);
}

__kernel void min_sep_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_OUT_3D flag_dst, DTYPE_IMAGE_IN_2D src,
  const int dim, const int N, const float s
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2)(i,j);
  const int2 dir   = (int2)(dim==0,dim==1);

  // center
  const int   c = (N-1)/2;

  float res = (float)(READ_IMAGE_2D(src,sampler,coord).x);
  if (res > 0.0) {
    float orig = res;
    for (int v = -c; v <= c; v++) {
      if (v != 0) {
        float value = (float)(READ_IMAGE_2D(src,sampler,    coord+v*dir).x);
        if (value > 0.0) {
          res = min(res, value);
        }
      }
    }

    if (res != orig) {
      WRITE_IMAGE_3D(flag_dst,(int4)(0,0,0,0),1);
    }
  }
  WRITE_IMAGE_2D(dst,coord,(DTYPE_OUT)res);
}


__kernel void set_nonzero_pixels_to_pixelindex
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);
  const int k = get_global_id(2);

  const int w = GET_IMAGE_WIDTH(src);
  const int h = GET_IMAGE_HEIGHT(src);
  const int d = GET_IMAGE_DEPTH(src);

  float pixelindex = i * h * d + j * d + k;
  float value = (float)(READ_IMAGE_3D(src,sampler,(int4)(i,j,k,0)).x);
  if (value != 0) {
    WRITE_IMAGE_3D(dst,(int4)(i,j,k,0),pixelindex);
  } else {
    WRITE_IMAGE_3D(dst, (int4)(i,j,k,0), 0);
  }
}

__kernel void replace
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const float in, const float out
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);
  const int k = get_global_id(2);

  const int w = GET_IMAGE_WIDTH(src);
  const int h = GET_IMAGE_HEIGHT(src);
  const int d = GET_IMAGE_DEPTH(src);

  float pixelindex = i * h * d + j * d + k;
  float value = (float)(READ_IMAGE_3D(src,sampler,(int4)(i,j,k,0)).x);
  if (fabs(value - in) < 0.1) {
    WRITE_IMAGE_3D(dst, (int4)(i,j,k,0), (DTYPE_OUT)out);
  } else {
    WRITE_IMAGE_3D(dst, (int4)(i,j,k,0), (DTYPE_OUT)value);
  }
}

