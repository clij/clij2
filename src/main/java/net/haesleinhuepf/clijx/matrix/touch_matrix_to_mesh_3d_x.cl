__kernel void touch_matrix_to_mesh_3d(
IMAGE_src_pointlist_TYPE src_pointlist,
IMAGE_src_touch_matrix_TYPE src_touch_matrix,
IMAGE_dst_mesh_TYPE dst_mesh) {

  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int pointIndex = get_global_id(0);

  int2 pos = (int2){pointIndex, 0};
  const float pointAx = READ_src_pointlist_IMAGE(src_pointlist, sampler, pos).x;
  pos = (int2){pointIndex, 1};
  const float pointAy = READ_src_pointlist_IMAGE(src_pointlist, sampler, pos).x;
  pos = (int2){pointIndex, 2};
  const float pointAz = READ_src_pointlist_IMAGE(src_pointlist, sampler, pos).x;

  int4 pointA = (int4){pointAx, pointAy, pointAz, 0};

  // so many point candidates are available:
  const int num_pointBs = GET_IMAGE_HEIGHT(src_touch_matrix);
  for (int pointBIndex = pointIndex + 1; pointBIndex < num_pointBs; pointBIndex++) {
    pos = (int2){pointIndex + 1, pointBIndex + 1};
    const float touching = READ_src_touch_matrix_IMAGE(src_touch_matrix, sampler, pos).x;
    if (touching > 0.5) {
      pos = (int2){pointBIndex, 0};
      const float pointBx = READ_src_pointlist_IMAGE(src_pointlist, sampler, pos).x;
      pos = (int2){pointBIndex, 1};
      const float pointBy = READ_src_pointlist_IMAGE(src_pointlist, sampler, pos).x;
      pos = (int2){pointBIndex, 2};
      const float pointBz = READ_src_pointlist_IMAGE(src_pointlist, sampler, pos).x;

      // draw line from A to B
      float distanceX = pow(pointAx - pointBx, (float)2.0);
      float distanceY = pow(pointAy - pointBy, (float)2.0);
      float distanceZ = pow(pointAz - pointBz, (float)2.0);

      float distance = sqrt(distanceX + distanceY + distanceZ);
      for (float d = 0; d < distance; d = d + 0.5) {
        int4 tPos = (int4){pointAx + (pointBx - pointAx) * d / distance,
                           pointAy + (pointBy - pointAy) * d / distance,
                           pointAz + (pointBz - pointAz) * d / distance,
                           0};
        WRITE_dst_mesh_IMAGE(dst_mesh, tPos, 1);
      }
    }
  }
}