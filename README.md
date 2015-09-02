# mcutilswebpanel
This is the beginning of a set of tools server operators can use to monitor
and get information about who is playing on their minecraft servers and 
events from the log file etc

Runtime properties instructions...
1. Edit the src/main/resources/mcwebpanel.properties file
2. You will need to list out the users by name (case sensitive in initial release)
   whom you wish to monitor (email.when.player values)
3. Edit your email relay properties to use your email server, not mine
   (smtp and email properties)
4. Save and build. Do NOT commit these values in a pull request!

Build and Install instructions...
1. I use Java 8 and Maven 3.3.x to compile the jar file and dependencies.
   From the project folder, run mvn clean install -DskipTests
2. copy mcwebpanel-1.0.jar AND the target/lib folder to the same location as your minecraft_server.jar

Running the application instructions...
1. Make sure your minecraft server is running
2. From the folder where you have your minecraft_server.jar and the mcwebpanel-1.0.jar run this command:
   java -d64 -cp lib -Dapp.log.level=debug -jar mcwebpanel-1.0.jar
   
