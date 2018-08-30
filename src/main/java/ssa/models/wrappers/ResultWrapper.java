package ssa.models.wrappers;

public class ResultWrapper implements Wrapper {

    private String result;

    public ResultWrapper(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

