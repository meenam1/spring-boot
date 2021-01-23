package com.entity.service;

import com.entity.dao.AuthorityDAO;
import com.entity.model.Jurisdiction;
import com.entity.pdo.AuthorityPDO;
import com.entity.model.Authority;
import com.entity.model.Meta;
import com.entity.model.MetaData;
import com.entity.resolver.CsvParserHelper;
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
public class AuthorityService {

    @Autowired
    final AuthorityDAO authorityDAO;

    @Autowired
    final CsvParserHelper csvParserHelper;

    @Autowired
    final JurisdictionService jurisdictionService;

    @Autowired
    final PaginationHelper paginationHelper;

    public AuthorityService(AuthorityDAO authorityDAO,
                            CsvParserHelper csvParserHelper,
                            JurisdictionService jurisdictionService,
                            PaginationHelper paginationHelper) {
        this.authorityDAO = authorityDAO;
        this.csvParserHelper = csvParserHelper;
        this.jurisdictionService = jurisdictionService;
        this.paginationHelper = paginationHelper;
    }

    public void importAuthority(final InputStream inputStream)
            throws IOException {
        final Iterable<CSVRecord> csvRecords = csvParserHelper.getCsvRecords(
                inputStream);

        final List<AuthorityPDO> authorityPDOS = new ArrayList<>();
        csvRecords.forEach(r -> {
            final AuthorityPDO authorityPDO = AuthorityPDO.builder()
                    .name(r.get("Authority Name"))
                    .authorityId(r.get("Authority ID"))
                    .jurisdictionCode(r.get("Jurisdiction Code"))
                    .domains(r.get("Domains"))
                    .departments(r.get("Departments"))
                    .build();
            authorityPDOS.add(authorityPDO);
        });
        authorityDAO.saveAll(authorityPDOS);
    }

    public Authority getAuthority(int id) {
        return pdoToModel(authorityDAO.findById(id));
    }

    public List<Authority> getAuthorities() {
        List<Authority> authorities = ((List<AuthorityPDO>) authorityDAO.findAll())
                .stream().map(a -> pdoToModel(a)).collect(Collectors.toList());
        return authorities;
    }

    private Authority pdoToModel(final AuthorityPDO pdo) {
        Jurisdiction jurisdiction = jurisdictionService.findByCode(pdo.getJurisdictionCode());
        return Authority.builder()
                .id(pdo.getId())
                .authorityId(pdo.getAuthorityId())
                .name(pdo.getName())
                .jurisdictionCode(pdo.getJurisdictionCode())
                .domainName(pdo.getDomains())
                .departments(pdo.getDepartments())
                .jurisdictionCountry(jurisdiction.getCountry())
                .jurisdictionType(jurisdiction.getType())
                .jurisdictionName(jurisdiction.getName())
                .jurisdictionLocation(jurisdiction.getLocation())
                .build();
        /*return Authority.builder()
                .jurisdictionCountry(jurisdiction.getCountry())
                .jurisdictionLocation(jurisdiction.getLocation())
                .jurisdictionName(jurisdiction.getName())
                .jurisdictionType(jurisdiction.getType()).build();*/
    }

    private AuthorityPDO modelToPdo(final Authority authority) {
        return AuthorityPDO.builder()
                .authorityId(authority.getAuthorityId())
                .name(authority.getName())
                .jurisdictionCode(authority.getJurisdictionCode())
                .domains(authority.getDomainName())
                .departments(authority.getDepartments()).build();
    }

    public MetaData getAuthoritiesByPage(Integer pageNo, Integer pageSize) {
        final List<Authority> paginatedAuthorities = paginationHelper.doPagination(
                getAuthorities(), pageNo, pageSize);
        final Long totalItems = (long) getAuthorities().size();

        final Meta meta = Meta.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .itemsCount(paginatedAuthorities.size())
                .totalItems(totalItems).build();
        return new MetaData(meta, paginatedAuthorities, new ArrayList());
    }

    public Authority updateAuthority(Authority authority) {
        AuthorityPDO authorityPDO = modelToPdo(authority);
        authorityPDO.setId(authority.getId());
        authorityDAO.save(modelToPdo(authority));
        return pdoToModel(authorityPDO);
    }

    public void deleteAuthority(Integer id) {
        authorityDAO.deleteById(id);
    }

    public List<Authority> sortAuthorities(String sortAttr, Boolean isDesc) {
        List<Authority> authorities = getAuthorities();
        Comparator compareBy = Comparator.comparing(Authority::getId);
        if (sortAttr.equalsIgnoreCase("name")) {
            compareBy = isDesc ? Comparator.comparing(Authority::getName)
                    .reversed() : Comparator.comparing(Authority::getName);
        } else if (sortAttr.equalsIgnoreCase("authorityId")) {
            compareBy = isDesc ? Comparator.comparing(Authority::getAuthorityId,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Authority::getAuthorityId,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else if (sortAttr.equalsIgnoreCase("jurisdictionCode")) {
            compareBy = isDesc ? Comparator.comparing(Authority::getJurisdictionCode,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Authority::getJurisdictionCode,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else if (sortAttr.equalsIgnoreCase("domainName")) {
            compareBy = isDesc ? Comparator.comparing(Authority::getDomainName,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Authority::getDomainName,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            authorities.sort(compareBy);
        } else if (sortAttr.equalsIgnoreCase("departments")) {
            compareBy = isDesc ? Comparator.comparing(Authority::getDepartments,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Authority::getDepartments,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            authorities.sort(compareBy);
        }
        return authorities;

    }

    public void createAuthority(Authority authority) {
        authorityDAO.save(modelToPdo(authority));
    }

    public void createAuthorities(List<Authority> authorities) {
        List<AuthorityPDO> authorityPDOS = authorities.stream()
                .map(d -> (modelToPdo(d)))
                .collect(Collectors.toList());
        authorityDAO.saveAll(authorityPDOS);
    }
}
