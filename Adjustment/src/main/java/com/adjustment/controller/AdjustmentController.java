package com.adjustment.controller;

import com.adjustment.model.Adjustment;
import com.adjustment.service.AdjustmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdjustmentController {

    @Autowired
    final AdjustmentService adjustmentService;

    public AdjustmentController(AdjustmentService adjustmentService) {
        this.adjustmentService = adjustmentService;
    }

    @ApiOperation(tags = "Adjustment Service", value = "Add Adjustment",
            notes = "Use this operation to add Adjustment")
    @RequestMapping(value = "/adjustments", method = RequestMethod.POST)
    public void saveAdjustment(@RequestBody final Adjustment adjustment) {
        adjustmentService.saveAdjustment(adjustment);
    }

    @ApiOperation(tags = "Adjustment Service", value = "Add Adjustments",
            notes = "Use this operation to add list of Adjustments")
    @RequestMapping(value = "/save-adjustments", method = RequestMethod.POST)
    public void saveAllAdjustments(@RequestBody final List<Adjustment> adjustments) {
        adjustmentService.saveAllAdjustments(adjustments);
    }


    @ApiOperation(tags = "Adjustment Service", value = "Update Adjustment",
            notes = "Use this operation to Update Adjustment")
    @RequestMapping(value = "/adjustments", method = RequestMethod.PUT)
    public Adjustment updateAdjustment(@RequestBody final Adjustment adjustment) {
        return adjustmentService.updateAdjustment(adjustment);
    }

    @ApiOperation(tags = "Adjustment Service", value = "Delete Adjustment",
            notes = "Use this operation to Delete Adjustment")
    @RequestMapping(value = "/adjustments", method = RequestMethod.DELETE)
    public void deleteAdjustment(@RequestParam final Integer id) {
        adjustmentService.deleteById(id);
    }

    @ApiOperation(tags = "Adjustment Service", value = "Get Adjustment",
            notes = "Use this operation to Get Adjustment")
    @RequestMapping(value = "/adjustments/{id}", method = RequestMethod.GET)
    public Adjustment getAdjustmentById(@PathVariable final Integer id) {
        return adjustmentService.getAdjustmentByID(id);
    }

    @ApiOperation(tags = "Adjustment Service", value = "Get Adjustments",
            notes = "Use this operation to Get Adjustments")
    @RequestMapping(value = "/adjustments", method = RequestMethod.GET)
    public List<Adjustment> getAdjustments() {
        return adjustmentService.findAll();
    }

}
