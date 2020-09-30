import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {PersonaService} from './service/persona.service';
import {FormControl, FormGroup, FormControlName} from '@angular/forms';
import {NotificationService} from './service/notification.server';
import {BehaviorSubject} from 'rxjs';
import { Persona } from 'src/modelo/persona';

import { NEXT } from '@angular/core/src/render3/interfaces/view';

@Component({
  selector: 'umg-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy, AfterViewInit {
  title = 'profesional';

  // definir "FormGroup" para ingreso de datos por formulario
  public formGroup: FormGroup;

  public mySubject: BehaviorSubject<any>;
  personas:Persona[];
  texto: string = null;

  constructor(private personaService: PersonaService,
              private notificationService: NotificationService) {
     this.mySubject = new BehaviorSubject(null);
  }

  public onClick(): void {
    console.log('on click');
  }

  public enviarFormulario(): void {
    console.log('Datos de formulario:' + JSON.stringify(this.formGroup.value));

    let parametros: any = null;
    parametros = Object.assign({}, this.formGroup.value);

    let datosAEnviar: any = {
      nombre: parametros.nombre,
      edad: parametros.edad
      
    };

    console.log('Datos a enviar 2:' + JSON.stringify(datosAEnviar));

    this.personaService.create(datosAEnviar).subscribe(result => {
      console.log('Datos from server:' + JSON.stringify(result));
    });
    this.personaService.add(datosAEnviar).subscribe(result => {
      console.log('Datos from server:' + JSON.stringify(result));
    });
    this.formGroup.patchValue({
      nombre:'',
      edad:''
      

      
    })  }


  public actualizarFormulario(persona:Persona): void {

    let parametros: any = null;
    parametros = Object.assign({}, this.formGroup.value);

    let datosAEnviar: any = {
      id:localStorage.getItem("id"),
      nombre: parametros.nombre,
      edad: parametros.edad
    };

    console.log('Datos a enviar 1:' + JSON.stringify(datosAEnviar));

    this.personaService.update(datosAEnviar).subscribe(result => {
      console.log('Datos from server:' + JSON.stringify(result));
    });

    this.formGroup.patchValue({
      nombre:'',
      edad:''
      

      
    })
    localStorage.setItem("id","");
  }


  private initForm(): void {
    this.formGroup = new FormGroup({
      nombre: new FormControl('', []
      ),
      edad: new FormControl('', []
      )
    });


  }

  /* ------------------------------------------------------------------------------------------------- */
  private handleMessageReceived(message: any): void {
    console.log('Mensaje recibido:' + JSON.stringify(message));
    this.mySubject.next(message);
  }

  /* ------------------------------------------------------------------------------------------------- */
  public doNotificationSubscription(): void {
    try {
      this.notificationService.getPersonaNotification().subscribe((result) => {
        this.handleMessageReceived(result);
      });
    } catch (e) {
      console.log(e);
    }
  }

  /* ------------------------------------------------------------------------------------------------- */

  ngAfterViewInit(): void {
    console.log('on after view');
  }

  ngOnDestroy(): void {
    console.log('on destroy');
  }

  public procesarSubject(result: any): void {
    if (result == null) { return; }

    this.personaService
      .personaList(null)
      .subscribe((result) => {
        this.personas=result;
        console.log('RESULTADO:' + JSON.stringify(result));
      });


    console.log('hacer algo..' + JSON.stringify(result));
    this.texto = this.texto + ' '  + JSON.stringify(result);
  }

 public botonazo(): void {
   this.mySubject.next('botonazo!!!!!');
 }

 Delete(persona:Persona){
  this.personaService.eliminaPersona(persona).subscribe(data=>{
    this.personas=this.personas.filter(p=>p!==persona);})
  
   this.personaService
  .personaList(null)
  .subscribe((result) => {
    this.personas=result;
    console.log('RESULTADO:' + JSON.stringify(result));})

}

  ngOnInit(): void {

    console.log('on init');

    this.mySubject.subscribe(result => {
      this.procesarSubject(result);
    });

    // realizar suscripcion
    this.doNotificationSubscription();

    // iniciar formulario
    this.initForm();

    // ejecutar llamada de servicio restful al iniciar la aplicacion
    this.personaService
      .personaList(null)
      .subscribe((result) => {
        this.personas=result;
        console.log('RESULTADO:' + JSON.stringify(result));
      });
  }

  Editar(persona:Persona){
    
    localStorage.setItem("id",persona.id.toString());
    

    this.formGroup.patchValue({
      nombre:persona.nombre,
      edad:persona.edad,
      
      

      
    })
  }


  


}
