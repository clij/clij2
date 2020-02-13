__kernel void exclude_labels_sub_surface_2d (
    IMAGE_src_pointlist_TYPE src_pointlist,
    IMAGE_src_label_map_TYPE src_label_map,
    IMAGE_label_index_dst_TYPE label_index_dst,
    float centerX,
    float centerY
)
{
  const sampler_t intsampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int label_id = get_global_id(0);

  const float x = READ_src_pointlist_IMAGE(src_pointlist, intsampler, POS_src_pointlist_INSTANCE(label_id - 1, 0, 0, 0)).x;
  const float y = READ_src_pointlist_IMAGE(src_pointlist, intsampler, POS_src_pointlist_INSTANCE(label_id - 1, 1, 0, 0)).x;

  float2 directionVector = (float2){x - centerX, y - centerY};
  //if (direction != 0) {
  //  directionVector.x = -directionVector.x;
  //  directionVector.y = -directionVector.y;
  //}
  float len = length(directionVector);
  directionVector.x = directionVector.x / len;
  directionVector.y = directionVector.y / len;

  //if (label_id == 33) {
  //  printf("DIR %f / %f \n", directionVector.x, directionVector.y);
  //}

  int width = GET_IMAGE_WIDTH(src_label_map);
  int height = GET_IMAGE_HEIGHT(src_label_map);

  float2 position = (float2){x, y};

  bool foundMyself = false;
  bool foundAnother = false;
  while (true) {
      int2 pos = (int2){(int)position.x, (int)position.y};
      //if (label_id == 33) {
      //  printf("\n%d / %d", pos.x, pos.y);
      //}
      int label = READ_src_label_map_IMAGE(src_label_map, intsampler, pos).x;
      if (label_id == label) {
        foundMyself = true;
        //if (label_id == 33) {
        //  printf(" found myself");
        //}
      } else if (foundMyself && label_id != label && label > 0) { // I found another label sitting between myself and the image border
        foundAnother = true;
        //if (label_id == 33) {
        //  printf(" found another");
        //}
        break;
      }

      position = position + directionVector;
      if (position.x < 0 || position.y < 0 || position.x >= width || position.y >= height) {
        break;
      }
  }

  if (!foundAnother) {
    //printf("%d eliminate\n", label_id);
    WRITE_label_index_dst_IMAGE (label_index_dst, POS_label_index_dst_INSTANCE(label_id,0,0,0), 0);
  } else {
    //printf("%d keep old value\n", label_id);
    WRITE_label_index_dst_IMAGE (label_index_dst, POS_label_index_dst_INSTANCE(label_id,0,0,0), CONVERT_label_index_dst_PIXEL_TYPE(label_id));
    }
}
