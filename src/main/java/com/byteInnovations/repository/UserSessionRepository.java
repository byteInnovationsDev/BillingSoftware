package com.byteInnovations.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.byteInnovations.model.UserSession;

public interface UserSessionRepository  extends JpaRepository<UserSession, Long>  {
	@Query(value = """
		   SELECT 
			  login_date,
			  ROUND(SUM(EXTRACT(EPOCH FROM (logout_time - login_time)) / 3600), 2) AS total_hours
			FROM bi_ma_user_session
			WHERE user_id = :userId
			  AND login_date BETWEEN :startDate AND :endDate
			  AND logout_time IS NOT NULL
			GROUP BY login_date
			ORDER BY login_date;
		""", nativeQuery = true)
		List<Object[]> findTotalLoginHoursPerDay(
		    @Param("userId") Long userId,
		    @Param("startDate") LocalDate startDate,
		    @Param("endDate") LocalDate endDate
		);


}
