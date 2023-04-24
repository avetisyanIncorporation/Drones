# Drones API

## Database Design 
Database - PostgreSQL, Migrations - Liquibase

![drones_db](https://user-images.githubusercontent.com/29467133/233858496-c1d3fff9-1e80-4114-9476-affee63b4d32.png)


## Environment
For building, running and testing the application you must have:

0) Git
1) Java 17 (in project)
2) Maven 3.8.6 (in project)
3) PostgreSQL 13 (in project)
4) Docker (Optional) - for running in container

P.s. Auto build (CI/CD) will be soon (You should have only Docker (4))

### For self-build
1) Clone the project (```git clone https://github.com/avetisyanIncorporation/Drones.git```)
2) Go to ```/Drones/``` directory
3) Set the PostgreSQL ```host:port/database_name, user and password in application.properties``` file
4) Open ```cmd```
5) run ```mvn clean package``` - it was init db and built the project (with tests)

For local runnig:
1) ```mvn spring-boot:run```


For running in container:
1) ```docker-compose up -d```

### For auto-build (use built container)
P.s. Soon

## API:

@GET

1) For get all available drones use ```/drone-management/drones```
2) For get current drone battery capacity use ```/drone-management/drones/{drone-id}/battery-capacity/```, where ```drone-id``` is ```drone.id from db```
3) For get drone medications use ```/drone-management/drones/{drone-id}/medications/```, where ```drone-id``` is ```drone.id from db```

@POST

1) For register a new drone use ```/drone-management/drones```
   with params: ```serialNumber, modelId, stateId, weightLimit, batteryCapacity```
2) For create new medication use ```/medication-management/medications```
   with params: ```name, weight, code, image (non-required), droneId (for add to current drone, non-required)```
3) For add medication to current drone use ```/drone-management/drones/{drone-id}/medication/{medication-id}/add```
   , where ```drone-id``` is ```drone.id from db``` and ```medication-id``` is ```medication.id from db```
  
