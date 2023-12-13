import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MetaDataService } from '../meta-data.service';

@Component({
  selector: 'app-third-party-integration',
  templateUrl: './third-party-integration.component.html',
  styleUrls: ['./third-party-integration.component.sass']
})
export class ThirdPartyIntegrationComponent implements OnInit {

  AuthTypes: any = [
    { value: 'SECRET_KEY', name: 'Secret key based' },
    { value: 'BASIC', name: 'Username password based' },
    { value: 'TOKEN_BASED', name: 'JWT token Based' },
    { value: 'PUBLIC', name: 'No authentication is required' }
  ];

  HttpMethods: any = [
    { name: 'Get', value: 'GET' },
    { name: 'Post', value: 'POST' },
    { name: 'Put', value: 'PUT' },
    { name: 'Delete', value: 'DELETE' },
  ];

  Integrations: any = [];
  action: string = 'add';
  editData: any = null;

  constructor(
    private modalService: NgbModal,
    private metaService: MetaDataService
  ) { }

  ngOnInit(): void {
    this.getAllIntegrations();
  }

  getAllIntegrations() {
    this.metaService.getAllIntegrations().subscribe(
      res => {
        console.log(res);
        this.Integrations = res;
      }
    )
  }

  addEditRow(content: any, type: string, row: any) {
    this.action = type;
    this.editData = row;
    const modalRef = this.modalService.open(content,
      {
        size: 'lg',
        ariaLabelledBy: '',
        backdrop: false
      })
  }

  submitForm(event) {
    this.metaService.createIntegration(event).subscribe(
      res => {
        console.log(res);
        this.getAllIntegrations();
      }
    )
  }

  getAuthType(type: string) {
    return this.AuthTypes.filter(e => e.value === type)[0]?.name;
  }

  getHttpMethod(method: string) {
    return this.HttpMethods.filter(e => e.value === method)[0]?.name;
  }

}
