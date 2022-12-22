# FROM tells docker what environment to build our application on top of
# "slim" images are used when you only want the necessary files/libraries for your app to be able to run
FROM openjdk:11-jdk-slim

# COPY tells docker what files/directories to save into the container, and it allows you to rename those files/directories
# left side is the file/directory you want to copy
# right side is the file/direcetory copied into your container. You can rename it
COPY target/boot-0.0.1-SNAPSHOT.jar app.jar

# ENTRYPOINT tells Docker what command to run to startup your application
# You put the individual parts of the command inside of square brackets like strings, seperating them by commas
ENTRYPOINT ["java","-jar","app.jar"]

# To build your image you will use the command docker build, and then the location of the dockerfile
# If the docker file is in your working directory just put a .
# use the -t flag so we can name our image
