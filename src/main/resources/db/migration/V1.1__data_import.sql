DELIMITER //

CREATE PROCEDURE insert_exemplary_categories()
BEGIN
	DECLARE category_id binary(16);
	DECLARE main_category_id binary(16);

    INSERT INTO categories (id, name, description) VALUES (UUID_TO_BIN(UUID()), 'Electronics', 'This category includes items such as smartphones, laptops, tablets, smartwatches, headphones, TVs, gaming consoles, and other electronic devices.');
    INSERT INTO categories (id, name, description) VALUES (UUID_TO_BIN(UUID()), 'Clothing & Apparel','Clothing, shoes, accessories, and fashion items for men, women, children, and even pets fall under this category.');
    INSERT INTO categories (id, name, description) VALUES (UUID_TO_BIN(UUID()), 'Home & Garden','Products for home improvement, furniture, kitchenware, bedding, gardening tools, and decor are typically categorized here.');
    INSERT INTO categories (id, name, description) VALUES (UUID_TO_BIN(UUID()), 'Health & Beauty','Personal care products like skincare, haircare, makeup, fragrances, vitamins, supplements, and fitness equipment are often grouped together.');
    INSERT INTO categories (id, name, description) VALUES (UUID_TO_BIN(UUID()), 'Sports & Outdoors','Equipment and gear for various sports, camping, hiking, fishing, and outdoor activities are included in this category.');
    INSERT INTO categories (id, name, description) VALUES (UUID_TO_BIN(UUID()), 'Books & Media','Books, audiobooks, e-books, music CDs/DVDs, movies, and video games are typically found under this heading.');
    INSERT INTO categories (id, name, description) VALUES (UUID_TO_BIN(UUID()), 'Baby Products','Toys, strollers, car seats, diapers, baby clothes, and other items for infants and toddlers are categorized here.');
    INSERT INTO categories (id, name, description) VALUES (UUID_TO_BIN(UUID()), 'Pet Supplies','Food, toys, grooming tools, and accessories for dogs, cats, birds, and other pets fall under this category.');
    INSERT INTO categories (id, name, description) VALUES (UUID_TO_BIN(UUID()), 'Automotive','Car parts, accessories, tools, and services related to vehicle maintenance and repair are included in this category.');
    INSERT INTO categories (id, name, description) VALUES (UUID_TO_BIN(UUID()), 'Office Supplies','Paper products, pens, pencils, printers, computers, and other office equipment and supplies are grouped together here.');

    SELECT id INTO main_category_id FROM categories WHERE name = 'Electronics' LIMIT 1;

    INSERT INTO categories (id, name, description, parent_category_id, main_category_id) VALUES (UUID_TO_BIN(UUID()), 'Smartphones','This subcategory includes mobile phones from various manufacturers such as Apple (iPhone), Samsung, Google (Pixel), OnePlus, Huawei, and others. It covers both Android and iOS operating systems.', main_category_id, main_category_id);
	INSERT INTO categories (id, name, description, parent_category_id, main_category_id) VALUES (UUID_TO_BIN(UUID()), 'Laptops & Tablets', 'This subcategory encompasses portable computing devices, including: Laptops and Tablet computers', main_category_id, main_category_id);
	INSERT INTO categories (id, name, description, parent_category_id, main_category_id) VALUES (UUID_TO_BIN(UUID()), 'Gaming Consoles', 'This subcategory includes various types of gaming platforms: PlayStation consoles (PS4, PS5), Xbox consoles (Xbox One, Series X/S), Nintendo Switch and other handheld gaming devices, PC gaming components and accessories', main_category_id, main_category_id);

    SELECT id INTO category_id FROM categories WHERE name = 'Smartphones' LIMIT 1;

    INSERT INTO categories (id, name, description, parent_category_id, main_category_id) VALUES (UUID_TO_BIN(UUID()), 'Android Phones','Smartphones running the Android operating system, offering customization options and choices from multiple manufacturers like Samsung, Google, OnePlus, etc.', category_id, main_category_id);
    INSERT INTO categories (id, name, description, parent_category_id, main_category_id) VALUES (UUID_TO_BIN(UUID()), 'iPhone Models','Apple''s line of smartphones known for their sleek design, user-friendly interface, and integration with other Apple devices and services.', category_id, main_category_id);
    INSERT INTO categories (id, name, description, parent_category_id, main_category_id) VALUES (UUID_TO_BIN(UUID()), 'Budget Smartphones','Affordable entry-level smartphones with basic features, ideal for those on a tight budget or looking for a secondary device', category_id, main_category_id);
    INSERT INTO categories (id, name, description, parent_category_id, main_category_id) VALUES (UUID_TO_BIN(UUID()), 'Flagship Devices', 'High-end smartphones with premium features, large displays, advanced cameras, and powerful processors from leading manufacturers.', category_id, main_category_id);
    INSERT INTO categories (id, name, description, parent_category_id, main_category_id) VALUES (UUID_TO_BIN(UUID()), 'Pre-Owned Phones', 'Previously owned or refurbished smartphones, often offering significant cost savings for buyers looking for a reliable device at a lower price point.', category_id, main_category_id);


END //

DELIMITER ;

CALL insert_exemplary_categories();

DROP PROCEDURE insert_exemplary_categories;
