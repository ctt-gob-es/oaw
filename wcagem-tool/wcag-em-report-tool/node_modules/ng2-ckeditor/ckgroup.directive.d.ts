import { AfterContentInit, QueryList } from '@angular/core';
import { CKEditorComponent } from './ckeditor.component';
import { CKButtonDirective } from './ckbutton.directive';
/**
 * CKGroup component
 * Usage :
 *  <ckeditor [(ngModel)]="data" [config]="{...}" debounce="500">
 *      <ckgroup [name]="'exampleGroup2'" [previous]="'1'" [subgroupOf]="'exampleGroup1'">
 *          .
 *          .
 *      </ckgroup>
 *   </ckeditor>
 */
export declare class CKGroupDirective implements AfterContentInit {
    name: string;
    previous: any;
    subgroupOf: string;
    toolbarButtons: QueryList<CKButtonDirective>;
    ngAfterContentInit(): void;
    initialize(editor: CKEditorComponent): void;
}
