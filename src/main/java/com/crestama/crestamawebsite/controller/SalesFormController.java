package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.component.TokenManager;
import com.crestama.crestamawebsite.entity.SalesReportForm;
import com.crestama.crestamawebsite.entity.User;
import com.crestama.crestamawebsite.service.city.CityService;
import com.crestama.crestamawebsite.service.companyType.CompanyTypeService;
import com.crestama.crestamawebsite.service.prospect.ProspectService;
import com.crestama.crestamawebsite.service.salesReportForm.SalesReportFormService;
import com.crestama.crestamawebsite.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/salesForm")
public class SalesFormController {
    private SalesReportFormService salesReportFormService;
    private CityService cityService;
    private CompanyTypeService companyTypeService;
    private ProspectService prospectService;
    private TokenManager tokenManager;
    private UserService userService;
    private final int HEADINGS = 15;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

    @Autowired
    public SalesFormController(SalesReportFormService salesReportFormService,
                               CityService cityService, CompanyTypeService companyTypeService,
                               ProspectService prospectService, TokenManager tokenManager,
                               UserService userService) {
        this.salesReportFormService = salesReportFormService;
        this.cityService = cityService;
        this.companyTypeService = companyTypeService;
        this.prospectService = prospectService;
        this.tokenManager = tokenManager;
        this.userService = userService;
    }

    @GetMapping("/salesActivities")
    public String listOfSalesActivity(Model model) {
        model.addAttribute("salesActivities", salesReportFormService.findAll());

        return "salesForm/listOfSalesActivity";
    }

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("salesReportForm", new SalesReportForm());

        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("companyTypes", companyTypeService.findAll());
        model.addAttribute("prospects", prospectService.findAll());

        return "salesForm/salesForm";
    }

    @GetMapping("/editSalesActivity/{id}")
    public String editForm(Model model, @PathVariable Long id) {
        model.addAttribute("salesReportForm", salesReportFormService.findById(id));

        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("companyTypes", companyTypeService.findAll());
        model.addAttribute("prospects", prospectService.findAll());

        return "salesForm/salesForm";
    }

    @PostMapping("/save")
    public String saveForm(@ModelAttribute SalesReportForm salesReportForm, HttpServletResponse response) throws IOException {
        salesReportForm.setSubmissionDate(LocalDateTime.now());
        // Getting the session
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);

        if (session != null && session.getAttribute("token") != null) {
            // Getting user using token
            String token = (String) session.getAttribute("token");
            String userEmail = tokenManager.getUsernameFromToken(token.substring(7));

            User user = userService.findByEmail(userEmail);

            salesReportForm.setUser(user);
            salesReportFormService.save(salesReportForm);

            return "redirect:/salesForm/salesActivities";
        }
        else {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
        return "error/exception";
    }

    @GetMapping("/deleteSalesActivity/{id}")
    public String deleteSalesActivity(@PathVariable Long id) {
        salesReportFormService.deleteById(id);

        return "redirect:/salesForm/salesActivities";
    }

    @GetMapping("/excelFormDateRange")
    public String pickDateRange() {
        return "salesForm/pickDateRange";
    }

    @GetMapping("/createExcelForm")
    public String createForm(@RequestParam(required = false, value = "start")
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                           @RequestParam(required = false, value = "end")
                           @DateTimeFormat(pattern = "yyyy-MM-dd") Date end)
            throws IOException {
        File currDir = new File("sales-reports");
        String path = currDir.getAbsolutePath();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fileLocation = dtf.format(LocalDateTime.now()) + ".xlsx";

        try (OutputStream os = Files.newOutputStream(Paths.get(path, fileLocation));
             Workbook workbook = new Workbook(os, "MyApplication", "1.0")) {
            Worksheet ws = formHeadings(workbook);

            List<SalesReportForm> listSalesReportForm = null;

            if (start == null || end == null) {
                listSalesReportForm = salesReportFormService.findAll();
            }
            else {
                listSalesReportForm = salesReportFormService.findByDateRange(start, end);
            }

            String[][] salesReportFormArray = listSalesReportForm.stream().map(s -> new String[]
                    {
                            s.getSubmissionDate().format(formatter),
                            s.getUser().getId().toString(),
                            s.getStartActivityDate().format(formatter),
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
        return "redirect:/salesForm/salesActivities";
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
