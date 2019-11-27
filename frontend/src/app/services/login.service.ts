import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserLog } from '../classes/User';
import { Observable, BehaviorSubject, observable, Subject } from 'rxjs';
import { storage_Avaliable } from '../classes/storage_checker';
import { Route, Router } from '@angular/router';

declare var FB: any;

export interface fbResponse {
  authResponse: fbAuthResponse,
  status?:string,
}

export interface fbAuthResponse {
  accessToken?: string,
  data_access_expiration_time?: number
  expiresIn?: number
  signedRequest?: string,
  userID?: string,
}

export interface FBPostRequest {
  authResponse:fbAuthResponse,
  companyId:number,
  status:{
    status:string,
  }
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  Logged = new BehaviorSubject(this.CheckLogged());
  public fbResponse:fbResponse;
  public httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    observe: 'response' as 'response'
  };
  public FBConnection = new BehaviorSubject<boolean>(false);

  public FBstatus = new Subject<boolean>();
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

  public facebookLogin(companyId:number) {
    let subject=new Subject<any>();
      if(FB) {
        
        FB.login((response)=>
            {
              this.fbResponse = response;
              if (response.authResponse)
              {
                let postRequest:FBPostRequest = {
                  authResponse:this.fbResponse.authResponse,
                  companyId:companyId,
                  status:{
                    status:this.fbResponse.status,
                  }
                }
                this.fbPostRequest(postRequest).subscribe(response=>{
                  if(response) {
                    subject.next(true);
                    this.FBstatus.next(true);
                  } else {
                    subject.next(false);
                    this.FBstatus.next(false);
                  }
                });
               }
               else
               {
                subject.next(false);
                this.FBstatus.next(false);
               console.log('User login failed');
             }
          },{scope: 'manage_pages,public_profile,pages_show_list,publish_pages'});
      } else {
        subject.next(false);
        this.FBstatus.next(false);
      }
      return subject;
  }

  public checkIfMediaConnected(companyId:number) {
    let subject = new Subject<any>();
    this.http.get(`http://localhost:8090/api/social/connections/${companyId}`,{observe:'response'}).subscribe(response=>{
      console.log(response)
      subject.next(response.body);
    },error=>{
      console.log(error);
      subject.next(false);
    });
    return subject;
  }

  private fbPostRequest(fbPostRequest:FBPostRequest) {
    let subject = new Subject<any>();
    this.http.post(`http://localhost:8090/api/fb/login_in`,fbPostRequest).subscribe(response=>{
      console.log(response);
      subject.next(response);
    },error=>{
      console.log(error);
      subject.next(false);
    });
    return subject;
  }

  public checkIfUserFBLogged() {
    FB.getLoginStatus(response=> {  
      if(response.status == "unknown") {
        this.FBConnection.next(true);      
      } else {
        this.FBConnection.next(false);
      } 
    },true);
  }

  public twitterLogin() {
    let subject = new Subject<any>();

    return subject;
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
