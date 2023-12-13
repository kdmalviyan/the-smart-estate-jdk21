import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-integration-form-dialog',
  templateUrl: './integration-form-dialog.component.html',
  styleUrls: ['./integration-form-dialog.component.scss']
})
export class IntegrationFormDialogComponent implements OnInit {

  integrationForm: UntypedFormGroup;

  @Input() AuthTypes: any;
  @Input() HttpMethods: any;
  @Input() Action: string;
  @Input() EditData: any;
  @Output() emitFormValue = new EventEmitter();

  constructor(
    private modalService: NgbModal,
    private fb: UntypedFormBuilder
  ) { }

  ngOnInit(): void {
    this.crateForm();
    if (this.Action == 'edit') {
      this.patchFormValue();
    }
  }

  crateForm() {
    this.integrationForm = this.fb.group({
      name: ['', Validators.required],
      url: ['', Validators.required],
      authType: ['', Validators.required],
      httpMethod: ['', Validators.required],
      isActive: true,
      params: this.fb.array([this.paramsControls()]),
      fieldsMappers: this.fb.array([this.fieldControls()]),
      headers: this.fb.array([this.headerControls()])
    })
  }

  private paramsControls() {
    return this.fb.group({
      id: [],
      paramKey: [''],
      paramValue: ['']
    });
  }

  private fieldControls() {
    return this.fb.group({
      id: [],
      propKey: [''],
      propValue: ['']
    });
  }

  private headerControls() {
    return this.fb.group({
      id: [],
      headerKey: [''],
      headerValue: ['']
    });
  }

  addParamsControls() {
    const control = <UntypedFormArray>this.integrationForm.controls['params'];
    control.push(this.paramsControls());
  }

  addHeadersControls() {
    const control = <UntypedFormArray>this.integrationForm.controls['headers'];
    control.push(this.headerControls());
  }

  addFieldControls() {
    const control = <UntypedFormArray>this.integrationForm.controls['fieldsMappers'];
    control.push(this.fieldControls());
  }

  removeParamsControls(i: number) {
    const control = <UntypedFormArray>this.integrationForm.controls['params'];
    control.removeAt(i);
  }

  removeFieldsControls(i: number) {
    const control = <UntypedFormArray>this.integrationForm.controls['fieldsMappers'];
    control.removeAt(i);
  }

  removeHeadersControls(i: number) {
    const control = <UntypedFormArray>this.integrationForm.controls['headers'];
    control.removeAt(i);
  }

  get ParamControls() {
    return this.integrationForm.get('params') as UntypedFormArray;
  }
  get FieldControls() {
    return this.integrationForm.get('fieldsMappers') as UntypedFormArray;
  }
  get HeaderControls() {
    return this.integrationForm.get('headers') as UntypedFormArray;
  }


  onCloseModal() {
    this.modalService.dismissAll();
    this.integrationForm.reset();
  }

  submitForm() {
    if (this.integrationForm.valid) {
      const data = this.integrationForm.value;
      this.emitFormValue.emit(data);
      this.modalService.dismissAll();
    }
  }

  patchFormValue() {
    this.EditData.params.forEach((element, index) => {
      if (index > 0) {
        this.addParamsControls();
      }
    });
    this.EditData.fieldsMappers.forEach((element, index) => {
      if (index > 0) {
        this.addFieldControls();
      }
    });
    this.EditData.headers.forEach((element, index) => {
      if (index > 0) {
        this.addHeadersControls();
      }
    });
    this.integrationForm.patchValue(this.EditData);
  }


}
