import { Component } from '@angular/core';
import { Sidebar } from '../sidebar/sidebar';

@Component({
  selector: 'app-layout',
  imports: [Sidebar],
  templateUrl: './layout.html',
  styleUrl: './layout.css'
})
export class Layout {

}
