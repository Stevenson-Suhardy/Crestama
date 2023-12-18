package com.crestama.crestamawebsite.repository;

import com.crestama.crestamawebsite.entity.SalesReportForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SalesReportFormRepository extends JpaRepository<SalesReportForm, Long> {
    @Query(value = "SELECT * FROM sales_report_form WHERE start_activity_date > :start AND start_activity_date < :end",
            nativeQuery = true)
    List<SalesReportForm> findByDateRange(@Param("start") Date start, @Param("end") Date end);


}
