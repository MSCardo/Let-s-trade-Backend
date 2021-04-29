# Let's Trade Backend

Technologies: Java, Spring Boot.

Idea for this project was suggested by Green Fox Academy and BlackRock Inc.

Contributors: Dániel Babinszky, Péter Tóth, Thanh Tung Nguyen

## Getting Started

### Prerequisites

 * JDK 11
 * Intellij IDEA
 * MySQL 8 
 
### Run the app locally

#### Clone this repository

```
> git clone https://github.com/MSCardo/Lets-trade-Backend.git
```

If you want to see the current status of the development, switch to branch `development`

```
> git checkout development
```

#### Initialise a database
Create the database and initialize with some seed data (basic roles, technologies, example apprentices...)

```
> mysql -u <username> -p

mysql> create database trade;
mysql> \q
```

#### Set up Lombok

Our project uses [Lombok](https://projectlombok.org/), to enable it in IntelliJ you have to add its plugin 
 * follow these [instructions](https://projectlombok.org/setup/intellij)
 * restart IntelliJ IDEA
 * make sure that annotation processing is enabled in `Settings>Build>Compiler>Annotation processors`
 
#### Environment variables

**Database connection**

| Key | Value |
| --- | ----- |
|DB_URL | jdbc:mysql://localhost/trade?serverTimezone=UTC |
|DB_USERNAME | *your local mysql username* |
|DB_PASSWORD | *your local mysql password* |

**Email verification**

| Key | Value |
| --- | ----- | 
|MAIL_USERNAME| validatebro@gmail.com |
|MAIL_PASSWORD| *the configured password to the email, please ask us for it* |

**Others**

| Key | Value |
| --- | ----- | 
|SECRET_KEY| this will be used for token generation, you can use any random string |
|API_KEY| *your API KEY from your account at https://iexcloud.io/* |
