package com.example.realworld.application.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

// 지정한 패키지의 하위 패키지에 존재하는 모든 클래스에 대해서 분석
class LayeredArchitectureTest {

    // 패키지 하위의 모든 클래스를 저장
    private final JavaClasses classes = new ClassFileImporter().importPackages("com.example.realworld.application");

    @DisplayName("코드 내에 print 코드 있는지 확인 테스트")
    @Test
    void prevent_calls_to_systems() {
        noClasses()
                .should(GeneralCodingRules.ACCESS_STANDARD_STREAMS)
                .check(classes);
    }

    /**
     * presentation
     * ⤉
     * service
     */
    @DisplayName("서비스 레이어에서 컨트롤러에 접근하고 있는지 테스트")
    @Test
    void services_should_not_access_controllers() {
        noClasses()
                .that().resideInAPackage("..service..")
                .should().accessClassesThat().resideInAPackage("..presentation..")
                .check(classes);
    }

    /**
     * service
     * ⤉
     * persistence
     */
    @DisplayName("영속성 레이어에서 서비스 레이어에 접근하고 있는지 테스트")
    @Test
    void persistence_should_not_access_services() {
        noClasses()
                .that().resideInAPackage("..persistence..")
                .should().accessClassesThat().resideInAPackage("..service..")
                .check(classes);
    }

    /**
     * presentation
     * ↑
     * service  →  service
     */
    @DisplayName("서비스 레이어를 컨트롤러 레이어 또는 서비스 레이어에서만 접근하고 있는지 테스트")
    @Test
    void services_should_only_be_accessed_by_controllers_or_other_services() {
        classes()
                .that().resideInAPackage("..service..")
                .should().onlyBeAccessed().byAnyPackage("..presentation..", "..service..")
                .check(classes);
    }
}
