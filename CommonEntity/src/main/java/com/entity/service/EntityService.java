package com.entity.service;

import com.entity.pdo.EntityPDO;
import com.entity.dao.JPAEntityDAO;
import com.entity.model.Address;
import com.entity.model.AssociatedApp;
import com.entity.model.Entity;
import com.entity.pdo.AssociatedAppPDO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EntityService {

    //QEntityPDO qEntityPDO;

    @Autowired
    JPAEntityDAO jpaEntityDAO;

    /*@Autowired
    public EntityService(QEntityPDO qEntityPDO) {
        this.qEntityPDO = qEntityPDO;
    }

*/
    public void saveEntity(Entity entity) {
        jpaEntityDAO.save(modelToPDO(entity));
    }

    public Entity updateEntity(Entity entity) {
        jpaEntityDAO.save(modelToPDO(entity));
        return null;
    }

    public List<Entity> findAll() {

        Iterable<EntityPDO> entityPDOS = jpaEntityDAO.findAll();
        Iterator<EntityPDO> iterator = entityPDOS.iterator();
        List<Entity> result = new ArrayList();
        iterator.forEachRemaining(e -> result.add(pdoToModel(e)));
        return result;
    }

    public void saveAllEntities(List<Entity> entities) {
        List<EntityPDO> entityPDOS = entities.stream()
                .map(e -> modelToPDO(e))
                .collect(Collectors.toList());
        jpaEntityDAO.saveAll(entityPDOS);
    }

    public void saveEntities() {
        final List<EntityPDO> entityPDOS = new ArrayList<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.createObjectNode();
        int counter = 5001;
        for (int i = 1001; i <= 2000; i++) {
            final Address address = new Address("Address" + i, "City" + i, "AnM500" + i);
            final EntityPDO entityPDO = EntityPDO.builder()
                    .code("code" + i)
                    .createdAt(new Date())
                    .createdBy("Entity" + i)
                    .id(i)
                    .data(objectMapper.convertValue(address, JsonNode.class))
                    //.data(objectMapper.createObjectNode())
                    .status("status" + i)
                    .endDate(new Date())
                    .name("name" + i)
                    .startDate(new Date())
                    .updatedAt(new Date()).build();

            //qEntityPDO.id=1
            final List<AssociatedAppPDO> associatedAppPDOS = new ArrayList<>();

            for (int j = 1; j <= 5; j++) {
                String status;
                if (j % 2 == 0) {
                    status = "Active";
                } else {
                    status = "InActive";
                }
                AssociatedAppPDO associatedAppPDO = AssociatedAppPDO.builder()
                        .id(counter)
                        .name("app" + counter)
                        .percentage((float) ((i + j) % 100))
                        .status(status)
                        .entityPDO(entityPDO).build();
                counter++;
                associatedAppPDOS.add(associatedAppPDO);
            }
            entityPDO.setAssociatedAppPDOS(associatedAppPDOS);
            //jpaEntityDAO.save(entityPDO);  This will hit DB 1000 times
            entityPDOS.add(entityPDO);
        }
        List<Entity> entities = entityPDOS.stream()
                .map(e -> pdoToModel(e))
                .collect(Collectors.toList());
        saveAllEntities(entities);
    }

    public Optional<EntityPDO> getEntityByID(Integer id) {

        return jpaEntityDAO.findById(id);
    }

    public void deleteById(Integer id) {

        jpaEntityDAO.deleteById(id);
    }

    private EntityPDO modelToPDO(Entity entity) {
        List<AssociatedAppPDO> associatedAppPDOS = new ArrayList<>();
        EntityPDO entityPDO = EntityPDO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .associatedAppPDOS(associatedAppPDOS).build();
        List<AssociatedApp> associatedApps = entity.getAssociatedApps();
        for (AssociatedApp associatedApp : associatedApps) {
            AssociatedAppPDO associatedAppPDO = AssociatedAppPDO.builder()
                    .id(associatedApp.getId())
                    .name(associatedApp.getName())
                    .percentage(associatedApp.getPercentage())
                    .status(associatedApp.getStatus())
                    .entityPDO(entityPDO).build();
            associatedAppPDOS.add(associatedAppPDO);

        }
        return entityPDO;
    }

    private Entity pdoToModel(EntityPDO entityPDO) {
        List<AssociatedApp> associatedApps = new ArrayList<>();
        List<AssociatedAppPDO> associatedAppPDOs = entityPDO.getAssociatedAppPDOS();
        for (AssociatedAppPDO associatedAppPDO : associatedAppPDOs) {
            AssociatedApp associatedApp = AssociatedApp.builder()
                    .id(associatedAppPDO.getId())
                    .name(associatedAppPDO.getName())
                    .percentage(associatedAppPDO.getPercentage())
                    .status(associatedAppPDO.getStatus())
                    .entityId(entityPDO.getId()).build();
            associatedApps.add(associatedApp);
        }

        Entity entity = Entity.builder()
                .id(entityPDO.getId())
                .name(entityPDO.getName())
                .code(entityPDO.getCode())
                .startDate(entityPDO.getStartDate())
                .endDate(entityPDO.getEndDate())
                .status(entityPDO.getStatus())
                .createdBy(entityPDO.getCreatedBy())
                .createdAt(entityPDO.getCreatedAt())
                .updatedAt(entityPDO.getUpdatedAt())
                .associatedApps(associatedApps).build();
        return entity;
    }


}

