CREATE TABLE categories (
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(100) NOT NULL UNIQUE,
    icon    VARCHAR(50)
);

INSERT INTO categories (name, icon) VALUES
('سباك', 'plumber'),
('كهربائي', 'electrician'),
('نجار', 'carpenter'),
('دهان', 'painter'),
('ميكانيكي', 'mechanic'),
('تكييف وتبريد', 'ac'),
('بناء وتشييد', 'construction'),
('نظافة', 'cleaning'),
('أنترنت وكاميرات', 'network'),
('أعمال ألومنيوم', 'aluminum');