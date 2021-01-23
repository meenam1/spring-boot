package com.entity.controller;

import com.entity.service.DefinitionService;
import com.entity.model.Definition;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class DefinitionController {

    @Autowired
    final DefinitionService definitionService;

    public DefinitionController(DefinitionService definitionService) {
        this.definitionService = definitionService;
    }

    @ApiOperation(tags = "Definition Service", value = "Create Definition",
            notes = "Use this operation to add Definition")
    @RequestMapping(value = "/definition", method = RequestMethod.POST)
    public Integer createDefinition(@RequestBody final Definition definition) {
        return definitionService.createDefinition(definition);
    }

    @ApiOperation(tags = "Definition Service", value = "Get Definition",
            notes = "Use this operation to Get Definition")
    @RequestMapping(value = "/definition", method = RequestMethod.GET)
    public List<Definition> getDefinitions(@RequestParam(name = "sort_attr") String sortAttr,
                                           @RequestParam(name = "order") String order) {
        Boolean isDesc = order.equalsIgnoreCase("desc") ? true : false;
        return definitionService.getDefinitions(sortAttr, isDesc);
    }


}
