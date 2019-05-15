import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { UserLog } from '../classes/User';
import { LoginService } from '../services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  constructor(private fb: FormBuilder, private lgservice: LoginService) { }

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: [''],
      password: ['']
    });
  }
  onSubmit() {
    const User_data = new UserLog();
    User_data.email = this.loginForm.get('email').value;
    User_data.password = this.loginForm.get('password').value;
    this.lgservice.Login(User_data).subscribe((data) => {
        console.log(data);
    }, (error) => {
      console.log(error);
    });
  }

}
