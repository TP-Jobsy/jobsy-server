CREATE TYPE user_role_enum AS ENUM ('CLIENT', 'FREELANCER', 'ADMIN');
CREATE TYPE experience_enum AS ENUM ('BEGINNER', 'MIDDLE', 'EXPERT');
CREATE TYPE complexity_enum AS ENUM ('EASY', 'MEDIUM', 'HARD');
CREATE TYPE payment_type_enum AS ENUM ('HOURLY', 'FIXED');
CREATE TYPE project_status_enum AS ENUM ('OPEN', 'IN_PROGRESS', 'COMPLETED');
CREATE TYPE application_type_enum AS ENUM ('RESPONSE', 'INVITATION');
CREATE TYPE project_application_status_enum AS ENUM ('PENDING', 'APPROVED', 'DECLINED');
CREATE TYPE project_duration_enum AS ENUM ('LESS_THAN_1_MONTH','LESS_THAN_3_MONTHS','LESS_THAN_6_MONTHS');
CREATE TYPE confirmation_action_enum AS ENUM ('REGISTRATION', 'PASSWORD_RESET');