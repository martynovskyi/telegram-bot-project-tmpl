# telegram-bot-project-tmpl

## How to run

### Prerequisites

- postgres db  [docker-compose.yml](scripts/docker-compose.yml)
- environment variables
    - `POSTGRES_HOSTNAME`
    - `POSTGRES_DB`
    - `POSTGRES_PASSWORD`
    - `POSTGRES_USE`
- `application.yml` properties

```yaml
app-config:
    roles:
      MAIN: one_bot
      LOG: two_bot
    log-chat: 1001
telegram:
  bots:
    - name: one_bot
      token: <bot-token>
    - name: two_bot
      token: <bot-token>
```