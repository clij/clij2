
__kernel void average_angle_between_adjacent_triangles (
    IMAGE_src_pointlist_TYPE src_pointlist,
    IMAGE_src_touch_matrix_TYPE src_touch_matrix,
    IMAGE_dst_average_angle_list_TYPE dst_average_angle_list
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int label_id = get_global_id(0);
  const int label_count = get_global_size(0);

  int count = 0;
  float sum = 0;
  POS_src_touch_matrix_TYPE pos1;
  POS_src_touch_matrix_TYPE pos2;

  float touching_bool1;
  float touching_bool2;

  POS_src_pointlist_TYPE pX;
  POS_src_pointlist_TYPE pY;
  POS_src_pointlist_TYPE pZ;

  for (int l = 0; l < label_count; l++) {
    if (l != label_id) {
      if (l < label_id) {
        pos1 = POS_src_touch_matrix_INSTANCE(l, label_id, 0, 0);
      } else {
        pos1 = POS_src_touch_matrix_INSTANCE(label_id, l, 0, 0);
      }

      touching_bool1 = READ_src_touch_matrix_IMAGE(src_touch_matrix, sampler, pos1).x;
      if (touching_bool1 != 0) {

        //printf(" touchA %d %d\n", label_id, l);
        // distance = READ_src_distance_matrix_IMAGE(src_distance_matrix, sampler, pos1).x;

        for (int k = 0; k < label_count; k++) {
          if (k != l && k != label_id) {
            if (k < label_id) {
              pos1 = POS_src_touch_matrix_INSTANCE(k, label_id, 0, 0);
            } else {
              pos1 = POS_src_touch_matrix_INSTANCE(label_id, k, 0, 0);
            }
            if (k < l) {
              pos2 = POS_src_touch_matrix_INSTANCE(k, l, 0, 0);
            } else {
              pos2 = POS_src_touch_matrix_INSTANCE(l, k, 0, 0);
            }

            touching_bool1 = READ_src_touch_matrix_IMAGE(src_touch_matrix, sampler, pos1).x;
            if (touching_bool1 != 0) {
              //printf("  touchB %d %d\n", label_id, k);
              touching_bool2 = READ_src_touch_matrix_IMAGE(src_touch_matrix, sampler, pos2).x;
              if (touching_bool2 != 0) {
                //printf("   touchC %d %d\n", l, k);
                // triangle between: label_id, l, k
                for (int m = 0; m < label_count; m++) {
                  if (m != l && m != label_id && m != k) {
                    if (m < label_id) {
                      pos1 = POS_src_touch_matrix_INSTANCE(m, label_id, 0, 0);
                    } else {
                      pos1 = POS_src_touch_matrix_INSTANCE(label_id, m, 0, 0);
                    }
                    if (m < k) {
                      pos2 = POS_src_touch_matrix_INSTANCE(m, l, 0, 0);
                    } else {
                      pos2 = POS_src_touch_matrix_INSTANCE(l, m, 0, 0);
                    }

                    touching_bool1 = READ_src_touch_matrix_IMAGE(src_touch_matrix, sampler, pos1).x;
                    if (touching_bool1 != 0) {
                      //printf("    touchD %d %d\n", label_id, m);

                      touching_bool2 = READ_src_touch_matrix_IMAGE(src_touch_matrix, sampler, pos2).x;
                      if (touching_bool2 != 0) {
                        //printf("     touchE %d %d\n", l, m);

                        // another triangle between label_id, l and m
                        //printf("label_id %d\n", label_id);
                        //printf("l %d\n", l);
                        //printf("k %d\n", k);
                        //printf("m %d\n", m);

                        pX = POS_src_pointlist_INSTANCE(label_id, 0, 0, 0);
                        pY = POS_src_pointlist_INSTANCE(label_id, 1, 0, 0);
                        pZ = POS_src_pointlist_INSTANCE(label_id, 2, 0, 0);

                        float4 pLabelId = (float4){
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pX).x,
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pY).x,
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pZ).x,
                            0
                        };

                        pX = POS_src_pointlist_INSTANCE(l, 0, 0, 0);
                        pY = POS_src_pointlist_INSTANCE(l, 1, 0, 0);
                        pZ = POS_src_pointlist_INSTANCE(l, 2, 0, 0);
                        float4 pL = (float4){
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pX).x,
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pY).x,
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pZ).x,
                            0
                        };

                        pX = POS_src_pointlist_INSTANCE(k, 0, 0, 0);
                        pY = POS_src_pointlist_INSTANCE(k, 1, 0, 0);
                        pZ = POS_src_pointlist_INSTANCE(k, 2, 0, 0);
                        float4 pK = (float4){
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pX).x,
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pY).x,
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pZ).x,
                            0
                        };

                        pX = POS_src_pointlist_INSTANCE(m, 0, 0, 0);
                        pY = POS_src_pointlist_INSTANCE(m, 1, 0, 0);
                        pZ = POS_src_pointlist_INSTANCE(m, 2, 0, 0);
                        float4 pM = (float4){
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pX).x,
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pY).x,
                            READ_src_pointlist_IMAGE(src_pointlist, sampler, pZ).x,
                            0
                        };

                        //printf("p1 %f %f %f \n", pLabelId.x, pLabelId.y, pLabelId.z);
                        //printf("p2 %f %f %f \n", pL.x, pM.y, pL.z);
                        //printf("p3 %f %f %f \n", pK.x, pM.y, pK.z);
                        //printf("p4 %f %f %f \n", pM.x, pM.y, pM.z);


                        // https://math.stackexchange.com/questions/305642/how-to-find-surface-normal-of-a-triangle
                        float4 v = pL - pLabelId;
                        float4 w = pK - pLabelId;
                        float4 normalT1 = cross(v, w);

                        v = pL - pLabelId;
                        w = pM - pLabelId;
                        float4 normalT2 = cross(v, w);

                        //printf("dot %f\n", dot(normalT1, normalT2));
                        //printf("|n1| %f\n", length(normalT1));
                        //printf("|n2| %f\n", length(normalT2));


                        float cos_angle = dot(normalT1, normalT2) / length(normalT1) / length(normalT2);
                        float angle = acos(cos_angle) * 180.0 / M_PI;

                        //printf("angle %f\n", angle);
                        //printf("cos_angle %f\n", cos_angle);


                        if (angle > 180) {
                          angle = 360 - angle;
                        }
                        sum = sum + angle;
                        count++;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  float average = 0;
  if (count > 0) {
    average = sum / count;
  }
  average = count;
  WRITE_dst_average_angle_list_IMAGE(dst_average_angle_list, (POS_dst_average_angle_list_INSTANCE(label_id, 0, 0, 0)), CONVERT_dst_average_angle_list_PIXEL_TYPE(average));
}

