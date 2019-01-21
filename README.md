# DomXmppBot
Aplicación para Android mínimo versión 5 por el momento.

Una vez instalada en el celular, se necesita una cuenta de XMPP para el Bot y otra para vos!. Podrías registrarte en 404.city, en la parte de usuario cargas los datos, el usuario y la password.

En la cuenta del bot tenes que agregar de contacto tu cuenta y todas las cuentas a las que queres recibir las notificaciones. Esto lo podes hacer con un cliente web por ejemplo (no implementado aun en la app), por ejemplo http://inverse.chat

En tu celular podes instalarte cualquier cliente, por afinidad recomiendo pix-art-messager https://github.com/kriztan/Pix-Art-Messenger.git podes encontrar un version compilada en f-droid.org

La aplicación se ejecuta en un servicio en background todo el tiempo. Se deben conectar los nodos inalambricos mediante ESP8266, el código esta en https://github.com/sebest06/Esp8266DomBot.git y también hay un binario para simplificar la tarea.

Los nodos se conectan al celular, que actua de servidor, (todos en la misma red). Cada vez que uno de los pines del esp se acciona se envía un msj por xmpp a todos los contactos. Esta funcion se puede activar o desactivar mediante los comandos

/alarmaActivar 1234

/alarmaDesactivar 1234

/alarmaEstado 1234

y se puede tomar una foto mediante el comando

/foto 1234

# Probar sin ESP8266
Desde una consola de linux podemos usar el comando nc para probar como si fueramos un nodo

se ejecuta

nc 192.168.x.xxx 30666

La ip es la del celular, la aplicación la dice apenas inicia

Después nos loggeamos con el comando

/hola XXXX

Donde XXXX es el número de serie reemplazar por cualquier número, se debe usar el mismo si se quiere recuperar los datos almacenados en la base de datos.

después con el comando 

/sensor1

Se envía la activación del sensor.

