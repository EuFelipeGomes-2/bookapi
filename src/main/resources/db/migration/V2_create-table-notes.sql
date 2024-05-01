create table
  public.notes (
    noteid uuid not null,
    bookid uuid not null,
    userid uuid not null,
    content text not null,
    created_time timestamp without time zone not null default current_timestamp,
    updated_at timestamp without time zone not null default current_timestamp,
    constraint notes_pkey primary key (noteid),
    constraint notes_bookid_fkey foreign key (bookid) references books (bookid),
    constraint notes_userid_fkey foreign key (userid) references users (userid)
  ) tablespace pg_default;