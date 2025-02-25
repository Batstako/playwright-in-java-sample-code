package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ASimplePlaywrightTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;

    Page page;

    @BeforeAll
    public static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
        );
    }

    @BeforeEach
    public void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterAll
    public static void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void shouldShowThePageTitle() {
        page.navigate("https://practicesoftwaretesting.com");
        String title = page.title();
        Assertions.assertTrue(title.contains("Practice Software Testing"));
    }

    @Test
    void shouldShowSearchTermsInTheTitle() {
        page.navigate("https://practicesoftwaretesting.com");
        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();

        int matchingProductCount = page.locator(".card-title").count();

        Assertions.assertTrue(matchingProductCount > 0);
    }

    @DisplayName("Interacting with text fields")
    @Nested
    class WhenInteractingWithTextField {
        @BeforeEach
        void openContactPage(){ page.navigate("https://practicesoftwaretesting.com/contact");}

        @DisplayName("Complete the Form")
        @Test
        void completeForm(){
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailNameField = page.getByLabel("Email");
            var messageNameField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");

            firstNameField.fill("Sahra-jane");
            lastNameField.fill("Smith");
            emailNameField.fill("sahra@tino.com");
            messageNameField.fill("Hello, world");
            subjectField.selectOption("Warranty");
//            subjectField.selectOption(new SelectOption().setIndex(5));

            assertThat(firstNameField).hasValue("Sahra-jane");
            assertThat(lastNameField).hasValue("Smith");
            assertThat(emailNameField).hasValue("sahra@tino.com");
            assertThat(messageNameField).hasValue("Hello, world");
            assertThat(subjectField).hasValue("warranty");

        }

        @DisplayName("Mandatory fields")
        @ParameterizedTest
        @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
        void mandatoryFields(String fieldName){
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailNameField = page.getByLabel("Email");
            var messageNameField = page.getByLabel("Message");
//            var subjectField = page.getByLabel("Subject");
            var sendButton = page.getByText("Send");


            //Fill in the field values
            firstNameField.fill("Sahra-jane");
            lastNameField.fill("Smith");
            emailNameField.fill("sahra@tino.com");
            messageNameField.fill("Hello, world");
//            subjectField.selectOption("Warranty");

            //Clear one of the fields
            page.getByLabel(fieldName).clear();

            sendButton.click();

            //Check the error message for that field


            var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");
//            assertThat(errorMessage).isVisible();
        }
    }

}
