
inline void sort(IMAGE_dst_PIXEL_TYPE array[], int array_size)
{
    IMAGE_dst_PIXEL_TYPE temp;
    for(int i = 0; i < array_size; i++) {
        int j;
        temp = array[i];
        for(j = i - 1; j >= 0 && temp < array[j]; j--) {
            array[j+1] = array[j];
        }
        array[j+1] = temp;
    }
}

inline IMAGE_dst_PIXEL_TYPE median(IMAGE_dst_PIXEL_TYPE array[], int array_size)
{
    sort(array, array_size);
    return array[array_size / 2];
}


__kernel void median_z_projection_3d_2d
(
   IMAGE_dst_TYPE dst,
   IMAGE_src_TYPE src
)
{
  IMAGE_dst_PIXEL_TYPE array[MAX_ARRAY_SIZE];
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);

  int count = 0;
  for(int z = 0; z < GET_IMAGE_DEPTH(src); z++)
  {
    array[count] = (float)(READ_src_IMAGE(src,sampler,(int4)(x,y,z,0)).x);
    count++;
  }

  IMAGE_dst_PIXEL_TYPE res = median(array, count);

  WRITE_dst_IMAGE(dst,(int2)(x,y), CONVERT_dst_PIXEL_TYPE(res));
}

