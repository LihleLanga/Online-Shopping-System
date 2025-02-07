package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import za.ac.cput.domain.Account;
import za.ac.cput.factory.AccountFactory;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerTest {
    @LocalServerPort
    private int port;

    @Autowired private AccountController controller;
    @Autowired private TestRestTemplate restTemplate;

    private Account account;
    private String baseUrl;

    @BeforeEach
    void setUp(){
        this.account = AccountFactory.saveAccount("1st of July, 2021","1st of July, 2026");
        this.baseUrl = "http://localhost:" + this.port + "/online-shopping-system/account/";
    }

    @Test
    void a_save(){
        String url = baseUrl + "save";
        System.out.println(url);
        ResponseEntity<Account> response = this.restTemplate
                .postForEntity(url, this.account, Account.class);
        System.out.println(response);
        assertAll(
                () -> assertEquals(HttpStatus.OK,response.getStatusCode()),
                () -> assertNotNull(response.getBody())
        );
    }

    @Test
    void b_read(){
        String url = baseUrl + "read/" + this.account.getAccountId();
        System.out.println(url);
        ResponseEntity<Account> response = this.restTemplate.getForEntity(url, Account.class);
        System.out.println(response);
        assertAll(
                ()-> assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()-> assertNotNull(response.getBody())
        );
    }

    @Test
    void d_delete(){
        String url = baseUrl + "delete/" + this.account.getAccountId();
        System.out.println(url);
        this.restTemplate.delete(url);
    }

    @Test
    void c_findAll(){
        String url = baseUrl + "all";
        System.out.println(url);
        ResponseEntity<Account []> response =
                this.restTemplate.getForEntity(url, Account[].class);
        System.out.println(Arrays.asList(response.getBody()));
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, response.getBody().length == 0)
        );
    }
}