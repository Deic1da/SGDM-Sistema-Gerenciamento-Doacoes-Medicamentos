import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, timer } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Entidade, CadastroEntidadeDTO } from '../models/entidade';

@Injectable({ providedIn: 'root' })
export class EntidadeService {
  private API_URL = 'http://localhost:8080/api/entidades';

  // Subject que guarda a lista atual
  private entidadesSubject = new BehaviorSubject<Entidade[]>([]);
  // Observable que componentes vão usar
  entidades$ = this.entidadesSubject.asObservable();

  constructor(private http: HttpClient) {
    // Polling automático (ajuste o intervalo como quiser)
    timer(0, 60000).pipe(  // 60000ms = 60s
      switchMap(() => this.http.get<Entidade[]>(this.API_URL))
    ).subscribe(data => this.entidadesSubject.next(data));
  }

  // Atualização manual (caso precise em algum evento):
  atualizar() {
    this.http.get<Entidade[]>(this.API_URL)
      .subscribe(data => this.entidadesSubject.next(data));
  }

  // Se quiser manter o método antigo (apenas uma vez):
  listar(): Observable<Entidade[]> {
    return this.http.get<Entidade[]>(this.API_URL);
  }

  cadastrar(dto: CadastroEntidadeDTO): Observable<Entidade> {
    return this.http.post<Entidade>(this.API_URL, dto);
  }

  buscarPorId(id: number): Observable<Entidade> {
    return this.http.get<Entidade>(`${this.API_URL}/${id}`);
  }
  verificarSeUsuarioTemEntidade(usuarioId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.API_URL}/usuario-tem-entidade/${usuarioId}`);
  }
  verificarSeUsuarioEhRT(usuarioId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.API_URL}/usuario-eh-rt/${usuarioId}`);
  }

  listarEntidadesDoRT(usuarioId: number): Observable<Entidade[]> {
    return this.http.get<Entidade[]>(`${this.API_URL}/entidades-rt/${usuarioId}`);
  }


}
