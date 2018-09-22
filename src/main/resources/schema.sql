CREATE TABLE accounts (
    id VARCHAR PRIMARY KEY,
    password VARCHAR NOT NULL
);

CREATE TABLE urls (
    short_url VARCHAR NOT NULL,
    full_url VARCHAR NOT NULL,
    redirect_type INTEGER NOT NULL,
    account_id VARCHAR NOT NULL,
    redirect_count INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (short_url, account_id),
    FOREIGN KEY (account_id) REFERENCES accounts (id)
);