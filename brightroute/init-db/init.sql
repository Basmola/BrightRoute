-- =====================================================
-- BrightRoute Database Initialization Script
-- Database: SCM (Student Course Management)
-- SQL Server 2022 Express
-- =====================================================

USE master;
GO

-- Create database if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'SCM')
BEGIN
    CREATE DATABASE SCM;
    PRINT 'Database SCM created successfully.';
END
ELSE
BEGIN
    PRINT 'Database SCM already exists.';
END
GO

USE SCM;
GO

-- =====================================================
-- Create Schemas
-- =====================================================

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'users')
BEGIN
    EXEC('CREATE SCHEMA users');
    PRINT 'Schema users created successfully.';
END
GO

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'courses')
BEGIN
    EXEC('CREATE SCHEMA courses');
    PRINT 'Schema courses created successfully.';
END
GO

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'lectures')
BEGIN
    EXEC('CREATE SCHEMA lectures');
    PRINT 'Schema lectures created successfully.';
END
GO

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'quiz')
BEGIN
    EXEC('CREATE SCHEMA quiz');
    PRINT 'Schema quiz created successfully.';
END
GO

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'access')
BEGIN
    EXEC('CREATE SCHEMA access');
    PRINT 'Schema access created successfully.';
END
GO

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'logs')
BEGIN
    EXEC('CREATE SCHEMA logs');
    PRINT 'Schema logs created successfully.';
END
GO

-- =====================================================
-- Create Tables
-- =====================================================

-- users.Users Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'users.Users') AND type in (N'U'))
BEGIN
    CREATE TABLE users.Users (
        user_id INT IDENTITY(1,1) PRIMARY KEY,
        user_first_name NVARCHAR(100) NOT NULL,
        user_last_name NVARCHAR(100) NOT NULL,
        user_email NVARCHAR(255) NOT NULL UNIQUE,
        user_phone_number NVARCHAR(20) NOT NULL,
        user_password_hash NVARCHAR(255) NOT NULL,
        user_role NVARCHAR(50) NOT NULL CHECK (user_role IN ('STUDENT', 'INSTRUCTOR', 'ADMIN')),
        user_account_status NVARCHAR(50) NOT NULL CHECK (user_account_status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),
        user_image VARBINARY(MAX),
        user_created_at DATETIME NOT NULL DEFAULT GETDATE()
    );
    PRINT 'Table users.Users created successfully.';
END
GO

-- users.Student Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'users.Student') AND type in (N'U'))
BEGIN
    CREATE TABLE users.Student (
        student_id INT PRIMARY KEY,
        student_national_id NVARCHAR(50) UNIQUE,
        parent_number BIGINT,
        student_id_type NVARCHAR(50),
        student_national_id_front VARBINARY(MAX),
        student_birth_certificate VARBINARY(MAX),
        student_level_of_education NVARCHAR(100),
        student_created_at DATETIME NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_Student_User FOREIGN KEY (student_id) REFERENCES users.Users(user_id) ON DELETE CASCADE
    );
    PRINT 'Table users.Student created successfully.';
END
GO

-- courses.Course Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'courses.Course') AND type in (N'U'))
BEGIN
    CREATE TABLE courses.Course (
        course_id INT IDENTITY(1,1) PRIMARY KEY,
        course_title NVARCHAR(200) NOT NULL,
        course_description NVARCHAR(MAX) NOT NULL,
        course_image_cover VARBINARY(MAX),
        course_instructor NVARCHAR(150) NOT NULL,
        course_number_of_lectures INT NOT NULL DEFAULT 0,
        level_id INT,
        course_created_at DATETIME NOT NULL DEFAULT GETDATE()
    );
    PRINT 'Table courses.Course created successfully.';
END
GO

-- courses.Lecture Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'courses.Lecture') AND type in (N'U'))
BEGIN
    CREATE TABLE courses.Lecture (
        lecture_id INT IDENTITY(1,1) PRIMARY KEY,
        course_id INT NOT NULL,
        lecture_title NVARCHAR(200) NOT NULL,
        lecture_description NVARCHAR(MAX),
        lecture_image VARBINARY(MAX),
        lecture_order_number INT NOT NULL,
        lecture_created_at DATETIME NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_Lecture_Course FOREIGN KEY (course_id) REFERENCES courses.Course(course_id) ON DELETE CASCADE
    );
    PRINT 'Table courses.Lecture created successfully.';
END
GO

-- courses.CourseSubscription Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'courses.CourseSubscription') AND type in (N'U'))
BEGIN
    CREATE TABLE courses.CourseSubscription (
        id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        course_id INT NOT NULL,
        subscription_subscribed_at DATETIME NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_CourseSubscription_User FOREIGN KEY (user_id) REFERENCES users.Users(user_id) ON DELETE CASCADE,
        CONSTRAINT FK_CourseSubscription_Course FOREIGN KEY (course_id) REFERENCES courses.Course(course_id) ON DELETE CASCADE
    );
    PRINT 'Table courses.CourseSubscription created successfully.';
