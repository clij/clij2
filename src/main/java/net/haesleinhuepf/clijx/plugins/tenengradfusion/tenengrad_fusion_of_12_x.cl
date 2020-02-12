__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__constant float hx[] = {-1,-2,-1,-2,-4,-2,-1,-2,-1,0,0,0,0,0,0,0,0,0,1,2,1,2,4,2,1,2,1};
__constant float hy[] = {-1,-2,-1,0,0,0,1,2,1,-2,-4,-2,0,0,0,2,4,2,-1,-2,-1,0,0,0,1,2,1};
__constant float hz[] = {-1,0,1,-2,0,2,-1,0,1,-2,0,2,-4,0,4,-2,0,2,-1,0,1,-2,0,2,-1,0,1};



__kernel void tenengrad_fusion_with_provided_weights_12_images(
  IMAGE_dst_TYPE dst, const int factor,
  IMAGE_src0_TYPE src0, IMAGE_src1_TYPE src1, IMAGE_src2_TYPE src2, IMAGE_src3_TYPE src3, IMAGE_src4_TYPE src4, IMAGE_src5_TYPE src5,
  IMAGE_src6_TYPE src6, IMAGE_src7_TYPE src7, IMAGE_src8_TYPE src8, IMAGE_src9_TYPE src9, IMAGE_src10_TYPE src10, IMAGE_src11_TYPE src11,
  IMAGE_weight0_TYPE weight0, IMAGE_weight1_TYPE weight1, IMAGE_weight2_TYPE weight2, IMAGE_weight3_TYPE weight3, IMAGE_weight4_TYPE weight4, IMAGE_weight5_TYPE weight5,
  IMAGE_weight6_TYPE weight6, IMAGE_weight7_TYPE weight7, IMAGE_weight8_TYPE weight8, IMAGE_weight9_TYPE weight9, IMAGE_weight10_TYPE weight10, IMAGE_weight11_TYPE weight11
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const POS_src0_TYPE coord = POS_src0_INSTANCE(i,j,k,0);

  const POS_weight0_TYPE coord_weight = POS_weight0_INSTANCE((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = READ_weight0_IMAGE(weight0,sampler_weight,coord_weight).x;
  float w1 = READ_weight1_IMAGE(weight1,sampler_weight,coord_weight).x;
  float w2 = READ_weight2_IMAGE(weight2,sampler_weight,coord_weight).x;
  float w3 = READ_weight3_IMAGE(weight3,sampler_weight,coord_weight).x;
  float w4 = READ_weight4_IMAGE(weight4,sampler_weight,coord_weight).x;
  float w5 = READ_weight5_IMAGE(weight5,sampler_weight,coord_weight).x;
  float w6 = READ_weight6_IMAGE(weight6,sampler_weight,coord_weight).x;
  float w7 = READ_weight7_IMAGE(weight7,sampler_weight,coord_weight).x;
  float w8 = READ_weight8_IMAGE(weight8,sampler_weight,coord_weight).x;
  float w9 = READ_weight9_IMAGE(weight9,sampler_weight,coord_weight).x;
  float w10 = READ_weight10_IMAGE(weight10,sampler_weight,coord_weight).x;
  float w11 = READ_weight11_IMAGE(weight11,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + w2 + w3 + w4 + w5 + w6 + w7 + w8 + w9 + w10 + w11 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;
  w4 /= wsum;
  w5 /= wsum;
  w6 /= wsum;
  w7 /= wsum;
  w8 /= wsum;
  w9 /= wsum;
  w10 /= wsum;
  w11 /= wsum;

  const float  v0 = (float)READ_src0_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_src1_IMAGE(src1,sampler,coord).x;
  const float  v2 = (float)READ_src2_IMAGE(src2,sampler,coord).x;
  const float  v3 = (float)READ_src3_IMAGE(src3,sampler,coord).x;
  const float  v4 = (float)READ_src4_IMAGE(src4,sampler,coord).x;
  const float  v5 = (float)READ_src5_IMAGE(src5,sampler,coord).x;
  const float  v6 = (float)READ_src6_IMAGE(src6,sampler,coord).x;
  const float  v7 = (float)READ_src7_IMAGE(src7,sampler,coord).x;
  const float  v8 = (float)READ_src8_IMAGE(src8,sampler,coord).x;
  const float  v9 = (float)READ_src9_IMAGE(src9,sampler,coord).x;
  const float  v10 = (float)READ_src10_IMAGE(src10,sampler,coord).x;
  const float  v11 = (float)READ_src11_IMAGE(src11,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1 + w2 * v2 + w3 * v3 + w4 * v4 + w5 * v5 + w6 * v6 + w7 * v7 + w8 * v8 + w9 * v9 + w10 * v10 + w11 * v11;

  WRITE_dst_IMAGE(dst,coord, CONVERT_dst_PIXEL_TYPE(res));
}
