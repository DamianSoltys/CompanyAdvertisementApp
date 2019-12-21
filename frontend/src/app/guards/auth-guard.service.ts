import { Injectable } from "@angular/core";
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
  Route
} from "@angular/router";
import { Observable } from "rxjs";
import { LoginService } from "../services/login.service";
import { UserService } from "../services/user.service";
import { SnackbarService, SnackbarType } from "../services/snackbar.service";

@Injectable({
  providedIn: "root"
})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private lgservice: LoginService,
    private uDataService: UserService,
    private sDataService: SnackbarService
  ) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    if (this.lgservice.CheckLogged()) {
      return true;
    } else {
      this.sDataService.open({
        snackbarType: SnackbarType.error,
        message: 'Nie jesteś zalogowany!'
      });
      this.router.navigate(['']);
      return false;
    }
  }
}
