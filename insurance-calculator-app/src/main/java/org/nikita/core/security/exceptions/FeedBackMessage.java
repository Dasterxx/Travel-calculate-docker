package org.nikita.core.security.exceptions;

public class FeedBackMessage {

    /*======================== Start User API=====================================*/
    public static final String CREATE_USER_SUCCESS = "User account created successfully";
    public static final String DELETE_USER_SUCCESS = "User account deleted successfully";
    public static final String USER_UPDATE_SUCCESS = "User updated successfully";
    public static final String USER_ACCOUNT_IS_DISABLED = "Your account is disabled";
    public static final String USER_FOUND = "User found";
    public static final String USER_NOT_FOUND = "Sorry, user not found";
    public static final String NO_USER_FOUND = "Oops!, no user found with : ";
    public static final String LOCKED_ACCOUNT_SUCCESS = "Account locked successfully";
    public static final String UNLOCKED_ACCOUNT_SUCCESS = "Account unlocked successfully";

    public static final String USER_UPDATE_ERROR = "User account not updated";
    /*======================== End User API=====================================*/


    /*======================== Start Password API=====================================*/
    public static final String PASSWORD_CHANGE_SUCCESS = "Password changed success!, you can now close this form.";
    public static final String PASSWORD_RESET_EMAIL_SENT = "A link was sent to your email, please check your to complete your password request";
    public static final String MISSING_PASSWORD = "Missing token or password";
    public static final String INVALID_RESET_TOKEN = "Invalid password reset token";
    public static final String INVALID_EMAIL = "Please, enter the email that is associated with your account.";
    public static final String PASSWORD_RESET_SUCCESS = "Your password has been reset successfully!";

    public static final String PASSWORD_SEND_ERROR = "An error occurred while sending your password reset email";
    public static final String PASSWORD_RESET_ERROR = "An error occurred while resetting your password";
    public static final String INVALID_VERIFICATION_TOKEN_ERROR = "unnable to refresh your token. Please try again.";

    /*======================== End Password API=====================================*/

    public static final String NOT_ALLOWED = "You must have a completed appointment with this veterinarian to leave a review";


    /*======================== Start General feedback =====================================*/
    public static final String SUCCESS = "Success!";
    public static final String RESOURCE_FOUND = "Resource found";
    public static final String SENDER_RECIPIENT_NOT_FOUND = "sender or recipient not found";
    public static final String ERROR = "Error occurred";
    public static final String RESOURCE_NOT_FOUND = "Resource not found";
    ;
    /*======================== End general feedback =====================================*/


    /*======================== Start authentication feedback =====================================*/
    public static final String EMPTY_PASSWORD = "All fields are required";
    public static final String INCORRECT_PASSWORD = "Incorrect password";
    public static final String PASSWORD_MISMATCH = "Password confirmation mismatch";
    public static final String AUTHENTICATION_SUCCESS = "Authentication Successful";
    public static final String ACCOUNT_DISABLED = "Sorry, your account is disabled, please contact the service desk";
    public static final Object INVALID_PASSWORD = "Invalid username or password";

    public static final String INVALID_AUTHENTICATION_ERROR = "AN ERROR HAS OCCURRED WHILE TRYING TO PROCESS YOUR REQUEST. PLEASE CONTACT THE SERVICE DECK";
    public static final String ACCOUNT_VERIFICATION_ERROR = "AN ERROR HAS OCCURRED WHILE TRYING TO VERIFY YOUR ACCOUNT. PLEASE CONTACT THE SERVICE DECK";
    public static final String REGISTRATION_ERROR = "AN ERROR HAS OCCURRED WHILE TRYING TO PROCESS YOUR REGISTRATION REQUEST. PLEASE CONTACT THE SERVICE DECK";

    /*======================== End authentication feedback =====================================*/

    /*======================== Start Token API =====================================*/
    public static final String INVALID_TOKEN = "INVALID";
    public static final String TOKEN_ALREADY_VERIFIED = "VERIFIED";
    public static final String EXPIRED_TOKEN = "EXPIRED";
    public static final String VALID_VERIFICATION_TOKEN = "VALID";
    public static final String TOKEN_VALIDATION_ERROR = "Token validation error";
    public static final String TOKEN_SAVED_SUCCESS = "Verification token saved successfully";
    public static final String TOKEN_DELETE_SUCCESS = "User token deleted successfully";
    public static final String INVALID_VERIFICATION_TOKEN = "Invalid verification token";
    public static final String NEW_VERIFICATION_TOKEN_SENT = "A new verification link has been sent to your email. Please check to complete your registration.";

    /*======================== End Token API =====================================*/

    /*======================== Start AGREEMENT API =====================================*/
    public static final String AGREEMENT_UPDATE_SUCCESS = "Agreement updated successfully";
    public static final String ALREADY_HAS_AN_AGREEMENT = "You already have an agreement, if you want to update it, please contact the service desk.";
    /*======================== End AGREEMENT API =====================================*/
}
