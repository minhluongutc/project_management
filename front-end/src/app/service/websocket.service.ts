import { Injectable } from '@angular/core';
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {ChatMessage} from "../models/chat-message.model";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  private stompClient: any;
  private messageSubject: BehaviorSubject<any> = new BehaviorSubject<any>([]);

  constructor() {
    this.initConnectionSocket();
  }


  initConnectionSocket() {
    const url = 'http://localhost:8080/websocket';
    const socket = new SockJS(url);
    this.stompClient = Stomp.over(socket);
  }

  joinRoom(roomId: string) {
    this.stompClient.connect({}, ()=>{
      this.stompClient.subscribe(`/topic/${roomId}`, (messages: any) => {
        console.log(messages.body)
        const messageContent = JSON.parse(messages.body);
        this.messageSubject.next(messageContent)
        // const currentMessage = this.messageSubject.getValue();
        // currentMessage.push(messageContent);

        // this.messageSubject.next(currentMessage);

      })
    })
  }

  sendMessage(roomId: string, chatMessage: ChatMessage) {
    console.log(chatMessage);
    this.stompClient.send(`/app/chat/${roomId}`, {}, JSON.stringify(chatMessage))
    // this.stompClient.send(`/app/notification/${roomId}`, {}, JSON.stringify(chatMessage))
  }

  onNotify(roomId: string, dto: any) {
    console.log(dto);
    this.stompClient.send(`/app/notification/${roomId}`, {}, JSON.stringify(dto))
  }

  getMessageSubject() {
    return this.messageSubject.asObservable();
  }
}
