-- public.rooms definition

-- Drop table

-- DROP TABLE public.rooms;

CREATE TABLE IF NOT EXISTS  public.rooms (
	room_number varchar NOT NULL,
	room_description varchar NOT NULL,
	CONSTRAINT rooms_pk PRIMARY KEY (room_number)
);

