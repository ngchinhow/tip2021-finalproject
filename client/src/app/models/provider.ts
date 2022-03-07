export interface Provider {
  provider: string;
  imageUri: string;
  imageAlt: string;
  loginUri: string;
  signUpUri: string;
}

export interface Providers {
  local: Partial<Provider>;
  social: Provider[];
}
