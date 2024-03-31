import {Injectable} from "@angular/core";
import {BaseService} from "./base.service";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../environment/environment";
import { saveAs } from "file-saver";
import {MessageService} from "primeng/api";

@Injectable({
  providedIn: 'root',
})
export class FileService extends BaseService {
  constructor(http: HttpClient, private toast: MessageService) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/files/content`;
  }

  downloadFile(fileName: string, filePath: string) {
    return this.getRequest(this.SERVICE_URL, {
      params: this.buildParams({ filePath: filePath }),
      observe: 'response',
      responseType: 'blob',
    }).subscribe({
      next: (res) => {
        saveAs(res.body, fileName);
      },
      error: (err) => {
        this.toast.add({severity: 'error', summary: "Lỗi", detail: "Tải file thất bại"});
      }
    });
  }

  base64toBlob = (
    base64Data: string,
    contentType: string = '',
    sliceSize: number = 512
  ) => {
    const byteCharacters = atob(base64Data);
    const byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
      const slice = byteCharacters.slice(offset, offset + sliceSize);

      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }

      const byteArray = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }

    const blob = new Blob(byteArrays, { type: contentType });
    return blob;
  };

  extractFileNameFromContentDisposition(contentDisposition: string): string {
    const fileNamePart = contentDisposition
      .split(';')
      .map((part) => part.trim())
      .find((part) => part.startsWith('filename='));

    if (fileNamePart) {
      return fileNamePart.split('=')[1].trim().replace(/"/g, '');
    }

    return '';
  }

  blob2Json(blob: Blob): Promise<Record<string, any>> {
    return blob.text().then((value: string) => JSON.parse(value));
  }
}
