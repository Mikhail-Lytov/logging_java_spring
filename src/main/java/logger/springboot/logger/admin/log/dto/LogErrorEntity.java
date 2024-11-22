package logger.springboot.logger.admin.log.dto;

public class LogErrorEntity {
    private int status;
    private String bodyError;
    private Long timeRequest;

    public Long getTimeRequest() {
        return timeRequest;
    }

    public void setTimeRequest(Long timeRequest) {
        this.timeRequest = timeRequest;
    }

    public String getBodyError() {
        return bodyError;
    }

    public void setBodyError(String bodyError) {
        this.bodyError = bodyError;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
