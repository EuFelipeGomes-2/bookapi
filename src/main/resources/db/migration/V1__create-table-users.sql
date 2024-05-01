create table
  public.users (
    userid uuid not null,
    username character varying(150) not null,
    password character varying(150) not null,
    useremail character varying(250) not null,
    role text null default 'USER'::text,
    constraint users_pkey primary key (userid),
    constraint users_role_check check (
      (
        role = any (
          array[
            ('ADMIN'::character varying)::text,
            ('USER'::character varying)::text
          ]
        )
      )
    )
  ) tablespace pg_default;