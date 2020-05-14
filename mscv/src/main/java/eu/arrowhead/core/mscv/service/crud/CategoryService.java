package eu.arrowhead.core.mscv.service.crud;

import java.util.Optional;

import eu.arrowhead.common.database.entity.mscv.MipCategory;
import eu.arrowhead.common.database.repository.mscv.MipCategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static eu.arrowhead.core.mscv.Validation.CATEGORY_NULL_ERROR_MESSAGE;
import static eu.arrowhead.core.mscv.Validation.PAGE_NULL_ERROR_MESSAGE;

@Service
public class CategoryService {

    private final Logger logger = LogManager.getLogger();
    private final MipCategoryRepository repository;

    @Autowired
    public CategoryService(final MipCategoryRepository repository) {this.repository = repository;}


    public MipCategory create(final MipCategory category) {
        return repository.saveAndFlush(category);
    }

    @Transactional(readOnly = true)
    public Optional<MipCategory> find(final String name) {
        logger.debug("find({}) started", name);
        return repository.findByName(name);
    }

    @Transactional(readOnly = true)
    public boolean exists(final MipCategory category) {
        logger.debug("exists({}) started", category);
        return repository.exists(Example.of(category, ExampleMatcher.matchingAll()));
    }

    @Transactional(readOnly = true)
    public Page<MipCategory> pageAll(final Pageable pageable) {
        logger.debug("pageAll({}) started", pageable);
        Assert.notNull(pageable, PAGE_NULL_ERROR_MESSAGE);
        return repository.findAll(pageable);
    }

    @Transactional
    public MipCategory replace(final MipCategory oldCategory, final MipCategory newCategory) {
        logger.debug("replace({},{}) started", oldCategory, newCategory);
        Assert.notNull(oldCategory, "old " + CATEGORY_NULL_ERROR_MESSAGE);
        Assert.notNull(newCategory, "new " + CATEGORY_NULL_ERROR_MESSAGE);

        oldCategory.setName(newCategory.getName());
        oldCategory.setAbbreviation(newCategory.getAbbreviation());
        return repository.saveAndFlush(oldCategory);
    }

    @Transactional
    public void delete(final String name) {
        logger.debug("delete({}) started", name);
        final Optional<MipCategory> optionalMipCategory = find(name);
        optionalMipCategory.ifPresent(repository::delete);
        repository.flush();
    }
}