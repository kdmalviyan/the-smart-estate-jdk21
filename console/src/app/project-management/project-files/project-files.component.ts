
import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { UntypedFormControl } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { ProjectManagementService } from '../project-management.service';

@Component({
  selector: 'app-project-files',
  templateUrl: './project-files.component.html',
  styleUrls: ['./project-files.component.sass']
})
export class ProjectFilesComponent implements OnInit {

  fileType = new UntypedFormControl;
  file = new UntypedFormControl;
  @Output() uploadFile = new EventEmitter();

  @Output() downloadFile = new EventEmitter();
  @Output() deleteFile = new EventEmitter();
  @Input() fileList;

  filedata = [];
  filefilteredData = [];
  @ViewChild(DatatableComponent, { static: false }) flatTable: DatatableComponent;

  constructor(
    private modalService: NgbModal,
    private projectService: ProjectManagementService
  ) { }


  ngOnChanges(changes: SimpleChanges) {
    this.filedata = changes.fileList.currentValue;
  }

  ngOnInit(): void {
    this.filedata = this.fileList;

  }

  addRow(content) {
    this.modalService.open(content);
  }

  submit() {
    if (this.fileType.value && this.file.value) {
      const formData = new FormData();
      formData.append("file", this.file.value);
      formData.append("fileType", this.fileType.value);
      this.uploadFile.emit(formData);
      this.file.reset();
      this.fileType.reset();
      this.modalService.dismissAll();
    };
  }

  downnloadFile(fileDetails) {
    this.downloadFile.emit(fileDetails);
  }

  deleteFileHandler(fileDetails) {
    this.deleteFile.emit(fileDetails);
  }

  getFileName(fileName) {
    if(fileName.includes('^')){
      return fileName.split('^')[1];
    }else{
      return fileName;
    }
  }

}
