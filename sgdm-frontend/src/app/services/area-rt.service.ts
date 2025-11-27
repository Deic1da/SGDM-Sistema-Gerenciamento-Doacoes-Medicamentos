import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Entidade } from '../models/entidade';

@Injectable({
  providedIn: 'root'
})
export class AreaRTService {
  private mostrarListaSubject = new BehaviorSubject<boolean>(false);
  private entidadesRTSubject = new BehaviorSubject<Entidade[]>([]);

  mostrarLista$ = this.mostrarListaSubject.asObservable();
  entidadesRT$ = this.entidadesRTSubject.asObservable();

  constructor() {
    console.log('🔧 AreaRTService foi criado!');
  }

  setMostrarLista(mostrar: boolean): void {
    this.mostrarListaSubject.next(mostrar);
  }

  setEntidadesRT(entidades: Entidade[]): void {
    this.entidadesRTSubject.next(entidades);
  }

  toggleLista(): void {
    this.mostrarListaSubject.next(!this.mostrarListaSubject.value);
  }

  fecharLista(): void {
    this.mostrarListaSubject.next(false);
  }

  getMostrarLista(): boolean {
    return this.mostrarListaSubject.value;
  }
}
