CREATE TABLE categories (
    id binary(16) NOT NULL,
    name VARCHAR(256) NOT NULL,
    description TEXT NOT NULL,
    main_category_id binary(16),
    parent_category_id binary(16),
    PRIMARY KEY(id),
    CONSTRAINT fk_parent_category_id FOREIGN KEY (parent_category_id) REFERENCES categories(id),
    CONSTRAINT fk_main_category_id FOREIGN KEY (main_category_id) REFERENCES categories(id),
    CONSTRAINT unique_name_parent_category_id UNIQUE (name, parent_category_id)
);

