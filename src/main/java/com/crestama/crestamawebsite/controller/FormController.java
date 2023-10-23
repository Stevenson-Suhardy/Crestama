package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.SalesReportForm;
import com.crestama.crestamawebsite.service.city.CityService;
import com.crestama.crestamawebsite.service.companyType.CompanyTypeService;
import com.crestama.crestamawebsite.service.salesReportForm.SalesReportFormService;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


@Controller
@RequestMapping("/salesForm")
public class FormController {
    private SalesReportFormService salesReportFormService;
    private CityService cityService;
    private CompanyTypeService companyTypeService;

    @Autowired
    public FormController(SalesReportFormService salesReportFormService,
                          CityService cityService, CompanyTypeService companyTypeService) {
        this.salesReportFormService = salesReportFormService;
        this.cityService = cityService;
        this.companyTypeService = companyTypeService;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("salesReportForm", new SalesReportForm());

        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("companyTypes", companyTypeService.findAll());

        return "salesForm/salesForm";
    }

    @PostMapping("/save")
    public void createForm() throws IOException {
        File currDir = new File("sales-reports");
        String path = currDir.getAbsolutePath();
        String fileLocation = "fastexcel.xlsx";

        try (OutputStream os = Files.newOutputStream(Paths.get(path, fileLocation));
             Workbook workbook = new Workbook(os, "MyApplication", "1.0")) {
            Worksheet ws = formHeadings(workbook);
        }
    }

    public Worksheet formHeadings(Workbook workbook) {
        Worksheet ws = workbook.newWorksheet("Sheet 1");
        for (int i = 0; i <= 15; i++) {
            ws.width(i, 25);
        }

        ws.range(0, 0, 0, 15).style()
                .fontName("Calibri")
                .fontSize(9)
                .bold()
                .fillColor("83F28F")
                .set();
        ws.value(0, 0, "Submission Date");
        ws.value(0, 1, "Tanggal Pengisian");
        ws.value(0, 2, "Kode Sales");
        ws.value(0, 3, "Tanggal & Jam Mulai Aktivitas");
        ws.value(0, 4, "Jenis Aktivitas");
        ws.value(0, 5, "Jenis Perusahaan Customer");
        ws.value(0, 6, "Nama Lengkap Perusahaan Customer");
        ws.value(0, 7, "Street Address");
        ws.value(0, 8, "City");
        ws.value(0, 9, "Nama Contact Person");
        ws.value(0, 10, "Nomor HP Contact Person");
        ws.value(0, 11, "Email Customer (Perusahaan)");
        ws.value(0, 12, "Detail Aktivitas");
        ws.value(0, 13, "Prospek");
        ws.value(0, 14, "Catatan");
        ws.value(0, 15, "File Upload");


        return ws;
    }
}
