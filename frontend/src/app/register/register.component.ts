import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { UserReg } from '../classes/User';
import { RegisterService } from '../services/register.service';
import { HttpResponse } from '@angular/common/http';
import { SnackbarOptions, SnackbarService, SnackbarType } from '../services/snackbar.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  constructor(private fb: FormBuilder, private regService: RegisterService, private snackbarService: SnackbarService) {}

  ngOnInit() {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required, Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ0-9]+$/))]],
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [Validators.required, Validators.pattern(new RegExp(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{5,}$/))]
      ],
      checkpassword: ['', [Validators.required]]
    });
  }

  get regForm() {
    return this.registerForm.controls;
  }

  public onSubmit() {
    if (this.regForm.password.value === this.regForm.checkpassword.value) {
      const User_Data: UserReg = this.setUserData();
      this.checkIfRegisterSuccess(User_Data);
    } else {
      this.snackbarService.open({
        message: 'Hasła są różne',
        snackbarType: SnackbarType.error
      });
    }
  }

  private checkIfRegisterSuccess(User_Data: UserReg) {
    this.regService.Register(User_Data).subscribe(
      (data: HttpResponse<any>) => {
        this.snackbarService.open({
          message: 'Link potwierdzający został wysłany na podany adres',
          snackbarType: SnackbarType.success
        });
      },
      error => {
        this.snackbarService.open({
          message: 'Rejestracja niepowiodła się!',
          snackbarType: SnackbarType.error
        });
      }
    );
  }

  private setUserData(): UserReg {
    const User_Data = new UserReg();
    User_Data.name = this.registerForm.get('name').value;
    User_Data.email = this.registerForm.get('email').value;
    User_Data.password = this.registerForm.get('password').value;
    return User_Data;
  }
}
