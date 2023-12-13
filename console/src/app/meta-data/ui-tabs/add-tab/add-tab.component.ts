import { Component, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from 'src/app/core/service/message.service';
import { EventEmitter } from '@angular/core';
import { MetaDataService } from '../../meta-data.service';

@Component({
  selector: 'app-add-tab',
  templateUrl: './add-tab.component.html',
  styleUrls: ['./add-tab.component.sass']
})
export class AddTabComponent implements OnInit {

  menuForm: UntypedFormGroup;
  isSubmitted: boolean = false;

  @Output() closeEvent = new EventEmitter();
  @Output() refresListEvent = new EventEmitter();

  constructor(
    private fb: UntypedFormBuilder,
    private modalService: NgbModal,
    private _metaService: MetaDataService,
    private _messageSevice: MessageService
  ) {
    this.createTabsForm();
  }

  ngOnInit(): void {
  }

  /**
   * @method createTabsForm
   * @description create's a blank form for the tabs
   */
  createTabsForm() {
    this.menuForm = this.fb.group({
      moduleName: ['', [Validators.required, Validators.minLength(5)]],
      path: ['', Validators.required],
      title: ['', [Validators.required, Validators.minLength(3)]],
      index: ['', Validators.required],
      icon: [''],
      topMenu: ['', Validators.required],
      cssClass: [''],
      parentModule: []
    })
  }

  /**
   * @method close
   * @description Emit's the close event to close the modal
   */
  close() {
    this.closeEvent.emit();
  }


  /**
   * @method refreshList
   * @description Emit's the refresh event to refresh the menu list
   */
  refreshList() {
    this.refresListEvent.emit();
  }

  /**
   * @method onSave
   * @param form 
   * @description call the add menu api to add menu
   */
  onSave(form: UntypedFormGroup) {
    if (form.valid) {
      this.isSubmitted = true
      const param = form.value;
      const parentModule = param.parentModule;
      delete param.parentModule;
      this._metaService.createMenu(param, parentModule).subscribe(
        res => {
          this._messageSevice.showSuccess("Menu Added Successfully");
          this.isSubmitted = false;
          this.close();
          this.refreshList();
        }
      )
    }
  }

}
