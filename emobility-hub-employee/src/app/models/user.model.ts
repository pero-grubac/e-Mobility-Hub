import { PageMetadata } from './pageMetadata.model';

export interface User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  role: string;
}

export interface UserRequest {
  username: string;
  firstName: string;
  lastName: string;
}
export interface DetailedUserRequest extends UserRequest {
  id: number;
  password: string;
  role: string;
}

export interface UserPage {
  content: User[];
  page: PageMetadata;
}
