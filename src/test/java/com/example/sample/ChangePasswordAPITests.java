package com.example.sample;

import com.example.sample.dto.ChangePasswordBody;
import com.example.sample.dto.User;
import com.example.sample.service.PasswordService;
import com.example.sample.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.*;

import javax.servlet.ServletContext;
import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SampleApplication.class)
public class ChangePasswordAPITests extends AbstractTestNGSpringContextTests {

    private static Logger logger = Logger.getLogger(ChangePasswordAPITests.class);
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private User thirdUser;
    private User fourthUser;

    /**
     * Creates Test Users
     *
     */
    private void createUsers() {
        thirdUser = new User("", "nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432");
        fourthUser = new User("", "nitishbector4@gmail.com", "Agoda123 Hello123 Test@!!");
    }


    @BeforeClass
    public void setup() {
        logger.debug("BeforeClass method is run...");
    }

    @BeforeMethod
    public void beforeTest() {
        logger.debug("BeforeMethod method is run...");
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        createUsers();
        userService.addUser(thirdUser);
        userService.addUser(fourthUser);
    }

    @AfterMethod
    public void afterTest(){
        logger.debug("AfterMethod method is run...");
        userService.removeUser(thirdUser);
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(webApplicationContext.getBean("userController"));
    }

    @Test(dataProvider = "credentialsProvider")
    public void changePasswordAPITest(ChangePasswordBody changePasswordBody) throws Exception
    {
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders
                .post("/user/changePassword")
                .content(objectMapper.writeValueAsString(changePasswordBody))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("true"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Password changed successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.message").value("Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").exists())
                .andReturn();

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }


    @Test(dataProvider = "invalidCredentialsProvider")
    public void invalidPasswordAPITest(ChangePasswordBody changePasswordBody)
    {
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc.perform( MockMvcRequestBuilders
                    .post("/user/changePassword")
                    .content(objectMapper.writeValueAsString(changePasswordBody))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isPreconditionFailed())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("false"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("new password doesn't match required format"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }


    @Test
    @DirtiesContext
    public void changePasswordAPIInvalidEmailTest() throws Exception
    {
        ChangePasswordBody changePasswordBody = new ChangePasswordBody("Test_agoda@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridndjsijdijscijs AfnDFFKDX432");
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders
                .post("/user/changePassword")
                .content(objectMapper.writeValueAsString(changePasswordBody))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("false"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User with Email ID doesn't exist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andReturn();

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void changePasswordAPINullFieldsTest() throws Exception
    {
        String body = "{\"newPassword\":\"aB@#rtttr2\",\"oldPassword\":\"aB@#rtttridndjsijdijsDFFKDX4321123456754\"}";
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders
                .post("/user/changePassword")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("false"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("emailId : must not be null, "))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andReturn();

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void changePasswordAPIXmlContentTypeTest() throws Exception
    {
        ChangePasswordBody changePasswordBody =new ChangePasswordBody("Test_agoda@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridndjsijdijscijs AfnDFFKDX432");
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders
                .post("/user/changePassword")
                .content(objectMapper.writeValueAsString(changePasswordBody))
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("false"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Content type 'application/xml' not supported"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andReturn();

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void changePasswordAPIUnsupportedHttpMethodTest()
    {
        ChangePasswordBody changePasswordBody =new ChangePasswordBody("Test_agoda@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridndjsijdijscijs AfnDFFKDX432");
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc.perform( MockMvcRequestBuilders
                    .put("/user/changePassword")
                    .content(objectMapper.writeValueAsString(changePasswordBody))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isMethodNotAllowed())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("false"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Request method 'PUT' not supported"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }

    @Test
    @DirtiesContext
    public void invalidUserTest() {
        ChangePasswordBody changePasswordBody = new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX42","aB@#rtttridndjsijdijscijs AfnDFFKDX432");
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc.perform( MockMvcRequestBuilders
                    .post("/user/changePassword")
                    .content(objectMapper.writeValueAsString(changePasswordBody))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("false"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("oldPassword not correct"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }

    @Test
    @DirtiesContext
    public void addUserTest() throws UnsupportedEncodingException {
        MvcResult mvcResult = null;
        String body = "{\"email\":\"test1@gmail.com\",\"password\": \"aB@#rtttridndjsijdijscijsAfnDFFKDX432\"}";
        String email = "test1@gmail.com";
        try {
            mvcResult = mockMvc.perform( MockMvcRequestBuilders
                    .post("/user/addUser")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("true"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User created"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(email))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.created").exists())
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());

    }

    @Test
    @DirtiesContext
    public void addDuplicateUserTest() throws UnsupportedEncodingException {
        MvcResult mvcResult = null;
        String body = "{\"email\":\"nitishbector.it@gmail.com\",\"password\": \"aB@#rtttridndjsijdijscijsAfnDFFKDX432\"}";

        try {
            mvcResult = mockMvc.perform( MockMvcRequestBuilders
                    .post("/user/addUser")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("false"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User already exist with email id"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());

    }


    @DataProvider(name = "credentialsProvider", parallel=false)
    public static Object[][] credentialsProvider() {
        ChangePasswordBody[][] changePasswordBodies=new ChangePasswordBody[5][1];
        //all conditiona matching
        changePasswordBodies[0][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridndjsijdijsDFFKDX4321123456754");
        //with 4 special characters
        changePasswordBodies[1][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridndjsijdijsDFFKDX4321123456754!$");
        //4 characters
        changePasswordBodies[2][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rttttridndjsijdijsDFFKDX4321123456754");
        //4 numbers
        changePasswordBodies[3][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridndjsijdijsDFFKDX44432112345675");
        //length = 18
        changePasswordBodies[4][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtrdKDX4321675");

        return changePasswordBodies;
    }

    @DataProvider(name = "invalidCredentialsProvider", parallel=false)
    public static Object[][] invalidCredentialsProvider() {
        ChangePasswordBody[][] changePasswordBodies=new ChangePasswordBody[17][1];
        //more than 4 different special characters
        changePasswordBodies[0][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#!$&rtttridndjsijdijsDFFKDX4321123456754");
        //more than 4 same special characters
        changePasswordBodies[1][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@@@@@rtttridndjsijdij sDFFKDX4321123456754");
        //more than 4 duplicate characters
        changePasswordBodies[2][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttttridndjsijdijsDF FKDX4321123456754");
        //more than 4 duplicate upper case characters
        changePasswordBodies[3][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rttridndjsijdijsDFFFFF K4321123456754");
        //more than 4 duplicate numbers
        changePasswordBodies[4][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridndjsijdij sDFFKDX444432112345675");
        //less than 18
        changePasswordBodies[5][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridKDX423");
        //more than 4 duplicate 2 numbers
        changePasswordBodies[6][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridndjsijdijsDFFKDX431111154444");
        //more than 50% numbers
        changePasswordBodies[7][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtridnDX432112345675445221");
        //numbers = 50% edge case
        changePasswordBodies[8][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtridnDX432112345675");

        //no upper case characters
        changePasswordBodies[9][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","a@#rtttridndjsijdijss4321156754");
        //no lower case characters
        changePasswordBodies[10][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","B@#DFFKDXRRTTBB23456754");

        //no Special characters
        changePasswordBodies[11][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aBrttdndjsijdijs DFFKDX4321123456754");
        //no numbers
        changePasswordBodies[12][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridndjsijdijs DFFKDX");

        //special characters like % ^ ( ) = -
        changePasswordBodies[13][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridndjsijdijsDFFK DX4321123456754(^");

        //password same as old password
        changePasswordBodies[14][0]=new ChangePasswordBody("nitishbector3@gmail.com", "aB@#rtttridndjsijdijscijs AfnDFFKDX432","aB@#rtttridndjsijdijscijs AfnDFFKDX432");

        //new password = 80 % old password
        changePasswordBodies[15][0]=new ChangePasswordBody("nitishbector4@gmail.com", "Agoda123 Hello123 Test@!!","Agoda123 Hello123 !!");

        //new password > 80 % old password
        changePasswordBodies[16][0]=new ChangePasswordBody("nitishbector4@gmail.com", "Agoda123 Hello123 Test@!!","Agoda123 Hello123 @!!");

        return changePasswordBodies;
    }

}
