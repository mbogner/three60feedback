# T60f

- gitlab: https://gitlab.openresearch.com/openresearch/t60f
- sonar: https://sonar2.i.openresearch.com/dashboard?id=t60f

## Deployment

Simply create a new tag to create a new docker image in the registry in gitlab.

It needs extra configuration to run:
- APP_BASE_URL
- APP_CLIENT_MITE_URL
- APP_CLIENT_MITE_API_KEY
- database and mail config - see [application-local.yml](src/main/resources/application-local.yml)
