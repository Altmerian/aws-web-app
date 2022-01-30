CREATE TABLE IF NOT EXISTS subscription_metadata (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255),
    subscription_arn VARCHAR(255),
    CONSTRAINT PK_images PRIMARY KEY (id)
);