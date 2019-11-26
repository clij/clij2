__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;


__kernel void sobel_2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src
)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  float valueCenter = READ_IMAGE_2D(src, sampler, pos).x;
  float valueRight = READ_IMAGE_2D(src, sampler, (pos + (int2){1, 0})).x;
  float valueLeft = READ_IMAGE_2D(src, sampler, (pos + (int2){-1, 0})).x;
  float valueBottom = READ_IMAGE_2D(src, sampler, (pos + (int2){0, 1})).x;
  float valueTop = READ_IMAGE_2D(src, sampler, (pos + (int2){0, -1})).x;

  float valueTopLeft = READ_IMAGE_2D(src, sampler, (pos + (int2){-1, -1})).x;
  float valueTopRight = READ_IMAGE_2D(src, sampler, (pos + (int2){1, -1})).x;
  float valueBottomLeft = READ_IMAGE_2D(src, sampler, (pos + (int2){-1, 1})).x;
  float valueBottomRight = READ_IMAGE_2D(src, sampler, (pos + (int2){1, 1})).x;

  float result_x = valueTopLeft * -1.0 +
                 valueLeft * -2.0 +
                 valueBottomLeft * -1.0 +
                 valueTopRight * 1.0 +
                 valueRight * 2.0 +
                 valueBottomRight * 1.0;

  float result_y = valueTopLeft * -1.0 +
                 valueTop * -2.0 +
                 valueTopRight * -1.0 +
                 valueBottomLeft * 1.0 +
                 valueBottom * 2.0 +
                 valueBottomRight * 1.0;

  float result = sqrt(result_x*result_x+result_y*result_y);
  WRITE_IMAGE_2D (dst, pos, CONVERT_DTYPE_OUT(result));
}


__kernel void sobel_3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src
)
{

  /*need 3 operations with 3 kernels for 3d sobel*/
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  int gy[3][3][3];
  int gx[3][3][3];
  int gz[3][3][3];
  
  int hx[3]={1,2,1};
  int hy[3]={1,2,1};
  int hz[3]={1,2,1};

  int hpx[3]={1,0,-1};
  int hpy[3]={1,0,-1};
  int hpz[3]={1,0,-1};

  float sum_x,sum_y,sum_z;
  int m,n,k;

  /*build the kernels i.e. h'_x(x,y,z)=h'(x)h(y)h(z)=gx(x,y,z)*/
  for(m=0;m<=2;m++) {
    for(n=0;n<=2;n++) {
      for(k=0;k<=2;k++){
  	    gx[m][n][k] = hpx[m] * hy[n]  * hz[k];
  	    gy[m][n][k] = hx[m]  * hpy[n] * hz[k];
  	    gz[m][n][k] = hx[m]  * hy[n]  * hpz[k];
      }
    }
  }

    sum_x=0,sum_y=0,sum_z=0;
    for(m=0;m<=2;m++){
        for(n=0;n<=2;n++){
            for(k=0;k<=3;k++){
                sum_x += gx[m][n][k]*(READ_IMAGE_3D(src, sampler, (pos + (int4){m-1, n-1,k-1, 0})).x);
                sum_y += gy[m][n][k]*(READ_IMAGE_3D(src, sampler, (pos + (int4){m-1, n-1,k-1, 0})).x);
                sum_z += gz[m][n][k]*(READ_IMAGE_3D(src, sampler, (pos + (int4){m-1, n-1,k-1, 0})).x);
            }
        }
    }

	float result = sqrt(sum_x * sum_x + sum_y * sum_y + sum_z * sum_z);

    WRITE_IMAGE_3D (dst, pos, CONVERT_DTYPE_OUT(result));;
}
