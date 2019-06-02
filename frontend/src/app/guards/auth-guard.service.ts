import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, Route } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../services/login.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router, private lgservice: LoginService) { }
    canActivate(
      route: ActivatedRouteSnapshot,
      state: RouterStateSnapshot):
      Observable<boolean> | Promise<boolean> | boolean {
        if (this.lgservice.CheckLogged()) {
          return true;
        }
        this.router.navigate(['']);
        return false;
    }
  }


