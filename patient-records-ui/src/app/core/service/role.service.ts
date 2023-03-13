import {RoleModel} from "../model/role.model";

export class RoleService {

  static CHIEF_PHYSICIAN_ROLE: RoleModel = RoleService.generateRole(1, "CHIEF_PHYSICIAN")
  static PHYSICIAN_ROLE: RoleModel = RoleService.generateRole(2, "PHYSICIAN")
  static LOCAL_ADMIN_ROLE: RoleModel = RoleService.generateRole(3, "LOCAL_ADMIN")
  static ADMIN_ROLE: RoleModel = RoleService.generateRole(4, "ADMIN")

  static ROLES: RoleModel[] = [this.CHIEF_PHYSICIAN_ROLE, this.PHYSICIAN_ROLE, this.LOCAL_ADMIN_ROLE, this.ADMIN_ROLE]

  private static generateRole(id: number, title: string) : RoleModel {
    return {"id":id, "title":title}
  }
}
