import { Component, OnInit } from '@angular/core';
import { ResetPasswordRequest, UserResponse } from '../../models/user.type';
import { UserService } from '../../service/user.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontawsomeIcons } from '../../shared/fa-icons';
import { Router } from '@angular/router';
import { ConfirmComponent } from '../../forms/confirm/confirm.component';
import { AuthService } from '../../service/auth.service';
import { AlertService } from '../../service/alert.service';

@Component({
  selector: 'app-profile',
  imports: [ReactiveFormsModule, FontAwesomeModule, ConfirmComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  profile: UserResponse | undefined;
  update: UserResponse | undefined;

  updateForm: FormGroup;
  updateFormErrors: boolean = false;
  isUpdated: boolean = false;

  resetForm: FormGroup;
  resetFormErrors: boolean = false;
  passwordType:Record<string, string> = {
    'confirmPassword': 'password',
    'updatedPassword': 'password'
  }
  password:Record<string, boolean> = {
    'confirmPassword': true,
    'updatedPassword': true
  }

  resetToggle: boolean = false;
  confirmToggle: boolean = false;

  message: string | undefined;
  operation: Operation | undefined;

  constructor(private authService: AuthService, private userService: UserService, private alertService: AlertService, private faLibrary: FaIconLibrary, private router: Router) {
    this.updateForm = new FormGroup({
      firstname: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email])
    });

    this.resetForm = new FormGroup({
      updatedPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
      confirmPassword: new FormControl('', [Validators.required, Validators.minLength(8)])
    })
  } 

  ngOnInit(): void {
    this.userService.fetchUserInfo().subscribe({
      next: (response) => {
        this.profile = response;

        this.update = {
          firstname: response.firstname,
          email: response.email,
          role: response.role
        };

        this.updateForm.controls['firstname'].setValue(this.update.firstname);
        this.updateForm.controls['email'].setValue(this.update.email);
      },
      error: (err) => {
        console.error(err);
      }
    });
    this.faLibrary.addIcons(...fontawsomeIcons);
  }

  get updateControls() {
    return this.updateForm.controls;
  }

  get resetControls() {
    return this.resetForm.controls;
  }

  changeFirstname(value: string) {
    if (this.update) {
      this.update.firstname = value;
      this.callUpdate();
    }
  }

  changeEmail(value: string) {
    if (this.update) {
      this.update.email = value;
      this.callUpdate();
    }
  }

  onUpdate() {
    this.message = `You will be logout once your profile is updated`;
    this.operation = Operation.UPDATE_USER;
    this.confirm();
  }

  onReset() {
    this.message = `Do you want to reset your password?`;
    this.operation = Operation.RESET_PASSWORD;
    this.confirm();
  }

  confirm() {
    this.confirmToggle = true;
  }

  trigger(event: boolean) {
    if (event) {
      if (this.operation === Operation.UPDATE_USER) {
        this.updateUser();
      } else if (this.operation === Operation.RESET_PASSWORD) {
        this.resetPassword()
      }
    }
    this.confirmToggle = false;
  }

  showPassword(value: string) {
    this.password[value] = !this.password[value]
    if (this.passwordType[value] === 'text') {
      this.passwordType[value] = 'password';
    } else{ 
      this.passwordType[value] = 'text';
    }
  }

  toggleReset() {
    this.resetToggle = !this.resetToggle;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['sign-in'], {replaceUrl: true})
  }

  private callUpdate() {
    if (this.profile && this.update) {
      if (
        this.profile.firstname === this.update.firstname 
        && this.profile.email === this.update.email
      ) {
        this.isUpdated = false;
      } else {
        this.isUpdated = true;
      }
    }
  }

  private updateUser() {
    if(this.updateForm.invalid) {
      this.updateFormErrors = true;
      return;
    }
    
    this.updateFormErrors = false;
    const request: UserResponse = {
      firstname: this.updateForm.get('firstname')?.value,
      email: this.updateForm.get('email')?.value,
      role: this.profile!.role
    }
    
    this.userService.updateUser(request).subscribe({
      next: (response) => {
        this.router.navigate(['sign-in'], {replaceUrl: true});
      },
      error: (err) => {
        console.error(err.error);
        this.alertService.alert = 'Error creating user';
      }
    });
  }

  private resetPassword() {
    if(this.resetForm.invalid  || 
      this.resetForm.get('updatedPassword')?.value != this.resetForm.get('confirmPassword')?.value) {
      this.resetFormErrors = true;
      return;
    }
    
    this.resetFormErrors = false;
    const request: ResetPasswordRequest = {
      updatePassword: this.resetForm.get('updatedPassword')?.value,
      confirmPassword: this.resetForm.get('confirmPassword')?.value
    }
    this.resetForm.reset()

    this.userService.resetPassword(request).subscribe({
      next: (response) => {
        this.alertService.alert = 'Password reset succesfully';
      },
      error: (err) => {
        console.error(err.error);
        this.alertService.alert = 'Error reseting password';
      }
    })
  }
}

enum Operation {
  UPDATE_USER,
  RESET_PASSWORD
}
