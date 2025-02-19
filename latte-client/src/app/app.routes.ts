import { Routes } from '@angular/router';
import { homeGuard } from './guard/home.guard';
import { AuthComponent } from './pages/auth/auth.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { HomeComponent } from './pages/home/home.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { TicketDetailsComponent } from './pages/ticket-details/ticket-details.component';
import { TicketComponent } from './pages/ticket/ticket.component';
import { UserComponent } from './pages/user/user.component';

export const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'home'},
  {path: 'sign-in', component: AuthComponent},
  {
    path: 'home', 
    component: HomeComponent,
    children: [
      {path: 'dashboard', component: DashboardComponent},
      {path: 'tickets', component: TicketComponent},
      {path: 'tickets/:id', component: TicketDetailsComponent},
      {path: 'users', component: UserComponent},
      {path: 'profile', component: ProfileComponent},
      {path: '', pathMatch: 'full', redirectTo: 'dashboard'}
    ],
    canActivate: [homeGuard]
  }
];
