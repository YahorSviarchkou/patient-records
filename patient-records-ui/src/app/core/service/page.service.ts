export class PageService {

  private _array: any[]
  private _arrayPage: any[]

  static _pageSize: number = 10
  private _currentPage: number = 0

  constructor(array: any[]) {
    this._array = array;
    this._arrayPage = array.slice(0, PageService._pageSize)
  }

  static customPage(array: any[], pageSize: number) {
    PageService._pageSize = pageSize
    return new PageService(array);
  }

  openPrevPage(){
    if(this._currentPage - PageService._pageSize < 0) {
      this._currentPage = 0
    } else {
      this._currentPage -= PageService._pageSize
    }
    this._arrayPage = this._array.slice( this._currentPage, this._currentPage + PageService._pageSize)
  }

  openNextPage(){
    if(!(this._currentPage + PageService._pageSize > this._array.length)) {
      this._currentPage += PageService._pageSize
    }
    this._arrayPage = this._array.slice(this._currentPage,this._currentPage + PageService._pageSize)
  }

  set array(value: any[]) {
    this._array = value;
  }

  get pageSize(): number {
    return PageService._pageSize;
  }

  get arrayPage(): any[] {
    return this._arrayPage;
  }
}
