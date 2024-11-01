CREATE TABLE categories (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(256) NOT NULL,
    description TEXT NOT NULL,
    main_category_id INT,
    parent_category_id INT,
    PRIMARY KEY(id),
    CONSTRAINT fk_parent_category_id FOREIGN KEY (parent_category_id) REFERENCES categories(id),
    CONSTRAINT fk_main_category_id FOREIGN KEY (main_category_id) REFERENCES categories(id),
    CONSTRAINT unique_name_parent_category_id UNIQUE (name, parent_category_id)
);

