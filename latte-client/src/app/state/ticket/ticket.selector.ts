import { createSelector } from "@ngrx/store";
import { AppState } from "../app.state";

export const selectTicketState = (state: AppState) => state.tickets;

export const selectTickets = createSelector(
  selectTicketState,
  (state) => state.tickets
);