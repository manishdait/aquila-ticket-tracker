import { createReducer, on } from "@ngrx/store";
import { TicketResponse } from "../../models/ticket.type";
import { addTicket, setTickets } from "./ticket.action";

export interface TicketState {
  tickets: TicketResponse[]
}

export const initialTicketState: TicketState = {
  tickets: []
}

export const ticketReducer = createReducer(
  initialTicketState,
  
  on(addTicket, (state, {ticket}) => ({
    ...state,
    tickets: [...state.tickets, ticket]
  })),

  on(setTickets, (state, {tickets}) => ({
    ...state,
    tickets: tickets
  }))
);