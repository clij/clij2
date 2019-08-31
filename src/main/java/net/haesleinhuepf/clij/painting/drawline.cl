__kernel void draw_line_3D   (DTYPE_IMAGE_OUT_3D dst,
                                   float x1,
                                   float y1,
                                   float z1,
                                   float x2,
                                   float y2,
                                   float z2,
                                   float radius
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  if (!((x >= x1 - radius && x <= x2 + radius) || (x >= x2 - radius && x <= x1 + radius))) {
    return;
  }
  if (!((y >= y1 - radius && y <= y2 + radius) || (y >= y2 - radius && y <= y1 + radius))) {
    return;
  }
  if (!((z >= z1 - radius && z <= z2 + radius) || (z >= z2 - radius && z <= z1 + radius))) {
    return;
  }


  float4 r = (float4){x-x2, y-y2, z-z2, 0};
  float4 r1 = (float4){x1-x2, y1-y2, z1-z2, 0};
  float4 v = cross(r1, r);
  float distance = length(v) / length(r1);

  if (distance < radius) {
    int4 ipos = (int4){x,y,z,0};
    WRITE_IMAGE_3D (dst, ipos, 1);
  }
}

__kernel void draw_line_2D   (DTYPE_IMAGE_OUT_3D dst,
                                   float x1,
                                   float y1,
                                   float x2,
                                   float y2,
                                   float radius
                     )
{
  const float x = get_global_id(0);
  const float y = get_global_id(1);

  if (!((x >= x1 - radius && x <= x2 + radius) || (x >= x2 - radius && x <= x1 + radius))) {
    return;
  }
  if (!((y >= y1 - radius && y <= y2 + radius) || (y >= y2 - radius && y <= y1 + radius))) {
    return;
  }


  float4 r = (float4){x-x2, y-y2, 0, 0};
  float4 r1 = (float4){x1-x2, y1-y2, 0, 0};
  float4 v = cross(r1, r);
  float distance = length(v) / length(r1);

  if (distance < radius) {
    int2 ipos = (int2){x,y};
    WRITE_IMAGE_2D (dst, ipos, 1);
  }
  //WRITE_IMAGE_2D (dst, ipos, distance);
}