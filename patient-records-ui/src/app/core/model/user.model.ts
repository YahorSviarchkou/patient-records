import {RoleModel} from "./role.model";

export interface UserModel {
  id?: number
  login: string
  role: RoleModel
}
