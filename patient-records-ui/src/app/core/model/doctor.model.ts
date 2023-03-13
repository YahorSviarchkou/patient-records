import {PositionModel} from "./position.model";
import {UserModel} from "./user.model";

export interface DoctorModel {
  id?: number
  fio: string
  position: PositionModel
  user: UserModel
}
