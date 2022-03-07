import { HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError } from "rxjs";
import { UserService } from "../services/user.service";

@Injectable()
export class UnauthorizedInterceptor implements HttpInterceptor {

  constructor(private userService: UserService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return next.handle(req).pipe(catchError(err => {
      if (err.status == 401) {
        this.userService.logout();
      }
      throw err;
    }));
  }
}
