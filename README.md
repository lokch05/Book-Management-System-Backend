# Book-Management-System-Backend

A simple book management system demo app developed in Spring Boot with Netflix-DGS and deployed in a Docker Container.

## How to run the program
Install [Docker](https://www.docker.com/) on your PC and make sure it is running.
```
#Use mvnw to build the image
./mvnw clean install -DskipTests
#Deploy the application in a Docker Container
docker-compose up --build
```
## License
[MIT](https://github.com/lokch05/Book-Management-System-Backend/blob/main/LICENSE.txt/)