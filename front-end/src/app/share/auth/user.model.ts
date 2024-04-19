export class User {
  constructor(
    public id: string,
    public username: string,
    public firstName: string,
    public lastName: string,
    public email: string,
    public roles: string[],
    public companyId: string,
    public avatarId: string,
    public _token: string,
    public _tokenExpirationData: Date,
  ) {
  }

  get token(): string{
    if (!this._tokenExpirationData || new Date() > this._tokenExpirationData) {
      // @ts-ignore
      return null;
    }
    return this._token;
  }
}
