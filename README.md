# KinalApp - Sistema de Gestión Comercial

**KinalApp** es una aplicación web desarrollada con **Spring Boot** y **Thymeleaf** que permite administrar clientes, productos, ventas y usuarios de un negocio. Incluye autenticación manual basada en sesión HTTP, encriptación de contraseñas con BCrypt y una interfaz amigable.

## 📋 Tabla de Contenidos

- [Introducción](#introducción)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Requisitos Previos](#requisitos-previos)
- [Configuración del Proyecto](#configuración-del-proyecto)
  - [Base de Datos](#base-de-datos)
  - [Ejecución de la Aplicación](#ejecución-de-la-aplicación)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Funcionalidades Principales](#funcionalidades-principales)
  - [Autenticación y Registro](#autenticación-y-registro)
  - [Dashboard](#dashboard)
  - [Módulo de Clientes](#módulo-de-clientes)
  - [Módulo de Productos](#módulo-de-productos)
  - [Módulo de Ventas](#módulo-de-ventas)
  - [Módulo de Usuarios](#módulo-de-usuarios)
- [Guía de Uso Rápido](#guía-de-uso-rápido)
- [Solución de Problemas Comunes](#solución-de-problemas-comunes)
- [Contribución](#contribución)

---

## Introducción

KinalApp es una solución integral para la gestión de inventario y ventas. Permite:

- Registrar y administrar **clientes** (con estado activo/inactivo).
- Gestionar el catálogo de **productos** (control de stock, precios y estado).
- Realizar **ventas**, asociando cliente y usuario responsable.
- Administrar **usuarios** del sistema (roles: `ADMIN`, `USER`).
- Proteger las rutas mediante **autenticación por sesión** (login manual).
- Encriptar contraseñas con **BCrypt** para mayor seguridad.

La aplicación cuenta con una interfaz web desarrollada con **Thymeleaf** y estilos CSS personalizados.

---

## Tecnologías Utilizadas

| Tecnología          | Versión      | Descripción                                      |
|---------------------|--------------|--------------------------------------------------|
| Java                | 17+          | Lenguaje de programación                         |
| Spring Boot         | 3.2.x        | Framework principal                              |
| Spring MVC          | -            | Controladores y vistas web                       |
| Thymeleaf           | -            | Motor de plantillas para el frontend             |
| Spring Data JPA     | -            | Persistencia y acceso a datos                    |
| MySQL               | 8.x          | Base de datos relacional                         |
| Spring Crypto       | -            | Encriptación de contraseñas (BCrypt)             |
| Maven               | 3.9+         | Gestión de dependencias y build                  |
| HTML/CSS/JavaScript | -            | Interfaz de usuario                              |

---

## Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **JDK 17** o superior
- **Maven**
- **MySQL Server** (o cualquier motor compatible con JPA)
- **Git** (para clonar el repositorio)

---

## Configuración del Proyecto

### Base de Datos

1. Crea una base de datos en MySQL. Por ejemplo: `dbClientes_IN5AM`.
2. Configura las credenciales en el archivo `src/main/resources/application.properties`:

```properties
spring.application.name=KinalApp
server.port=8088

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Conexión a MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/dbClientes_IN5AM?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# Excluir autoconfiguración de Spring Security (no la usamos)
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
```

> **Nota:** `ddl-auto=update` crea/actualiza las tablas automáticamente según las entidades JPA.

### Ejecución de la Aplicación

```bash
# Clonar repositorio
git clone https://github.com/tu-usuario/kinalapp.git
cd kinalapp

# Compilar y ejecutar
mvn clean spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8088**

---

## Estructura del Proyecto

```
kinalapp/
├── src/main/java/com/brayanbautista/kinalapp/
│   ├── config/                  # Configuraciones (PasswordEncoder, Interceptor)
│   ├── controller/              # Controladores MVC (Cliente, Producto, Venta, Usuario, Login)
│   ├── entity/                  # Entidades JPA
│   ├── interceptor/             # Interceptor de autenticación (AuthInterceptor)
│   ├── repository/              # Repositorios Spring Data JPA
│   └── service/                 # Servicios e interfaces de negocio
├── src/main/resources/
│   ├── static/
│   │   ├── css/styles.css       # Estilos globales (incluye login/registro)
│   │   └── js/app.js            # JavaScript para modales y búsqueda
│   ├── templates/               # Vistas Thymeleaf
│   │   ├── fragments/layout.html
│   │   ├── index.html           # Dashboard
│   │   ├── login.html
│   │   ├── registro.html
│   │   ├── clientes/            # lista.html y formulario.html
│   │   ├── productos/           # lista.html y formulario.html
│   │   ├── ventas/              # lista.html y formulario.html
│   │   └── usuarios/            # lista.html y formulario.html
│   └── application.properties
└── pom.xml
```

---

## Funcionalidades Principales

### Autenticación y Registro

- **Login manual** con sesión HTTP.
- Las contraseñas se **encriptan con BCrypt** antes de almacenarse en la base de datos.
- **Registro de nuevos usuarios** desde la pantalla de login (rol por defecto: `USER`, estado activo).
- **Interceptor `AuthInterceptor`** protege todas las rutas excepto `/login`, `/registro`, `/logout` y recursos estáticos.

**Flujo:**
1. Usuario accede a `/login` e ingresa credenciales.
2. Si son válidas y el usuario está activo, se guarda en sesión y redirige al Dashboard (`/`).
3. Para cerrar sesión se accede a `/logout`.

### Dashboard

La página de inicio (`/`) muestra:

- Tarjetas con totales de clientes, productos, ventas y usuarios.
- Accesos rápidos para crear nuevos registros.
- Listado de las **últimas 5 ventas registradas**.

### Módulo de Clientes

- **Listado** con búsqueda en tiempo real.
- **Creación** y **edición** (formulario con validaciones: DPI numérico de 13 dígitos, nombre, apellido, dirección y estado).
- **Eliminación** con confirmación mediante modal.

### Módulo de Productos

- **Listado** con búsqueda.
- **Formulario** para crear/editar (nombre, precio, stock, estado).
- **Eliminación** con modal de confirmación.

### Módulo de Ventas

- **Listado** con fecha formateada, cliente, usuario, total y estado.
- **Formulario** de creación/edición:
    - Selección de cliente (desplegable con DPI y nombre).
    - Selección de usuario responsable.
    - Total y estado.
    - La fecha se asigna automáticamente al guardar (timestamp).

### Módulo de Usuarios

- **CRUD completo** (accesible desde el menú lateral).
- Al crear/editar un usuario, la contraseña se **encripta automáticamente**.
- Roles disponibles: `ADMIN`, `USER` (pueden ampliarse en el código).

---

## Guía de Uso Rápido

1. **Primer acceso:**
    - Ejecuta la aplicación.
    - Si no hay usuarios registrados, accede a `/registro` desde la pantalla de login y crea una cuenta.
    - Inicia sesión con tus credenciales.

2. **Navegación:**
    - Usa el menú lateral para moverte entre módulos.
    - Cada sección tiene botones para **Nuevo**, **Editar** y **Eliminar**.

3. **Búsqueda en tablas:**
    - Utiliza el campo de búsqueda en la parte superior de cada lista para filtrar registros.

4. **Cerrar sesión:**
    - Actualmente el botón de logout no está en la interfaz. Puedes acceder directamente a `/logout` en el navegador o agregarlo manualmente en el layout.

---

## Solución de Problemas Comunes

| Problema                              | Posible causa                                      | Solución                                                                 |
|---------------------------------------|----------------------------------------------------|---------------------------------------------------------------------------|
| Error de conexión a MySQL             | Credenciales incorrectas o MySQL no iniciado       | Verifica `application.properties` y que el servicio MySQL esté corriendo.  |
| No se puede iniciar sesión            | Usuario inactivo o contraseña incorrecta           | Asegúrate de que el `estado` sea `1` y la contraseña sea correcta.         |
| Pantalla en blanco o error 500        | Error en la vista Thymeleaf o falta de datos       | Revisa la consola del servidor. Asegúrate de que los objetos en el modelo existan. |
| No se encripta la contraseña al guardar | Falta el bean `PasswordEncoder`                   | Verifica que `PasswordEncoderConfig` esté presente y que `UsuarioService` lo inyecte. |
| El interceptor bloquea recursos estáticos | Rutas de exclusión incorrectas en `WebConfig`   | Asegúrate de tener `/css/**`, `/js/**`, etc. en `excludePathPatterns`.     |
| Al eliminar un producto con ventas asociadas | Restricción de integridad referencial         | Elimina primero los detalles de venta relacionados o ajusta la lógica.     |

---

## Contribución

¿Quieres mejorar KinalApp? ¡Excelente! Sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una rama para tu feature:  
   `git checkout -b feature/nueva-funcionalidad`
3. Realiza tus cambios y haz commit siguiendo buenas prácticas:  
   `git commit -m "feat: agregar reporte de ventas por fecha"`
4. Sube la rama:  
   `git push origin feature/nueva-funcionalidad`
5. Abre un **Pull Request** en GitHub describiendo los cambios.

**Consejos de documentación:**
- Mantén actualizado este `README.md` si agregas nuevas dependencias o cambias la configuración.
- Si encuentras un error común, agrégalo a la sección de solución de problemas.
- Incluye capturas de pantalla de la interfaz en la carpeta `assets` y enlázalas aquí.
