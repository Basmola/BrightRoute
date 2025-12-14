 

function setLogLevel() { }
setLogLevel('Debug');

const appId = typeof __app_id !== 'undefined' ? __app_id : 'default-app-id';
const firebaseConfig = JSON.parse(typeof __firebase_config !== 'undefined' ? __firebase_config : '{}');
const initialAuthToken = typeof __initial_auth_token !== 'undefined' ? __initial_auth_token : null;

let app;
let db;
let auth;
let userId;

let userRole = null;
let isSidebarOpen = localStorage.getItem('isSidebarOpen') !== 'false';
let currentLectureId = null;

let registeredUsers = [];
registeredUsers.push({ id: 1, email: 'student@br.com', password: '123', role: 'student', firstName: 'Basmala', lastName: 'Ali', imageUrl: null, phoneNumber: '0100100100' });
registeredUsers.push({ id: 2, email: 'admin12@gmail.com', password: 'admin123', role: 'admin', firstName: 'Admin', lastName: 'Manager', imageUrl: null, phoneNumber: '0100100101' });
registeredUsers.push({ id: 3, email: 'test@user.com', password: '123', role: 'student', firstName: 'Test', lastName: 'User', imageUrl: null, phoneNumber: '0123456789' });
const mockCourses = [
    {
        id: 'C1',
        title: 'Advanced Web Development with React',
        instructor: 'Fatma Ali',
        lectures: 15,
        status: 'Published',
        tag: 'primary',
        imageText: 'DEV',
        levelTag: 'primary'  
    },
    {
        id: 'C2',
        title: 'Database Design and SQL Mastery',
        instructor: 'Dr. Ahmed Hassan',
        lectures: 10,
        status: 'Draft',
        tag: 'secondary',
        imageText: 'SQL',
        levelTag: 'secondary'  
    },
    {
        id: 'C3',
        title: 'Python for Data Science',
        instructor: 'Mr. Waleed',
        lectures: 22,
        status: 'Published',
        tag: 'accent',
        imageText: 'PY',
        levelTag: 'highschool'  
    },
];

const mockLevels = [
    { level: 1, title: 'Foundations and Launchpad', desc: 'This level is for beginners. You will learn the core concepts and essential tools needed to start your career path.', tag: 'secondary', courses: 5, lectures: '20+', projects: 'Simple Practical Projects' },
    { level: 2, title: 'Application and Specialization', desc: 'Refine your skills through intermediate courses. You will start building more complex applications and understanding specializations.', tag: 'primary', courses: 8, lectures: '40+', projects: 'Real-World Case Studies' },
    { level: 3, title: 'Mastery and Leadership', desc: 'For professionals. Focus on advanced topics, engage in large team projects, and prepare for the job market.', tag: 'accent', courses: 4, lectures: '20+', projects: 'Capstone Graduation Project' },
];

const validAccessCodes = ['LEVEL1JS', 'LEVEL2REACT', 'EXPERTDB'];
let usedAccessCodes = [];

