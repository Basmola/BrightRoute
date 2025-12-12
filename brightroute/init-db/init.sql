-- ======================================
-- BrightRoute Database Initialization Script
-- ======================================

-- Create database if not exists
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'SCM')
BEGIN
    CREATE DATABASE SCM;
END
GO

USE SCM;
GO

-- ======================================
-- Create Schemas
-- ======================================

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'users')
BEGIN
    EXEC('CREATE SCHEMA users');
END
GO

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'courses')
BEGIN
    EXEC('CREATE SCHEMA courses');
END
GO

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'lectures')
BEGIN
    EXEC('CREATE SCHEMA lectures');
END
GO

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'quiz')
BEGIN
    EXEC('CREATE SCHEMA quiz');
END
GO

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'access')
BEGIN
    EXEC('CREATE SCHEMA access');
END
GO

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'logs')
BEGIN
    EXEC('CREATE SCHEMA logs');
END
GO

-- ======================================
-- Create Tables
-- ======================================

-- Users Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'users' AND t.name = 'Users')
BEGIN
    CREATE TABLE users.Users (
        user_id INT IDENTITY(1,1) PRIMARY KEY,
        user_first_name NVARCHAR(100) NOT NULL,
        user_last_name NVARCHAR(100) NOT NULL,
        user_email NVARCHAR(255) UNIQUE NOT NULL,
        user_phone_number NVARCHAR(20) NOT NULL,
        user_password_hash NVARCHAR(255) NOT NULL,
        user_role NVARCHAR(50) NOT NULL CHECK (user_role IN ('STUDENT', 'INSTRUCTOR', 'ADMIN')),
        user_account_status NVARCHAR(50) NOT NULL CHECK (user_account_status IN ('ACTIVE', 'SUSPENDED', 'DELETED')),
        user_image VARBINARY(MAX),
        user_created_at DATETIME2 DEFAULT GETDATE()
    );
    
    CREATE INDEX idx_users_email ON users.Users(user_email);
    CREATE INDEX idx_users_role ON users.Users(user_role);
END
GO

-- Student Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'users' AND t.name = 'Student')
BEGIN
    CREATE TABLE users.Student (
        student_id INT PRIMARY KEY,
        student_national_id NVARCHAR(50) UNIQUE,
        parent_number BIGINT,
        student_id_type NVARCHAR(50) CHECK (student_id_type IN ('NATIONAL_ID', 'BIRTH_CERTIFICATE')),
        student_national_id_front VARBINARY(MAX),
        student_birth_certificate VARBINARY(MAX),
        student_level_of_education NVARCHAR(100),
        student_created_at DATETIME2 DEFAULT GETDATE(),
        FOREIGN KEY (student_id) REFERENCES users.Users(user_id) ON DELETE CASCADE
    );
    
    CREATE INDEX idx_student_national_id ON users.Student(student_national_id);
END
GO

-- Course Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'courses' AND t.name = 'Course')
BEGIN
    CREATE TABLE courses.Course (
        course_id INT IDENTITY(1,1) PRIMARY KEY,
        course_title NVARCHAR(200) NOT NULL,
        course_description NVARCHAR(MAX) NOT NULL,
        course_image_cover VARBINARY(MAX),
        course_instructor NVARCHAR(150) NOT NULL,
        course_number_of_lectures INT NOT NULL DEFAULT 0,
        level_id INT,
        course_created_at DATETIME2 DEFAULT GETDATE()
    );
    
    CREATE INDEX idx_course_instructor ON courses.Course(course_instructor);
    CREATE INDEX idx_course_level ON courses.Course(level_id);
END
GO

-- CourseSubscription Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'courses' AND t.name = 'CourseSubscription')
BEGIN
    CREATE TABLE courses.CourseSubscription (
        subscription_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        course_id INT NOT NULL,
        subscription_status NVARCHAR(50) NOT NULL CHECK (subscription_status IN ('ACTIVE', 'EXPIRED', 'CANCELLED')),
        subscription_created_at DATETIME2 DEFAULT GETDATE(),
        FOREIGN KEY (user_id) REFERENCES users.Users(user_id) ON DELETE CASCADE,
        FOREIGN KEY (course_id) REFERENCES courses.Course(course_id) ON DELETE CASCADE
    );
    
    CREATE INDEX idx_subscription_user ON courses.CourseSubscription(user_id);
    CREATE INDEX idx_subscription_course ON courses.CourseSubscription(course_id);
