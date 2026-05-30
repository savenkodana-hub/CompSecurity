# Communication LTD Cyber Project 

## Team Members

- Full Name: דנה סבנקו | ID: 323082115
- Full Name: ענבר רחנמי | ID: 318913670
- Full Name: רותם אלוני | ID: 213388028
- Full Name: חנה שמואל | ID: 214275703
- Full Name: איתי קריטמלר | ID: 322567264


## Project Description

This repository contains the secure version of the Communication LTD Cyber Security project.

The project implements all Part A secure development requirements and includes the secure fixes required for Part B:

* SQL Injection prevention using PreparedStatement.
* Stored XSS prevention using HTML encoding.
* Password hashing using HMAC-SHA256 and per-password salts.
* Password history enforcement.
* Login attempt limitation and account lockout.
* Password reset via email verification code.

Two project versions were submitted:

* Secure Version – contains the protected implementation with all security controls and fixes applied.
* Vulnerable Version – contains the intentionally vulnerable implementation used to demonstrate SQL Injection and Stored XSS attacks before applying the security fixes.

The intentionally vulnerable version is maintained in a separate ZIP file.

---

## How to Run the Project

### Prerequisites

* Java 17
* Maven

### Configuration

All required configuration files are already included in the submitted ZIP file.

No additional setup is required.

Simply extract the project and run it using Maven:

```bash
mvn jetty:run
```

The application will use the included email and security configuration files automatically.

```text
src/main/resources/mail-config.properties
src/main/resources/security-config.properties
```

### Running

Run:

```bash
mvn jetty:run
```

Open:

```text
http://localhost:8080/
```

The database is initialized automatically on startup.

If required, database initialization can be triggered manually using:

```text
/init-db
```

---

## How to Use the System

### Register

1. Open:

```text
/register
```

2. Enter:

   * Username
   * Email
   * Password

3. Submit the form.

The system validates the password according to the configured password policy and stores a hashed password with a unique salt.

---

### Login

1. Open:

```text
/login
```

2. Enter email and password.

3. Submit the form.

After successful authentication a new HTTP session is created.

After the configured number of failed login attempts the account is locked.

---

### Add Customer

1. Log in.
2. Navigate to:

```text
/customer-details
```

3. Enter customer details.
4. Submit the form.

The customer information is stored in the database and displayed on the dashboard.

---

### Dashboard

Open:

```text
/dashboard.jsp
```

The dashboard displays:

* Logged-in username
* Customer added during the current session
* Password change notifications

All displayed user-generated content is HTML encoded.

---

### Change Password

1. Open:

```text
/change-password
```

2. Enter:

   * Current password
   * New password

3. Submit the form.

The system validates password policy requirements and prevents reuse of recently used passwords.

---

### Forgot Password

1. Open:

```text
/forgot-password
```

2. Enter the registered email address.

3. A reset code is sent to the user's email.

---

### Verify Reset Code

1. Open:

```text
/verify-code
```

2. Enter:

   * Email
   * Verification code

3. Submit.

If the code is valid the user is redirected to password reset.

---

### Reset Password

1. Open:

```text
/reset-password
```

2. Enter:

   * New password
   * Confirm password

3. Submit.

The password is updated and the reset token is invalidated.

---

### Logout

Open:

```text
/logout
```

The current session is invalidated and the user is returned to the login page.

---

## Security Features

* Password policy loaded from `password-config.properties`
* HMAC-SHA256 password hashing
* Unique salt per password
* Password history protection
* Login lockout after repeated failures
* Session renewal after successful login
* Email-based password reset
* Sensitive configuration excluded from Git

### Part B Vulnerabilities Demonstrated

This repository intentionally contains vulnerable implementations for educational purposes:

* SQL Injection vulnerability caused by constructing SQL queries using string concatenation instead of PreparedStatement parameters.
* Stored XSS vulnerability caused by displaying user-controlled input without HTML encoding.
* The vulnerable implementation is used only to demonstrate common web application security risks and compare them with the secure version.

---

## Configuration Files

Tracked:

```text
src/main/resources/mail-config.example.properties
src/main/resources/security-config.example.properties
src/main/resources/password-config.properties
```

```text
```

---

## Notes

To demonstrate the vulnerable behavior, use the separate vulnerable repository provided for Part B.

Run the vulnerable version using the same setup process described above, then:

* Demonstrate SQL Injection using the intentionally vulnerable login or customer-related functionality.
* Demonstrate Stored XSS by submitting customer data containing HTML/JavaScript content and viewing it on the dashboard.
* Compare the behavior with this secure version, where SQL Injection is prevented using PreparedStatement and Stored XSS is prevented using HTML encoding.

The secure repository contains only the protected implementation and is intended for demonstrating the applied security controls and fixes.

