CREATE TABLE bookings (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id              UUID NOT NULL REFERENCES jobs(id) ON DELETE CASCADE,
    worker_id           UUID NOT NULL REFERENCES worker_profiles(id),
    price_offered       DECIMAL(10,2) NOT NULL,
    message             TEXT,
    status              VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    client_confirmed    BOOLEAN NOT NULL DEFAULT false,
    worker_confirmed    BOOLEAN NOT NULL DEFAULT false,
    created_at          TIMESTAMP NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE(job_id, worker_id)
);