END
GO

-- Lecture Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'courses' AND t.name = 'Lecture')
BEGIN
    CREATE TABLE courses.Lecture (
        lecture_id INT IDENTITY(1,1) PRIMARY KEY,
        lecture_title NVARCHAR(200) NOT NULL,
        lecture_description NVARCHAR(MAX),
        lecture_order_number INT NOT NULL,
        course_id INT NOT NULL,
        lecture_created_at DATETIME2 DEFAULT GETDATE(),
        FOREIGN KEY (course_id) REFERENCES courses.Course(course_id) ON DELETE CASCADE
    );
    
    CREATE INDEX idx_lecture_course ON courses.Lecture(course_id);
    CREATE INDEX idx_lecture_order ON courses.Lecture(lecture_order_number);
END
GO

-- LecturePart Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'lectures' AND t.name = 'LecturePart')
BEGIN
    CREATE TABLE lectures.LecturePart (
        part_id INT IDENTITY(1,1) PRIMARY KEY,
        part_type NVARCHAR(50) NOT NULL CHECK (part_type IN ('QUIZ', 'VIDEO', 'PDF', 'TEXT')),
        part_content_url NVARCHAR(500),
        part_description NVARCHAR(MAX),
        part_order_number INT NOT NULL,
        lecture_id INT NOT NULL,
        FOREIGN KEY (lecture_id) REFERENCES courses.Lecture(lecture_id) ON DELETE CASCADE
    );
    
    CREATE INDEX idx_lecture_part_lecture ON lectures.LecturePart(lecture_id);
    CREATE INDEX idx_lecture_part_order ON lectures.LecturePart(part_order_number);
END
GO

-- Quiz Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'quiz' AND t.name = 'Quiz')
BEGIN
    CREATE TABLE quiz.Quiz (
        quiz_id INT IDENTITY(1,1) PRIMARY KEY,
        quiz_title NVARCHAR(200) NOT NULL,
        quiz_description NVARCHAR(MAX),
        quiz_duration_minutes INT,
        quiz_total_marks INT NOT NULL DEFAULT 0,
        part_id INT UNIQUE,
        quiz_created_at DATETIME2 DEFAULT GETDATE(),
        FOREIGN KEY (part_id) REFERENCES lectures.LecturePart(part_id) ON DELETE SET NULL
    );
    
    CREATE INDEX idx_quiz_part ON quiz.Quiz(part_id);
END
GO

-- QuizQuestion Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'quiz' AND t.name = 'QuizQuestion')
BEGIN
    CREATE TABLE quiz.QuizQuestion (
        question_id INT IDENTITY(1,1) PRIMARY KEY,
        question_text NVARCHAR(MAX) NOT NULL,
        question_type NVARCHAR(50) NOT NULL CHECK (question_type IN ('MULTIPLE_CHOICE', 'TRUE_FALSE', 'ESSAY')),
        question_marks INT NOT NULL DEFAULT 1,
        quiz_id INT NOT NULL,
        FOREIGN KEY (quiz_id) REFERENCES quiz.Quiz(quiz_id) ON DELETE CASCADE
    );
    
    CREATE INDEX idx_quiz_question_quiz ON quiz.QuizQuestion(quiz_id);
END
GO

-- QuestionsChoice Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'quiz' AND t.name = 'QuestionsChoice')
BEGIN
    CREATE TABLE quiz.QuestionsChoice (
        choice_id INT IDENTITY(1,1) PRIMARY KEY,
        choice_text NVARCHAR(MAX) NOT NULL,
        is_correct BIT NOT NULL DEFAULT 0,
        question_id INT NOT NULL,
        FOREIGN KEY (question_id) REFERENCES quiz.QuizQuestion(question_id) ON DELETE CASCADE
    );
    
    CREATE INDEX idx_choice_question ON quiz.QuestionsChoice(question_id);
END
GO

