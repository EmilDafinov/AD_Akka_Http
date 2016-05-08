#!/usr/bin/env bash

psql --username "$POSTGRES_USER" -c "
CREATE ROLE _testuser NOSUPERUSER LOGIN PASSWORD 'qwerty';
"
psql --username "$POSTGRES_USER" -c "
CREATE DATABASE ad_db
WITH OWNER = postgres
ENCODING = 'UTF8'
TABLESPACE = pg_default
LC_COLLATE = 'en_US.utf8'
LC_CTYPE = 'en_US.utf8'
CONNECTION LIMIT = -1;
"
psql -d ad_db --username "$POSTGRES_USER" -c "
CREATE TABLE public.test_table(
  id BIGINT,
  name TEXT,
  PRIMARY KEY(id)
);
"
psql -d ad_db --username "$POSTGRES_USER" -c"
CREATE TABLE public.subscriptions (
  company_name TEXT NOT NULL,
  account_identifier BIGSERIAL NOT NULL,
  creator CHARACTER VARYING(255) NOT NULL,
  PRIMARY KEY (company_name)
);
CREATE UNIQUE INDEX subscriptions_account_identifier_uindex ON public.subscriptions (account_identifier);
"
psql -d ad_db --username "$POSTGRES_USER" -c "GRANT SELECT ON TABLE public.test_table TO _testuser;"
psql -d ad_db --username "$POSTGRES_USER" -c "GRANT SELECT ON TABLE public.subscriptions TO _testuser;"
psql -d ad_db --username "$POSTGRES_USER" -c "GRANT INSERT ON TABLE public.subscriptions TO _testuser;"
psql -d ad_db --username "$POSTGRES_USER" -c "GRANT DELETE ON TABLE public.subscriptions TO _testuser;"
psql -d ad_db --username "$POSTGRES_USER" -c "GRANT USAGE, SELECT ON SEQUENCE subscriptions_account_identifier_seq TO _testuser;"

