# Communication LTD Cyber Project - Secure Version

## Team Members

- Full Name: דנה סבנקו | ID: 323082115
- Full Name: ענבר רחנמי | ID: 318913670
- Full Name: רותם אלוני | ID: 213388028
- Full Name: חנה שמואל | ID: 214275703
- Full Name: איתי קריטמלר | ID: 322567264

This repository is the secure, non-vulnerable version of the Communication LTD coursework project.

It implements the Part A secure development requirements and also demonstrates the secure fixes required for Part B:

- Stored XSS protection using HTML/special-character encoding.
- SQL Injection protection using `PreparedStatement` parameters.

SQL Injection and XSS payload instructions are intentionally not included in this secure repository. The intentionally vulnerable demo version is maintained in a separate repository.

## How to run locally

1. Install Java 17 and Maven.
2. Copy local config templates:
   - `src/main/resources/mail-config.example.properties` to `src/main/resources/mail-config.properties`
   - `src/main/resources/security-config.example.properties` to `src/main/resources/security-config.properties`
3. Fill local values only in the non-example files. Do not commit real secrets.
4. Run:

```bash
mvn jetty:run
```

5. Open the app in the browser, usually:

```text
http://localhost:8080/
```

The database is initialized on startup. `/init-db` is also available if manual initialization is needed.

## Main routes

- `/register` - creates a new user with username, email, and password.
- `/login` - authenticates a user.
- `/customer-details` - adds customer details after login.
- `/dashboard.jsp` - displays the logged-in user and newly added customer.
- `/change-password` - changes password after verifying the current password.
- `/forgot-password` - starts password reset by email code.
- `/verify-code` - verifies the reset code.
- `/reset-password` - sets a new password after reset-code verification.
- `/logout` - invalidates the current session.

## Security features

- Password policy is read from `src/main/resources/password-config.properties`.
- Passwords are stored with HMAC-SHA256 and a per-password salt.
- HMAC secret is loaded from `HMAC_SECRET` or `src/main/resources/security-config.properties`.
- Password history prevents reuse of the last configured number of passwords. The current config uses 3.
- Login attempt limit is read from `login.max.attempts`. The current config uses 3.
- Users are locked after the configured number of failed login attempts.
- Failed login counter resets after a successful login.
- Successful login renews the HTTP session to reduce session fixation risk.
- SQL access in the secure implementation uses `PreparedStatement` parameters for user-controlled values.
- Dashboard output is HTML-encoded to prevent stored XSS.
- Forgot-password reset values are SHA-1 based, emailed to the user, stored as hashed values, and limited to a short validity window.
- Runtime database and local secret config files are ignored by git.

## Configuration files

Tracked templates:

- `src/main/resources/mail-config.example.properties`
- `src/main/resources/security-config.example.properties`
- `src/main/resources/password-config.properties`

Local files not tracked:

- `communication_ltd.db`
- `src/main/resources/mail-config.properties`
- `src/main/resources/security-config.properties`

Use the `.example.properties` files as templates and keep real secrets only in the local ignored files or environment variables:

- `MAIL_USERNAME`
- `MAIL_APP_PASSWORD`
- `MAIL_SMTP_HOST`
- `MAIL_SMTP_PORT`
- `HMAC_SECRET`

## Notes

This repository is for the secure Part A implementation and the secure Part B fixes only. Attack-demonstration routes, payload instructions, and intentionally vulnerable code are not included here.
