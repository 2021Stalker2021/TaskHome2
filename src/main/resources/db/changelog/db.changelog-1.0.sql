-- liquibase formatted sql

-- changeset Eduard:1
CREATE TABLE address
(
    id       UUID PRIMARY KEY NOT NULL,
    building INT,
    street   VARCHAR(128),
    city     VARCHAR(128),
    region   VARCHAR(128)
);
-- rollback DROP TABLE address;

-- changeset Eduard:2
CREATE TABLE ticket_info
(
    id           UUID PRIMARY KEY NOT NULL,
    price        DECIMAL(10, 2),
    currency     VARCHAR(30),
    availability BOOLEAN
);
-- rollback DROP TABLE ticket_info;

-- changeset Eduard:3
CREATE TABLE service
(
    id           UUID PRIMARY KEY NOT NULL,
    name         VARCHAR(128),
    description  TEXT,
    service_type VARCHAR(128)
);
-- rollback DROP TABLE service;

-- changeset Eduard:4
CREATE TABLE attraction
(
    id              UUID PRIMARY KEY NOT NULL,
    name            VARCHAR(128),
    description     TEXT,
    attraction_type VARCHAR(128),
    address_id      UUID,
    ticket_info_id  UUID,
    CONSTRAINT fk_attraction_address FOREIGN KEY (address_id) REFERENCES address (id),
    CONSTRAINT fk_attraction_ticket_info FOREIGN KEY (ticket_info_id) REFERENCES ticket_info (id)
);
-- rollback DROP TABLE attraction;

-- changeset Eduard:5
CREATE TABLE attraction_service
(
    attraction_id UUID NOT NULL,
    service_id    UUID NOT NULL,
    PRIMARY KEY (attraction_id, service_id),
    CONSTRAINT fk_attraction_service_attraction FOREIGN KEY (attraction_id) REFERENCES attraction (id),
    CONSTRAINT fk_attraction_service_service FOREIGN KEY (service_id) REFERENCES service (id)
);
-- rollback DROP TABLE attraction_service;