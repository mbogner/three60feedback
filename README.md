# T60f

## Deployment

Simply create a new tag to create a new docker image in the registry in gitlab.

It needs extra configuration to run:
- APP_BASE_URL
- APP_AUTH_ADMIN_USERNAME
- APP_AUTH_ADMIN_PASSWORD
- APP_ENCRYPTION_KEY
- database and mail config - see [application-local.yml](src/main/resources/application-local.yml)
