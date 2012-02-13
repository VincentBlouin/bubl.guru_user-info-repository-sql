DROP TABLE IF EXISTS por_user;

CREATE TABLE por_user (
    id           BIGINT    PRIMARY KEY AUTO_INCREMENT,
    creationTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updateTime   TIMESTAMP NOT NULL,

    uuid   VARCHAR(36)   UNIQUE NOT NULL,
    email  VARCHAR(50)   UNIQUE NOT NULL,
    locale VARCHAR(10)   NOT NULL DEFAULT 'en_US',

    salt                 VARCHAR(36),
    passwordHash         VARCHAR(100),
    firstname            VARCHAR(50),
    lastname             VARCHAR(50)
);