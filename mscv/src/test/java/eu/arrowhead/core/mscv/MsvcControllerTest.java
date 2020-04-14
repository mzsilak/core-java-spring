package eu.arrowhead.core.mscv;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.arrowhead.common.database.service.CommonDBService;
import eu.arrowhead.core.mscv.database.service.MscvCrudService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {MsvcTestContext.class})
public class MsvcControllerTest {


    //=================================================================================================
    // members

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "mockOnboardingDBService")
    private MscvCrudService onboardingDBService;

    @MockBean(name = "mockCommonDBService")
    private CommonDBService commonDBService;


    //=================================================================================================
    // methods

    //-------------------------------------------------------------------------------------------------
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    //=================================================================================================
    // Tests of onboarding controller

    @Test
    public void echoMsvc() throws Exception {
        final MvcResult response = this.mockMvc.perform(get("/msvc/echo")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("Got it!", response.getResponse().getContentAsString());
    }
}