import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar'

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private _snackBar: MatSnackBar) { }

  showSuccess(text) {
    this._snackBar.open(text, '', {
      duration: 5000,
      verticalPosition: "top",
      horizontalPosition: "right",
      panelClass: "bg-green"
    });
  };

  showError(text) {
    this._snackBar.open(text, '', {
      duration: 10000,
      verticalPosition: "top",
      horizontalPosition: "right",
      panelClass: "bg-red"
    });
  };

  showInfo(text) {
    this._snackBar.open(text, '', {
      duration: 10000,
      verticalPosition: "top",
      horizontalPosition: "right",
      panelClass: "bg-blue"
    });
  };

  showWarn(text) {
    this._snackBar.open(text, '', {
      duration: 10000,
      verticalPosition: "top",
      horizontalPosition: "right",
      panelClass: "bg-yellow"
    });
  };


}
