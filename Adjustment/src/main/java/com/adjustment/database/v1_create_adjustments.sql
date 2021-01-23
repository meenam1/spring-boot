create table adjustment(
    id serial PRIMARY KEY,
	name varchar(48),
	code varchar(48),
	type varchar(48),
	start_date date,
	end_date date,
	status varchar(30),
	created_by varchar(48),
	created_at varchar(48),
	updated_at date
);


create table associated_app(
	id int PRIMARY KEY,
	name varchar(48),
	percentage float,
	status varchar(48),
	adjustment_id int,
	FOREIGN KEY(adjustment_id) REFERENCES adjustment(id)
);