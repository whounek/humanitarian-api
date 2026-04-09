-- liquibase formatted sql
-- changeset admin:init-schema

CREATE TABLE public.action_logs (
    id bigint NOT NULL,
    action_detail character varying(500) NOT NULL,
    "timestamp" timestamp(6) without time zone NOT NULL,
    username character varying(255) NOT NULL
);

CREATE SEQUENCE public.action_logs_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.action_logs_id_seq OWNED BY public.action_logs.id;

CREATE TABLE public.approval_history (
    id bigint NOT NULL,
    comment text,
    created_at timestamp(6) without time zone,
    calculation_id bigint NOT NULL,
    new_status_id bigint NOT NULL,
    reviewer_id bigint NOT NULL
);

CREATE SEQUENCE public.approval_history_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.approval_history_id_seq OWNED BY public.approval_history.id;

CREATE TABLE public.calculation_items (
    id bigint NOT NULL,
    quantity numeric(10,2) NOT NULL,
    calculation_id bigint NOT NULL,
    resource_id bigint NOT NULL
);

CREATE SEQUENCE public.calculation_items_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.calculation_items_id_seq OWNED BY public.calculation_items.id;

CREATE TABLE public.calculations (
    id bigint NOT NULL,
    affected_people integer NOT NULL,
    created_at timestamp(6) without time zone,
    manager_comment character varying(255),
    updated_at timestamp(6) without time zone,
    author_id bigint NOT NULL,
    disaster_type_id bigint NOT NULL,
    status_id bigint NOT NULL
);

CREATE SEQUENCE public.calculations_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.calculations_id_seq OWNED BY public.calculations.id;

CREATE TABLE public.departments (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);

CREATE SEQUENCE public.departments_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.departments_id_seq OWNED BY public.departments.id;

CREATE TABLE public.disaster_types (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);

CREATE SEQUENCE public.disaster_types_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.disaster_types_id_seq OWNED BY public.disaster_types.id;

CREATE TABLE public.notifications (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    is_read boolean,
    message character varying(255) NOT NULL,
    user_id bigint NOT NULL
);

CREATE SEQUENCE public.notifications_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.notifications_id_seq OWNED BY public.notifications.id;

CREATE TABLE public.regions (
    id bigint NOT NULL,
    climate_coefficient numeric(3,2) NOT NULL,
    name character varying(255) NOT NULL
);

CREATE SEQUENCE public.regions_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.regions_id_seq OWNED BY public.regions.id;

CREATE TABLE public.resource_categories (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);

CREATE SEQUENCE public.resource_categories_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.resource_categories_id_seq OWNED BY public.resource_categories.id;

CREATE TABLE public.resources (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    category_id bigint
);

CREATE SEQUENCE public.resources_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.resources_id_seq OWNED BY public.resources.id;

CREATE TABLE public.roles (
    id bigint NOT NULL,
    name character varying(50) NOT NULL
);

CREATE SEQUENCE public.roles_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;

CREATE TABLE public.shelters (
    id bigint NOT NULL,
    address character varying(255),
    capacity integer NOT NULL,
    name character varying(255) NOT NULL,
    region character varying(255) NOT NULL
);

CREATE SEQUENCE public.shelters_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.shelters_id_seq OWNED BY public.shelters.id;

CREATE TABLE public.standards (
    id bigint NOT NULL,
    quantity_per_person numeric(10,2) NOT NULL,
    disaster_type_id bigint NOT NULL,
    resource_id bigint NOT NULL
);

CREATE SEQUENCE public.standards_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.standards_id_seq OWNED BY public.standards.id;

CREATE TABLE public.statuses (
    id bigint NOT NULL,
    name character varying(50) NOT NULL
);

CREATE SEQUENCE public.statuses_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.statuses_id_seq OWNED BY public.statuses.id;

CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);

CREATE TABLE public.users (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    full_name character varying(255),
    is_active boolean,
    password character varying(255) NOT NULL,
    region character varying(255),
    role character varying(255),
    username character varying(50) NOT NULL,
    department_id bigint
);

CREATE SEQUENCE public.users_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;

-- Подключение Sequence (Auto-increment)
ALTER TABLE ONLY public.action_logs ALTER COLUMN id SET DEFAULT nextval('public.action_logs_id_seq'::regclass);
ALTER TABLE ONLY public.approval_history ALTER COLUMN id SET DEFAULT nextval('public.approval_history_id_seq'::regclass);
ALTER TABLE ONLY public.calculation_items ALTER COLUMN id SET DEFAULT nextval('public.calculation_items_id_seq'::regclass);
ALTER TABLE ONLY public.calculations ALTER COLUMN id SET DEFAULT nextval('public.calculations_id_seq'::regclass);
ALTER TABLE ONLY public.departments ALTER COLUMN id SET DEFAULT nextval('public.departments_id_seq'::regclass);
ALTER TABLE ONLY public.disaster_types ALTER COLUMN id SET DEFAULT nextval('public.disaster_types_id_seq'::regclass);
ALTER TABLE ONLY public.notifications ALTER COLUMN id SET DEFAULT nextval('public.notifications_id_seq'::regclass);
ALTER TABLE ONLY public.regions ALTER COLUMN id SET DEFAULT nextval('public.regions_id_seq'::regclass);
ALTER TABLE ONLY public.resource_categories ALTER COLUMN id SET DEFAULT nextval('public.resource_categories_id_seq'::regclass);
ALTER TABLE ONLY public.resources ALTER COLUMN id SET DEFAULT nextval('public.resources_id_seq'::regclass);
ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);
ALTER TABLE ONLY public.shelters ALTER COLUMN id SET DEFAULT nextval('public.shelters_id_seq'::regclass);
ALTER TABLE ONLY public.standards ALTER COLUMN id SET DEFAULT nextval('public.standards_id_seq'::regclass);
ALTER TABLE ONLY public.statuses ALTER COLUMN id SET DEFAULT nextval('public.statuses_id_seq'::regclass);
ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);

