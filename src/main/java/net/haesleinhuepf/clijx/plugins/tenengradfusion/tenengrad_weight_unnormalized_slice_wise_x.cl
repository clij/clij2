__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__constant float hx[] = {-1,-2,-1,-2,-4,-2,-1,-2,-1,0,0,0,0,0,0,0,0,0,1,2,1,2,4,2,1,2,1};
__constant float hy[] = {-1,-2,-1,0,0,0,1,2,1,-2,-4,-2,0,0,0,2,4,2,-1,-2,-1,0,0,0,1,2,1};
__constant float hz[] = {-1,0,1,-2,0,2,-1,0,1,-2,0,2,-4,0,4,-2,0,2,-1,0,1,-2,0,2,-1,0,1};


__kernel void tenengrad_weight_unnormalized_slice_wise(
    IMAGE_dst_TYPE dst, 
    IMAGE_src_TYPE src
) {
  const int x = get_global_id(0);
  const int y = get_global_id(1); 
  const int z = get_global_id(2);
  
  const int4 coord = (int4)(x,y,z,0);
  
  float Gx = 0.0f, Gy = 0.0f;
  for (int i = 0; i < 3; ++i) {
    for (int j = 0; j < 3; ++j) {
      for (int k = 0; k < 3; ++k) {
        const int dx = i-1, dy = j-1, dz = k-1;
        const int ind = i + 3*j + 3*3*k;
        const float pix = (float)READ_src_IMAGE(src,sampler,(int4)(x + dx,y + dy, z + dz,0)).x;
        Gx += hx[ind]*pix;
        Gy += hy[ind]*pix;
      }
    }
  }
  float w = Gx*Gx + Gy*Gy;
  //sobel_magnitude_squared_slice_wise(src,i,j,k);
  // w = w*w;
  WRITE_dst_IMAGE(dst,coord, CONVERT_dst_PIXEL_TYPE(w));
}
