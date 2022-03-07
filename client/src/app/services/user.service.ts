import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { lastValueFrom } from "rxjs";
import { Constants } from "../models/constants";
import { LoginRequest } from "../models/login";
import { SignUpRequest } from "../models/signup";
import { User } from "../models/user";
import { JWTService } from "./jwt.service";

@Injectable()
export class UserService {
  jsonHeaders: HttpHeaders = new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  });

  constructor(
    private http: HttpClient,
    private router: Router,
    private jwtService: JWTService
  ) { }

  evaluateUser(): void {
    let storedUser = this.getUserFromSessionStorage();
    if (storedUser == null) {
      this.getUserFromServer()
        .then(user => {
          this.saveUserToSessionStorage(user);
        });
    }
  }

  isLoggedIn() {
    return this.jwtService.hasJWT();
  }

  async getUserFromServer() {
    return lastValueFrom(
      this.http.get<User>(
        `${Constants.API_URL}/user/me`
      )
    );
  }

  // Session storage related methods
  getUserFromSessionStorage() {
    return sessionStorage.getItem(Constants.SESSION_STORAGE_USER_KEY);
  }

  saveUserToSessionStorage(user: User) {
    sessionStorage.setItem(Constants.SESSION_STORAGE_USER_KEY, JSON.stringify(user));
  }

  removeUserFromSessionStorage() {
    sessionStorage.removeItem(Constants.SESSION_STORAGE_USER_KEY);
  }

  // Authentication related methods
  async login(uri: string, loginRequest: LoginRequest): Promise<User> {
    /*
    * Tries to log in a user and returns a User if successful; 401 Unauthorized if not.
    */
    return lastValueFrom(this.http.post<any>(
      uri,
      loginRequest,
      { headers: this.jsonHeaders }
    ))
    .then((user) => {
      console.info(user);
      this.jwtService.setJWT(user.jwt);
      this.saveUserToSessionStorage(user);
      return user;
    });
  }

  async signUp(uri: string, signUpRequest: SignUpRequest): Promise<boolean> {
    /*
    * Returns only if user was registered successfully or not.
    */
    return lastValueFrom(this.http.post<any>(
      uri,
      signUpRequest,
      { headers: this.jsonHeaders }
    ))
    .then((value) => {
      console.info(value);
      return true;
    })
    .catch((error) => {
      console.error(error);
      return false;
    });
  }

  logout() {
    this.jwtService.removeJWT();
    this.removeUserFromSessionStorage();
    this.router.navigate(['/login']);
  }
}
