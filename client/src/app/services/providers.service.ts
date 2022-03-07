import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Constants } from "../models/constants";
import { Providers } from "../models/provider";

@Injectable()
export class ProvidersService {
  constructor(
    private http: HttpClient
  ) { }

  getProviders() {
    return this.http.get<Providers>(
      `${Constants.AUTH_URL}/supported_providers`
    );
  }

  appendRedirectUri(providers: Providers) {
    /*
    * Used to append the redirect_uri to the signup paths given by
    * the backend
    */
    providers.social = providers.social.map(provider => {
      let httpParams = new HttpParams()
        .set("redirect_uri", Constants.OAUTH2_REDIRECT_URI);
      provider.loginUri = `${provider.loginUri}?${httpParams.toString()}`;
      return provider;
    });
    return providers;
  }
}
