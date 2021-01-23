package com.entity.controller;

import com.entity.model.Authority;
import com.entity.model.MetaData;
import com.entity.service.AuthorityService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AuthorityController {

    private static final Logger logger = LoggerFactory.getLogger(
            AuthorityController.class);

    @Autowired
    final AuthorityService authorityService;

    public AuthorityController
            (AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @ApiOperation(tags = "Authority Service", value = "Import Authorities",
            notes = "Use this operation to add Authority")
    @RequestMapping(value = {"/upload-authorities"},
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void importAuthorities(@NotNull @RequestParam("file")
                                      final MultipartFile request) throws IOException {
        final InputStream inpuStream = request.getInputStream();
        authorityService.importAuthority(inpuStream);
    }

    @ApiOperation(tags = "Authority Service", value = "Get Authorities",
            notes = "Use this operation to Get Authorities")
    @Cacheable(value = "cacheAuthoritiesData")
    @RequestMapping(value = {"/authorities"}, method = RequestMethod.GET)
    public MetaData getAuthoritiesByPage(
            @RequestParam(value = "pageNo") final Integer pageNo,
            @RequestParam(value = "pageSize") final Integer pageSize) {
        logger.debug("Hello from Logback {}");
        return authorityService.getAuthoritiesByPage(pageNo, pageSize);
    }

    @ApiOperation(tags = "Authority Service", value = "Update Authority",
            notes = "Use this operation to Update Authority")
    @RequestMapping(value = "/update-authority", method = RequestMethod.PUT)
    public Authority updateAuthority(@RequestBody final Authority authority) {
        return authorityService.updateAuthority(authority);
    }

    @ApiOperation(tags = "Authority Service", value = "Delete Authority",
            notes = "Use this operation to Delete Authority")
    @RequestMapping(value = "/authority", method = RequestMethod.DELETE)
    public void deleteAuthority(@RequestParam final Integer id) {

        authorityService.deleteAuthority(id);
    }

    @ApiOperation(tags = "Authority Service", value = "Get Authorities",
            notes = "Use this operation to Sort Authorities")
    @RequestMapping(value = {"/authorities/sort"}, method = RequestMethod.GET)
    public List<Authority> sortAuthorities(
            @RequestParam(value = "sortAttr") final String sortAttr,
            @RequestParam(value = "order") final String order) {
        Boolean isDesc = order.equalsIgnoreCase("desc") ? true : false;
        return authorityService.sortAuthorities(sortAttr, isDesc);
    }

    @ApiOperation(tags = "Authority Service", value = "Get Authority",
            notes = "Use this operation to Get Authority")
    @RequestMapping(value = {"/authority/{id}"}, method = RequestMethod.GET)
    public Authority getAuthority(@PathVariable final Integer id) {
        return authorityService.getAuthority(id);
    }

    @ApiOperation(tags = "Authority Service", value = "Create Authority",
            notes = "Use this operation to Create Authority")
    @RequestMapping(value = {"/authority"}, method = RequestMethod.POST)
    public void createAuthority(@RequestParam final Authority authority) {
        authorityService.createAuthority(authority);
    }

    @ApiOperation(tags = "Authority Service", value = "Create Authorities",
            notes = "Use this operation to Create Authorities")
    @RequestMapping(value = {"/authorities"}, method = RequestMethod.POST)
    public void createAuthorities(@RequestParam final List<Authority> authorities) {
        authorityService.createAuthorities(authorities);
    }

    @ApiOperation(tags = "Authority Service",
            value = "Export Authorities",
            notes = "Use this operation to export Authority")
    @RequestMapping(value = {"/export-authorities"},
            method = RequestMethod.GET,
            produces = {"text/csv"})
    public void export(final HttpServletResponse httpServletResponse) throws IOException,
            CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        httpServletResponse.setContentType("text/csv");

        String filename = "Authorities.csv";
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<Authority> writer =
                new StatefulBeanToCsvBuilder<Authority>(
                        httpServletResponse.getWriter())
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                        .withOrderedResults(false)
                        .build();
        writer.write(authorityService.getAuthorities());
    }
}
