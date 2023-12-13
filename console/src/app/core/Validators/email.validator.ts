import { AbstractControl } from '@angular/forms';

/**
Email Validation
--------------------
Valid formats:
 */
export function ValidateEmail(control: AbstractControl) {
    const regExp = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/;
    if (control.value && !regExp.test(control.value)) {
        return { invalidEmail: true };
    }
    return null;
}