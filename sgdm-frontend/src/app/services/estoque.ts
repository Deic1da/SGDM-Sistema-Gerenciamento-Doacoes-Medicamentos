import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EstoqueService {
  private API_URL = 'http://localhost:8080/api/estoque-entidade';

  constructor(private http: HttpClient) {}

  listarMedicamentos(entidadeId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/entidade/${entidadeId}`);
  }
}
