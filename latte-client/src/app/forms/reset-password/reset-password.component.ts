import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontawsomeIcons } from '../../shared/fa-icons';
import { ResetPasswordRequest, UserResponse } from '../../models/user.type';
import { UserService } from '../../service/user.service';
import { AlertService } from '../../service/alert.service';

@Component({
  selector: 'app-reset-password',
  imports: [ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent implements OnInit {
  @Input('user') user: UserResponse | undefined;
  @Output('toggle') toggle: EventEmitter<boolean> = new EventEmitter();

  form: FormGroup;
  formErrors: boolean = false;

  passwordType:Record<string, string> = {
    'confirmPassword': 'password',
    'updatedPassword': 'password'
  }
  password:Record<string, boolean> = {
    'confirmPassword': true,
    'updatedPassword': true
  }

  constructor(private faLibrary: FaIconLibrary, private userService: UserService, private alertService: AlertService) {
    this.form = new FormGroup({
      updatedPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
      confirmPassword: new FormControl('', [Validators.required, Validators.minLength(8)])
    });
  }

  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons);
  }

  get formControls() {
    return this.form.controls;
  }

  onSubmit() {
    if (this.form.invalid || this.form.get('updatedPassword')?.value != this.form.get('confirmPassword')?.value) {
      this.formErrors = true;
      return;
    }

    this.formErrors = false;
    const request: ResetPasswordRequest = {
      updatePassword: this.form.get('updatedPassword')?.value,
      confirmPassword: this.form.get('confirmPassword')?.value
    }
    this.form.reset();

    if(this.user) {
      this.userService.resetPasswordForUser(request, this.user.email).subscribe({
        next: () => {
          this.alertService.alert = 'Updated user information';
        },
        error: (err) => {
          this.alertService.alert = err.error.error;
        }
      });
    }
  }

  showPassword(value: string) {
    this.password[value] = !this.password[value]
    if (this.passwordType[value] === 'text') {
      this.passwordType[value] = 'password';
    } else{ 
      this.passwordType[value] = 'text';
    }
  }

  cancel() {
    this.toggle.emit(false);
  }
}
