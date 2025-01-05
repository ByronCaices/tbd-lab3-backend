# Instrucciones de uso

En esta fase preeliminar del proyecto, está pensado para uso de manera local. Por lo que no está pensado aún para levantarse en un servidor IaaS o PaaS en la nube. Esto se verá en otras asignaturas como Técnicas de Ingeniería de Software.
Algunos integrantes usaron Docker para levantar el proyecto, debido que no era requisito se dejó en un gitignore para que no se incluyera en los archivos del proyecto.

## Despliegue

### Levantar el proyecto

Ejecutar el comando de docker compose para levantar el proyecto:

```Docker
docker compose up --build -d
```

Con esto se levantarán las bases de datos PostgreSQL, Mongodb y PgAdmin para administrar la db Postgres.

### Preparar las bases de datos

Crear la base de datos ejecutando los siguientes comandos

```bash
# Entramos al contenedor
docker exec -it tbd-lab3-backend-postgres-1 bash

# Nos movemos a la carpeta donde se encuentra el script
cd /docker-entrypoint-initdb.d

# Ejecutamos el script
psql -U postgres -f dbCreate.sql

# Ejecutamos script para poblar base de datos
psql -U postgres -d ecommercedb -f loadData.sql
```

Para la db mongo ya está automatizado este proceso y nos podemos conectar a ella mediante nuestro mongo compass o cualquier cliente de mongo.

```bash
mongodb://${MONGO_INITDB_ROOT_USERNAME}:${MONGO_INITDB_ROOT_PASSWORD}@${MONGO_HOST}:${MONGO_PORT}
```
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

