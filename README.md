# CRUDMIXTO

Proyecto de ejemplo para gestión de empleados y proyectos usando Spring Boot, MySQL y MongoDB Atlas.

## Requisitos previos

- Java 17 o superior
- Maven 3.8+
- MySQL 5.7+ (con base de datos `empresa` creada)
- Cuenta en MongoDB Atlas (con base de datos `empresa` y usuario con permisos)

## Instalación

1. **Clona el repositorio:**
   ```
   git clone <URL_DEL_REPOSITORIO>
   cd CRUDMIXTO
   ```

2. **Configura las credenciales de base de datos:**
   - Edita el archivo `src/main/resources/application.properties` y ajusta:
     - Usuario y contraseña de MySQL
     - URI de MongoDB Atlas (usuario, contraseña y base de datos)

3. **Crea la base de datos MySQL:**
   - Ingresa a tu consola MySQL y ejecuta:
     ```sql
     CREATE DATABASE empresa;
     ```

## Ejecución

Puedes ejecutar la aplicación de dos formas:

- **Con Maven Wrapper:**
  ```
  ./mvnw spring-boot:run
  ```
  o en Windows:
  ```
  mvnw spring-boot:run
  ```

- **Con el JAR generado:**
  ```
  java -jar target/CRUDMIXTO-0.0.1-SNAPSHOT.jar
  ```

## Pruebas rápidas

- Accede a la app en tu navegador:
  - Empleados: [http://localhost:8080/empleados](http://localhost:8080/empleados)
  - Proyectos: [http://localhost:8080/proyectos](http://localhost:8080/proyectos)

## Créditos

- DAMIAN Y SU COMBO
- Autor principal: DAMIAN
- Colaboradores: Equipo de desarrollo CRUDMIXTO

---
¡Listo para usar y modificar según tus necesidades!
