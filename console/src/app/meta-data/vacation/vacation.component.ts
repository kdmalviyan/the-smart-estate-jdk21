import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-vacation',
  templateUrl: './vacation.component.html',
  styleUrls: ['./vacation.component.sass']
})
export class VacationComponent implements OnInit {
  statusType: string;
  showHide: boolean;
  vacationStatus = [
    { key: 1, value: 'Approve' },
    { key: 2, value: 'Reject' }
  ];
  remark = new UntypedFormControl('', [Validators.required]);
  rows = [
    {
      name: "mercy", age: 10, town: "Nairobi", country: "kenya"
    },
    {
      name: "Vincent", age: 40, town: "Kampala", country: "Uganda"
    },
    {
      name: "Wesley", age: 41, town: "Cairo", country: "Egypt"
    }
  ]
  constructor(
    private modalService: NgbModal
  ) { }

  ngOnInit(): void {
  }
  changeStatus(event, content: any) {
    console.log(event.target.value);
    const modalRef = this.modalService.open(content,
      {
        ariaLabelledBy: '',
        backdrop: false
      })
    if (event.target.value === 'Approve') {
      this.statusType = 'Approve';
      this.showHide = false;

    }
    else {
      this.statusType = 'Reject';
      this.showHide = true;
    }
  }
  onCloseModal() {
    this.modalService.dismissAll();

  }
}
