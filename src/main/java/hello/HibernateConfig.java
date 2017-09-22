package hello;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

  /*@Autowired
  private EntityManagerFactory entityManagerFactory;

  @Bean
  public SessionFactory getSessionFactory() {
    if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
      throw new NullPointerException("factory is not a hibernate factory");
    }
    return entityManagerFactory.unwrap(SessionFactory.class);
  }*/

  /*@Value("${spring.datasource.db.driver}")
  private String DRIVER;

  @Value("${spring.datasource.password}")
  private String PASSWORD;

  @Value("${spring.datasource.url}")
  private String URL;

  @Value("${spring.datasource.username}")
  private String USERNAME;*/

  @Value("${jpa.hibernate.dialect}")
  private String DIALECT;

  @Value("${jpa.hibernate.show_sql}")
  private String SHOW_SQL;

  @Value("${jpa.hibernate.ddl-auto}")
  private String HBM2DDL_AUTO;

  @Value("${jpa.hibernate.entitymanager.packagesToScan}")
  private String PACKAGES_TO_SCAN;

  /*@Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(DRIVER);
    dataSource.setUrl(URL);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    return dataSource;
  }*/

  @Bean
  @ConfigurationProperties("spring.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  public LocalSessionFactoryBean sessionFactory() {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    sessionFactory.setPackagesToScan(PACKAGES_TO_SCAN);
    Properties hibernateProperties = new Properties();
    hibernateProperties.put("hibernate.dialect", DIALECT);
    hibernateProperties.put("hibernate.show_sql", SHOW_SQL);
    hibernateProperties.put("hibernate.hbm2ddl.auto", HBM2DDL_AUTO);
    sessionFactory.setHibernateProperties(hibernateProperties);
    return sessionFactory;
  }
}
