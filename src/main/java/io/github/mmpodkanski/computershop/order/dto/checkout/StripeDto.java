package io.github.mmpodkanski.computershop.order.dto.checkout;

public class StripeDto {
    private String sessionId;

    public StripeDto() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public StripeDto(String sessionId) {
        this.sessionId = sessionId;
    }
}