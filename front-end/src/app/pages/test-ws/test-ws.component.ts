import {Component, Injector, OnInit} from '@angular/core';
import {WebsocketService} from "../../service/websocket.service";
import {ChatMessage} from "../../models/chat-message.model";
import {ActivatedRoute} from "@angular/router";
import {BaseComponent} from "../../share/ui/base-component/base.component";

@Component({
  selector: 'app-test-ws',
  templateUrl: './test-ws.component.html',
  styleUrl: './test-ws.component.scss'
})
export class TestWSComponent extends BaseComponent implements OnInit {

  messageInput: string = '';
  userId: string = "";
  messageList: any[] = []

  constructor(injector: Injector,
              private websocketService: WebsocketService) {
    super(injector);
  }

  ngOnInit(): void {
    this.userId = this.route.snapshot.params['userId'];
    this.websocketService.joinRoom("ABC");
    this.lisenerMessage();
  }

  sendMessage() {
    const chatMessage = {
      message: this.messageInput,
      user: this.userId
    } as ChatMessage;
    this.websocketService.sendMessage("ABC", chatMessage);
    this.messageInput = '';
  }

  sendNoti() {
    const dto = {
      toUserId: "fbd6bc05-f1ec-4314-969e-2bb31d9b74ae",
      content: this.messageInput,
      actionType: 1
    }
    this.websocketService.onNotify("ABC", dto);
    this.messageInput = '';
  }

  lisenerMessage() {
    this.websocketService.getMessageSubject().subscribe((messages: any) => {
      // this.messageList = messages.map((item: any)=> ({
      //   ...item,
      //   message_side: item.user == this.userId ? 'sender': 'receiver'
      // }))
      if(messages.userId == this.userId) {
        this.createSuccessToast("notification", messages?.optionalContent)
      }
      console.log(messages)
    });
  }
}
