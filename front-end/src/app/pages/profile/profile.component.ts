import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent extends BaseComponent implements OnInit {
  profileUser: any;
  editMode: boolean = false;

  constructor(injector: Injector,
              private userService: UserService,) {
    super(injector);
  }

  ngOnInit() {
    this.getUserById();

  }

  getUserById(){
    this.userService.getUserById(this.user.id).subscribe({
      next: (res) => {
        console.log(res)
        this.profileUser = res.data;
      }
    })
  }

  startEditProfile() {
    this.editMode = true;
  }
}
