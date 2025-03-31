CREATE TYPE user_role_enum AS ENUM ('client', 'freelancer', 'admin');
CREATE TYPE experience_enum AS ENUM ('beginner', 'middle', 'expert');
CREATE TYPE complexity_enum AS ENUM ('easy', 'medium', 'hard');
CREATE TYPE payment_type_enum AS ENUM ('hourly', 'fixed');
CREATE TYPE project_status_enum AS ENUM ('open', 'in_progress', 'completed');
CREATE TYPE project_application_status_enum AS ENUM ('pending', 'approved', 'declined');
CREATE TYPE project_duration_enum AS ENUM ('less_than_1_month','less_than_3_months','less_than_6_months');
CREATE TYPE confirmation_action_enum AS ENUM ('registration', 'password_reset');