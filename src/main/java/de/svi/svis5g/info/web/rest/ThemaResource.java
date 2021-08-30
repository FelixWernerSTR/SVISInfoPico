package de.svi.svis5g.info.web.rest;

import de.svi.svis5g.info.domain.Thema;
import de.svi.svis5g.info.repository.ThemaRepository;
import de.svi.svis5g.info.web.rest.errors.BadRequestAlertException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link de.svi.svis5g.info.domain.Thema}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ThemaResource {

    private final Logger log = LoggerFactory.getLogger(ThemaResource.class);

    private static final String ENTITY_NAME = "thema";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThemaRepository themaRepository;

    public ThemaResource(ThemaRepository themaRepository) {
        this.themaRepository = themaRepository;
    }

    /**
     * {@code POST  /themas} : Create a new thema.
     *
     * @param thema the thema to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new thema, or with status {@code 400 (Bad Request)} if the thema has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/themas")
    public ResponseEntity<Thema> createThema(@Valid @RequestBody Thema thema) throws URISyntaxException {
        log.debug("REST request to save Thema : {}", thema);
        if (thema.getId() != null) {
            throw new BadRequestAlertException("A new thema cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Thema result = themaRepository.save(thema);
        return ResponseEntity
            .created(new URI("/api/themas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /themas/:id} : Updates an existing thema.
     *
     * @param id the id of the thema to save.
     * @param thema the thema to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated thema,
     * or with status {@code 400 (Bad Request)} if the thema is not valid,
     * or with status {@code 500 (Internal Server Error)} if the thema couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/themas/{id}")
    public ResponseEntity<Thema> updateThema(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Thema thema)
        throws URISyntaxException {
        log.debug("REST request to update Thema : {}, {}", id, thema);
        if (thema.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, thema.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Thema result = themaRepository.save(thema);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, thema.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /themas/:id} : Partial updates given fields of an existing thema, field will ignore if it is null
     *
     * @param id the id of the thema to save.
     * @param thema the thema to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated thema,
     * or with status {@code 400 (Bad Request)} if the thema is not valid,
     * or with status {@code 404 (Not Found)} if the thema is not found,
     * or with status {@code 500 (Internal Server Error)} if the thema couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/themas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Thema> partialUpdateThema(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Thema thema
    ) throws URISyntaxException {
        log.debug("REST request to partial update Thema partially : {}, {}", id, thema);
        if (thema.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, thema.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Thema> result = themaRepository
            .findById(thema.getId())
            .map(
                existingThema -> {
                    if (thema.getName() != null) {
                        existingThema.setName(thema.getName());
                    }
                    if (thema.getRechte() != null) {
                        existingThema.setRechte(thema.getRechte());
                    }
                    if (thema.getDisplaycount() != null) {
                        existingThema.setDisplaycount(thema.getDisplaycount());
                    }

                    return existingThema;
                }
            )
            .map(themaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, thema.getId().toString())
        );
    }

    /**
     * {@code GET  /themas} : get all the themas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of themas in body.
     */
    @GetMapping("/themas")
    public ResponseEntity<List<Thema>> getAllThemas(Pageable pageable) {
        log.debug("REST request to get a page of Themas");
        Page<Thema> page = themaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /themas/:id} : get the "id" thema.
     *
     * @param id the id of the thema to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the thema, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/themas/{id}")
    public ResponseEntity<Thema> getThema(@PathVariable Long id) {
        log.debug("REST request to get Thema : {}", id);
        Optional<Thema> thema = themaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(thema);
    }

    /**
     * {@code DELETE  /themas/:id} : delete the "id" thema.
     *
     * @param id the id of the thema to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/themas/{id}")
    public ResponseEntity<Void> deleteThema(@PathVariable Long id) {
        log.debug("REST request to delete Thema : {}", id);
        themaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
