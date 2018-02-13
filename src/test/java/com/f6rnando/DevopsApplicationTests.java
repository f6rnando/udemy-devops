package com.f6rnando;

import com.f6rnando.backend.service.I18NService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevopsApplicationTests {

	@Autowired
    private I18NService i18NService;

	@Test
    public void testMessageByLocalService() throws Exception {
	    String expectedResult = "Bootstrap starter template";
	    String messageId = "index.main.callout";
	    String actual = i18NService.getMessage(messageId);
        Assert.assertEquals("The actual and expected String don't match", expectedResult, actual);
    }

}
