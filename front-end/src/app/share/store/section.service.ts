import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private sessionVariables: Map<string, any> = new Map<string, any>();
  public mapMenuUri: Map<string, any> = new Map<string, any>();

  public setSessionData(key: string, value: any): void {
    this.sessionVariables.set(key, value);
  }

  public getSessionData(key: string): any {
    if (key !== undefined && key !== null) {
      return this.sessionVariables.get(key);
    }

    return undefined;
  }

  public clearSessionData(key: string): void {
    if (key !== undefined && key !== null) {
      this.sessionVariables.delete(key);
    }
  }

  public clearSession(): void {
    this.sessionVariables.clear();
  }
}
