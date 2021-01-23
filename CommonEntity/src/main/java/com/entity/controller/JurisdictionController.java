package com.entity.controller;

import com.entity.model.Jurisdiction;
import com.entity.model.JurisdictionFilters;
import com.entity.service.JurisdictionService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class JurisdictionController {

    @Autowired
    final JurisdictionService jurisdictionService;

    public JurisdictionController(JurisdictionService
                                          jurisdictionService) {
        this.jurisdictionService = jurisdictionService;
    }

    @ApiOperation(tags = "Jurisdiction Service",
            value = "Import Jurisdiction",
            notes = "Use this operation to add Jurisdiction")
    @RequestMapping(value = {"/jurisdiction-uploads"},
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void importJurisdictions(
            @NotNull @RequestParam("file") final MultipartFile request)
            throws IOException {
        final InputStream inpuStream = request.getInputStream();
        jurisdictionService.uploadData(inpuStream);
    }

    @ApiOperation(tags = "Jurisdiction Service",
            value = "Export Jurisdictions",
            notes = "Use this operation to export Jurisdictions")
    @RequestMapping(value = {"/export-jurisdictions"},
            method = RequestMethod.GET,
            produces = {"text/csv"})
    public void export(final HttpServletResponse httpServletResponse) throws IOException,
            CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        httpServletResponse.setContentType("text/csv");

        String filename = "Jurisdictions.csv";
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<Jurisdiction> writer =
                new StatefulBeanToCsvBuilder<Jurisdiction>(
                        httpServletResponse.getWriter())
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                        .withOrderedResults(false)
                        .build();
        writer.write(jurisdictionService.getAllJurisdictions());
    }

    @ApiOperation(tags = "Jurisdiction Service",
            value = "Get Jurisdictions",
            notes = "Use this operation to Get Jurisdictions")
    @Cacheable(value = "cachejurisdictionData")
    @RequestMapping(value = {"/search-jurisdictions"}, method = RequestMethod.POST)
    public List<Jurisdiction> getJurisdictions(
            @RequestBody final JurisdictionFilters jurisdictionFilters) {
        return jurisdictionService.searchJurisdictions(jurisdictionFilters);
    }
}
