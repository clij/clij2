__kernel void draw_box_3D   (DTYPE_IMAGE_OUT_3D dst,
                                   float x1,
                                   float y1,
                                   float z1,
                                   float x2,
                                   float y2,
                                   float z2
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  if (!((x >= x1 && x <= x2) || (x >= x2 && x <= x1))) {
    return;
  }
  if (!((y >= y1 && y <= y2) || (y >= y2 && y <= y1))) {
    return;
  }
  if (!((z >= z1 && z <= z2) || (z >= z2 && z <= z1))) {
    return;
  }

  int4 ipos = (int4){x,y,z,0};
  WRITE_IMAGE_3D (dst, ipos, 1);
}

__kernel void draw_box_2D   (DTYPE_IMAGE_OUT_2D dst,
                                   float x1,
                                   float y1,
                                   float x2,
                                   float y2
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  if (!((x >= x1 && x <= x2) || (x >= x2 && x <= x1))) {
    return;
  }
  if (!((y >= y1 && y <= y2) || (y >= y2 && y <= y1))) {
    return;
  }

  int2 ipos = (int2){x,y};
  WRITE_IMAGE_2D (dst, ipos, 1);
}