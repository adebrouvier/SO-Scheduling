# SO-Scheduling
Trabajo práctico de planificación de procesos.

## Formato de entrada
-f ./res/chk4 -c 1 -ps fifo -tl fifo

* -f : file
* -c : core count
* -ps : process scheduling (fifo, rr_n: n=quantum)
* -tl : thread library(fifo, rr_m: m=quantum, srt, spn, hrrn)