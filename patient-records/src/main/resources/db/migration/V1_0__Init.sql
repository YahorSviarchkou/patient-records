CREATE TABLE roles
(
    id    serial PRIMARY KEY NOT NULL,
    title varchar(60)        NOT NULL UNIQUE
);

CREATE TABLE users
(
    id       serial PRIMARY KEY           NOT NULL,
    login    varchar(60)                  NOT NULL UNIQUE,
    password varchar(60)                  NOT NULL,
    role_id  bigint REFERENCES roles (id) NOT NULL,
    deleted  boolean                      NOT NULL
);

CREATE TABLE positions
(
    id    serial PRIMARY KEY NOT NULL,
    title varchar(60)        NOT NULL UNIQUE
);

CREATE TABLE doctor_details
(
    id          serial PRIMARY KEY               NOT NULL,
    user_id     bigint REFERENCES users (id)     NOT NULL UNIQUE,
    fio         varchar                          NOT NULL,
    position_id bigint REFERENCES positions (id) NOT NULL
);

CREATE TABLE patient_cards
(
    id               serial PRIMARY KEY NOT NULL,
    photo            bytea ,
    fio              varchar            NOT NULL,
    date_of_birthday timestamp          NOT NULL,
    created          timestamp          NOT NULL,
    deleted          boolean            NOT NULL
);

CREATE TABLE comments
(
    id         serial PRIMARY KEY                    NOT NULL,
    doctor_id  bigint REFERENCES doctor_details (id) NOT NULL,
    patient_id bigint REFERENCES patient_cards (id)  NOT NULL,
    content    varchar(255)                          NOT NULL,
    created    timestamp                             NOT NULL
);