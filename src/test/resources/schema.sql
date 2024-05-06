CREATE TABLE IF NOT EXISTS todos
(
     id INTEGER PRIMARY KEY AUTOINCREMENT,
     description TEXT NOT NULL,
     is_completed BOOLEAN NOT NULL
);

delete from  todos;
INSERT INTO todos (id, description, is_completed) VALUES (1, 'Sample Todo1', false);
INSERT INTO todos (id, description, is_completed) VALUES (2, 'Sample Todo2', false);
INSERT INTO todos (id, description, is_completed) VALUES (3, 'Sample Todo3', true);
INSERT INTO todos (id, description, is_completed) VALUES (4, 'Sample Todo4', false);
INSERT INTO todos (id, description, is_completed) VALUES (5, 'Sample Todo5', false);
Commit;
