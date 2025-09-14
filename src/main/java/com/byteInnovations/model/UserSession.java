package com.byteInnovations.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_ma_user_session")
public class UserSession {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "login_time")
    private LocalDateTime loginTime;
    @Column(name = "logout_time")
    private LocalDateTime logoutTime;
    @Column(name = "login_date")
    private LocalDate loginDate;  


	public UserSession() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserSession(Long id, Long userId, LocalDateTime loginTime, LocalDateTime logoutTime, LocalDate loginDate) {
		super();
		this.id = id;
		this.userId = userId;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
		this.loginDate = loginDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}

	public LocalDateTime getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(LocalDateTime logoutTime) {
		this.logoutTime = logoutTime;
	}

	public LocalDate getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(LocalDate loginDate) {
		this.loginDate = loginDate;
	}

	@Override
	public String toString() {
		return "UserSessionController [id=" + id + ", userId=" + userId + ", loginTime=" + loginTime + ", logoutTime="
				+ logoutTime + ", loginDate=" + loginDate + "]";
	}
    
    

}
