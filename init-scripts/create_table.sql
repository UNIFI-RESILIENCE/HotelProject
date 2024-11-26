-- public.rooms definition

-- Drop table

-- DROP TABLE public.rooms;

CREATE TABLE IF NOT EXISTS  public.rooms (
	room_number int4 NOT NULL,
	room_amount numeric NOT NULL,
	room_description varchar NOT NULL,
	id int4 NOT NULL,
	CONSTRAINT rooms_pk PRIMARY KEY (id)
);

