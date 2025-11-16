CREATE SCHEMA IF NOT EXISTS social_network;

CREATE TABLE social_network.dialog_message (
    id UUID PRIMARY KEY,
    from_user_id UUID NOT NULL,
    to_user_id UUID NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);
