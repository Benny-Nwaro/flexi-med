-- Enable UUID extension for PostgreSQL
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users Table
CREATE TABLE users (
  user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  date_of_birth DATE,
  email VARCHAR(255) UNIQUE NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  gender VARCHAR(10),
  last_name VARCHAR(255) NOT NULL,
  password VARCHAR(50) NOT NULL,
  phone_number VARCHAR(50),
  profile_bio TEXT,
  role VARCHAR(50) CHECK (role IN ('STUDENT', 'INSTRUCTOR', 'ADMIN'))
);

-- Courses Table
--CREATE TABLE courses (
--  course_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--  title VARCHAR(255) NOT NULL,
--  description TEXT,
--  teacher_id UUID NOT NULL,
--  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--  CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES users(user_id) ON DELETE CASCADE
--);

-- Enrollments Table (Students Enrolling in Courses)
CREATE TABLE enrollments (
  enrollment_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  student_id UUID NOT NULL,
  course_id UUID NOT NULL,
  enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
  UNIQUE (student_id, course_id)
);

-- Lessons Table (Each Course has multiple Lessons)
CREATE TABLE lessons (
  lesson_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  course_id UUID NOT NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT,
  video_url VARCHAR(512),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_lesson_course FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- Assignments Table (Each Course can have multiple Assignments)
CREATE TABLE assignments (
  assignment_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  course_id UUID NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  due_date TIMESTAMP NOT NULL,
  CONSTRAINT fk_assignment_course FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- Submissions Table (Students submit assignments)
CREATE TABLE submissions (
  submission_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  student_id UUID NOT NULL,
  assignment_id UUID NOT NULL,
  submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_submission_student FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_submission_assignment FOREIGN KEY (assignment_id) REFERENCES assignments(assignment_id) ON DELETE CASCADE,
  UNIQUE (student_id, assignment_id)
);
