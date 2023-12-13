import { Component, OnInit } from '@angular/core';
import { HttpService } from 'src/app/core/service/http.service';
import { MessageService } from 'src/app/core/service/message.service';
import { StorageService } from 'src/app/core/service/storage.service';
import { MetaDataService } from '../meta-data.service';

@Component({
  selector: 'app-uipermissions',
  templateUrl: './uipermissions.component.html',
  styleUrls: ['./uipermissions.component.scss']
})
export class UIPermissionsComponent implements OnInit {

  metadata: any;
  allPermissionTabs: any;
  allPermissionTabsCopy: any;
  roles: any;
  loggedInUser: any;
  constructor(private storageService: StorageService,
    private metadataservice: MetaDataService,
    private messageService: MessageService,
    private http: HttpService) { }

  ngOnInit(): void {
    this.loggedInUser = this.storageService.getStorage().loggedInUser;
    this.roles = this.storageService.getStorage().roles;
    if (!this.isSuperAdmin()) {
      this.roles = this.roles.filter(ele => {
        if (ele.name === "SUPERADMIN" || ele.name === "ADMIN") {
        } else {
          return ele;
        }
      })
    }

    this.http.get('permissionTabs').subscribe(res => {
      //set permission
      this.metadataservice.setPermission(res);
      this.roles.forEach(currentRole => {
        let permission = this.metadataservice.getPermission();
        currentRole.alltabs = JSON.parse(permission);
        //("checking role", currentRole.name);
        currentRole.alltabs.forEach(permission => {

          const isExisits = currentRole.uiPermissions.filter(s => s.id == permission.id);
          if (isExisits && isExisits.length > 0) {
            permission['checked'] = true;
          } else {
            permission.checked = false;
          }

          if (permission.submenu.length > 0) {
            permission.submenu.forEach(submenuItem => {
              const isExisits = currentRole.uiPermissions.filter(s => s.id == submenuItem.id);
              if (isExisits && isExisits.length > 0) {
                submenuItem['checked'] = true;
              } else {
                permission.checked = false;
              }
            });
          }
        });
      });

    });
  }

  updateParent(checked, permissionTab, role) {
    let status = ''
    status = checked ? 'ADD' : 'REMOVE';

    this.http.post('permissionTabs/addPermission/' + role.name + '/' + status, permissionTab).subscribe(res => {
      if (status == 'ADD') {
        this.messageService.showSuccess("Added new permission to role " + role.name);
      } else if (status == 'REMOVE') {
        this.messageService.showSuccess("Removed permission from role " + role.name);
      }
    });

  }

  updateChild(checked, permissionTab, role) {
    
  }

  isSuperAdmin() {
    return this.loggedInUser.superAdmin;
  }
}
