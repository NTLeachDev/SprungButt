package com.nleachdev.derivativedi.framework

import com.nleachdev.derivativedi.framework.component.OtherTestingServiceImpl
import com.nleachdev.derivativedi.framework.component.TestingDependentService
import com.nleachdev.derivativedi.framework.component.TestingService
import com.nleachdev.derivativedi.framework.core.Container
import com.nleachdev.derivativedi.framework.domain.ContainerConfiguration
import com.nleachdev.derivativedi.framework.exception.MissingBeanDefinitionException
import com.nleachdev.derivativedi.framework.exception.MultipleBeanDefinitionException
import spock.lang.Specification

class IntegrationTest extends Specification {

    def setup() {
        final ContainerConfiguration config = ContainerConfiguration.getConfig(IntegrationTest)
                .withPropertyFile("application.properties")
                .withProfile("Test")
        Container.getInstance().startContainer(config)
    }

    def 'We can start up a DerivativeDI environment and request beans from the Container'() {
        given:
        final TestingService testingService = Container.getInstance().getBean(TestingService, "TestingServiceImpl")
        final Integer someInt = Container.getInstance().getBean(Integer, "someInt")

        when:
        final def results = testingService.getSomeInt()

        then:
        results == someInt
    }

    def 'We expect a MultipleBeanDefinitionException to be thrown if we request a bean type with multiple instances, without specifying the name'() {
        when:
        Container.getInstance().getBean(Integer)

        then:
        thrown(MultipleBeanDefinitionException)
    }

    def 'We can inject properties values into constructor fields to make available to the component'() {
        when:
        final String name = Container.getInstance().getBean(TestingDependentService).getName()

        then:
        name == "Nicholas Leach"
    }

    def 'We can use the provided default value if a property key is not found'() {
        when:
        final def results = Container.getInstance().getBean(OtherTestingServiceImpl).getSomeLong()

        then:
        results == 26
    }

    def 'When we setup the container with a specific profile, we do not expect to instantiate beans from a different profile'() {
        when:
        Container.getInstance().getBean(BigDecimal)

        then:
        thrown(MissingBeanDefinitionException)
    }

    def 'We do expect to get beans from the specified profile'() {
        when:
        final def results = Container.getInstance().getBean(List)

        then:
        results.sort() == ['Bar', 'Foo']
    }
}
