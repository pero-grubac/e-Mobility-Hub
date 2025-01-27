import { PageMetadata } from './pageMetadata.model';
import { User } from './user.model';

export interface Client extends User {
  idCardNumber: string;
  image: string;
  email: string;
  phoneNumber: string;
  isDeactivated: boolean;
  isBlocked: boolean;
}

export interface ClientPage {
  content: Client[];
  page: PageMetadata;
}
