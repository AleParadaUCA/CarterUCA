package es.uca.iw.carteruca.views.registro;

import es.uca.iw.carteruca.models.UsuarioTest;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.services.UsuarioService;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.mock.mockito.MockBean;

import es.uca.iw.carteruca.services.EmailService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//La etiqueta @Transactional no funciona con selenium y hay que restaurar manualmente el estado de la BD
public class ActivarTest {
    private final String uribase = "http://127.0.0.1:";
    @LocalServerPort
    private int port;
    private WebDriver driver;
    private Usuario testUser;

    @MockBean
    private EmailService emailService; // Inyecta el EmailFakeService

    @Autowired
    private UsuarioService usuarioService;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();

    }

    @BeforeEach
    public void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");
//        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("disable-gpu");
        chromeOptions.addArguments("--window-size=1920,1200");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }

        if (testUser != null && testUser.getId() != null) {
            usuarioService.deleteUser(testUser.getId());
        }
    }

    @Test
    public void shouldNotActivateANoExistingUser() {

        // Given
        // a certain user
        testUser = UsuarioTest.createTestUsuario();

        // point the browser to the activation page
        driver.get(uribase + port + "/useractivation");

        // and introduce form data
        driver.findElement(By.id("email")).sendKeys(testUser.getEmail());
        driver.findElement(By.id("secretCode")).sendKeys("key");

        // and press the activate button
        driver.findElement(By.id("activate")).click();

        // and
        WebElement element = driver.findElement(By.id("status"));

        assertThat(element.getText().equals("Ups. No se ha podido activar el usuario")).isTrue();

    }


    @Test
    public void shouldActivateAnExistingUser() {

        // Given
        // a certain user
        testUser = UsuarioTest.createTestUsuario();

        //who is registered
        usuarioService.createUser(testUser.getNombre(), testUser.getApellidos(), testUser.getUsername(), testUser.getEmail(), testUser.getPassword(), testUser.getCentro());

        // When

        // point the browser to the activation page
        driver.get(uribase + port + "/useractivation");

        // and introduce form data
        driver.findElement(By.id("email")).sendKeys(testUser.getEmail());
        driver.findElement(By.id("secretCode")).sendKeys(testUser.getCodigoRegistro());

        // and press the activate button
        driver.findElement(By.id("activate")).click();

        // Then
        WebElement element = driver.findElement(By.id("status"));

        assertThat(element.getText().equals("Felicidades. El usuario ha sido activado")).isTrue();

    }
}
