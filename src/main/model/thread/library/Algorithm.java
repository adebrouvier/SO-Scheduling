package main.model.thread.library;

import main.model.thread.UserLevelThread;

import java.util.Queue;


public interface Algorithm {//Una idea de como podemos implementar la biblioteca. Trae problemas para modificar cosas de la instancia del klt
    //Podiramos devolver una especie de nodo con el bloqueado y el que esta corriendo? Seria medio cabeza pero funciona y es general
    //Deberia devolver el nuevo o viejo RunningUlt, o no deberia devolver nada?
    //Si se bloquea un ult, el execute del klt es el encargado de bloquear al klt no? Y eventualmente el execute del proceso hara lo mismo
    UserLevelThread execute(Queue<UserLevelThread> ults, UserLevelThread runningUlt, int core);
}
