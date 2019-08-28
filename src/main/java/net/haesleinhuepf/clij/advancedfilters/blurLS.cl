// Adapted from Uwe Schmidt, https://github.com/ClearControl/FastFuse/blob/master/src/fastfuse/tasks/kernels/blur.cl
//
#define MAX_GROUP_SIZE 128

__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void gaussian_blur_sep_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int dim, const int N, const float s
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);
  const int4 dir   = (int4)(dim==0,dim==1,dim==2,0);

  // center
  const int   c = (N-1)/2;
  // normalization
  const float n = -2*s*s;

  float res = 0, hsum = 0;
  for (int v = -c; v <= c; v++) {
    const float h = exp((v*v)/n);
    res += h * (float)READ_IMAGE_3D(src,sampler,coord+v*dir).x;
    hsum += h;
  }
  res /= hsum;
  WRITE_IMAGE_3D(dst,coord, CONVERT_DTYPE_OUT(res));
}


__kernel void gaussian_blur_sep_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int dim, const int N, const float s
)
{
  const int i = get_global_id(0);
  const int j = get_global_id(1);
  const int dims[2] = {GET_IMAGE_WIDTH(src), GET_IMAGE_HEIGHT(src)};
  if (i >= dims[0]) {
    return;
  }
  if (j >= dims[1]) {
    return;
  }

  const int2 coord = (int2)(i,j);
  const int2 dir   = (int2)(dim==0,dim==1);
  const int2 notdir   = (int2)(dim!=0,dim!=1);

  int dimPos = i;
  if (dim == 1) {
    dimPos = j;
  }


  const int  local_id = get_local_id(dim);


  __local float local_input[MAX_GROUP_SIZE];
  __local float local_output[MAX_GROUP_SIZE];


  const int startD = get_global_id(dim) - get_global_id(dim) % MAX_GROUP_SIZE;

  /*
  if (local_id == 0) {
    if (dim == 0) {
      for (int i = 0; i < MAX_GROUP_SIZE; i ++) {
        int2 pos = (int2)(startD + i, coord.y);
        local_input[i] = (float)READ_IMAGE_2D(src, sampler, pos).x;
      }
    } else {
      for (int i = 0; i < MAX_GROUP_SIZE; i ++) {
        int2 pos = (int2)(coord.x, startD + i);
        local_input[i] = (float)READ_IMAGE_2D(src, sampler, pos).x;
      }
    }
  }*/
  local_input[local_id] = (float)READ_IMAGE_2D(src, sampler, coord).x;

  barrier(CLK_LOCAL_MEM_FENCE);

  // center
  const int   c = (N-1)/2;
  // normalization
  const float n = -2*s*s;

  float res = 0, hsum = 0;
  for (int v = -c; v <= c; v++) {
    const float h = exp((v*v)/n);
    int test_v = coord.x * dir.x + coord.y * dir.y + v;
    if (test_v < startD || test_v >= startD + MAX_GROUP_SIZE || coord.x + v * dir.x >= dims[0] || coord.y + v * dir.y >= dims[1]) {
        res += h * (float)READ_IMAGE_2D(src,sampler,coord+v*dir).x;
    } else {
        res += h * local_input[test_v - startD];
    }
    hsum += h;
  }
  res /= hsum;
  local_output[dimPos - startD] = res;

  barrier(CLK_LOCAL_MEM_FENCE);

  if (local_id == 0) {
    if (dim == 0) {
      for (int i = 0; i < MAX_GROUP_SIZE; i ++) {
        float res = local_output[i];
        int2 pos = (int2)(startD + i, coord.y);
        WRITE_IMAGE_2D(dst, pos, CONVERT_DTYPE_OUT(res));
      }
    } else {
      for (int i = 0; i < MAX_GROUP_SIZE; i ++) {
        float res = local_output[i];
        int2 pos = (int2)(coord.x, startD + i);
        WRITE_IMAGE_2D(dst, pos, CONVERT_DTYPE_OUT(res));
      }
    }
    /*
    for (int i = 0; i < MAX_GROUP_SIZE; i ++) {
      //res = local_output[i];
      float res = local_input[i];
      int2 dimCoord = (int2)(startD + i, startD + i);
      //WRITE_IMAGE_2D(dst,coord * notdir + dimCoord * dir, CONVERT_DTYPE_OUT(res));
    }
    */
  }
  //barrier(CLK_LOCAL_MEM_FENCE);
  //WRITE_IMAGE_2D(dst,coord, CONVERT_DTYPE_OUT(local_id));
}


