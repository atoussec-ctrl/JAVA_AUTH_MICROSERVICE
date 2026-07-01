package com.atous.auth.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.*;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages="com.atous.auth", importOptions=ImportOption.DoNotIncludeTests.class)
class CleanArchitectureTest {
    @ArchTest static final ArchRule domain_should_not_depend_on_spring_or_jpa = noClasses().that().resideInAPackage("..domain..").should().dependOnClassesThat().resideInAnyPackage("org.springframework..","jakarta.persistence..","jakarta.servlet..");
    @ArchTest static final ArchRule application_should_not_depend_on_infrastructure_or_presentation = noClasses().that().resideInAPackage("..application..").should().dependOnClassesThat().resideInAnyPackage("..infrastructure..","..presentation..");
}
