ALTER TABLE companies
    ADD COLUMN sync_type VARCHAR(32) NOT NULL DEFAULT 'MITE';
ALTER TABLE companies
    RENAME mite_base_url TO sync_base_url;
ALTER TABLE companies
    RENAME mite_api_key TO sync_api_key;