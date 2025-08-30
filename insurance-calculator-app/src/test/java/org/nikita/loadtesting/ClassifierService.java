//package org.nikita.loadtesting;
//
//import org.nikita.core.domain.Classifier;
//import org.nikita.core.repositories.ClassifierRepository;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ClassifierService {
//
//    private final ClassifierRepository classifierRepository;
//
//    public ClassifierService(ClassifierRepository classifierRepository) {
//        this.classifierRepository = classifierRepository;
//    }
//
//    @Cacheable(value = "classifiers", key = "#classifierId")
//    public Classifier getClassifierById(Long classifierId) {
//        return classifierRepository.findById(classifierId)
//                .orElseThrow(() -> new RuntimeException("Classifier not found"));
//    }
//}
