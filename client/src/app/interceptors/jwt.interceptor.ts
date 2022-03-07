import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Constants } from "../models/constants";
import { JWTService } from "../services/jwt.service";

@Injectable()
export class JWTInterceptor implements HttpInterceptor {

  constructor(private jwtService: JWTService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    let isAPIRequest = req.url.startsWith(Constants.API_URL);
    if (isAPIRequest && this.jwtService.hasJWT()) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${this.jwtService.getJWT()}`
        }
      });
    }
    return next.handle(req);
  }
}
