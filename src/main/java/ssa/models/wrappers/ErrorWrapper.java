package ssa.models.wrappers;

public class ErrorWrapper implements Wrapper {

    private String message;

    public ErrorWrapper(String message) {
        this.message = message;
    }

    public String getError() {
        return message;
    }

    public void setError(String error) {
        this.message = error;
    }
}
