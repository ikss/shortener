CREATE TABLE accounts (
    id VARCHAR NOT NULL PRIMARY KEY,
    password VARCHAR NOT NULL
);

CREATE SEQUENCE urls_sequence START WITH 1;

CREATE TABLE urls (
    id BIGINT PRIMARY KEY,
    full_url VARCHAR NOT NULL,
    redirect_type INTEGER NOT NULL,
    account_id VARCHAR NOT NULL,
    redirect_count INTEGER NOT NULL DEFAULT 0,
    UNIQUE KEY (full_url, account_id),
    FOREIGN KEY (account_id) REFERENCES accounts (id)
);