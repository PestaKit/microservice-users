CREATE DATABASE "users-service";

USE "users-service";

CREATE TABLE "user" (
        id INT NOT NULL DEFAULT unique_rowid(),
        username STRING NOT NULL,
        firstname STRING,
        lastname STRING,
        email STRING NOT NULL,
        password STRING NOT NULL,
        CONSTRAINT "primary" PRIMARY KEY (id ASC),
        UNIQUE INDEX user_username_key (username ASC),
        UNIQUE INDEX user_email_key (email ASC),
        FAMILY "primary" (id, username, firstname, lastname, email, password)
);
