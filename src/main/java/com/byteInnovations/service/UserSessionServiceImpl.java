package com.byteInnovations.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byteInnovations.dto.UserLoginReportDTO;
import com.byteInnovations.model.UserSession;
import com.byteInnovations.repository.UserSessionRepository;

@Service
public class UserSessionServiceImpl implements UserSessionService{

    @Autowired
    private UserSessionRepository userSessionRepository;

    public UserSession recordLogin(Long userId) {
        LocalDateTime loginTime = LocalDateTime.now();
        UserSession session = new UserSession();
        session.setUserId(userId);
        session.setLoginTime(loginTime);
        session.setLoginDate(loginTime.toLocalDate());

        return userSessionRepository.save(session);
    }

    public void recordLogout(Long sessionId) {
        UserSession session = userSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setLogoutTime(LocalDateTime.now());
        userSessionRepository.save(session);
    }
    
    @Override
    public List<UserLoginReportDTO> getUserLoginReport(Long userId, LocalDate startDate, LocalDate endDate) {
    	List<Object[]> rows = userSessionRepository.findTotalLoginHoursPerDay(userId, startDate, endDate);

    	List<UserLoginReportDTO> reports = rows.stream()
    	    .map(row -> {
    	        java.sql.Date sqlDate = (java.sql.Date) row[0];
    	        LocalDate loginDate = sqlDate.toLocalDate();

    	        Double totalHours = 0.0;
    	        if (row[1] instanceof java.math.BigDecimal bd) {
    	            totalHours = bd.doubleValue();
    	        }

    	        return new UserLoginReportDTO(loginDate, totalHours);
    	    })
    	    .collect(Collectors.toList());


        return reports;
    }


}

