# Instrucciones de uso

En esta fase preeliminar del proyecto, está pensado para uso de manera local. Por lo que no está pensado aún para levantarse en un servidor IaaS o PaaS en la nube. Esto se verá en otras asignaturas como Técnicas de Ingeniería de Software.
Algunos integrantes usaron Docker para levantar el proyecto, debido que no era requisito se dejó en un gitignore para que no se incluyera en los archivos del proyecto.

## Backend

### Requisitos: PostgreSQL 12.17

Primero se crean las tablas, para este cometido ingresamos el siguiente comando en la terminal:

```bash
psql -U postgres -f dbCreate.sql
```

Luego de esto, configurar las variables de entorno de application.properties para que coincidan con las credenciales de la base de datos (Esto dependerá del IDE que se use).

### Requisitos: JDK 17.0.9

Levantar backend algún IDE de Java. O en su defecto ejecutar el backend sin antes ingresar api/src/main/java/cl/soge/api y ejecutar el siguiente comando:

```bash
mvnw spring-boot:run
```

### Equipo de Desarrollo

| [Sebastian Cassone](https://github.com/sebacassone/)                    | [Byron Caices](https://github.com/ByronCaices)                          | [Benjamin Bustamante](https://github.com/benbuselola)                   | [Bastián Brito](https://github.com/PerroWachooo)                         |
| ----------------------------------------------------------------------- | ----------------------------------------------------------------------- | ----------------------------------------------------------------------- | ------------------------------------------------------------------------ |
| <img src="https://github.com/sebacassone.png" width="100" height="100"> | <img src="https://github.com/ByronCaices.png" width="100" height="100"> | <img src="https://github.com/benbuselola.png" width="100" height="100"> | <img src="https://github.com/PerroWachooo.png" width="100" height="100"> |

| [Andrea Cosio](https://github.com/PerroWachooo)                          | [Isidora Oyanedel](https://github.com/IsisIOo)                      | [Tomás Riffo](https://github.com/Ovejazo)                           |
| ------------------------------------------------------------------------ | ------------------------------------------------------------------- | ------------------------------------------------------------------- |
| <img src="https://github.com/PerroWachooo.png" width="100" height="100"> | <img src="https://github.com/Ovejazo.png" width="100" height="100"> | <img src="https://github.com/Ovejazo.png" width="100" height="100"> |
