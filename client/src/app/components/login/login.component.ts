import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginRequest } from 'src/app/models/login';
import { Providers } from 'src/app/models/provider';
import { JWTService } from 'src/app/services/jwt.service';
import { ProvidersService } from 'src/app/services/providers.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form!: FormGroup;
  providers!: Providers;
  isLoggedIn = this.userService.isLoggedIn();
  isLoginFailed = false;
  errorMessage = '';

  constructor(
    private router: Router,
    private userService: UserService,
    private jwtService: JWTService,
    private providersService: ProvidersService,
    private activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {  }

  async ngOnInit() {
    let jwt = '';
    let error = '';
    if (this.activatedRoute.snapshot.fragment != null) {
      let kv = this.activatedRoute.snapshot.fragment.split('=');
      if (kv[0] == 'token') {
        jwt = kv[1];
      } else if (kv[0] == 'error') {
        error = kv[1];
      }
    }
    this.providers = this.activatedRoute.snapshot.data['providers'];

    if (this.providers)
      this.providers = this.providersService.appendRedirectUri(this.providers);

    if (!!error) {
      // Attempted OAuth2 login but failed
      this.errorMessage = error;
      this.isLoginFailed = true;
    }
    if (!!jwt) {
      // Attempted OAuth2 login and succeeded
      this.jwtService.setJWT(jwt);
      this.isLoggedIn = true;
    }

    if (this.isLoggedIn) {
      // User already logged in or just logged in. Redirect to Main
      this.userService.evaluateUser();
      this.router.navigate(['/']);
    }

    // User not logged in. Display login page
    this.form = this.fb.group({
      email: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [Validators.required, Validators.minLength(8)])
    });
  }

  get password() {
    return this.form.get('password');
  }

  login() {
    let request = this.form.value as LoginRequest;
    console.debug(this.providers.local.loginUri);
    this.userService.login(
      this.providers.local.loginUri || '',
      request
    )
    .catch(error => {
      this.errorMessage = error;
      this.isLoginFailed = true;
    });
  }
}
