import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ConstantConfig } from 'src/app/config/constant.config';
import { StorageService } from 'src/app/core/service/storage.service';
import { ValidateEmail, ValidatePhoneNumber } from 'src/app/core/Validators';
import { BookingService } from '../booking.service';

interface GroupedInventories {
  name: string;
  inventories: Array<any>;
}

@Component({
  selector: 'app-add-edit-booking',
  templateUrl: './add-edit-booking.component.html',
  styleUrls: ['./add-edit-booking.component.sass']
})
export class AddEditBookingComponent implements OnInit {
  bookingPrice = 'Zero';
  action: string;
  bookingForm: UntypedFormGroup;
  projects: any;
  groupedInventories: any;
  inventories: any;
  users;
  businessExecutives: any;
  businessHeads: any;
  businessManagers: any;
  startDate = new Date(1990, 0, 1);
  today = new Date();
  phoneMask = ConstantConfig.PHONE_MASK;
  state: any;
  leadId: any;
  leadData: any;
  customerPhone:any;

  constructor(
    private route: ActivatedRoute,
    private fb: UntypedFormBuilder,
    private router: Router,
    private storageService: StorageService,
    private bookingService: BookingService,
  ) {
    this.route.queryParams.subscribe(params => {
      this.action = params["action"];
      this.leadId = params["lead"];
      this.customerPhone = params["customerPhone"];
    });
  }

  ngOnInit(): void {
    this.setMetaData();
  }

  createForm(b) {
    this.bookingForm = this.fb.group({
      id: [b == null ? null : b.id],
      projectId: [b == null ? null : b.projectId, Validators.required],
      leadId: [b == null ? this.leadId : b.leadId, Validators.required],
      // bookingDate: [b == null ? this.today : b.bookingDate, Validators.required],
      buyer: this.fb.group({
        id: [b == null ? null : b.buyer.id],
        name: [b == null ? null : b.buyer.name, Validators.required],
        email: [b == null ? null : b.buyer.email, [Validators.required, ValidateEmail]],
        phone: [b == null ? this.customerPhone : b.buyer.phone, [Validators.required, ValidatePhoneNumber]],
        alternatePhone: [b == null ? null : b.buyer.alternatePhone, [ValidatePhoneNumber]],
      }),
      coBuyerName: [b == null ? null : b.coBuyerName],
      towers: [b == null ? null : b.towers, Validators.required],
      inventoryId: [b == null ? null : b.inventoryId, Validators.required],
      sellingPrice: [b == null ? null : b.sellingPrice, Validators.required],
      businessExecutiveId: [b == null ? null : b.businessExecutiveId, Validators.required],
      businessManagerId: [b == null ? null : b.businessManagerId, Validators.required],
      businessHeadId: [b == null ? null : b.businessHeadId, Validators.required],
      channelPartner: [b == null ? null : b.channelPartner],
      remark: [b == null ? null : b.channelPartner],
      applicationForm: [b == null ? null : b.applicationForm, Validators.required],
      buyerPan: [b == null ? null : b.buyerPan, Validators.required],
      coBuyerPan: [b == null ? null : b.coBuyerPan],
      buyerAadhar: [b == null ? null : b.buyerAadhar, Validators.required],
      coBuyerAadhar: [b == null ? null : b.coBuyerAadhar],
      paymentCopy: [b == null ? null : b.paymentCopy, Validators.required]
    })
  }

  setMetaData() {
    const metaData = this.storageService.getStorage();
    if (metaData == undefined) {
      setTimeout(() => {
        this.setMetaData();
      }, 50);
    } else {
      this.projects = metaData.projects;
      this.users = metaData.users;
      this.businessExecutives = metaData.users.filter(u => u.roles[0].name === 'BUSINESS_EXECUTIVE');
      this.businessManagers = metaData.users.filter(u => u.roles[0].name === 'BUSINESS_MANAGER');
      this.businessHeads = metaData.users.filter(u => u.roles[0].name === 'BUSINESS_HEAD');
      this.createForm(null);
    }
  }
  toWords(event) {
    this.bookingPrice = event.currentTarget.value;
  }

  checkForUser() {
    const phone = this.bookingForm.controls["buyer"].value.phone;
    if (phone.length === 10) {
      this.bookingService.findCustomer(phone).subscribe(
        res => {
          if (res != null) {
            this.setCustomerValueInForm(res)
          }
        }
      )
    }
  }

  findProjectDetails(event) {
    this.bookingService.findProject(event.value).subscribe(
      res => {
        let inventories = res.inventories;
        let towers = new Set<string>();
        inventories.forEach(ele => {
          towers.add(ele.tower);
        });
        this.setGroupedInventories(inventories, towers);
      }
    )
  }

  setGroupedInventories(rawInventories, towers) {
    let groupedInventories = new Array<GroupedInventories>();
    towers.forEach(ele => {
      const inventories = rawInventories.filter(e => e.tower === ele && e.inventoryStatus.name!='BOOKED');
      const obj = {
        name: ele,
        inventories: inventories
      }
      groupedInventories.push(obj);
    });
    groupedInventories = groupedInventories.sort((a, b) => a.name.localeCompare(b.name));
    console.log(groupedInventories);
    this.groupedInventories = groupedInventories;

  }

  setInventory(event) {
    console.log(event);
    this.inventories = event.value.inventories;
  }

  setCustomerValueInForm(buyer) {
    this.bookingForm.patchValue({
      buyer: {
        id: buyer.id,
        name: buyer.name,
        email: buyer.email,
        alternatePhone: buyer.alternatePhone,
        phone: buyer.phone
      }
    })
  }

  submitBooking() {
    if (this.bookingForm.valid) {
      console.log(this.bookingForm.value);
      const formValues = this.bookingForm.value;
      const formObj = new FormData();

      // application form 
      formObj.append("applicationForm", formValues.applicationForm);
      delete formValues.applicationForm;

      // buyer Pan
      formObj.append("buyerPan", formValues.buyerPan);
      delete formValues.buyerPan;

      // buyer Aadhar
      formObj.append("buyerAadhar", formValues.buyerAadhar);
      delete formValues.buyerAadhar;

      // payment Copy
      formObj.append("paymentCopy", formValues.paymentCopy);
      delete formValues.paymentCopy;

      // co-buyer Pan
      if (formValues.coBuyerPan != null) {
        formObj.append("coBuyerPan", formValues.coBuyerPan);
      }
      delete formValues.coBuyerPan;

      // co-Aadhar Pan
      if (formValues.coBuyerAadhar != null) {
        formObj.append("coBuyerAadhar", formValues.coBuyerAadhar);
      }
      delete formValues.coBuyerAadhar;
      delete formValues.towers;
      formObj.append("bookingDetails", JSON.stringify(formValues));

      this.bookingService.createBooking(formObj).subscribe(
        res => {
          console.log(res);
          this.back();
        }
      )


    }
  }

  back() {
    this.router.navigateByUrl("/booking-management");
  }
}
