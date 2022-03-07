import { Constants } from "../models/constants";

export class JWTService {
  /*
  * Performs simple CRUD operations on JWT in Session Storage
  */

  getJWT() {
    return sessionStorage.getItem(Constants.SESSION_STORAGE_JWT_KEY);
  }

  hasJWT() {
    return !!this.getJWT();
  }

  setJWT(jwt: string) {
    sessionStorage.setItem(Constants.SESSION_STORAGE_JWT_KEY, jwt);
  }

  removeJWT() {
    sessionStorage.removeItem(Constants.SESSION_STORAGE_JWT_KEY);
  }
}
