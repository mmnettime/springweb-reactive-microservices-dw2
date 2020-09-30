import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/index';
import {Persona} from 'src/modelo/persona';

@Injectable()
export class PersonaService {

  /* -------------------------------------------------------------------------------------------------------------- */
  private serviceURL = 'http://localhost:8082/rest/persona';

  private httpHeaders = new HttpHeaders({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  });

  /* -------------------------------------------------------------------------------------------------------------- */

  constructor(private httpClient: HttpClient) {
  }

  /* -------------------------------------------------------------------------------------------------------------- */

  public personaList(params: any): Observable<any> {
    const url: string = this.serviceURL + '/all';

    return this.httpClient
      .get(url, {headers: this.httpHeaders, params: params});
  }

  /* -------------------------------------------------------------------------------------------------------------- */

  /**
   * Servicio de envio de POST
   * @param model
   */
  public create(model: any): Observable<any> {
    const url: string = this.serviceURL + '/create';
    const headers: any = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.httpClient
      .post(url, JSON.stringify(model), {headers: headers});
      
  }
  public add(model: any): Observable<any> {
    const url: string = this.serviceURL + '/employee';
    const headers: any = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.httpClient
      .post(url, JSON.stringify(model), {headers: headers});
      
  }
  /**
   *
   * Servicio de envio de PUT
// tslint:disable-next-line: no-redundant-jsdoc
   * @param model
   */
  public update(model: any): Observable<any> {
    const url: string = this.serviceURL + '/actualiza/'+model.id;
    const headers: any = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.httpClient
      .put(url, JSON.stringify(model), {headers: headers});
  }

  /* -------------------------------------------------------------------------------------------------------------- */

  /*deletePersona(persona:Persona){
    return this.httpClient.delete<Persona>(this.serviceURL+"/delete/"+persona.id);
  }
*/
deletePersona(persona:Persona){
  return this.httpClient.delete<Persona>(this.serviceURL+"/borrar/");
  
}
eliminaPersona(persona:Persona){
  return this.httpClient.delete<Persona>(this.serviceURL+"/delete/"+persona.id);
}

actualizar(persona:Persona){
  

}


}
