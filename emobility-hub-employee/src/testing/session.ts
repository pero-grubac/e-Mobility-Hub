import { Session } from '../app/models/auth.models';

export function fakeSession(role: string, expiresInSeconds = 3600): Session {
  return {
    username: 'tester',
    role,
    expiresAt: new Date(Date.now() + expiresInSeconds * 1000).toISOString(),
  };
}
