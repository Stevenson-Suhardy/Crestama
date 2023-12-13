package com.crestama.crestamawebsite.service.validation;

import com.crestama.crestamawebsite.entity.SalesReportForm;
import org.springframework.stereotype.Service;

@Service
public class SalesFormValidation {
    public String validateSalesForm(SalesReportForm salesReportForm) {
        String message = "";
        if (salesReportForm.getEndActivityDate() != null && salesReportForm.getStartActivityDate() != null) {
            if (salesReportForm.getEndActivityDate().isBefore(salesReportForm.getStartActivityDate())) {
                message = "The end activity date must not be before the start activity date.";
            }
        }
        return message;
    }
}
