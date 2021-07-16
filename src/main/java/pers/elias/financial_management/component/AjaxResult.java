package pers.elias.financial_management.component;

import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;

@Component
public class AjaxResult {
    private boolean success;
    private String message;
    private String status;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

