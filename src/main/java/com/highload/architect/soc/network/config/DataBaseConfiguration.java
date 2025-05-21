package com.highload.architect.soc.network.config;

import com.highload.architect.soc.network.config.routingdatasource.ReplicationRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DataBaseConfiguration {
    @Value("${datasource.driverClassName}")
    private String driverClassName;

    @Value("${datasource.url}")
    private String dataSourceUrl;

    @Value("${datasource.username}")
    private String userName;

    @Value("${datasource.password}")
    private String password;

    @Primary
    @Bean
    @DependsOn({"writeDataSource", "readDataSource1", "readDataSource2", "routingDataSource"})
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public DataSource routingDataSource() {
        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("write", writeDataSource());
        dataSourceMap.put("read1", readDataSource1());
        dataSourceMap.put("read2", readDataSource2());
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(writeDataSource());

        return routingDataSource;
    }

    @Bean//(destroyMethod = "shutdown")
    public DataSource writeDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(dataSourceUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        fillDefaultProperties(dataSource);

        return dataSource;
    }

    @Bean//(destroyMethod = "shutdown")
    public DataSource readDataSource1() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(dataSourceUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        fillDefaultProperties(dataSource);

        return dataSource;
    }

    @Bean//(destroyMethod = "shutdown")
    public DataSource readDataSource2() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(dataSourceUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        fillDefaultProperties(dataSource);

        return dataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.highload.architect.soc.network.model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "false");
        return properties;
    }

    private void fillDefaultProperties(HikariDataSource dataSource) {
        // Дополнительные параметры настройки HikariCP
        dataSource.setMaximumPoolSize(10);          // Максимальное число соединений в пуле
        dataSource.setMinimumIdle(0);               // Минимальное количество свободных соединений в пуле
        dataSource.setConnectionTimeout(30_000);    // Максимальное время в миллисекундах, которое приложение будет ожидать получения соединения из пула
        dataSource.setIdleTimeout(600_000);         // Максимальное время в миллисекундах, которое соединение может оставаться в режиме idle в пуле
        dataSource.setMaxLifetime(1_800_000);       // Максимальное время в миллисекундах, прежде чем соединение будет закрыто (например, перезапущено)
    }
}
