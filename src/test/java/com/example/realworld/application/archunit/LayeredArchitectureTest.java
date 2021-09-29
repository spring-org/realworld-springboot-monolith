package com.example.realworld.application.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

// 지정한 패키지의 하위 패키지에 존재하는 모든 클래스에 대해서 분석
@AnalyzeClasses(packages = "com.example.realworld.application")
class LayeredArchitectureTest {

    // 패키지 하위의 모든 클래스를 저장
    private final JavaClasses classes = new ClassFileImporter().importPackages("com.example.realworld.application");

    @DisplayName("Presentation 레이어의 클래스는 Service 레이어의 클래스를 호출하는 지 확인하는 테스트")
    @Test
    void article_presentation_called_service_layer() {
        classes()
                .that().haveSimpleNameEndingWith("Api")
                .should().accessClassesThat().haveSimpleNameEndingWith("Service")
                .check(classes);
    }

    @DisplayName("Service 레이어의 클래스는 DomainService 또는 Repository 레이어의 클래스를 호출하는 지 확인하는 테스트")
    @Test
    void article_service_dependency_persistence_repository_layer() {
        classes()
                .that().haveSimpleNameEndingWith("Service")
                .should().accessClassesThat().haveSimpleNameEndingWith("DomainService")
                .orShould().accessClassesThat().haveSimpleNameEndingWith("Repository");
    }

    @DisplayName("DomainService 레이어의 클래스는 repository 레이어의 클래스를 호출하고 있는지 확인하는 테스트")
    @Test
    void article_domainsService_called_persistence_repository_layer() {
        classes()
                .that().haveSimpleNameEndingWith("DomainService")
                .should().accessClassesThat().haveSimpleNameEndingWith("Repository")
                .check(classes);
    }

    @DisplayName("Article 서비스 패키지 의존성 테스트")
    @Test
    void dependency_article_service_check() {
        noClasses()
                .that().resideInAnyPackage("com.example.realworld.application.articles.service..")
                .or().resideInAnyPackage("com.example.realworld.application.articles.persistence..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("com.example.realworld.application.articles.presentation..")
                .because("서비스와 레포지토리는 웹 레이어에 의존하면 안된다.")
                .check(classes);
    }

    @DisplayName("서비스(Service) 패키지의 클래스를 컨트롤러(Presentation)에서만 접근하고 있는지 테스트")
    @Test
    void dependency_article_service_access_by_controller() {
        classes()
                .that().resideInAPackage("com.example.realworld.application.articles.service..")
                .should().onlyBeAccessed().byAnyPackage(
                        "com.example.realworld.application.articles.presentation..",
                        "com.example.realworld.application.articles.service..",
                        "com.example.realworld.application.follows.service..", // follow 한 사용자(feed) 글을 확인 하는 테스트
                        "com.example.realworld.application.favorites.service.." // 글 좋아요 테스트

                )
                .check(classes);
    }

    @DisplayName("레포지토리(Persistence) 패키지의 클래스를 서비스 레이어에서만 접근하고 있는지 테스트")
    @Test
    void dependency_article_repository_access_by_controller() {
        classes()
                .that().resideInAPackage("com.example.realworld.application.articles.persistence.repository..")
                .should().onlyBeAccessed().byAnyPackage(
                        "com.example.realworld.application.articles.service..",
                        "com.example.realworld.application.articles.persistence.repository.." // querydsl
                )
                .check(classes);
    }
}