END
GO

-- lectures.LecturePart Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'lectures.LecturePart') AND type in (N'U'))
BEGIN
    CREATE TABLE lectures.LecturePart (
        part_id INT IDENTITY(1,1) PRIMARY KEY,
        lecture_id INT NOT NULL,
        part_type NVARCHAR(50) NOT NULL CHECK (part_type IN ('QUIZ', 'VIDEO', 'PDF', 'TEXT')),
        part_content_url NVARCHAR(500),
        part_description NVARCHAR(MAX),
        part_order_number INT NOT NULL,
        CONSTRAINT FK_LecturePart_Lecture FOREIGN KEY (lecture_id) REFERENCES courses.Lecture(lecture_id) ON DELETE CASCADE
    );
    PRINT 'Table lectures.LecturePart created successfully.';
END
GO

-- quiz.Quiz Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'quiz.Quiz') AND type in (N'U'))
BEGIN
    CREATE TABLE quiz.Quiz (
        quiz_id INT IDENTITY(1,1) PRIMARY KEY,
        part_id INT NOT NULL UNIQUE,
        quiz_title NVARCHAR(200),
        quiz_passing_score INT,
        quiz_created_at DATETIME NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_Quiz_LecturePart FOREIGN KEY (part_id) REFERENCES lectures.LecturePart(part_id) ON DELETE CASCADE
    );
    PRINT 'Table quiz.Quiz created successfully.';
END
GO

-- quiz.QuizQuestion Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'quiz.QuizQuestion') AND type in (N'U'))
BEGIN
    CREATE TABLE quiz.QuizQuestion (
        question_id INT IDENTITY(1,1) PRIMARY KEY,
        quiz_id INT NOT NULL,
        question_text NVARCHAR(MAX),
        question_image VARBINARY(MAX),
        CONSTRAINT FK_QuizQuestion_Quiz FOREIGN KEY (quiz_id) REFERENCES quiz.Quiz(quiz_id) ON DELETE CASCADE
    );
    PRINT 'Table quiz.QuizQuestion created successfully.';
END
GO

-- quiz.QuestionsChoice Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'quiz.QuestionsChoice') AND type in (N'U'))
BEGIN
    CREATE TABLE quiz.QuestionsChoice (
        choice_id INT IDENTITY(1,1) PRIMARY KEY,
        question_id INT NOT NULL,
        choice_text NVARCHAR(MAX) NOT NULL,
        choice_image VARBINARY(MAX),
        choice_is_correct BIT NOT NULL,
        choice_explanation_text NVARCHAR(MAX),
        choice_explanation_image VARBINARY(MAX),
        CONSTRAINT FK_QuestionsChoice_Question FOREIGN KEY (question_id) REFERENCES quiz.QuizQuestion(question_id) ON DELETE CASCADE
    );
    PRINT 'Table quiz.QuestionsChoice created successfully.';
END
GO

-- quiz.StudentQuizSubmission Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'quiz.StudentQuizSubmission') AND type in (N'U'))
BEGIN
    CREATE TABLE quiz.StudentQuizSubmission (
        submission_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        quiz_id INT NOT NULL,
        submission_score INT,
        submission_is_passed BIT,
        submission_submitted_at DATETIME NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_StudentQuizSubmission_User FOREIGN KEY (user_id) REFERENCES users.Users(user_id) ON DELETE CASCADE,
        CONSTRAINT FK_StudentQuizSubmission_Quiz FOREIGN KEY (quiz_id) REFERENCES quiz.Quiz(quiz_id) ON DELETE NO ACTION
    );
    PRINT 'Table quiz.StudentQuizSubmission created successfully.';
END
GO

-- quiz.StudentQuestionsAnswer Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'quiz.StudentQuestionsAnswer') AND type in (N'U'))
BEGIN
    CREATE TABLE quiz.StudentQuestionsAnswer (
        answer_id INT IDENTITY(1,1) PRIMARY KEY,
        submission_id INT NOT NULL,
        question_id INT NOT NULL,
        choice_id INT NOT NULL,
        is_correct BIT NOT NULL,
        CONSTRAINT FK_StudentQuestionsAnswer_Submission FOREIGN KEY (submission_id) REFERENCES quiz.StudentQuizSubmission(submission_id) ON DELETE CASCADE,
        CONSTRAINT FK_StudentQuestionsAnswer_Question FOREIGN KEY (question_id) REFERENCES quiz.QuizQuestion(question_id) ON DELETE NO ACTION,
        CONSTRAINT FK_StudentQuestionsAnswer_Choice FOREIGN KEY (choice_id) REFERENCES quiz.QuestionsChoice(choice_id) ON DELETE NO ACTION
    );
    PRINT 'Table quiz.StudentQuestionsAnswer created successfully.';
