# argus-scanner
Argus helps domestic violence advocates check a survivor's phone for stalkerware.
A scanner app on the phone lists installed apps and sends the results to a server,
which scores each app for risk. Advocates review the results in a web dashboard.

The system has three parts: an Android scanner app, a Java Spring Boot server,
and a web dashboard, backed by a MySQL database.

## Folder Structure

### `server/` – Backend server (Java, Spring Boot)


### `scanner-android/` – Android scanner app (Kotlin)


### `dashboard/` – Web dashboard (HTML, CSS, JavaScript)

### `db/` – Database scripts (MySQL)

### `docs/` – Test Scans (JSON Files)


How to Run

## Installation:
1. Install MySQL Configurator/Server at https://dev.mysql.com/downloads/mysql
2. Install MySQL Workbench at https://dev.mysql.com/downloads/workbench or any MySQL client
3. Install Java JDK at https://www.oracle.com/java/technologies/downloads/#jdk27-windows
4. Install Android Studio


## MySQL Configurator Section:
1. Run the MySQL Configurator program.
2. Select TCP/IP and use port 8889.
3. Make the root password "root".
4. Continue until you reach Apply Configuration, click execute. 
5. MySQL Configurator should be complete. You can close the program.  
6. Press WIN + R then open services.msc. 
7. If you see MySQL running in Services (Local), it works. 

## MySQL Database Section:
1. Open the MySQL Database Program. 
2. If MYSQL is running, you should see a + to the right that says "New Connection".
3. Name caption "Argus", use connection method "mysql", set user name to "root", then set port to 8889. 
4. Leave everything else as default. Click Ok. 
5. If you are prompted to enter a password, it will be the password for your user name.  
6. When you get into the connection, it should say connected in the middle right corner. 
7. ArgusScanner should be in the schemas section. 
8. Go to File --> Open --> argus-scanner-main --> db.
9. Open both the db/DatabaseTables.sql and db/Testsignatures.sql files.
10. Now run db/DatabaseTables.sql and db/Testsignatures.sql. 

## Command Prompt Terminal (Default Windows CMD):
Use cd until you get to the argus-scanner-main\server section. 
Run command "mvnw.cmd spring-boot:run". 

Note: The HTTPS key is already included in the zip (server\src\main\resources\argus-keystore.p12),
and the matching certificate is already in the scanner app, so no keys need to be generated.

## Argus Dashboard: 
1. Open link "https://localhost:8080/".
2. The username is "admin@argus.local" and password is "changeme".
3. Open scanner-android in Android Studio and run the app on an emulator.
4. On the dashboard's Devices tab, click "Pair scanner", enter the code in the app, and tap "Scan device".
5. View the results on the Scans and Reports tabs. A saved scan file can also be uploaded on the Scans tab. We have added samples in docs/.


