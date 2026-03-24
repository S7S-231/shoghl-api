CREATE TABLE worker_profiles (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    bio                 TEXT,
    years_experience    INTEGER NOT NULL DEFAULT 0,
    city                VARCHAR(100),
    area                VARCHAR(100),
    is_available        BOOLEAN NOT NULL DEFAULT true,
    avg_rating          DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    total_reviews       INTEGER NOT NULL DEFAULT 0,
    created_at          TIMESTAMP NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE worker_categories (
    worker_id       UUID REFERENCES worker_profiles(id) ON DELETE CASCADE,
    category_id     INTEGER REFERENCES categories(id) ON DELETE CASCADE,
    PRIMARY KEY (worker_id, category_id)
);