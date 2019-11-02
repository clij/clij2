__kernel void draw_sphere_3D   (DTYPE_IMAGE_OUT_3D dst,
                                   float cx,
                                   float cy,
                                   float cz,
                                   float rx,
                                   float ry,
                                   float rz,
                                   float rxsq,
                                   float rysq,
                                   float rzsq
                     )
{
  const float x = get_global_id(0);
  const float y = get_global_id(1);
  const float z = get_global_id(2);

  if ((x < cx - rx) || (x > cx + rx)) {
    return;
  }
  if ((y < cy - ry) || (y > cy + ry)) {
    return;
  }
  if ((z < cz - rz) || (z > cz + rz)) {
    return;
  }

  float xSquared = pow(x - cx, 2);
  float ySquared = pow(y - cy, 2);
  float zSquared = pow(z - cz, 2);

  if ((xSquared / rxsq + ySquared / rysq + zSquared / rzsq) <= 1.0) {
      int4 ipos = (int4){x,y,z,0};
      WRITE_IMAGE_3D (dst, ipos, 1);
  }
}

__kernel void draw_sphere_2D   (DTYPE_IMAGE_OUT_2D dst,
                                   float cx,
                                   float cy,
                                   float rx,
                                   float ry,
                                   float rxsq,
                                   float rysq
                     )
{
  const float x = get_global_id(0);
  const float y = get_global_id(1);
/*
  if ((x < cx - rx) || (x > cx + rx)) {
    return;
  }
  if ((y < cy - ry) || (y > cy + ry)) {
    return;
  }*/

  float xSquared = pow(x - cx, 2);
  float ySquared = pow(y - cy, 2);

  if ((xSquared / rxsq + ySquared / rysq) <= 1.0) {
      int2 ipos = (int2){x,y};
      WRITE_IMAGE_2D (dst, ipos, 1);
  }
}