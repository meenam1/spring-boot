package com.entity.service;

import com.entity.model.Meta;
import com.entity.model.MetaData;
import com.entity.resolver.CsvParserHelper;
import com.entity.dao.CurrencyDAO;
import com.entity.model.Currency;
import com.entity.pdo.CurrencyPDO;
import com.entity.resolver.PaginationHelper;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    @Autowired
    final CurrencyDAO currencyDAO;

    @Autowired
    final PaginationHelper paginationHelper;

    @Autowired
    final CsvParserHelper csvParserHelper;

    public CurrencyService(CurrencyDAO currencyDAO,
                           PaginationHelper paginationHelper,
                           CsvParserHelper csvParserHelper) {
        this.currencyDAO = currencyDAO;
        this.paginationHelper = paginationHelper;
        this.csvParserHelper = csvParserHelper;

    }

    public void createCurrency(Currency currency) {
        currencyDAO.save(modelToPdo(currency));
    }

    public void createCurrencies(List<Currency> currencies) {
        List<CurrencyPDO> currencyPDOS = currencies.stream()
                .map(d -> (modelToPdo(d)))
                .collect(Collectors.toList());
        currencyDAO.saveAll(currencyPDOS);
    }

    public void importCurrency(final InputStream inputStream) throws IOException {
        final Iterable<CSVRecord> csvRecords = csvParserHelper.getCsvRecords(inputStream);

        final List<CurrencyPDO> currencyPDOS = new ArrayList<>();
        csvRecords.forEach(r -> {
            final CurrencyPDO currencyPDO = CurrencyPDO.builder()
                    .name(r.get("Currency Name"))
                    .currencyId(r.get("Currency ID"))
                    .code(r.get("Currency Code"))
                    .code2(r.get("Currency Code (2 Digits)"))
                    .build();
            currencyPDOS.add(currencyPDO);
        });
        currencyDAO.saveAll(currencyPDOS);
    }

    public Currency getCurrency(int id) {
        return currencyDAO.findAllById(id);
    }

    public List<Currency> getCurrencies() {
        List<CurrencyPDO> currencyPDOS = (List<CurrencyPDO>) currencyDAO.findAll();
        final List<Currency> currencies = currencyPDOS.stream()
                .map(c -> pdoToModel(c)).collect(Collectors.toList());
        return currencies;
    }

    public MetaData getCurrenciesByPage(final int pageNo, final int pageSize) {
        final List<Currency> paginatedCurrencies = paginationHelper.doPagination(
                getCurrencies(), pageNo, pageSize);
        final Long totalItems = (long) getCurrencies().size();

        final Meta meta = Meta.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .itemsCount(paginatedCurrencies.size())
                .totalItems(totalItems).build();
        return new MetaData(meta, paginatedCurrencies, new ArrayList());
    }

    public void deleteCurrency(int id) {
        currencyDAO.deleteById(id);
    }

    public List<Currency> sortCurrencies(
            String sortAttr, Boolean isDesc) {
        List<Currency> currencies = getCurrencies();
        Comparator compareBy = Comparator.comparing(Currency::getId);
        if (sortAttr.equalsIgnoreCase("name")) {
            compareBy = isDesc ? Comparator.comparing(Currency::getName)
                    .reversed() : Comparator.comparing(Currency::getName);
        } else if (sortAttr.equalsIgnoreCase("currencyId")) {
            compareBy = isDesc ? Comparator.comparing(Currency::getCurrencyId,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Currency::getCurrencyId,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else if (sortAttr.equalsIgnoreCase("code")) {
            compareBy = isDesc ? Comparator.comparing(Currency::getCode,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Currency::getCode,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else if (sortAttr.equalsIgnoreCase("code2")) {
            compareBy = isDesc ? Comparator.comparing(Currency::getCode2,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Currency::getCode2,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            currencies.sort(compareBy);
        }
        return currencies;
    }

    private Currency pdoToModel(final CurrencyPDO pdo) {
        return Currency.builder()
                .id(pdo.getId())
                .code(pdo.getCode())
                .name(pdo.getName())
                .code2(pdo.getCode2())
                .build();
    }

    private CurrencyPDO modelToPdo(final Currency currency) {
        return CurrencyPDO.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .name(currency.getName())
                .code2(currency.getCode2())
                .build();
    }

    public Currency updateCurrency(Currency currency) {
        CurrencyPDO currencyPDO = modelToPdo(currency);
        currencyPDO.setId(currency.getId());
        currencyDAO.save(currencyPDO);
        return pdoToModel(currencyPDO);
    }
}
