package es.uca.iw.carteruca.views.registro;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import es.uca.iw.carteruca.models.Centro;
import es.uca.iw.carteruca.models.Usuario;
import es.uca.iw.carteruca.services.CentroService;
import es.uca.iw.carteruca.services.EmailService;
import es.uca.iw.carteruca.services.UsuarioService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationAndActivation {

    private final String uribase = "http://localhost:";
    @LocalServerPort
    private int port;
    private WebDriver driver;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CentroService centroService;

    @MockBean
    private EmailService emailService; // Inyecta el EmailFakeService

    private Usuario testUser;

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }

        if (testUser != null && testUser.getId() != null) {
            usuarioService.deleteUser(testUser.getId());
        }

    }

    @Given("An user with name {string} is registered on the system")
    public void an_user_is_registered_on_the_system(String username) {

        testUser = (Usuario) usuarioService.loadUserByUsername(username);

    }

    @When("The user {string} with email {string} and password {string} registers on the app")
    public void registerUser(String username, String email, String password) {

        // Given
        Centro testCentro = new Centro();
        testCentro.setNombre("Centro de Prueba");
        testCentro.setAcronimo("CP");
        centroService.addCentro(testCentro);

        // a certain user
        testUser = new Usuario();
        testUser.setNombre(username);
        testUser.setApellidos(username);
        testUser.setUsername(username);
        testUser.setEmail(email);
        testUser.setPassword(password);
        testUser.setCentro(testCentro);

        // When

        // point the browser to the activation page
        driver.get(uribase + port + "/registro");

        // and introduce form data
        driver.findElement(By.id("nombre")).sendKeys(testUser.getNombre());
        driver.findElement(By.id("apellidos")).sendKeys(testUser.getApellidos());
        driver.findElement(By.id("usuario")).sendKeys("u00000001");
        driver.findElement(By.id("email")).sendKeys(testUser.getEmail());
        driver.findElement(By.id("centro")).sendKeys(testUser.getCentro().getNombre());
        driver.findElement(By.id("password")).sendKeys(testUser.getPassword());
        driver.findElement(By.id("repetir_password")).sendKeys(testUser.getPassword());
        driver.findElement(By.id("checkbox")).click();

        // and press the activate button
        driver.findElement(By.id("registro")).click();

        // Then
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {}

//        assertThat(driver.getCurrentUrl()).isNotEqualTo(uribase + port + "/registro");
        assertThat(driver.getCurrentUrl()).isEqualTo(uribase + port + "/");
    }

    @When("The user {string} introduces their email {string} and a verification code to activate")
    public void the_user_introduces_their_email_and_a_verification_code(String username, String email) {

        // HTTP web invocation
        driver.get(uribase + port + "/useractivation");

        // user interaction
        driver.findElement(By.id("email")).sendKeys(email);

        if (testUser != null) {
            driver.findElement(By.id("secretCode")).sendKeys(
                    usuarioService.findAllUsuariosExcludingAdmin().stream()
                            .filter(usuario -> testUser.getEmail().equals(usuario.getEmail()))
                            .findFirst().get().getCodigoRegistro()
            );
        } else {
            driver.findElement(By.id("secretCode")).sendKeys("randomkey");
        }

        driver.findElement(By.id("activate")).click();

    }

    @Then("The user gets a message with the text {string}")
    public void the_user_gets_a_message_with_the_text(String text) {
        // Assertion

        WebElement element = driver.findElement(By.id("status"));

        assertThat(element.getText().equals(text)).isTrue();

    }

}