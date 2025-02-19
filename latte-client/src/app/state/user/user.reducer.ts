import { createReducer, on } from "@ngrx/store";
import { UserResponse } from "../../models/user.type";
import { addUser, removeUser, setUsers } from "./user.action";

export interface UserState {
  users: UserResponse[]
}

export const initialUserState: UserState = {
  users: []
}

export const userReducer = createReducer(
  initialUserState,

  on(addUser, (state, {user}) => ({
    ...state,
    users: [...state.users, user]
  })),

  on(setUsers, (state, {users}) => ({
    ...state,
    users: users
  })),

  on(removeUser, (state, {email}) => ({
    ...state,
    users: [...state.users.filter(u => u.email !== email)]
  }))
)