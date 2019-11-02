__kernel void average_distance_of_n_closest_points(
IMAGE_src_distancematrix_TYPE src_distancematrix,
IMAGE_dst_indexlist_TYPE dst_indexlist, int nPoints) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int pointIndex = get_global_id(0);

  // so many point candidates are available:
  const int height = GET_IMAGE_HEIGHT(src_distancematrix);

  float distances[1000];
  float indices[1000];

  int initialized_values = 0;

  int2 pos = (int2){pointIndex, 0};
  for (int y = 0; y < height; y++) {
    pos.y = y;
    float distance = READ_src_distancematrix_IMAGE(src_distancematrix, sampler, pos).x;

    if (initialized_values < nPoints) {
      initialized_values++;
      distances[initialized_values - 1] = distance;
      indices[initialized_values - 1] = y;
    }
    // sort by insert
    for (int i = initialized_values - 1; i >= 0; i--) {
        if (distance > distances[i]) {
            break;
        }
        if (distance < distances[i] && (i == 0 || distance >= distances[i - 1])) {
           for (int j = initialized_values - 1; j > i; j--) {
                indices[j] = indices[j - 1];
                distances[j] = distances[j - 1];
           }
           distances[i] = distance;
           indices[i] = y;
           break;
        }
    }
  }

  float sum = 0;
  int count = 0;
  for (int i = 1; i < initialized_values; i++) {
    sum = sum + distances[i];
    count++;
  }

  int4 iPos = (int4){pointIndex, 0, 0, 0};
  float res = sum / count;
  WRITE_dst_indexlist_IMAGE(dst_indexlist, iPos, CONVERT_dst_indexlist_PIXEL_TYPE(res));
}