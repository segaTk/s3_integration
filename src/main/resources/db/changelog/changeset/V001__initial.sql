CREATE TABLE resource (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   size DECIMAL,
   created_at TIMESTAMP WITHOUT TIME ZONE,
   name VARCHAR(150),
   type VARCHAR(200),
   CONSTRAINT pk_resource PRIMARY KEY (id)
);