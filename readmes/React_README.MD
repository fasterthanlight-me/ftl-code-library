# [Project Name]

`app version` `CI status` `code coverage` `other badges`

Staging: [http://localhost:3000](http://localhost:3000) \
Production: [http://localhost:3000](http://localhost:3000)

## Prerequisites
Before you started, make sure that you have installed and set-up properly:
- node version `18.*.*` // provide node version. Alternatively you can add `.nvmrc` file
- yarn/npm version

## Installation steps

```bash
nvm use your_node_version
yarn install
```

Set your `.env` parameters(ask `@somebody` to give you the list)

## Development

### Run local

```
yarn start
```

http://localhost:8080 to view web app.

### Auth credentials
login: `admin@example.com`

password: `password`

### Storybook

We use Storybook to build functionality in isolation from the app, as well as documenting components.

```bash
yarn storybook
```

[http://localhost:6006](http://localhost:6006) to view stories.

### GraphQL

We use GraphQL to send requests to API.

To test the API response you can use one of apps below:
- [Altair](https://altairgraphql.dev/)
- [GraphiQL App](https://www.electronjs.org/apps/graphiql)
- [Postman](https://www.postman.com/)

## Tests
Run tests
```
yarn test
```

## CI
http://ci-link.com 

## Deployment

### if changes are deployed automatically:

- merge your PR into `staging_branch` and CI/CD will deploy it automatically on staging

### if changes are deployed manually:
To deploy latest changes on staging use:
```
yarn deploy:staging
```
