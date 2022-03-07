export interface User {
  jwt: string;
  userId: number;
  displayName: string;
  email: string;
  provider: string;
  providerUserId: string;
  profilePictureUrl: string;
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  enabled: boolean;
}
