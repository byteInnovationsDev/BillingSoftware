package com.byteInnovations.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.byteInnovations.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer>{
		
	@Query(value = "SELECT * FROM bi_ma_reportheader WHERE LOWER(rh_desc) = LOWER(?1) ORDER BY rh_dis_order ASC", nativeQuery = true)
	List<Report> findByDesc(String description);

		
}
