package com.entity.service;

import com.entity.model.Jurisdiction;
import com.entity.model.JurisdictionFilters;
import com.entity.resolver.CsvParserHelper;
import com.entity.dao.JurisdictionDAO;
import com.entity.model.JurisdictionPredicateAttributes;
import com.entity.pdo.JurisdictionPDO;
import com.entity.resolver.PaginationHelper;
import net.bytebuddy.matcher.StringMatcher;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class JurisdictionService {

    @Autowired
    final JurisdictionDAO jurisdictionDAO;

    @Autowired
    final PaginationHelper paginationHelper;

    @Autowired
    final CsvParserHelper csvParserHelper;
    private StringMatcher.Mode FilterAttributes;

    public JurisdictionService(JurisdictionDAO jurisdictionDAO,
                               PaginationHelper paginationHelper,
                               CsvParserHelper csvParserHelper) {
        this.jurisdictionDAO = jurisdictionDAO;
        this.paginationHelper = paginationHelper;
        this.csvParserHelper = csvParserHelper;
    }

    private Jurisdiction pdoToModel(final JurisdictionPDO pdo) {
        return Jurisdiction.builder()
                .id(pdo.getId())
                .code(pdo.getCode())
                .associatedApps(pdo.getAssociatedApps())
                .country(pdo.getCountry())
                .location(pdo.getLocation())
                .name(pdo.getName())
                .type(pdo.getType())
                .build();
    }

    public void uploadData(final InputStream inputStream) throws IOException {

        final Iterable<CSVRecord> csvRecords = csvParserHelper.getCsvRecords(inputStream);
        final List<JurisdictionPDO> jurisdictionPDOS = new ArrayList<>();
        csvRecords.forEach(r -> {
            final JurisdictionPDO jurisdictionPDO = JurisdictionPDO.builder()
                    .country(r.get("Jurisdiction Country"))
                    .code(r.get("Jurisdiction Code"))
                    .name(r.get("Jurisdiction Name"))
                    .type(r.get("Jurisdiction Type"))
                    .location(r.get("Jurisdiction Location"))
                    .associatedApps(r.get("Associated Application")).build();
            jurisdictionPDOS.add(jurisdictionPDO);
        });
        jurisdictionDAO.saveAll(jurisdictionPDOS);
    }

    public List<JurisdictionPDO> getJurisdictionPDOS() {
        List<JurisdictionPDO> pdos = jurisdictionDAO.findAll();
        return pdos;
    }

    public List<Jurisdiction> getAllJurisdictions() {
        final List<Jurisdiction> jurisdictions = getJurisdictionPDOS()
                .stream().map(p -> pdoToModel(p)).collect(Collectors.toList());
        return jurisdictions;
    }

    public List<Jurisdiction> sortJurisdictions(
            String sortAttr, Boolean isDesc) {
        List<Jurisdiction> jurisdictions = jurisdictionDAO.findAll().stream()
                .map(j -> pdoToModel(j)).collect(Collectors.toList());
        Comparator compareBy = Comparator.comparing(Jurisdiction::getId);
        if (sortAttr.equalsIgnoreCase("name")) {
            compareBy = isDesc ? Comparator.comparing(Jurisdiction::getName)
                    .reversed() : Comparator.comparing(Jurisdiction::getName);
        } else if (sortAttr.equalsIgnoreCase("country")) {
            compareBy = isDesc ? Comparator.comparing(Jurisdiction::getCountry,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Jurisdiction::getCountry,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else if (sortAttr.equalsIgnoreCase("code")) {
            compareBy = isDesc ? Comparator.comparing(Jurisdiction::getCode,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Jurisdiction::getCode,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else if (sortAttr.equalsIgnoreCase("location")) {
            compareBy = isDesc ? Comparator.comparing(Jurisdiction::getLocation,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Jurisdiction::getLocation,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else if (sortAttr.equalsIgnoreCase("type")) {
            compareBy = isDesc ? Comparator.comparing(Jurisdiction::getType,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Jurisdiction::getType,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        }
        jurisdictions.sort(compareBy);
        return jurisdictions;
    }

    public Jurisdiction findByCode(final String code){
        return pdoToModel(jurisdictionDAO.findByCode(code));
    }

    public List<Jurisdiction> searchJurisdictions(final JurisdictionFilters jurisdictionFilters) {
        final List<Jurisdiction> jurisdictionList = getAllJurisdictions();
        List<Predicate<Jurisdiction>> predicates = new ArrayList<>();
        final List<Jurisdiction> jurisdictions = new ArrayList<Jurisdiction>();

        jurisdictionFilters.getConditions().forEach(filter -> {
            final String column = filter.getColumn();
            final String operator = filter.getOperator();
            final List<String> values = filter.getValues();
            JurisdictionPredicateAttributes predicateColumn = JurisdictionPredicateAttributes.valueOf(column);
            //List<Predicate<Jurisdiction>> predicates = new ArrayList<>();
            if (operator.equalsIgnoreCase("equals")) {
                values.forEach(
                        value -> predicates.add(e -> (predicateColumn.getter.apply(e) != null &&
                                predicateColumn.getter.apply(e).toLowerCase().equals(value.toLowerCase()))));
            }
            if (operator.equalsIgnoreCase("contains")) {
                values.forEach(
                        value -> predicates.add(e -> (predicateColumn.getter.apply(e) != null && predicateColumn.getter.apply(
                                e).toLowerCase().contains(value.toLowerCase()))));
            }
            if (operator.equalsIgnoreCase("startsWith")) {
                values.forEach(
                        value -> predicates.add(e -> (predicateColumn.getter.apply(e) != null && predicateColumn.getter.apply(
                                e).toLowerCase().startsWith(value.toLowerCase()))));
            }
            jurisdictions.addAll(jurisdictionList.stream()
                    .filter(predicates.stream().reduce(x -> false, Predicate::or)).collect(Collectors.toList()));

    });
    return jurisdictions;
    }
}

