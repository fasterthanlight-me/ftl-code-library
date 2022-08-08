#This is demo spring app.

Before you read everything below. I would rather create new Spring project, 
and then use this one as a reference to configure some specific staff. 
In order to reuse this one, too much namings need to be changed. And usually, you
don't need everything from the very beginning.

This project contain some general concepts that are widely used in java world. 
Please check this list.

 - 2 packages. user-service and common. You can extend common with other microservices. 
It contains some useful setup that is missed in spring autoconfig
 - Migrations: see resources of common project. Configured with flyway, so when app starts, migration will be executed.
 - CorrelationFilter in common.configuration. Required for proper logging in microservice env.
 - CustomSecurityConfig in common.configuration. Set up some common rules, that swagger and health are public, and rest are private.
 - ErrorHandlingControllerAdvice in common.configuration. Adds more verbose logging for constraint violation.
 - PhysicalNamingStrategyImpl in common.configuration. Adds proper naming for db tables. So in the db everything is snake_case, and jps camelCase.
 - RestApiConfigurer in common.configuration. Adds CORS plus specifications. Specifications are super useful for filtering.
 - Health controller in common.controller. Just normal healthcheck. No fancy logic here.
 - common.dto - dto for validation errors
 - common.entity, common.util, common.integrations - staff for s3 pressigned urls.
 - common.exception - some custom exception that will produce proper message and status.
 - userservice.mapper - mapstruct mapper to quickly generate dto to entity mapping methods.
 - userservice.repository.spec - easy way to create new specifications. Also see list endpoint in UserController
 - other than that userserice is just simple CRUD.

In order to start the app. Set up .env plugin in the IDE. 
 - Start DB, and update .env
 - Create aws account and update .env, so you can use uploads bucket.
 - Make sure, that you defined all variables required in application.yml
 - Update security.oauth2 config in application.yml to use proper resource server. This project is set up for AWS Cognito, 
but you might use different security provider.