const mockQuizQuestions = [
    { questionNumber: 1, question: "What is the primary language used for styling web pages?", answerOptions: [{ text: "HTML", rationale: "HTML is for structure, not styling.", isCorrect: false }, { text: "CSS", rationale: "CSS (Cascading Style Sheets) is explicitly designed for styling web documents.", isCorrect: true }, { text: "JavaScript", rationale: "JavaScript is primarily used for interactivity and dynamic behavior.", isCorrect: false }, { text: "Python", rationale: "Python is a general-purpose programming language often used on the server side.", isCorrect: false }] },
    { questionNumber: 2, question: "Which tag is used to define an internal style sheet?", answerOptions: [{ text: "<css>", rationale: "The '<css>' tag is not a standard HTML element.", isCorrect: false }, { text: "<script>", rationale: "The '<script>' tag is used for JavaScript.", isCorrect: false }, { text: "<style>", rationale: "The '<style>' tag is the correct way to embed internal styles in an HTML document.", isCorrect: true }, { text: "<link>", rationale: "The '<link>' tag is typically used to reference external style sheets.", isCorrect: false }] },
    { questionNumber: 3, question: "What property is used to change the background color of an element?", answerOptions: [{ text: "color", rationale: "The 'color' property changes text color, not background color.", isCorrect: false }, { text: "bgcolor", rationale: "This attribute is obsolete in modern CSS/HTML.", isCorrect: false }, { text: "background-color", rationale: "The 'background-color' property is the standard way to set an element's background color in CSS.", isCorrect: true }, { text: "background", rationale: "The 'background' shorthand property could work, but 'background-color' is more specific.", isCorrect: false }] },
    { questionNumber: 4, question: "In JavaScript, how do you declare a variable that cannot be reassigned?", answerOptions: [{ text: "var", rationale: "'var' allows both reassignment and redeclaration.", isCorrect: false }, { text: "let", rationale: "'let' allows reassignment but prevents redeclaration in the same scope.", isCorrect: false }, { text: "const", rationale: "The 'const' keyword is used to declare a block-scoped variable that must be initialized and cannot be reassigned.", isCorrect: true }, { text: "static", rationale: "'static' is used for class properties or methods, not general variable declaration for immutability.", isCorrect: false }] },
    { questionNumber: 5, question: "Which CSS property controls the spacing between lines of text?", answerOptions: [{ text: "word-spacing", rationale: "'word-spacing' controls space between words.", isCorrect: false }, { text: "letter-spacing", rationale: "'letter-spacing' controls space between letters.", isCorrect: false }, { text: "line-height", rationale: "The 'line-height' property defines the amount of space used for lines of text.", isCorrect: true }, { text: "text-indent", rationale: "'text-indent' controls indentation of the first line.", isCorrect: false }] },
    { questionNumber: 6, question: "Which of the following is NOT a JavaScript data type?", answerOptions: [{ text: "Boolean", rationale: "Boolean is a fundamental JavaScript data type.", isCorrect: false }, { text: "Float", rationale: "JavaScript uses 'Number' for both integers and floating-point numbers.", isCorrect: true }, { text: "Symbol", rationale: "Symbol is a valid primitive data type introduced in ES6.", isCorrect: false }, { text: "BigInt", rationale: "BigInt is a valid primitive data type for handling large integers.", isCorrect: false }] },
    { questionNumber: 7, question: "How do you correctly link an external CSS file named 'styles.css' to an HTML document?", answerOptions: [{ text: "<style src='styles.css'>", rationale: "The 'style' tag is for internal CSS and uses the 'type' attribute, not 'src'.", isCorrect: false }, { text: "<link href='styles.css' rel='stylesheet'>", rationale: "The 'link' tag with 'href' and 'rel=stylesheet' is the correct method for external CSS files.", isCorrect: true }, { text: "<stylesheet>styles.css</stylesheet>", rationale: "This is not a standard HTML element.", isCorrect: false }, { text: "<import style='styles.css'>", rationale: "The '@import' rule is used within CSS itself, not directly in HTML.", isCorrect: false }] },
    { questionNumber: 8, question: "Which operator is used for strictly checking both value and type in JavaScript?", answerOptions: [{ text: "==", rationale: "'==' checks only the value, allowing type coercion.", isCorrect: false }, { text: "!==", rationale: "'!==' is the strict not equal operator.", isCorrect: false }, { text: "===", rationale: "'===' is the strict equality operator, checking both value and data type.", isCorrect: true }, { text: "!= ", rationale: "'!=' checks only the value (non-strict not equal).", isCorrect: false }] },
    { questionNumber: 9, question: "To apply CSS styles to only one specific HTML element, which selector should you use?", answerOptions: [{ text: "Class selector", rationale: "Class selectors can target multiple elements.", isCorrect: false }, { text: "Tag selector", rationale: "Tag selectors target all elements of a given tag name.", isCorrect: false }, { text: "ID selector", rationale: "The ID selector targets one unique element per page, making it ideal for specific styling.", isCorrect: true }, { text: "Universal selector", rationale: "The universal selector targets all elements.", isCorrect: false }] },
];

function findUserByEmail(email) {
    return registeredUsers.find(u => u.email === email);
}

function registerUser(user) {
    if (findUserByEmail(user.email)) {
        return { success: false, message: "Email already registered" };
    }
    user.id = registeredUsers.length + 1;
    registeredUsers.push(user);
    return { success: true, message: "User registered successfully" };
}

function loginUser(email, password) {
    const user = findUserByEmail(email);
    if (!user) return { success: false, message: "User not found" };
    if (user.password !== password) return { success: false, message: "Incorrect password" };
    return { success: true, message: "Login successful", user };
}