-- Primary Keys
ALTER TABLE ONLY public.action_logs ADD CONSTRAINT action_logs_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.approval_history ADD CONSTRAINT approval_history_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.calculation_items ADD CONSTRAINT calculation_items_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.calculations ADD CONSTRAINT calculations_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.departments ADD CONSTRAINT departments_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.disaster_types ADD CONSTRAINT disaster_types_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.notifications ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.regions ADD CONSTRAINT regions_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.resource_categories ADD CONSTRAINT resource_categories_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.resources ADD CONSTRAINT resources_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.roles ADD CONSTRAINT roles_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.shelters ADD CONSTRAINT shelters_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.standards ADD CONSTRAINT standards_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.statuses ADD CONSTRAINT statuses_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.user_roles ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);
ALTER TABLE ONLY public.users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

-- Unique Constraints
ALTER TABLE ONLY public.regions ADD CONSTRAINT uk_1m9qnhbk56c8iskxvfupln9me UNIQUE (name);
ALTER TABLE ONLY public.statuses ADD CONSTRAINT uk_9ob63rkqg8ppaon1l37w8id2p UNIQUE (name);
ALTER TABLE ONLY public.roles ADD CONSTRAINT uk_ofx66keruapi6vyqpv6f2or37 UNIQUE (name);
ALTER TABLE ONLY public.users ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);

-- Foreign Keys
ALTER TABLE ONLY public.calculation_items ADD CONSTRAINT fk1qkrqtxy5mpu127ayhkx9toda FOREIGN KEY (calculation_id) REFERENCES public.calculations(id);
ALTER TABLE ONLY public.calculations ADD CONSTRAINT fk2awtc3lvji9t6mqhnplsc2lw FOREIGN KEY (disaster_type_id) REFERENCES public.disaster_types(id);
ALTER TABLE ONLY public.standards ADD CONSTRAINT fk4nmwrqnnnv4n6v04gvy3vxvik FOREIGN KEY (disaster_type_id) REFERENCES public.disaster_types(id);
ALTER TABLE ONLY public.standards ADD CONSTRAINT fk78q7dhmpkt8kicfiuufeqpb8i FOREIGN KEY (resource_id) REFERENCES public.resources(id);
ALTER TABLE ONLY public.approval_history ADD CONSTRAINT fk9akf9bfauqs3u1y313hjmryex FOREIGN KEY (new_status_id) REFERENCES public.statuses(id);
ALTER TABLE ONLY public.notifications ADD CONSTRAINT fk9y21adhxn0ayjhfocscqox7bh FOREIGN KEY (user_id) REFERENCES public.users(id);
ALTER TABLE ONLY public.resources ADD CONSTRAINT fkfm1n334xobnnqbvkl9y01lihw FOREIGN KEY (category_id) REFERENCES public.resource_categories(id);
ALTER TABLE ONLY public.user_roles ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES public.roles(id);
ALTER TABLE ONLY public.user_roles ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id);
ALTER TABLE ONLY public.approval_history ADD CONSTRAINT fkk6qvm4xjc9gycaep0hdvfxk3y FOREIGN KEY (reviewer_id) REFERENCES public.users(id);
ALTER TABLE ONLY public.calculation_items ADD CONSTRAINT fkmk7he2774ta13puvkcdr5g0bv FOREIGN KEY (resource_id) REFERENCES public.resources(id);
ALTER TABLE ONLY public.approval_history ADD CONSTRAINT fkpfu349bcejcy5d25rbwu6nh9q FOREIGN KEY (calculation_id) REFERENCES public.calculations(id);
ALTER TABLE ONLY public.calculations ADD CONSTRAINT fkq7o4j82h4pwyyvhxc5xgt6lfp FOREIGN KEY (author_id) REFERENCES public.users(id);
ALTER TABLE ONLY public.users ADD CONSTRAINT fksbg59w8q63i0oo53rlgvlcnjq FOREIGN KEY (department_id) REFERENCES public.departments(id);
ALTER TABLE ONLY public.calculations ADD CONSTRAINT fktj0qcxqsixam9bmvvwutb2v1s FOREIGN KEY (status_id) REFERENCES public.statuses(id);