END
GO

-- access.AccessCode Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'access.AccessCode') AND type in (N'U'))
BEGIN
    CREATE TABLE access.AccessCode (
        code_id INT IDENTITY(1,1) PRIMARY KEY,
        course_id INT NOT NULL,
        code_value NVARCHAR(200) NOT NULL UNIQUE,
        code_is_used BIT NOT NULL DEFAULT 0,
        code_used_at DATETIME,
        used_by INT,
        used_for_lecture INT,
        code_created_at DATETIME NOT NULL DEFAULT GETDATE(),
        code_expires_at DATETIME,
        CONSTRAINT FK_AccessCode_Course FOREIGN KEY (course_id) REFERENCES courses.Course(course_id) ON DELETE CASCADE,
        CONSTRAINT FK_AccessCode_UsedBy FOREIGN KEY (used_by) REFERENCES users.Users(user_id) ON DELETE NO ACTION,
        CONSTRAINT FK_AccessCode_Lecture FOREIGN KEY (used_for_lecture) REFERENCES courses.Lecture(lecture_id) ON DELETE NO ACTION
    );
    PRINT 'Table access.AccessCode created successfully.';
END
GO

-- access.Enrollment Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'access.Enrollment') AND type in (N'U'))
BEGIN
    CREATE TABLE access.Enrollment (
        enrollment_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        lecture_id INT NOT NULL,
        date_enrolled DATETIME NOT NULL DEFAULT GETDATE(),
        status NVARCHAR(50) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'COMPLETED')),
        CONSTRAINT FK_Enrollment_User FOREIGN KEY (user_id) REFERENCES users.Users(user_id) ON DELETE CASCADE,
        CONSTRAINT FK_Enrollment_Lecture FOREIGN KEY (lecture_id) REFERENCES courses.Lecture(lecture_id) ON DELETE CASCADE
    );
    PRINT 'Table access.Enrollment created successfully.';
END
GO

-- logs.SystemLog Table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'logs.SystemLog') AND type in (N'U'))
BEGIN
    CREATE TABLE logs.SystemLog (
        log_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT,
        action NVARCHAR(255) NOT NULL,
        details NVARCHAR(MAX),
        timestamp DATETIME NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_SystemLog_User FOREIGN KEY (user_id) REFERENCES users.Users(user_id) ON DELETE SET NULL
    );
    PRINT 'Table logs.SystemLog created successfully.';
END
GO

-- =====================================================
-- Create Indexes for Performance
-- =====================================================

-- Index on users.Users email for faster lookups
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Users_Email' AND object_id = OBJECT_ID('users.Users'))
BEGIN
    CREATE INDEX IX_Users_Email ON users.Users(user_email);
    PRINT 'Index IX_Users_Email created successfully.';
END
GO

-- Index on courses.Lecture for course lookups
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Lecture_CourseId' AND object_id = OBJECT_ID('courses.Lecture'))
BEGIN
    CREATE INDEX IX_Lecture_CourseId ON courses.Lecture(course_id);
    PRINT 'Index IX_Lecture_CourseId created successfully.';
END
GO

-- Index on quiz.QuizQuestion for quiz lookups
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_QuizQuestion_QuizId' AND object_id = OBJECT_ID('quiz.QuizQuestion'))
BEGIN
    CREATE INDEX IX_QuizQuestion_QuizId ON quiz.QuizQuestion(quiz_id);
    PRINT 'Index IX_QuizQuestion_QuizId created successfully.';
END
GO

-- Index on quiz.StudentQuizSubmission for user lookups
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_StudentQuizSubmission_UserId' AND object_id = OBJECT_ID('quiz.StudentQuizSubmission'))
BEGIN
    CREATE INDEX IX_StudentQuizSubmission_UserId ON quiz.StudentQuizSubmission(user_id);
    PRINT 'Index IX_StudentQuizSubmission_UserId created successfully.';
END
GO

-- Index on access.Enrollment for user and lecture lookups
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Enrollment_UserId' AND object_id = OBJECT_ID('access.Enrollment'))
BEGIN
    CREATE INDEX IX_Enrollment_UserId ON access.Enrollment(user_id);
    PRINT 'Index IX_Enrollment_UserId created successfully.';
END
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Enrollment_LectureId' AND object_id = OBJECT_ID('access.Enrollment'))
BEGIN
    CREATE INDEX IX_Enrollment_LectureId ON access.Enrollment(lecture_id);
    PRINT 'Index IX_Enrollment_LectureId created successfully.';
END
GO

PRINT '===============================================';
PRINT 'Database initialization completed successfully!';
PRINT '===============================================';
GO
