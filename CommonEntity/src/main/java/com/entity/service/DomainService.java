package com.entity.service;

import com.entity.model.Domain;
import com.entity.dao.DefinitionDAO;
import com.entity.dao.DomainDAO;
import com.entity.model.Meta;
import com.entity.model.MetaData;
import com.entity.pdo.DomainPDO;
import com.entity.resolver.PaginationHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DomainService {

    @Autowired
    final DomainDAO domainDAO;

    @Autowired
    final DefinitionDAO definitionDAO;

    @Autowired
    final PaginationHelper paginationHelper;

    public DomainService(DomainDAO domainDAO,
                         DefinitionDAO definitionDAO,
                         PaginationHelper paginationHelper) {
        this.domainDAO = domainDAO;
        this.definitionDAO = definitionDAO;
        this.paginationHelper = paginationHelper;
    }

    public void saveDomain(final InputStream inputStream) throws IOException {
        final BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(inputStream,
                        "UTF-8"));
        final CSVParser csvParser = new CSVParser(fileReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim());
        final Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        final List<DomainPDO> domainPDOS = new ArrayList<>();
        csvRecords.forEach(r -> {
            final DomainPDO domainPDO = DomainPDO.builder()
                    .name(r.get("Name"))
                    .code(r.get("Code"))
                    .type(r.get("Type"))
                    .alternateName(r.get("Alternate Name"))
                    .associatedApps(r.get("Associated Application"))
                    .build();
            domainPDOS.add(domainPDO);
        });
        domainDAO.saveAll(domainPDOS);
    }

    public MetaData getDomains(final int pageNo, final int pageSize) {
        final List<Domain> domains = getAllDomains();
        final List<Domain> paginatedDomains = paginationHelper.doPagination(
                domains, pageNo, pageSize);
        final Long totalItems = (long) domains.size();

        final Meta meta = Meta.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .itemsCount(paginatedDomains.size())
                .totalItems(totalItems).build();
        return new MetaData(meta, paginatedDomains, new ArrayList());
    }

    public Domain findById(int id) {
        DomainPDO domainPDO = domainDAO.findById(id).get();
        return pdoToModel(domainPDO);
    }

    public Domain updateDomain(Domain domain) {
        DomainPDO domainPDO = modelToPdo(domain);
        domainPDO.setId(domain.getId());
        domainDAO.save(domainPDO);
        return pdoToModel(domainPDO);
    }

    public void deleteDomain(int id) {
        domainDAO.deleteById(id);
    }

    public List<Domain> getAllDomains() {
        List<Domain> domains = domainDAO.findAll()
                .stream().map(p -> pdoToModel(p)).collect(Collectors.toList());
        return domains;
    }

    public List<Domain> sortdomains(
            String sortAttr, Boolean isDesc) {
        List<Domain> domains = domainDAO.findAll().stream()
                .map(j -> pdoToModel(j)).collect(Collectors.toList());
        Comparator compareBy = Comparator.comparing(Domain::getId);
        if (sortAttr.equalsIgnoreCase("name")) {
            compareBy = isDesc ? Comparator.comparing(Domain::getName)
                    .reversed() : Comparator.comparing(Domain::getName);
        } else if (sortAttr.equalsIgnoreCase("type")) {
            compareBy = isDesc ? Comparator.comparing(Domain::getType,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Domain::getType,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else if (sortAttr.equalsIgnoreCase("code")) {
            compareBy = isDesc ? Comparator.comparing(Domain::getCode,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Domain::getCode,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else if (sortAttr.equalsIgnoreCase("alternateName")) {
            compareBy = isDesc ? Comparator.comparing(Domain::getAlternateName,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Domain::getAlternateName,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else if (sortAttr.equalsIgnoreCase("associatedApps")) {
            compareBy = isDesc ? Comparator.comparing(Domain::getAssociatedApps,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Domain::getAssociatedApps,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        }
        domains.sort(compareBy);
        return domains;
    }

    public void createDomain(Domain domain){
        domainDAO.save(modelToPdo(domain));
    }

    private Domain pdoToModel(final DomainPDO pdo) {
        return Domain.builder()
                .id(pdo.getId())
                .name(pdo.getName())
                .type(pdo.getType())
                .code(pdo.getCode())
                .alternateName(pdo.getAlternateName())
                .associatedApps(pdo.getAssociatedApps())
                .build();
    }

    private DomainPDO modelToPdo(final Domain domain) {
        return DomainPDO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .type(domain.getType())
                .code(domain.getCode())
                .alternateName(domain.getAlternateName())
                .associatedApps(domain.getAssociatedApps())
                .build();
    }

    public void createDomains(List<Domain> domains) {
        List<DomainPDO> domainPDOS = domains.
                stream()
                .map(d -> (modelToPdo(d)))
                .collect(Collectors.toList());
        domainDAO.saveAll(domainPDOS);
    }
}
