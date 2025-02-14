package com.associate.associate.web;

import com.associate.associate.api.AssociateService;
import com.associate.associate.model.Associate;
import com.associate.associate.web.dto.AssociateDto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Albert Gomes Cabral
 */
@RestController
@CrossOrigin("localhost:8080")
public class AssociateRest {

    @Autowired
    public AssociateRest(AssociateService associateService) {
        this._associateService = associateService;
    }

    @DeleteMapping("/associate/delete-by-id/")
    public ResponseEntity<HttpStatus> deleteAssociate(@RequestBody long associateId) {
        _associateService.deleteAssociate(associateId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/associate/delete-by-company-id")
    public ResponseEntity<HttpStatus> deleteAssociateByCompanyId(
            @RequestBody long associateId, @RequestBody long companyId) {

        _associateService.deleteAssociateByCompanyId(associateId, companyId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/associate/create-new-associate/")
    public ResponseEntity<Associate>
        createAssociate(@RequestBody AssociateDto associateDto) {

        Associate associate = _associateService.addAssociate(
                associateDto.email(), associateDto.companyId(), associateDto.name(),
                associateDto.status(), associateDto.type());

        if (associate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(associate, HttpStatus.CREATED);
    }

    @GetMapping("/associate/all-by-company-id/{companyId}")
    public ResponseEntity<List<Associate>>
        fetchAssociateByCompanyId(@PathVariable long companyId) {

        List<Associate> associateList =
                _associateService.fetchAllAssociatesByCompanyId(
                        companyId);

        if (associateList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(associateList, HttpStatus.OK);
    }

    @PutMapping("/associate/update-associate/")
    public ResponseEntity<Associate> updateAssociate(
            @RequestBody AssociateDto associateDto, @RequestBody long associateId) {

        if (_associateService.fetchAssociateById(associateId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Associate associate = _associateService.updateAssociate(
                associateId, associateDto.name(),
                associateDto.status(), associateDto.type());

        if (associate != null) {
            return new ResponseEntity<>(associate, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private final AssociateService _associateService;

}
