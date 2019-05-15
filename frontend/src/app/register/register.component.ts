import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { UserReg } from '../classes/User';
import { RegisterService } from '../services/register.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;
  constructor(private fb: FormBuilder, private regService: RegisterService) { }

  ngOnInit() {
    this.registerForm = this.fb.group({
      name: [''],
      email: [''],
      password: [''],
      checkpassword: ['']
    });
  }
 onSubmit() {
    const User_Data = new UserReg();
    User_Data.name = this.registerForm.get('name').value;
    User_Data.email = this.registerForm.get('email').value;
    User_Data.password = this.registerForm.get('password').value;
   this.regService.Register(User_Data).subscribe((data) => {
     console.log(data);
   }, (error) => {
     console.log(error);
   });
 }
}
