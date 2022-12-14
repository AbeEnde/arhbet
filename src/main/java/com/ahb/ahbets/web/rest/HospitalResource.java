package com.ahb.ahbets.web.rest;

import com.ahb.ahbets.domain.Hospital;
import com.ahb.ahbets.repository.HospitalRepository;
import com.ahb.ahbets.service.HospitalService;
import com.ahb.ahbets.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ahb.ahbets.domain.Hospital}.
 */
@RestController
@RequestMapping("/api")
public class HospitalResource {

    private final Logger log = LoggerFactory.getLogger(HospitalResource.class);

    private static final String ENTITY_NAME = "hospital";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HospitalService hospitalService;

    private final HospitalRepository hospitalRepository;

    public HospitalResource(HospitalService hospitalService, HospitalRepository hospitalRepository) {
        this.hospitalService = hospitalService;
        this.hospitalRepository = hospitalRepository;
    }

    /**
     * {@code POST  /hospitals} : Create a new hospital.
     *
     * @param hospital the hospital to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hospital, or with status {@code 400 (Bad Request)} if the hospital has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hospitals")
    public ResponseEntity<Hospital> createHospital(@Valid @RequestBody Hospital hospital) throws URISyntaxException {
        log.debug("REST request to save Hospital : {}", hospital);
//        if (hospital.getHospId() != null) {
//            throw new BadRequestAlertException("A new hospital cannot already have an ID", ENTITY_NAME, "idexists");
//        }
        Hospital result = hospitalService.save(hospital);
        return ResponseEntity
            .created(new URI("/api/hospitals/" + result.getCode()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getCode().toString()))
            .body(hospital);
    }

    /**
     * {@code PUT  /hospitals/:id} : Updates an existing hospital.
     *
     * @param id the id of the hospital to save.
     * @param hospital the hospital to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hospital,
     * or with status {@code 400 (Bad Request)} if the hospital is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hospital couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hospitals/{id}")
    public ResponseEntity<Hospital> updateHospital(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Hospital hospital
    ) throws URISyntaxException {
        log.debug("REST request to update Hospital : {}, {}", id, hospital);
        if (hospital.getCode() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hospital.getCode())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hospitalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Hospital result = hospitalService.update(hospital);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hospital.getCode().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hospitals/:id} : Partial updates given fields of an existing hospital, field will ignore if it is null
     *
     * @param id the id of the hospital to save.
     * @param hospital the hospital to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hospital,
     * or with status {@code 400 (Bad Request)} if the hospital is not valid,
     * or with status {@code 404 (Not Found)} if the hospital is not found,
     * or with status {@code 500 (Internal Server Error)} if the hospital couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hospitals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Hospital> partialUpdateHospital(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Hospital hospital
    ) throws URISyntaxException {
        log.debug("REST request to partial update Hospital partially : {}, {}", id, hospital);
        if (hospital.getCode() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hospital.getCode())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hospitalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Hospital> result = hospitalService.partialUpdate(hospital);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hospital.getCode().toString())
        );
    }

    /**
     * {@code GET  /hospitals} : get all the hospitals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hospitals in body.
     */
    @GetMapping("/hospitals")
    public List<Hospital> getAllHospitals() {
        log.debug("REST request to get all Hospitals");
        return hospitalService.findAll();
    }

    /**
     * {@code GET  /hospitals/:id} : get the "id" hospital.
     *
     * @param id the id of the hospital to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hospital, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hospitals/{id}")
    public ResponseEntity<Hospital> getHospital(@PathVariable Long id) {
        log.debug("REST request to get Hospital : {}", id);
        Optional<Hospital> hospital = hospitalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hospital);
    }

    /**
     * {@code DELETE  /hospitals/:id} : delete the "id" hospital.
     *
     * @param id the id of the hospital to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hospitals/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        log.debug("REST request to delete Hospital : {}", id);
        hospitalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
