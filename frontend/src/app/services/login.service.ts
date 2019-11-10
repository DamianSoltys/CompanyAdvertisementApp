import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserLog } from '../classes/User';
import { Observable, BehaviorSubject, observable, Subject } from 'rxjs';
import { storage_Avaliable } from '../classes/storage_checker';
import { Route, Router } from '@angular/router';
declare var FB: any;
@Injectable({
  providedIn: 'root'
})
export class LoginService {
  Logged = new BehaviorSubject(this.CheckLogged());
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    observe: 'response' as 'response'
  };
  constructor(private http: HttpClient, private router: Router) {}

  public initFacebookApi() {
    (window as any).fbAsyncInit = function() {
      FB.init({
        appId      : '422141488475751',
        cookie     : true,
        xfbml      : true,
        version    : 'v5.0'
      });
      FB.AppEvents.logPageView();
    };

    (function(d, s, id){
       var js, fjs = d.getElementsByTagName(s)[0];
       if (d.getElementById(id)) {return;}
       js = d.createElement(s); js.id = id;
       js.src = "https://connect.facebook.net/en_US/sdk.js";
       fjs.parentNode.insertBefore(js, fjs);
     }(document, 'script', 'facebook-jssdk'));
  }

  public facebookLogin() {
    let subject=new Subject<any>();
      if(FB) {
        console.log("submit login to facebook");
        // FB.login();
        FB.login((response)=>
            {
              console.log('submitLogin',response);
              if (response.authResponse)
              {
                //login success
                //login success code here
                //redirect to home page
                subject.next(true);
               }
               else
               {
                subject.next(false);
               console.log('User login failed');
             }
          });
      } else {
        subject.next(false);
      }
      return subject;
  }

  public checkIfFacebookLogged() {

  }

  public twitterLogin() {
    let oAuthHeaders = new HttpHeaders();
  }

  public Login(User_Data: UserLog): Observable<any> {
    return this.http.post(
      'http://localhost:8090/auth/login',
      { email: User_Data.email, password: User_Data.password },
      this.httpOptions
    );
  }
  public ChangeLogged() {
    this.Logged.next(!this.Logged.value);
  }
  public CheckLogged() {
    if (localStorage.getItem('token')) {
      return true;
    } else {
      return false;
    }
  }
  public logoutStorageClean() {
    if (storage_Avaliable('localStorage')) {
      window.localStorage.clear();
      this.ChangeLogged();
      this.router.navigate(['']);
    } else {
      console.log('Storage nie jest dostępny');
    }
  }
}
