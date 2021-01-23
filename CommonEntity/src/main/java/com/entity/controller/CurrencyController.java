package com.entity.controller;

import com.entity.model.MetaData;
import com.entity.service.CurrencyService;
import com.entity.model.Currency;
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
public class CurrencyController {

    private static final Logger logger = LoggerFactory.getLogger(
            CurrencyController.class);

    @Autowired
    final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @ApiOperation(tags = "Currency Service", value = "Import Currencies",
            notes = "Use this operation to add Currency")
    @RequestMapping(value = {"/upload-currencies"},
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void importCurrency(
            @NotNull @RequestParam("file")
            final MultipartFile request) throws IOException {
        final InputStream inpuStream = request.getInputStream();
        currencyService.importCurrency(inpuStream);
    }

    @ApiOperation(tags = "Currency Service", value = "Get Currencies",
            notes = "Use this operation to Get Currencies")
    @Cacheable(value = "cacheCurrencyData")
    @RequestMapping(value = {"/currencies"}, method = RequestMethod.GET)
    public MetaData getCurrenciesByPage(
            @RequestParam(value = "pageNo") final Integer pageNo,
            @RequestParam(value = "pageSize") final Integer pageSize) {
        logger.debug("Hello from Logback {}");
        return currencyService.getCurrenciesByPage(pageNo, pageSize);
    }

    @ApiOperation(tags = "Currency Service", value = "Update Currency",
            notes = "Use this operation to Update Currency")
    @RequestMapping(value = "/update-currency", method = RequestMethod.PUT)
    public Currency updateCurrency(@RequestBody final Currency currency) {
        return currencyService.updateCurrency(currency);
    }

    @ApiOperation(tags = "Currency Service", value = "Delete Currency",
            notes = "Use this operation to Delete Currency")
    @RequestMapping(value = "/currency", method = RequestMethod.DELETE)
    public void deleteCurrency(@RequestParam final Integer id) {
        currencyService.deleteCurrency(id);
    }

    @ApiOperation(tags = "Currency Service", value = "Get Currencies",
            notes = "Use this operation to Sort Currenies")
    @RequestMapping(value = {"/currencies/sort"}, method = RequestMethod.GET)
    public List<Currency> sortCurrencies(
            @RequestParam(value = "sortAttr") final String sortAttr,
            @RequestParam(value = "order") final String order) {
        Boolean isDesc = order.equalsIgnoreCase("desc") ? true : false;
        return currencyService.sortCurrencies(sortAttr, isDesc);
    }

    @ApiOperation(tags = "Currency Service", value = "Get Currency",
            notes = "Use this operation to Get Currency")
    @RequestMapping(value = {"/currency/{id}"}, method = RequestMethod.GET)
    public Currency getCurrency(@PathVariable Integer id) {
        return currencyService.getCurrency(id);
    }

    @ApiOperation(tags = "Currency Service", value = "Create Currency",
            notes = "Use this operation to Create Domain")
    @RequestMapping(value = {"/currency"}, method = RequestMethod.POST)
    public void createCurrency(@RequestParam final Currency currency) {
        currencyService.createCurrency(currency);
    }

    @ApiOperation(tags = "Currency Service", value = "Create Currencies",
            notes = "Use this operation to Create Currencies")
    @RequestMapping(value = {"/currencies"}, method = RequestMethod.POST)
    public void createCurrencies(@RequestParam final List<Currency> currencies) {
        currencyService.createCurrencies(currencies);
    }

    @ApiOperation(tags = "Currency Service",
            value = "Export Currencies",
            notes = "Use this operation to export Currency")
    @RequestMapping(value = {"/export-currencies"},
            method = RequestMethod.GET,
            produces = {"text/csv"})
    public void export(final HttpServletResponse httpServletResponse) throws IOException,
            CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        httpServletResponse.setContentType("text/csv");

        String filename = "Currencies.csv";
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<Currency> writer =
                new StatefulBeanToCsvBuilder<Currency>(
                        httpServletResponse.getWriter())
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                        .withOrderedResults(false)
                        .build();
        writer.write(currencyService.getCurrencies());
    }
}
