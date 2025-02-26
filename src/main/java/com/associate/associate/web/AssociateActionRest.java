package com.associate.associate.web;

import com.associate.associate.api.AssociateActionService;
import com.associate.associate.constants.AssociateConstantStatus;
import com.associate.associate.model.Associate;
import com.associate.benefit.model.Benefit;
import com.associate.benefit.model.BenefitResource;
import com.associate.notify.model.Notify;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Albert Gomes Cabral
 */
@RestController
@CrossOrigin("localhost:8080")
public class AssociateActionRest {

    @Autowired
    public AssociateActionRest(AssociateActionService associateActionService) {
        this._associateActionService = associateActionService;
    }

    @GetMapping("/associate-action/get-all-benefit-by-type/{associateId}/{companyId}/")
    public ResponseEntity<List<Benefit>> getAllBenefitByType(
            @PathVariable long associateId, @PathVariable long companyId) {

        List<Benefit> benefits =
                _associateActionService.fetchAllBenefitByAssociateId(
                        associateId, companyId);

        if (benefits.isEmpty()) {
            return new ResponseEntity<>(
                new ArrayList<>(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(benefits, HttpStatus.OK);
    }

    @GetMapping("/associate-action/get-all-notifies/{associateId}/{companyId}")
    public ResponseEntity<List<Notify>>
        getAllNotifies(@PathVariable long associateId, @PathVariable long companyId) {

        List<Notify> notifies =
                _associateActionService.findNotifiesByReceiverId(
                        associateId, companyId);

        if (notifies.isEmpty()) {
            return new ResponseEntity<>(
                new ArrayList<>(0), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(notifies, HttpStatus.OK);
    }

    @GetMapping("/associate-action/identifier-associate/{associateId}/{companyId}")
    public ResponseEntity<String> identifierAssociate(
            @PathVariable long associateId, @PathVariable long companyId) {

        String identifier = _associateActionService.identifierAssociate(
                associateId, companyId);

        if (identifier.isEmpty() || identifier.isBlank()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(identifier, HttpStatus.OK);
    }

    @PostMapping("/associate-action/send-a-notify/{associateId}/{receiver}")
    public ResponseEntity<Notify> sendANotify(
            @PathVariable long associateId, @RequestBody Notify notify,
            @PathVariable long receiver) {

        Notify sentNotify = _associateActionService.sendNotify(
                associateId, notify.getCompanyId(), notify.getNotifyBody(),
                notify.getNotifyHeader(), notify.getNotifyTitle(), receiver);

        if (sentNotify == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(sentNotify, HttpStatus.OK);
    }

    @PatchMapping("/associate-action/reactive-plan-associate/{associateId}")
    public ResponseEntity<Associate> reactivePlanAssociate(
            @PathVariable long associateId, @RequestBody String newType) {

        Associate associate = _associateActionService.reactivePlanAssociate(
                associateId, AssociateConstantStatus.APPROVED, newType);

        if (associate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(associate, HttpStatus.OK);
    }

    @DeleteMapping("/associate-action/shutdown/{associateId}/{companyId}")
    public ResponseEntity<HttpStatus> shutdownAssociate (
            @PathVariable long associateId, @PathVariable long companyId) {

        _associateActionService.shutdown(associateId, companyId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/associate-action/suspend-plan-associate/{associateId}")
    public ResponseEntity<String> suspendPlanAssociate(
            @PathVariable long associateId, @RequestBody String reason) {

        String status =
                _associateActionService.suspendPlanAssociate(
                        associateId, reason);

        if (status.equals(AssociateConstantStatus.SUSPEND)) {
            return new ResponseEntity<>(
                "Associate assign was suspend. %s"
                        .formatted(associateId), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/associate-action/view-all-available-benefits/{companyId}")
    public ResponseEntity<List<Benefit>>
        viewAllAvailableBenefits(@PathVariable long companyId) {

        List<Benefit> benefits = _associateActionService.fetchAllBenefits(companyId);

        if (benefits.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(benefits, HttpStatus.OK);
    }

    @GetMapping("/associate-action/view-benefit-resource-by-benefit-id/{benefitId}")
    public ResponseEntity<BenefitResource> viewBenefitResourceByBenefitId(
            @PathVariable long benefitId) {

        BenefitResource benefitResource =
                _associateActionService.fetchBenefitResource(benefitId);

        if (benefitResource == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(benefitResource, HttpStatus.OK);
    }

    private final AssociateActionService _associateActionService;

}
