import { createAction, props } from "@ngrx/store";
import { TicketResponse } from "../../models/ticket.type";

export const addTicket = createAction('[Ticket] Add', props<{ticket: TicketResponse}>());
export const setTickets = createAction('[Ticket] Set', props<{tickets: TicketResponse[]}>());