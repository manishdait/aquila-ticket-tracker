import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirm',
  imports: [],
  templateUrl: './confirm.component.html',
  styleUrl: './confirm.component.css'
})
export class ConfirmComponent {
  @Input('message') message: string | undefined;
  @Output('state') state: EventEmitter<boolean> = new EventEmitter();

  trigger(state: boolean) {
    this.state.emit(state);
  }
}
