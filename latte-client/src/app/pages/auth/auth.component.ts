import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontawsomeIcons } from '../../shared/fa-icons';
import { AuthRequest } from '../../models/auth.type';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { AlertService } from '../../service/alert.service';

@Component({
  selector: 'app-auth',
  imports: [ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent implements OnInit {
  form: FormGroup;
  formError: boolean = false;

  passtype: string = 'password';
  passicon: string = 'eye'

  constructor(private faLibrary: FaIconLibrary, private alertService: AlertService, private authService: AuthService, private router: Router) {
    this.form = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)])
    });  
  }

  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons);
  }

  get formControls() {
    return this.form.controls;
  }

  onSubmit() {
    if (this.form.invalid) {
      this.formError = true;
      return;
    }

    this.formError = false;
    var request: AuthRequest = {
      email: this.form.get('email')?.value,
      password: this.form.get('password')?.value
    }
    this.form.reset();

    this.authService.authenticateUser(request).subscribe({
      next: () => {
        this.router.navigate(['home'], {replaceUrl: true})
      },
      error: (err) => {
        this.alertService.alert = err.error.error;
      }
    });
  }

  showPassword() {
    if (this.passtype === 'password') {
      this.passtype = 'text';
      this.passicon = 'eye-slash';
    } else {
      this.passtype = 'password';
      this.passicon = 'eye';
    }
  }
}
