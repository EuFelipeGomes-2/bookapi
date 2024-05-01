create table
  public.books (
    bookid uuid not null,
    userid uuid null,
    bookname text not null,
    bookauthor text null,
    bookstatus text null,
    description text null,
    completed boolean null,
    rating double precision null,
    constraint books_pkey primary key (bookid),
    constraint books_userid_fkey foreign key (userid) references users (userid)
  ) tablespace pg_default;