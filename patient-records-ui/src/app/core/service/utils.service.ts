export class UtilsService{

  static formatDate(dateAny: any): string {
    let date: Date = new Date(dateAny)
    return this.digitToString(date.getDate()) + "."
      + this.digitToString(date.getMonth() + 1) + "."
      + this.digitToString(date.getFullYear()) + " "
      + this.digitToString(date.getHours()) + ":"
      + this.digitToString(date.getMinutes())
  }

  static digitToString(value: number): string {
    return value / 10 < 1 ? "0" + value : value.toString()
  }
}
