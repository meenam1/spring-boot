package com.adjustment.service;

import com.adjustment.pdo.AdjustmentPDO;
import com.adjustment.dao.AdjustmentDAO;
import com.adjustment.model.AssociatedApp;
import com.adjustment.model.Adjustment;
import com.adjustment.pdo.AssociatedAppPDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdjustmentService {


    @Autowired
    AdjustmentDAO jpaAdjustmentDAO;

    public void saveAdjustment(Adjustment adjustment) {
        jpaAdjustmentDAO.save(modelToPDO(adjustment));
    }

    public Adjustment updateAdjustment(Adjustment adjustment) {
        jpaAdjustmentDAO.save(modelToPDO(adjustment));
        return null;
    }

    public List<Adjustment> findAll() {

        Iterable<AdjustmentPDO> adjustmentPDOS = jpaAdjustmentDAO.findAll();
        Iterator<AdjustmentPDO> iterator = adjustmentPDOS.iterator();
        List<Adjustment> result = new ArrayList();
        iterator.forEachRemaining(e -> result.add(pdoToModel(e)));
        return result;
    }

    public void saveAllAdjustments(List<Adjustment> entities) {
        List<AdjustmentPDO> adjustmentPDOS = entities.stream()
                .map(e -> modelToPDO(e))
                .collect(Collectors.toList());
        jpaAdjustmentDAO.saveAll(adjustmentPDOS);
    }

    public Adjustment getAdjustmentByID(Integer id) {
        return pdoToModel(jpaAdjustmentDAO.findById(id).get());
    }

    public void deleteById(Integer id) {
        jpaAdjustmentDAO.deleteById(id);
    }

    private AdjustmentPDO modelToPDO(Adjustment adjustment) {
        List<AssociatedAppPDO> associatedAppPDOS = new ArrayList<>();
        AdjustmentPDO adjustmentPDO = AdjustmentPDO.builder()
                .id(adjustment.getId())
                .name(adjustment.getName())
                .code(adjustment.getCode())
                .type(adjustment.getType())
                .startDate(adjustment.getStartDate())
                .endDate(adjustment.getEndDate())
                .status(adjustment.getStatus())
                .createdBy(adjustment.getCreatedBy())
                .createdAt(adjustment.getCreatedAt())
                .updatedAt(adjustment.getUpdatedAt())
                .associatedAppPDOS(associatedAppPDOS).build();
        List<AssociatedApp> associatedApps = adjustment.getAssociatedApps();
        for (AssociatedApp associatedApp : associatedApps) {
            AssociatedAppPDO associatedAppPDO = AssociatedAppPDO.builder()
                    .id(associatedApp.getId())
                    .name(associatedApp.getName())
                    .percentage(associatedApp.getPercentage())
                    .status(associatedApp.getStatus())
                    .adjustmentPDO(adjustmentPDO).build();
            associatedAppPDOS.add(associatedAppPDO);

        }
        return adjustmentPDO;
    }

    private Adjustment pdoToModel(AdjustmentPDO adjustmentPDO) {
        List<AssociatedApp> associatedApps = new ArrayList<>();
        List<AssociatedAppPDO> associatedAppPDOs = adjustmentPDO.getAssociatedAppPDOS();
        for (AssociatedAppPDO associatedAppPDO : associatedAppPDOs) {
            AssociatedApp associatedApp = AssociatedApp.builder()
                    .id(associatedAppPDO.getId())
                    .name(associatedAppPDO.getName())
                    .percentage(associatedAppPDO.getPercentage())
                    .status(associatedAppPDO.getStatus())
                    .adjustmentId(adjustmentPDO.getId()).build();
            associatedApps.add(associatedApp);
        }

        Adjustment adjustment = Adjustment.builder()
                .id(adjustmentPDO.getId())
                .name(adjustmentPDO.getName())
                .code(adjustmentPDO.getCode())
                .type(adjustmentPDO.getType())
                .startDate(adjustmentPDO.getStartDate())
                .endDate(adjustmentPDO.getEndDate())
                .status(adjustmentPDO.getStatus())
                .createdBy(adjustmentPDO.getCreatedBy())
                .createdAt(adjustmentPDO.getCreatedAt())
                .updatedAt(adjustmentPDO.getUpdatedAt())
                .associatedApps(associatedApps).build();
        return adjustment;
    }


}

