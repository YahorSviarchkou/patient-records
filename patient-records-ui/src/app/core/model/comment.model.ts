import {DoctorModel} from "./doctor.model";

export interface CommentModel {
  id?: number
  content: string
  created?: Date
  doctorDetails?: DoctorModel
}
