import {User} from "./user.model";

export class AuthResponseData {
  public accessToken: string;
  public tokenType: string;
  public expires_in: number;
  public user: User;


  constructor(accessToken: string, tokenType: string, expires_in: number, user: User) {
    this.accessToken = accessToken;
    this.tokenType = tokenType;
    this.expires_in = expires_in;
    this.user = user;
  }
}
