package com.cgi.parizek.matej;

import com.cgi.parizek.matej.config.RedisConfig;
import com.cgi.parizek.matej.controller.PlayerController;
import com.cgi.parizek.matej.repository.IMissionRepository;
import com.cgi.parizek.matej.repository.IPlayerRepository;
import com.cgi.parizek.matej.service.MissionService;
import com.cgi.parizek.matej.service.PlayerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

@DirtiesContext
@ActiveProfiles("test")
@SpringBootTest
class PlayerMissionsApplicationTests {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(applicationContext);
        Assertions.assertNotNull(applicationContext.getBean(MissionService.class));
        Assertions.assertNotNull(applicationContext.getBean(PlayerService.class));
        Assertions.assertNotNull(applicationContext.getBean(PlayerController.class));
        Assertions.assertNotNull(
                applicationContext.getBean(LocalContainerEntityManagerFactoryBean.class));
        Assertions.assertNotNull(applicationContext.getBean(DataSource.class));
        Assertions.assertNotNull(applicationContext.getBean(RedisConfig.class));
        Assertions.assertNotNull(applicationContext.getBean(IMissionRepository.class));
        Assertions.assertNotNull(applicationContext.getBean(IPlayerRepository.class));
    }
}