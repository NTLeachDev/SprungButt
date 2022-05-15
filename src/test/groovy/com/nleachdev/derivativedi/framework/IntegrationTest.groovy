package com.nleachdev.derivativedi.framework

import com.nleachdev.derivativedi.framework.component.OtherTestingServiceImpl
import com.nleachdev.derivativedi.framework.component.TestingDependentService
import com.nleachdev.derivativedi.framework.component.TestingService
import com.nleachdev.derivativedi.framework.core.Containerr
import com.nleachdev.derivativedi.framework.config.ContainerConfig
import com.nleachdev.derivativedi.framework.exception.MissingBeanDefinitionException
import com.nleachdev.derivativedi.framework.exception.MultipleBeanDefinitionException
import spock.lang.Specification

class IntegrationTest extends Specification {

    def setup() {
        final ContainerConfig config = ContainerConfig.getConfig(IntegrationTest)
                .withPropertyFile("application.properties")
                .withProfile("Test")
        Containerr.getInstance().startContainer(config)
    }

    def 'We can start up a DerivativeDI environment and request beans from the Container'() {
        given:
        final TestingService testingService = Containerr.getInstance().getBean(TestingService, "TestingServiceImpl")
        final Integer someInt = Containerr.getInstance().getBean(Integer, "someInt")

        when:
        final def results = testingService.getSomeInt()

        then:
        results == someInt
    }

    def 'We expect a MultipleBeanDefinitionException to be thrown if we request a bean type with multiple instances, without specifying the name'() {
        when:
        Containerr.getInstance().getBean(Integer)

        then:
        thrown(MultipleBeanDefinitionException)
    }

    def 'We can inject properties values into constructor fields to make available to the component'() {
        when:
        final String name = Containerr.getInstance().getBean(TestingDependentService).getName()

        then:
        name == "Nicholas Leach"
    }

    def 'We can use the provided default value if a property key is not found'() {
        when:
        final def results = Containerr.getInstance().getBean(OtherTestingServiceImpl).getSomeLong()

        then:
        results == 26
    }

    def 'When we setup the container with a specific profile, we do not expect to instantiate beans from a different profile'() {
        when:
        Containerr.getInstance().getBean(BigDecimal)

        then:
        thrown(MissingBeanDefinitionException)
    }

    def 'We do expect to get beans from the specified profile'() {
        when:
        final def results = Containerr.getInstance().getBean(List)

        then:
        results.sort() == ['Bar', 'Foo']
    }
}
