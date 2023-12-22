package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.component.TokenManager;
import com.crestama.crestamawebsite.entity.Role;
import com.crestama.crestamawebsite.entity.SalesReportForm;
import com.crestama.crestamawebsite.entity.User;
import com.crestama.crestamawebsite.service.companyType.CompanyTypeService;
import com.crestama.crestamawebsite.service.prospect.ProspectService;
import com.crestama.crestamawebsite.service.salesReportForm.SalesReportFormService;
import com.crestama.crestamawebsite.service.user.UserService;
import com.crestama.crestamawebsite.service.validation.SalesFormValidation;
import com.crestama.crestamawebsite.utility.S3Util;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/salesForm")
public class SalesFormController {
    private SalesReportFormService salesReportFormService;
    private CompanyTypeService companyTypeService;
    private ProspectService prospectService;
    private TokenManager tokenManager;
    private UserService userService;
    private SalesFormValidation salesFormValidation;
    private final int HEADINGS = 15;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm");

    @Autowired
    public SalesFormController(SalesReportFormService salesReportFormService, CompanyTypeService companyTypeService,
                               ProspectService prospectService, TokenManager tokenManager,
                               UserService userService, SalesFormValidation salesFormValidation) {
        this.salesReportFormService = salesReportFormService;
        this.companyTypeService = companyTypeService;
        this.prospectService = prospectService;
        this.tokenManager = tokenManager;
        this.userService = userService;
        this.salesFormValidation = salesFormValidation;
    }

    @GetMapping("/salesActivities")
    public String listOfSalesActivity(Model model) {
        // Getting the session
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);

        if (session != null && session.getAttribute("token") != null) {
            // Getting user using token
            String token = (String) session.getAttribute("token");
            String userEmail = tokenManager.getUsernameFromToken(token.substring(7));

            User user = userService.findByEmail(userEmail);

            boolean isAdmin = false;

            for (Role role : user.getRoles()) {
                if (role.getName().equals("ROLE_ADMIN")) {
                    isAdmin = true;
                    break;
                }
            }

            if (isAdmin) {
                model.addAttribute("salesActivities", salesReportFormService.findAll());
            }
            else {
                model.addAttribute("salesActivities", salesReportFormService.findByUser(user.getId()));
            }
        }