-- StudentQuizSubmission Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'quiz' AND t.name = 'StudentQuizSubmission')
BEGIN
    CREATE TABLE quiz.StudentQuizSubmission (
        submission_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        quiz_id INT NOT NULL,
        submission_score DECIMAL(5,2),
        submission_submitted_at DATETIME2 DEFAULT GETDATE(),
        FOREIGN KEY (user_id) REFERENCES users.Users(user_id) ON DELETE CASCADE,
        FOREIGN KEY (quiz_id) REFERENCES quiz.Quiz(quiz_id) ON DELETE CASCADE
    );
    
    CREATE INDEX idx_submission_user ON quiz.StudentQuizSubmission(user_id);
    CREATE INDEX idx_submission_quiz ON quiz.StudentQuizSubmission(quiz_id);
END
GO

-- StudentQuestionsAnswer Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'quiz' AND t.name = 'StudentQuestionsAnswer')
BEGIN
    CREATE TABLE quiz.StudentQuestionsAnswer (
        answer_id INT IDENTITY(1,1) PRIMARY KEY,
        submission_id INT NOT NULL,
        question_id INT NOT NULL,
        selected_choice_id INT,
        answer_text NVARCHAR(MAX),
        is_correct BIT,
        FOREIGN KEY (submission_id) REFERENCES quiz.StudentQuizSubmission(submission_id) ON DELETE CASCADE,
        FOREIGN KEY (question_id) REFERENCES quiz.QuizQuestion(question_id) ON DELETE NO ACTION,
        FOREIGN KEY (selected_choice_id) REFERENCES quiz.QuestionsChoice(choice_id) ON DELETE NO ACTION
    );
    
    CREATE INDEX idx_answer_submission ON quiz.StudentQuestionsAnswer(submission_id);
    CREATE INDEX idx_answer_question ON quiz.StudentQuestionsAnswer(question_id);
END
GO

-- Enrollment Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'access' AND t.name = 'Enrollment')
BEGIN
    CREATE TABLE access.Enrollment (
        enrollment_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        course_id INT NOT NULL,
        enrollment_status NVARCHAR(50) NOT NULL CHECK (enrollment_status IN ('PENDING', 'ACTIVE', 'COMPLETED', 'DROPPED')),
        enrollment_date DATETIME2 DEFAULT GETDATE(),
        FOREIGN KEY (user_id) REFERENCES users.Users(user_id) ON DELETE CASCADE,
        FOREIGN KEY (course_id) REFERENCES courses.Course(course_id) ON DELETE CASCADE
    );
    
    CREATE INDEX idx_enrollment_user ON access.Enrollment(user_id);
    CREATE INDEX idx_enrollment_course ON access.Enrollment(course_id);
END
GO

-- AccessCode Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'access' AND t.name = 'AccessCode')
BEGIN
    CREATE TABLE access.AccessCode (
        access_code_id INT IDENTITY(1,1) PRIMARY KEY,
        code_value NVARCHAR(100) UNIQUE NOT NULL,
        course_id INT NOT NULL,
        generated_by INT NOT NULL,
        used_by INT,
        code_created_at DATETIME2 DEFAULT GETDATE(),
        code_expires_at DATETIME2,
        code_is_used BIT NOT NULL DEFAULT 0,
        FOREIGN KEY (course_id) REFERENCES courses.Course(course_id) ON DELETE CASCADE,
        FOREIGN KEY (generated_by) REFERENCES users.Users(user_id) ON DELETE NO ACTION,
        FOREIGN KEY (used_by) REFERENCES users.Users(user_id) ON DELETE NO ACTION
    );
    
    CREATE INDEX idx_access_code_value ON access.AccessCode(code_value);
    CREATE INDEX idx_access_code_course ON access.AccessCode(course_id);
END
GO

-- SystemLog Table
IF NOT EXISTS (SELECT * FROM sys.tables t JOIN sys.schemas s ON t.schema_id = s.schema_id WHERE s.name = 'logs' AND t.name = 'SystemLog')
BEGIN
    CREATE TABLE logs.SystemLog (
        log_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT,
        log_action NVARCHAR(255) NOT NULL,
        log_description NVARCHAR(MAX),
        log_timestamp DATETIME2 DEFAULT GETDATE(),
        FOREIGN KEY (user_id) REFERENCES users.Users(user_id) ON DELETE SET NULL
    );
    
    CREATE INDEX idx_system_log_user ON logs.SystemLog(user_id);
    CREATE INDEX idx_system_log_timestamp ON logs.SystemLog(log_timestamp);
END
GO

-- ======================================
-- Completion Message
-- ======================================
PRINT 'Database initialization completed successfully!';
GO
