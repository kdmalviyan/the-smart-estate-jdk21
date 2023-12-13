import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FilterService {

  constructor() { }

  filter(event, filteredData, data) {
    let val = event.target.value.toLowerCase();
    // get the amount of columns in the table
    // get the key names of each column in the dataset
    let keys = Object.keys(filteredData[0]);
    let colsAmt = keys.length;
    // assign filtered matches to the active datatable
    data = filteredData.filter(function (item) {
      // iterate through each row's column data
      for (let i = 0; i < colsAmt; i++) {
        // check for a match
        if (item[keys[i]] != null) {
          if (item[keys[i]] instanceof Object) {
            let nestedObject = item[keys[i]];
            let nestedKeys = Object.keys(nestedObject);
            for (let i = 0; i < nestedKeys.length; i++) {
              if (
                nestedObject[nestedKeys[i]] != null &&
                (nestedObject[nestedKeys[i]]
                  .toString()
                  .toLowerCase()
                  .indexOf(val) !== -1 ||
                  !val)
              ) {
                // found match, return true to add to result set
                return true;
              }
            }
          } else {
            if (
              item[keys[i]] != null &&
              (item[keys[i]].toString().toLowerCase().indexOf(val) !== -1 ||
                !val)
            ) {
              // found match, return true to add to result set
              return true;
            }
          }
        }
      }
    });
    // whenever the filter changes, always go back to the first page
    return data;
  }
}
