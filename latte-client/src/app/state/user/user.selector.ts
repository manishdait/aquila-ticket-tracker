import { createSelector } from "@ngrx/store";
import { AppState } from "../app.state";

export const selectUserstate = (state: AppState) => state.users;

export const userSelector = createSelector(
  selectUserstate,
  (state) => state.users
);