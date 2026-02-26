# Lab Demo - Angular + Spring Boot CRUD

This demo has two projects:
- `tutorial/` -> Spring Boot REST API (port `8080`)
- `AngularCRUD/` -> Angular frontend (port `4200`)

The frontend calls:
- `http://localhost:8080/api/tutorials`

## Prerequisites
Install these first:
- Java 8+ (Java 17 recommended)
- Maven 3.8+
- Node.js 16+ and npm
- Angular CLI (`@angular/cli`)
- MongoDB (local, port `27017`)

## Install Dependencies

### 1) Backend dependencies
```bash
cd tutorial
mvn clean install
```

### 2) Frontend dependencies
```bash
cd ../AngularCRUD
npm install
```

If `ng` is not found, use one of:
```bash
npx ng version
# or
npm install -g @angular/cli
```

## Run the Application
Open 3 terminals.

### Terminal A - MongoDB
Start MongoDB locally (example with systemd):
```bash
sudo systemctl start mongod
```

Check it is running on `27017`:
```bash
mongosh --eval "db.runCommand({ ping: 1 })"
```

### Terminal B - Spring Boot backend
```bash
cd tutorial
mvn spring-boot:run
```

Backend URL:
- `http://localhost:8080`

### Terminal C - Angular frontend
```bash
cd AngularCRUD
npx ng serve
```

Frontend URL:
- `http://localhost:4200`

## Verify
- Open `http://localhost:4200`
- Create/update/delete tutorials from the UI
- API base is configured in:
  - `AngularCRUD/src/app/services/tutorial.service.ts`

## Common Issues

### Port 8080 already in use
Run backend on another port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```
Then update frontend base URL in `tutorial.service.ts` to `8081`.

### MongoDB connection errors
Check `tutorial/src/main/resources/application.properties`:
- database: `angular_db`
- port: `27017`

Make sure MongoDB service is running.
