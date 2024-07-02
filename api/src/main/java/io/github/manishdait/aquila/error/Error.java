package io.github.manishdait.aquila.error;

public enum Error {
    USERNMAE_NOT_FOUND("Username Not found Exception"),
    TICKET_NOT_FOUND("Ticket Not found Exception"),
    PROJECT_NOT_FOUND("Project Not found Exception"),
    COMMENT_NOT_FOUND("Comment Not found Exception"),
    DUPLICATE_VALUE_ERROR("Duplicate Value Exception"),
    INVALID_TOKEN ("Invalid Token Exception"),
    AUTHENTICATION_ERROR("Authentication Exception");

    String error;
    Error(String error) {
        this.error = error;
    }

    public String error() {
        return this.error;
    }
}
