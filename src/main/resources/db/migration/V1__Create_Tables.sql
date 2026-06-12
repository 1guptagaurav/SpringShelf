CREATE TABLE roles (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(50) NOT NULL,
    CONSTRAINT uk_roles_role UNIQUE (role)
);

CREATE TABLE users (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    lastname  VARCHAR(255),
    email     VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE books (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    author      VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    isbn        VARCHAR(13)  NOT NULL,
    borrowable  BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE book_copies (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    book_id             BIGINT       NOT NULL,
    availability_status VARCHAR(50)  NOT NULL,
    barcode             VARCHAR(255) NOT NULL,
    location            VARCHAR(255),
    CONSTRAINT fk_book_copies_book FOREIGN KEY (book_id) REFERENCES books (id)
);

CREATE TABLE borrowing_transactions (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT      NOT NULL,
    book_copy_id  INT         NOT NULL,
    borrow_date   DATETIME(6) NOT NULL,
    due_date      DATE        NOT NULL,
    late_fee      DECIMAL(5, 2) DEFAULT 0.00,
    return_date   DATE,
    status        VARCHAR(50) NOT NULL,
    CONSTRAINT fk_borrowing_transactions_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_borrowing_transactions_book_copy FOREIGN KEY (book_copy_id) REFERENCES book_copies (id)
);

CREATE TABLE audit_logs (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(255) NOT NULL,
    action    VARCHAR(255) NOT NULL,
    timestamp DATETIME(6)  NOT NULL,
    details   TEXT
);
