import { Injectable } from '@angular/core';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { Providers } from '../models/provider';
import { ProvidersService } from '../services/providers.service';

@Injectable({
  providedIn: 'root'
})
export class ProvidersResolver implements Resolve<Providers> {

  constructor(private providersService: ProvidersService) { }
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Providers> {
    return this.providersService.getProviders();
  }
}
