import {User} from "./user.model";

export class AuthResponseData {
  public access_token: string;
  public tokenType: string;
  public expires_in: number;
  public user: User;


  constructor(access_token: string, tokenType: string, expires_in: number, user: User) {
    this.access_token = access_token;
    this.tokenType = tokenType;
    this.expires_in = expires_in;
    this.user = user;
  }
}
