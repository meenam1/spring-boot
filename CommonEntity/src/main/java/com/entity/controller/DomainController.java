package com.entity.controller;

import com.entity.model.Domain;
import com.entity.model.MetaData;
import com.entity.service.DefinitionService;
import com.entity.service.DomainService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.cache.annotation.Cacheable;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class DomainController {

    @Autowired
    final DomainService domainService;
    final DefinitionService definitionService;

    public DomainController(DomainService domainService,
                            DefinitionService definitionService) {
        this.domainService = domainService;
        this.definitionService = definitionService;
    }

    @ApiOperation(tags = "Domain Service", value = "Import Domains",
            notes = "Use this operation to add Domain")
    @RequestMapping(value = {"/upload-domain"},
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void importDomains(@NotNull @RequestParam("file")
                                  final MultipartFile request) throws IOException {
        final InputStream inpuStream = request.getInputStream();
        domainService.saveDomain(inpuStream);
    }

    @ApiOperation(tags = "Domain Service", value = "Get Domains",
            notes = "Use this operation to Get Domains")
    @Cacheable(value = "cacheDomainsData")
    @RequestMapping(value = {"/domains"}, method = RequestMethod.GET)
    public MetaData getDomains(@RequestParam(value = "pageNo") final Integer pageNo,
                               @RequestParam(value = "pageSize") final Integer pageSize) {
        return domainService.getDomains(pageNo, pageSize);
    }

    @ApiOperation(tags = "Domain Service", value = "Update Domain",
            notes = "Use this operation to Update Domain")
    @RequestMapping(value = "/update-domain", method = RequestMethod.PUT)
    public Domain updateDomain(@RequestBody final Domain domain) {
        return domainService.updateDomain(domain);
    }

    @ApiOperation(tags = "Domain Service", value = "Delete Domain",
            notes = "Use this operation to Delete Domain")
    @RequestMapping(value = "/domain", method = RequestMethod.DELETE)
    public void deleteDomain(@RequestParam final Integer id) {
        domainService.deleteDomain(id);
    }

    @ApiOperation(tags = "Domain Service", value = "Get Domains",
            notes = "Use this operation to Sort Domains")
    @Cacheable(value = "cacheDomainsData", key = "id")
    @RequestMapping(value = {"/domains/sort"}, method = RequestMethod.GET)
    public List<Domain> sortDomanis(@RequestParam(value = "sortAttr") final String sortAttr,
                                    @RequestParam(value = "order") final String order) {
        Boolean isDesc = order.equalsIgnoreCase("desc") ? true : false;
        return domainService.sortdomains(sortAttr, isDesc);
    }

    @ApiOperation(tags = "Domain Service", value = "Get Domain",
            notes = "Use this operation to Get Domain")
    @RequestMapping(value = {"/domains/{id}"}, method = RequestMethod.GET)
    public Domain getDomain(@PathVariable Integer id) {
        return domainService.findById(id);
    }

    @ApiOperation(tags = "Domain Service", value = "Create Domain",
            notes = "Use this operation to Create Domain")
    @RequestMapping(value = {"/domain"}, method = RequestMethod.POST)
    public void createDomain(@RequestParam final Domain domain) {
        domainService.createDomain(domain);
    }

    @ApiOperation(tags = "Domain Service", value = "Create Domains",
            notes = "Use this operation to Create Domains")
    @RequestMapping(value = {"/domains"}, method = RequestMethod.POST)
    public void createDomains(@RequestParam final List<Domain> domain) {
        domainService.createDomains(domain);
    }


    @ApiOperation(tags = "Domain Service",
            value = "Export Domains",
            notes = "Use this operation to export Domains")
    @RequestMapping(value = {"/export-domains"},
            method = RequestMethod.GET,
            produces = {"text/csv"})
    public void export(final HttpServletResponse httpServletResponse) throws IOException,
            CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        httpServletResponse.setContentType("text/csv");

        String filename = "Domains.csv";
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<Domain> writer =
                new StatefulBeanToCsvBuilder<Domain>(
                        httpServletResponse.getWriter())
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                        .withOrderedResults(false)
                        .build();
        writer.write(domainService.getAllDomains());
    }
}
