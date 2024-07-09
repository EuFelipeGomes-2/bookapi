-- Tabela collections
CREATE TABLE collections (
    collection_id uuid PRIMARY KEY,
    user_id uuid NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(userid)
);

-- Tabela collections_books
CREATE TABLE collections_books (
    collection_id uuid NOT NULL,
    book_id uuid NOT NULL,
    PRIMARY KEY (collection_id, book_id),
    CONSTRAINT fk_collection FOREIGN KEY(collection_id) REFERENCES collections(collection_id),
    CONSTRAINT fk_book FOREIGN KEY(book_id) REFERENCES books(bookid)
);