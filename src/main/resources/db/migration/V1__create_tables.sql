CREATE TABLE USERS (
    id bigint NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    email varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    role varchar(50) NOT NULL,
    created_at  timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE EVENTS (
    id bigint NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY,
    title varchar(255) NOT NULL,
    description varchar(255),
    location varchar(255) NOT NULL,
    start_date  timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    end_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    organizer_id bigint NOT NULL,
    created_at  timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (organizer_id) REFERENCES USERS (id)
);

CREATE TABLE TICKETS (
    id bigint NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY ,
    price decimal NOT NULL,
    type varchar(50) NOT NULL,
    quantity integer NOT NULL,
    event_id bigint NOT NULL,
    created_at  timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL ,
    PRIMARY KEY (id),
    FOREIGN KEY (event_id) REFERENCES EVENTS (id)
);

CREATE TABLE REGISTRATIONS (
   user_id bigint NOT NULL,
   event_id bigint NOT NULL,
   ticket_id bigint NOT NULL,
   registration_date  timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
   created_at  timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
   updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL ,
   PRIMARY KEY (user_id, event_id, ticket_id) ,
   FOREIGN KEY (user_id) REFERENCES USERS (id),
   FOREIGN KEY (event_id) REFERENCES EVENTS (id),
   FOREIGN KEY (ticket_id) REFERENCES TICKETS (id)
);
