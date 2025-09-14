package com.byteInnovations.service;

import java.time.LocalDate;
import java.util.List;

import com.byteInnovations.dto.UserLoginReportDTO;
import com.byteInnovations.model.UserSession;

public interface UserSessionService {
	
	public void recordLogout(Long sessionId);
	
	public UserSession recordLogin(Long userId);
	
	public List<UserLoginReportDTO> getUserLoginReport(Long userId, LocalDate startDate, LocalDate endDate);
}
