import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class MapaService {
  private directionsService?: google.maps.DirectionsService;

  getRoute(origem: google.maps.LatLngLiteral, destino: google.maps.LatLngLiteral, mode: google.maps.TravelMode = google.maps.TravelMode.DRIVING): Promise<google.maps.DirectionsResult | null> {
    // Cria a instance SOMENTE se google está definido!
    if (!this.directionsService && typeof google !== 'undefined' && google.maps) {
      this.directionsService = new google.maps.DirectionsService();
    }
    if (!this.directionsService) return Promise.resolve(null);

    return new Promise(resolve => {
      this.directionsService!.route(
        { origin: origem, destination: destino, travelMode: mode },
        (result, status) => {
          if (status === 'OK' && result) resolve(result);
          else resolve(null);
        });
    });
  }
}
