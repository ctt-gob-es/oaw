import { OnInit, EventEmitter } from '@angular/core';
import { CKEditorComponent } from './ckeditor.component';
/**
 * CKGroup component
 * Usage :
 *  <ckeditor [(ngModel)]="data" [config]="{...}" debounce="500">
 *      <ckbutton [name]="'SaveButton'" [command]="'saveCommand'" (click)="save($event)"
 *                [icon]="'/save.png'" [toolbar]="'customGroup,1'" [label]="'Save'">
 *      </ckbutton>
 *   </ckeditor>
 */
export declare class CKButtonDirective implements OnInit {
    click: EventEmitter<any>;
    label: string;
    command: string;
    toolbar: string;
    name: string;
    icon: string;
    initialize(editor: CKEditorComponent): void;
    ngOnInit(): void;
}
