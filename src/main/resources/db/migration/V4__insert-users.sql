CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pgcrypto;


-- Adiciona 5000 usuários
DO $$ 
DECLARE 
    i INT := 1;
BEGIN
    WHILE i <= 5000 LOOP
        -- Gerando valores fictícios para nome de usuário, senha e e-mail
        INSERT INTO public.users (userid, username, password, useremail)
        VALUES (uuid_generate_v4(), 'user' || i, encode(digest('password' || i, 'sha1'), 'hex'), 'user' || i || '@example.com');
        i := i + 1;
    END LOOP;
END $$;
