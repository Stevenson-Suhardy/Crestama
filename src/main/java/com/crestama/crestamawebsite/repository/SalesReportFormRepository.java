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

    @Query(value = "SELECT * FROM sales_report_form WHERE start_activity_date > :start",
            nativeQuery = true)
    List<SalesReportForm> findByStartDate(@Param("start") Date start);

    @Query(value = "SELECT * FROM sales_report_form WHERE start_activity_date < :end",
            nativeQuery = true)
    List<SalesReportForm> findByEndDate(@Param("end") Date end);

    @Query(value = "SELECT * FROM sales_report_form WHERE user_id = ?", nativeQuery = true)
    List<SalesReportForm> findByUser(Long id);
}
