## Analisis 

## Introduccion
La finalidad de este documento es mostrar para cada uno de los escenarios de prueba, el analisis de los resultados.

## Desarrollo

A continuacion se describen cada uno de los escenario de prueba:

Todos nuestros nodos virtuales tienen que tener esta configuracion:
- Limite de memoria = 300M
- Tamaño memoria-swap = 0
- Dedicacion de CPUs = personalizada 

Caso de stress 1A: 
- Necesitamos levantar 1 nodo virtual de nuestra app.
- 1 CPU
- 1 Nodo BD

Caso de stress 1B: 
- Necesitamos levantar 1 nodo virtual de nuestra app.
- 2 CPU
- 1 Nodo BD

Caso de stress 2A: 
- Necesitamos levantar 1 nodo virtual de nuestra app.
- 1 CPU
- 2 Nodo BD replica => Un Master y un Slave.

Caso de stress 1B: 
- Necesitamos levantar 1 nodo virtual de nuestra app.
- 1 CPU
- 3 Nodo BD replica => Un Master y 2 Slave


Caso de Tolerancia a Fallos y Alta Disponibilidad:
- Necesitamos levantar 1 nodo virtual de nuestra app.
- 1 CPU
- 3 Nodo BD replica => Un Master y 2 Slave
- Simulacion 3A: Eliminando un nodo Slave durante el tope de carga
- Simulacion 3B: Eliminando un nodo Master durante el tope de carga
- Simulacion 3C: Eliminando un nodo Master durante el tope de carga y luego reintegrarlo al Cluster.

## Problematicas

Cuando probamos la configuracion requerida para el nodo virtual, nos dimos cuenta que nuestra app con 300M de memoria no levanta, como minimo necesita 800M. 
Para resolver el inconveniente cambiamos la capacidad de la memoria a 1G.

## Conclusion
