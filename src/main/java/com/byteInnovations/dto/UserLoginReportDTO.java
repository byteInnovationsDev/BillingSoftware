package com.byteInnovations.dto;

import java.time.LocalDate;

public class UserLoginReportDTO {
	private LocalDate loginDate;
    private Double totalHours;

    public UserLoginReportDTO(LocalDate loginDate, Double totalHours) {
        this.loginDate = loginDate;
        this.totalHours = totalHours != null ? totalHours : 0.0;
    }

    // getters and setters
    public LocalDate getSessionDate() { return loginDate; }
    public void setSessionDate(LocalDate sessionDate) { this.loginDate = sessionDate; }

    public Double getTotalHours() { return totalHours; }
    public void setTotalHours(Double totalHours) { this.totalHours = totalHours; }

    @Override
    public String toString() {
        return "UserLoginReportDTO{" +
                "sessionDate=" + loginDate +
                ", totalHours=" + totalHours +
                '}';
    }
}
