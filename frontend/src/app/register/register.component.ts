import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { UserReg } from '../classes/User';
import { RegisterService } from '../services/register.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;
  passwordMatch = false;
  register_error: boolean;
  error_message: string;
  success_message = '';
  constructor(private fb: FormBuilder, private regService: RegisterService) { }

  ngOnInit() {
    this.registerForm = this.fb.group({
      name: ['',[Validators.required, Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ0-9]+$/))]],
      email: ['',[Validators.required, Validators.email]],
      password: ['',[Validators.required, Validators.pattern(new RegExp(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{5,}$/))]],
      checkpassword: ['', [Validators.required]]
    });
  }

  get regForm() {
    return this.registerForm.controls;
  }

 onSubmit() {
   if (this.regForm.password.value === this.regForm.checkpassword.value) {
    this.passwordMatch = false;
    const User_Data: UserReg = this.setUserData();
    this.checkIfRegisterSuccess(User_Data);
  } else {
    this.passwordMatch = true;
  }
 }

 checkIfRegisterSuccess(User_Data: UserReg) {
    this.regService.Register(User_Data).subscribe((data:HttpResponse<any>) => {
    if (data.status === 200) {
      this.register_error = false;
      this.success_message = 'Proszę potwierdzić swoje konto linkiem wysłanym na podany adres email';
       console.log('Proszę potwierdzić swoje konto linkiem wysłanym na podany email');
     }
   }, (error) => {
    this.error_message = error.error.text;
    this.register_error = true;
    console.log(error.error.text);
   });
 }

 setUserData(): UserReg{
    const User_Data = new UserReg();
    User_Data.name = this.registerForm.get('name').value;
    User_Data.email = this.registerForm.get('email').value;
    User_Data.password = this.registerForm.get('password').value;
    return User_Data;
 }
}
