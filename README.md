# Lennison-Auto-Detailing

Lennison Auto Detailing is a project involving the development of a web-based system for an automotive detailing business, designed to enhance its digital presence and streamline customer service. It will feature a display of available services, contact information, and a system for booking appointments quickly and easily.

[Versión en español](#español)

## Requirements
- Java JDK (version 17 or higher)
- Maven
- MySQL Server and MySQL Workbench

## Step #1: Initialize the database
Open the `schema.sql` file in MySQL Workbench (or any other tool capable of creating a database schema) and execute all the lines.

## Step #2: Start the program
Open the project in an IDE or code editor (such as NetBeans or Visual Studio Code) and locate the `Main.java` file, found at:

`LennisonAutoDetailing/src/main/java/com/lennison`

This is the file that needs to be executed.

Alternatively, from the terminal (inside the project folder):

```bash
mvn compile exec:java
```

## Step #3: Open the project in your browser
Once `Main.java` has been executed successfully, open your web browser and go to:

`http://localhost:8080/`

You will then be able to use the project.

---

## Español

### Requisitos
- Java JDK (versión 17 o superior)
- Maven
- MySQL Server y MySQL Workbench

### Paso #1: Iniciar la base de datos
Abre el archivo `schema.sql` en MySQL Workbench (o en otra herramienta que pueda crear el esquema de base de datos) y ejecuta todas las líneas.

### Paso #2: Iniciar el programa
Abre el proyecto en un editor de código o IDE (como NetBeans o Visual Studio Code) y busca el archivo `Main.java`, que se encuentra en:

`LennisonAutoDetailing/src/main/java/com/lennison`

Ese es el archivo que se debe ejecutar.

También puedes ejecutarlo desde la terminal (dentro de la carpeta del proyecto):

```bash
mvn compile exec:java
```

### Paso #3: Abrir el proyecto en el navegador
Después de ejecutar con éxito `Main.java`, abre tu navegador e ingresa:

`http://localhost:8080/`

Con esto podrás usar el proyecto.
