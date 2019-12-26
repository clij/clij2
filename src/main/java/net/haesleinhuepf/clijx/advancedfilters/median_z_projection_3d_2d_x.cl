__kernel void stddev_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst,
    DTYPE_IMAGE_IN_3D src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);

  float sum = 0;
  int count = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    sum = sum + (float)(READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x);
    count++;
  }
  float mean = (sum / count);

  sum = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    float value = (float)(READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x) - mean;
    sum = sum + (value * value);
  }
  float stdDev = sqrt((float2){sum / (count - 1), 0}).x;

  WRITE_IMAGE_2D(dst,(int2)(x,y),(DTYPE_OUT)stdDev);
}

inline void sort(DTYPE_OUT array[], int array_size)
{
    DTYPE_OUT temp;
    for(int i = 0; i < array_size; i++) {
        int j;
        temp = array[i];
        for(j = i - 1; j >= 0 && temp < array[j]; j--) {
            array[j+1] = array[j];
        }
        array[j+1] = temp;
    }
}

inline DTYPE_OUT median(DTYPE_OUT array[], int array_size)
{
    sort(array, array_size);
    return array[array_size / 2];
}


__kernel void median_project_3d_2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_3D src
)
{
  DTYPE_OUT array[MAX_ARRAY_SIZE];
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);

  int count = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    array[count] = (float)(READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x);
    count++;
  }

  DTYPE_OUT res = median(array, count);

  WRITE_IMAGE_2D(dst,(int2)(x,y),(DTYPE_OUT)res);
}

