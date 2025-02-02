export interface CustomJwtPayload {
  role: string;
  sub: string;
  iat: number;
  exp: number;
}
