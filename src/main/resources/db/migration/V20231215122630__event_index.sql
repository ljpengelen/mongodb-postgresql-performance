CREATE INDEX idx_event_contract_id_severity_id ON postgres_event(contract_id, severity DESC, id DESC);
