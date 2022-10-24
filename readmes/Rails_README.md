# [Project Name]

`app version` `CI status` `code coverage` `other badges`

Staging: [http://localhost:3000](http://localhost:3000) \
Production: [http://localhost:3000](http://localhost:3000)

## Prerequisites

Before you started, make sure that you have installed and set-up properly:

- Ruby `> 3.0.0`
- Rails `7.0.0`
- PostgreSQL `10`
- Redis

## Installation steps

- install ruby with
  ```sh
  rvm install 2.6.6 --with-out-ext=fiddle
  ```
- install bundler
  ```sh
  gem install bundler
  ```
- install all dependent gems
  ```sh
  bundle install
  ```
- set up and configure your environment variables (ask `@somebody` to give you the list)
  ```sh
  cp .env.example .env
  ```
- Set up and configure your database. \
  Set `host`, `port`, `username` and `password` variables (or just remove them if you use default database configuration)
  ```sh
  cp config/database.yml.example config/database.yml
  ```

- Create, migrate and seed the database
  ```sh
      rails db:create
      rails db:migrate
      rails db:seed
  ```

## Development

### Run local

- Start Redis
  ```sh
  redis-server
  ```

- Start SideKiq
  ```sh
  bundle exec sidekiq
  ```

- Start Rails server
  ```sh
  rails server
  ```

### Auth credentials
login: `admin@example.com`

password: `password`

## Run tests
```
rubocop
rspec
```

## CI
http://ci-link.com

## Deployment

### if changes are deployed automatically:

- merge your PR into `staging_branch` and CI/CD will deploy it automatically on staging

### if changes are deployed manually:
To deploy latest changes on staging use:
```
heroku run bash -a ftl-web
```

## Other useful commands or info

### Add annotation to the models

To annotate all models:
- Run ```annotate --exclude tests,fixtures,factories,serializers```

### something other
