import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({
    selector: '[OnlyString]'
})
export class OnlyString {
    constructor(private el: ElementRef) { }

    @Input() OnlyString: boolean;

    @HostListener('keydown', ['$event']) onKeyDown(event) {
        const evt = <KeyboardEvent>event;
        const alphaExp = /^[a-zA-Z]+$/;
        if (evt.key.match(alphaExp)) {
            return true;
        } else {
            return false;
        }
    }
}
