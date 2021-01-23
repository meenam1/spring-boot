package com.entity.service;

import com.entity.dao.DefinitionDAO;
import com.entity.dao.DomainDAO;
import com.entity.dao.JurisdictionDAO;
import com.entity.dao.TableDAO;
import com.entity.model.Definition;
import com.entity.model.Field;
import com.entity.pdo.DefinitionPDO;
import com.entity.pdo.FieldPDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DefinitionService {

    @Autowired
    final DefinitionDAO definitionDAO;
    @Autowired
    final TableDAO tableDAO;
    @Autowired
    final JurisdictionDAO jurisdictionDAO;
    @Autowired
    final DomainDAO domainDAO;

    public DefinitionService(DefinitionDAO definitionDAO,
                             TableDAO tableDAO,
                             JurisdictionDAO jurisdictionDAO,
                             DomainDAO domainDAO) {
        this.definitionDAO = definitionDAO;
        this.tableDAO = tableDAO;
        this.jurisdictionDAO = jurisdictionDAO;
        this.domainDAO = domainDAO;
    }

    public Integer createDefinition(final Definition definition) {
        final DefinitionPDO definitionPDO = definitionDAO.save(
                definitionToPDO(definition));
        tableDAO.createTable(definition.getName(),
                definition.getFields());
        return definitionPDO.getId();
    }

    private DefinitionPDO definitionToPDO(final Definition definition) {
        final Iterable<DefinitionPDO> definitions = definitionDAO.findAll();
        final List<FieldPDO> fieldPDOS = new ArrayList<>();
        final DefinitionPDO definitionPDO = DefinitionPDO.builder()
                .id(definition.getId())
                .name(definition.getName())
                .recordCount(0)
                .build();

        final List<Field> fields = definition.getFields();
        fields.forEach(f -> {
            final FieldPDO fieldPDO = FieldPDO.builder()
                    .id(f.getId())
                    .name(f.getName())
                    .type(f.getType())
                    .definitionPDO(definitionPDO)
                    .build();
            fieldPDOS.add(fieldPDO);
        });
        definitionPDO.setFieldCount(fields.size());
        definitionPDO.setFieldPDOS(fieldPDOS);
        return definitionPDO;
    }

    public List<Definition> getDefinitions(String sortAttr, Boolean isDesc) {
        Iterable<DefinitionPDO> definitionPDOS = definitionDAO.findAll();
        List<Definition> definitions = new ArrayList<>();
        definitionPDOS.forEach(e -> definitions.add(pdoToModel(e)));
        populateDefinitionCount(definitions);

        sortDefinitions(definitions, sortAttr, isDesc);
        return definitions;
    }

    private void sortDefinitions(List<Definition> definitions,
                                 String sortAttr, Boolean isDesc) {
        Comparator compareBy = Comparator.comparing(Definition::getId);
        if (sortAttr.equalsIgnoreCase("name")) {
            compareBy = isDesc ? Comparator.comparing(Definition::getName)
                    .reversed() : Comparator.comparing(Definition::getName);
        } else if (sortAttr.equalsIgnoreCase("fieldCount")) {
            compareBy = isDesc ? Comparator.comparing(Definition::getFieldCount,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed() : Comparator.comparing(Definition::getFieldCount,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        }
        definitions.sort(compareBy);

    }

    private Definition pdoToModel(DefinitionPDO definitionPDO) {
        List<FieldPDO> fieldPDOS = definitionPDO.getFieldPDOS();
        List<Field> fields = new ArrayList<>();
        fieldPDOS.stream().forEach(f -> fields.add(Field.builder()
                .id(f.getId())
                .name(f.getName())
                .type(f.getType())
                .definitionId(f.getDefinitionPDO().getId()).build()));
        return Definition.builder()
                .id(definitionPDO.getId())
                .name(definitionPDO.getName())
                .fields(fields)
                .fieldCount(definitionPDO.getFieldCount())
                .recordCount(definitionPDO.getRecordCount()).build();
    }

    private List<Definition> populateDefinitionCount(List<Definition> definitions) {
        definitions.forEach(d -> {
            if (d.getName().equals("jurisdiction")) {
                d.setRecordCount((int) jurisdictionDAO.count());
            } else if (d.getName().equals("domain")) {
                d.setRecordCount((int) domainDAO.count());
            } else if (d.getName().equals("Authorities")) {
                d.setRecordCount(0);

            } else if (d.getName().equals("Currency")) {
                d.setRecordCount(0);
            }
        });

        return definitions;
    }

}




