import { Component } from '@angular/core';
import { AlertService } from '../../service/alert.service';

@Component({
  selector: 'app-alert',
  imports: [],
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.css'
})
export class AlertComponent {
  message: string | undefined;

  constructor(private alertService: AlertService) {
    alertService.alert$.subscribe((message) => {
      this.message = message;
      this.reset();
    })
  }

  reset() {
    setTimeout(() => {
      this.message = undefined;
    }, 4000)
  }
}
