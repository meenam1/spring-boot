package com.entity.controller;

import com.entity.model.Entity;
import com.entity.pdo.EntityPDO;
import com.entity.service.EntityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EntityController {

    @Autowired
    final EntityService entityService;

    public EntityController(EntityService entityService) {
        this.entityService = entityService;
    }

    @ApiOperation(tags = "Entity Service", value = "Add Entitiy",
            notes = "Use this operation to add Entity")
    @RequestMapping(value = "/entities", method = RequestMethod.POST)
    public void saveEntity(@RequestBody final Entity entity) {
        entityService.saveEntity(entity);
    }

    @ApiOperation(tags = "Entity Service", value = "Add Entities",
            notes = "Use this operation to add list of Entities")
    @RequestMapping(value = "/save-entities", method = RequestMethod.POST)
    public void saveAllEntities(@RequestBody final List<Entity> entities) {
        entityService.saveAllEntities(entities);
    }

    @ApiOperation(tags = "Entity Service", value = "Add Bulk Entities",
            notes = "Use this operation to add list of Entities")
    @RequestMapping(value = "/populate-entities", method = RequestMethod.POST)
    public void saveEntities() {
        entityService.saveEntities();
    }

    @ApiOperation(tags = "Entity Service", value = "Update Entitiy",
            notes = "Use this operation to Update Entity")
    @RequestMapping(value = "/entities", method = RequestMethod.PUT)
    public Entity updateEntity(@RequestBody final Entity entity) {
        return entityService.updateEntity(entity);
    }

    @ApiOperation(tags = "Entity Service", value = "Delete Entitiy",
            notes = "Use this operation to Delete Entity")
    @RequestMapping(value = "/entities", method = RequestMethod.DELETE)
    public void deleteEntity(@RequestParam final Integer id) {
        entityService.deleteById(id);
    }

    @ApiOperation(tags = "Entity Service", value = "Get Entitiy",
            notes = "Use this operation to Get Entity")
    @RequestMapping(value = "/entities/{id}", method = RequestMethod.GET)
    public Optional<EntityPDO> getEntityById(@PathVariable final Integer id) {
        return entityService.getEntityByID(id);
    }

    @ApiOperation(tags = "Entity Service", value = "Get Entities",
            notes = "Use this operation to Get Entities")
    @RequestMapping(value = "/entities", method = RequestMethod.GET)
    public List<Entity> getEntities() {
        return entityService.findAll();
    }

}
