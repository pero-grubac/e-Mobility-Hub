export interface AuthRequest {
  username: string;
  password: string;
}

export interface Session {
  username: string;
  role: string;
  // ISO-8601 instant at which the session cookie stops being accepted.
  expiresAt: string;
}

export interface AuthResponse extends Session {
  token: string;
}
