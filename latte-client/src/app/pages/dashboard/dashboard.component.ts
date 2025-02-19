import { Component, OnInit } from '@angular/core';
import { getColor } from '../../shared/utils';
import { jwtDecode } from 'jwt-decode';
import { AuthService } from '../../service/auth.service';
import { TicketService } from '../../service/ticket.service';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  firstname: string;
  info: any;

  constructor (private authService: AuthService, private ticketServie: TicketService) {
    const token:any = jwtDecode(authService.getAccessToken());
    this.firstname = token.firstname;
  }

  ngOnInit(): void {
    this.ticketServie.fetchTicktsInfo().subscribe({
      next: (response) => {
        this.info = response;
      },
      error: (err) => {
        console.error(err.error);
      }
    })
  }

  color(username: any): string {
    if (!username) {return '#ddd'}
    return getColor(username);
  }

  greet(): string {
    const hours: number = new Date().getHours();
    if (hours >= 5 && hours <= 11) {
      return "Good Morning";
    } else if (hours >= 12 && hours <= 16) {
      return "Good Afternoon";
    } else {
      return "Good Evening";
    }
  }
}
