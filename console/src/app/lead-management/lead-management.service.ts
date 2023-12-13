import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpService } from '../core/service/http.service';
import { UnsubscribeOnDestroyAdapter } from '../shared/UnsubscribeOnDestroyAdapter';

@Injectable()
export class LeadManagementService extends UnsubscribeOnDestroyAdapter {

  // Temporarily stores data from dialogs
  dialogData: any;
  viewLeadData: any;

  constructor(
    private httpService: HttpService,
    private http: HttpClient
  ) {
    super();
  }

  getDialogData() {
    return this.dialogData;
  }

  getAllLeads() {
    return this.httpService.get('lead');
  }

  addLead(data) {
    return this.httpService.post('lead', data);
  }

  addRawToLead(data,id) {
    return this.httpService.post(`lead/rawlead/${id}`, data);
  }


  updateLead(data, leadEvent) {
    let url = 'lead?leadUpdateEvent=' + leadEvent;
    return this.httpService.put(url, data);
    //`?saleUpdateEvent=${saleUpdateEvent}`
  }

  updateProject(data) {
   return this.httpService.put(`lead/changeproject/`, data);
  }

  

  getLeadById(id) {
    return this.httpService.get(`lead/${id}`);
  }

  saveComment(id, data) {
    return this.httpService.post(`lead/comment/${id}`, data);
  }


  transferLeads(data, leadEvent) {
    let url = 'lead/transferLeads?leadUpdateEvent=' + leadEvent;
    return this.httpService.post(url, data);
  }

  setViewLeadData(lead, followups) {
    this.viewLeadData = lead;
    this.viewLeadData.followups = followups;
  }

  getViewLeadData() {
    return this.viewLeadData;
  }

  createLeadDataForExport(leads) {
    var leadData = [];
    leads.forEach((lead, index) => {

      var data = {
        "S.no": index,
        "Customer Name": lead.customerName,
        "Customer EMAIL": lead.customerEmail,
        "PHONE": lead.customerPhone,
        "PROJECT NAME": lead.projectName,
        "LEAD SOURCE": lead.leadSource,
        "ADMIN REMARK": lead.adminRemark,
        "STATUS": lead.status,
        "ASSIGN TO": lead.assignTo,
        "FOLLOW UP DATE": lead.followupDate,
        "LEAD UPLOAD DATE": new Date(lead.leadUploadDate).toDateString(),
        "BUDGET": lead.budget,
      }
      leadData.push(data);

    });
    return leadData;
  }

  uploadLeadsWithExcel(data: FormData) {
    const header = new HttpHeaders();
    header.append('Content-Type', 'file');
    const url = environment.apiUrl + '/uploadLead';
    return this.http.post(url, data, { headers: header });
  }


  uploadRawLeadsWithExcel(data: FormData) {
    const header = new HttpHeaders();
    header.append('Content-Type', 'file');
    const url = environment.apiUrl + '/uploadRawLead';
    return this.http.post(url, data, { headers: header });
  }


  pageableLeads(pageParams) {
    const url = environment.apiUrl + "/lead/pageableLeads";
    return this.http.post(url, pageParams);
  }

  pageableRawLeads(pageParams) {
    const url = environment.apiUrl + "/lead/raw/pageableLeads";
    return this.http.post(url, pageParams);
  }

  getRawLeads() {
    const url = environment.apiUrl + "/lead/raw/pageableLeads";
    return this.http.get(url);
  }

  exportPageableLeads(pageParams) {
    const url = environment.apiUrl + "/lead/pageableLeads/export";
    return this.http.post(url, pageParams);
  }

  addFollowup(id, data) {
    return this.httpService.post(`followup/${id}`, data);
  }

  deactivateLead(id, data) {
    return this.httpService.post(`lead/deactivateLead/${id}`, data);
  }
  updateCustomerName(body: any) {
    return this.httpService.put(`customer`, body);
  }

  downloadOtp() {
    const url = environment.apiUrl + "/lead/downloadFileOtp";
    return this.http.get(url);
  }
  
  validateDownloadFileOtp(otpd) {
    const url = environment.apiUrl + "/lead/validateDownloadFileOtp/" + otpd;
    return this.http.get(url);
  }

}
