package infsus.pampol.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class CamundaConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/camunda/app/tasklist/default");
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() throws IOException {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(dataSource());
        configuration.setTransactionManager(transactionManager());
        configuration.setDatabaseSchemaUpdate("true");
        configuration.setJobExecutorActivate(true);
        configuration.setAuthorizationEnabled(true);
        configuration.setDefaultSerializationFormat("application/json");
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        configuration.setDeploymentResources(resolver.getResources("classpath:/bpmn/*.bpmn"));
        return configuration;
    }

    @Bean
    public ProcessEngine processEngine(SpringProcessEngineConfiguration configuration) {
        return configuration.buildProcessEngine();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }
}