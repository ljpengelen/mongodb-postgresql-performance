CREATE TABLE postgres_event (
  id SERIAL PRIMARY KEY,
  contract_id character varying NOT NULL,
  severity bigint
);
