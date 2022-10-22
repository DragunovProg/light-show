create table Colors(
    id bigserial primary key,
    name text unique
);

create table Lights(
    id bigserial primary key,
    label text unique not null,
    color_id bigint not null references Colors(id),
    enabled boolean not null
);

create table Color_History(
    id bigserial primary key,
    light_id bigint not null references Lights(id),
    old_color_id bigint not null references Colors(id),
    new_color_id bigint not null references Colors(id),
    changed_at timestamptz
)