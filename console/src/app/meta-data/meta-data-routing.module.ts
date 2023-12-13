import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DeactivateReasonComponent } from './deactivate-reason/deactivate-reason.component';
import { LeadInventoryComponent } from './lead-inventory/lead-inventory.component';
import { LeadSourceComponent } from './lead-source/lead-source.component';
import { LeadStatusComponent } from './lead-status/lead-status.component';
import { LeadTypeComponent } from './lead-type/lead-type.component';
import { ThirdPartyIntegrationComponent } from './third-party-integration/third-party-integration.component';
import { UiTabsComponent } from './ui-tabs/ui-tabs.component';
import { UIPermissionsComponent } from './uipermissions/uipermissions.component';
import { VacationTypeComponent } from './vacation-type/vacation-type.component';
import { VacationComponent } from './vacation/vacation.component';

const routes: Routes = [
    {
        path: '',
        redirectTo: 'lead-source',
        pathMatch: 'full'
    },
    {
        path: 'lead-source',
        component: LeadSourceComponent
    },
    {
        path: 'ui-permissions',
        component: UIPermissionsComponent
    },
    {
        path: 'ui-tabs',
        component: UiTabsComponent
    },
    {
        path: 'lead-status',
        component: LeadStatusComponent
    },
    {
        path: 'lead-type',
        component: LeadTypeComponent
    },
    {
        path: 'lead-inventory',
        component: LeadInventoryComponent
    },
    {
        path: 'deactivate-reason',
        component: DeactivateReasonComponent
    },
    {
        path: 'thirdParty-integration',
        component: ThirdPartyIntegrationComponent
    },
    {
        path: 'vacation-type',
        component:VacationTypeComponent
    },
    {
        path: 'vacation',
        component:VacationComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class MetaDataRoutingModule { }
