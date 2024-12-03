CREATE TABLE IF NOT EXISTS post
(
    id          BIGSERIAL PRIMARY KEY,
    TITLE       VARCHAR,
    DESCRIPTION TEXT,
    user_id     BIGINT

);
DO $$
    BEGIN
        FOR i IN 1..100000 LOOP
                INSERT INTO post (title, description, user_id)
                VALUES (
                           'Post Title ' || i,
                           'This is a description for post ' || i,
                           (RANDOM() * 1000)::BIGINT
                       );
            END LOOP;
    END $$;


SELECT * FROM post
WHERE TITLE LIKE  'Post Title 1047'



