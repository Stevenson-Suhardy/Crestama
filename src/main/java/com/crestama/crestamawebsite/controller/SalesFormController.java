package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.SalesReportForm;
import com.crestama.crestamawebsite.service.city.CityService;
import com.crestama.crestamawebsite.service.companyType.CompanyTypeService;
import com.crestama.crestamawebsite.service.prospect.ProspectService;
import com.crestama.crestamawebsite.service.salesReportForm.SalesReportFormService;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping("/salesForm")
public class SalesFormController {
    private SalesReportFormService salesReportFormService;
    private CityService cityService;
    private CompanyTypeService companyTypeService;
    private ProspectService prospectService;
    private final int HEADINGS = 15;

    @Autowired
    public SalesFormController(SalesReportFormService salesReportFormService,
                               CityService cityService, CompanyTypeService companyTypeService,
                               ProspectService prospectService) {
        this.salesReportFormService = salesReportFormService;
        this.cityService = cityService;
        this.companyTypeService = companyTypeService;
        this.prospectService = prospectService;
    }

    @GetMapping("/salesActivities")
    public String listOfSalesActivity(Model model) {
        model.addAttribute("salesActivities", salesReportFormService.findAll());

        return "salesForm/listOfSalesActivity";
    }

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("salesReportForm", new SalesReportForm());

        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("companyTypes", companyTypeService.findAll());
        model.addAttribute("prospects", prospectService.findAll());

        return "salesForm/salesForm";
    }

    @PostMapping("/save")
    public String saveForm(@ModelAttribute SalesReportForm salesReportForm) {
        salesReportForm.setSubmissionDate(LocalDateTime.now());

        salesReportFormService.save(salesReportForm);

        return "redirect:/salesForm/salesForm";
    }

    @GetMapping("/createExcelForm")
    public void createForm() throws IOException {
        File currDir = new File("sales-reports");
        String path = currDir.getAbsolutePath();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fileLocation = dtf.format(LocalDateTime.now()) + ".xlsx";

        try (OutputStream os = Files.newOutputStream(Paths.get(path, fileLocation));
             Workbook workbook = new Workbook(os, "MyApplication", "1.0")) {
            Worksheet ws = formHeadings(workbook);

            List<SalesReportForm> listSalesReportForm = salesReportFormService.findAll();

            String[][] salesReportFormArray = listSalesReportForm.stream().map(s -> new String[]
                    {
                            s.getSubmissionDate().toString(),
                            "1",
                            s.getStartActivityDate().toString(),
                            s.getActivityType(),
                            s.getCompanyType().getType(),
                            s.getCompanyName(),
                            s.getStreetAddress(),
                            s.getCity().getCityName(),
                            s.getContactPersonName(),
                            s.getContactPersonPhone(),
                            "test email",
                            s.getDetailedActivity(),
                            s.getProspect().getId() + ": " + s.getProspect().getDescription(),
                            s.getComments(),
                            ""
                    }).toArray(String[][]::new);

            for (int i = 0; i < listSalesReportForm.size(); i++) {
                for (int j = 0; j < HEADINGS; j++) {
                    ws.value(i+1, j, salesReportFormArray[i][j]);
                }
            }
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
        ws.value(0, 1, "Kode Sales");
        ws.value(0, 2, "Tanggal & Jam Mulai Aktivitas");
        ws.value(0, 3, "Jenis Aktivitas");
        ws.value(0, 4, "Jenis Perusahaan Customer");
        ws.value(0, 5, "Nama Lengkap Perusahaan Customer");
        ws.value(0, 6, "Street Address");
        ws.value(0, 7, "City");
        ws.value(0, 8, "Nama Contact Person");
        ws.value(0, 9, "Nomor HP Contact Person");
        ws.value(0, 10, "Email Customer (Perusahaan)");
        ws.value(0, 11, "Detail Aktivitas");
        ws.value(0, 12, "Prospek");
        ws.value(0, 13, "Catatan");
        ws.value(0, 14, "File Upload");


        return ws;
    }
}
