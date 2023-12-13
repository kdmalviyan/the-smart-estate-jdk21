import { AbstractControl } from '@angular/forms';

/**
Phone Number Validation
--------------------
Valid formats:
9889987899,8909887877,7998987654,6387998098
 */
export function ValidatePhoneNumber(control: AbstractControl) {
    const regExp = /^[6-9]{1}\d{9}$/;
    if (control.value && !regExp.test(control.value)) {
        return { invalidPhoneNumber: true };
    }
    return null;
}