
Run:
```shell
docker run -p 8181:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev
```
Add realm from [file](config/realm-export.json) via import

Add configuration properties from [file](config/application.properties) to your application.properties of quarkus app.