        return "salesForm/listOfSalesActivity";
    }

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("salesReportForm", new SalesReportForm());

        model.addAttribute("companyTypes", companyTypeService.findAll());
        model.addAttribute("prospects", prospectService.findAll());

        return "salesForm/salesForm";
    }

    @GetMapping("/editSalesActivity/{id}")
    public String editForm(Model model, @PathVariable Long id) {
        model.addAttribute("salesReportForm", salesReportFormService.findById(id));

        model.addAttribute("companyTypes", companyTypeService.findAll());
        model.addAttribute("prospects", prospectService.findAll());

        return "salesForm/salesForm";
    }

    @PostMapping("/save")
    public String saveForm(@ModelAttribute @Valid SalesReportForm salesReportForm,
                           BindingResult result,
                           HttpServletResponse response, Model model) throws IOException {
        // Set the submission date
        salesReportForm.setSubmissionDate(LocalDateTime.now());
        // Getting the session
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        // Validate the form
        String err = salesFormValidation.validateSalesForm(salesReportForm);

        if (session != null && session.getAttribute("token") != null) {
            // Getting user using token
            String token = (String) session.getAttribute("token");
            String userEmail = tokenManager.getUsernameFromToken(token.substring(7));

            // Find the user
            User user = userService.findByEmail(userEmail);

            // Set the user
            salesReportForm.setUser(user);

            // Check if there are any errors
            if (!err.isEmpty()) {
                ObjectError error = new ObjectError("compareDateError", err);
                result.addError(error);
            }
            if (result.hasErrors()) {
                model.addAttribute("companyTypes", companyTypeService.findAll());
                model.addAttribute("prospects", prospectService.findAll());
                // Return to the form if there are any
                return "salesForm/salesForm";
            }
            // Capitalize Members
            salesReportForm.setActivityType(salesReportForm.getActivityType().toUpperCase());
            salesReportForm.setCompanyName(salesReportForm.getCompanyName().toUpperCase());
            salesReportForm.setContactPersonName(salesReportForm.getContactPersonName().toUpperCase());
            salesReportForm.setCity(salesReportForm.getCity().toUpperCase());

            // Save the form to the database
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
                           @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model)
            throws IOException {
        File currDir = new File("sales-reports");
        String path = currDir.getAbsolutePath();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d, yyyy");
        String fileLocation = dtf.format(LocalDateTime.now()) + ".xlsx";

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet ws = formHeadings(workbook);

        List<SalesReportForm> listSalesReportForm = null;

        if (start == null && end == null) {
            listSalesReportForm = salesReportFormService.findAll();
        }
        else if (start == null) {
            listSalesReportForm = salesReportFormService.findByEndDate(end);
        }
        else if (end == null) {
            listSalesReportForm = salesReportFormService.findByStartDate(start);
        }
        else {
            listSalesReportForm = salesReportFormService.findByDateRange(start, end);
        }

        String[][] salesReportFormArray = listSalesReportForm.stream().map(s -> new String[]
                {
                        s.getSubmissionDate().format(formatter),
                        s.getUser().getId().toString(),
                        s.getStartActivityDate().format(formatter),
                        s.getEndActivityDate().format(formatter),
                        s.getActivityType(),
                        s.getCompanyType().getType(),
                        s.getCompanyName(),
                        s.getStreetAddress(),
                        s.getCity(),
                        s.getContactPersonName(),
                        s.getContactPersonPhone(),
                        s.getContactPersonEmail(),
                        s.getDetailedActivity(),
                        s.getProspect().getId() + ": " + s.getProspect().getDescription(),
                        s.getComments(),
                }).toArray(String[][]::new);

        XSSFRow row = ws.createRow(1);
        XSSFCell cell = row.createCell(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont fontHeading = workbook.createFont();
        fontHeading.setFontName("Calibri");
        fontHeading.setFontHeightInPoints((short) 9);
        cellStyle.setFont(fontHeading);

        cell.setCellStyle(cellStyle);

        for (int i = 0; i < listSalesReportForm.size(); i++) {
            row = ws.createRow(i+1);
            for (int j = 0; j < HEADINGS; j++) {
                cell = row.createCell(j);

                cell.setCellValue(salesReportFormArray[i][j]);
            }
        }

        for (int i = 0; i < HEADINGS; i++) {
            ws.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();
            outputStream.close();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        S3Util.uploadReport("sales-reports/" + fileLocation, outputStream);

        model.addAttribute("filePath", "/salesForm/download/" + fileLocation);

        return "salesForm/downloadForm";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = S3Util.downloadReport("sales-reports/" + fileName);
        final ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; fileName=\"" + fileName + "\"")
                .body(resource);
    }

    public XSSFSheet formHeadings(XSSFWorkbook workbook) {
        final String[] headings = {
                "Submission Date",
                "Kode Sales",
                "Tanggal & Jam Mulai Aktivitas",
                "Tanggal & Jam Selesai Aktivitas",
                "Jenis Aktivitas",
                "Jenis Perusahaan Customer",
                "Nama Lengkap Perusahaan Customer",
                "Street Address",
                "City",
                "Nama Contact Person",
                "Nomor HP Contact Person",
                "Email Customer (Perusahaan)",
                "Detail Aktivitas",
                "Prospek",
                "Catatan, Rencana Project, Rencana Follow Up, dll"
        };

        XSSFSheet ws = workbook.createSheet("Sheet1");

        XSSFCellStyle cellStyle = workbook.createCellStyle();

        XSSFFont fontHeading = workbook.createFont();
        fontHeading.setBold(true);
        fontHeading.setFontName("Calibri");
        fontHeading.setFontHeightInPoints((short) 9);

        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        cellStyle.setFont(fontHeading);


        XSSFRow headerRow =  ws.createRow(0);
        XSSFCell cell;

        for (int i = 0; i < HEADINGS; i++) {
            cell = headerRow.createCell(i);
            cell.setCellValue(headings[i]);
            cell.setCellStyle(cellStyle);
        }

        return ws;
    }
}
