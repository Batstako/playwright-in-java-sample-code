package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.internal.Strings;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

//@UsePlaywright
public class ASimplePlaywrightTest {

    private static Playwright playwrith;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;


    @BeforeAll
    public static void setUpBrowser(){
        playwrith = Playwright.create();
        browser = playwrith.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
        );
        browserContext = browser.newContext();
        playwrith.selectors().setTestIdAttribute("data-test");
    }

    @BeforeEach
    public void setUp(){
        page = browserContext.newPage();
    }

    @AfterAll
    public static void tearDown(){
        browser.close();
        playwrith.close();
    }

    @Test
    void shouldShowThePageTitle() {

        page.navigate("https://practicesoftwaretesting.com");
        String title = page.title();

        Assertions.assertTrue(title.contains("Practice Software Testing"));

    }
    @Test
    void shouldSearchByKeyword(){

        page.navigate("https://practicesoftwaretesting.com");

        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();
        int matchingSearchResult = page.locator(".card").count();
        System.out.println(matchingSearchResult);

//        Locator searchField = page.locator("//input[@id='search-query']");
//        searchField.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
//        searchField.fill("Pliers");
//
//        page.locator("//button[@type='submit']").click();
//        page.waitForTimeout(3000);
//        int matchingSearchResult = page.locator("//a[@class='card']").count();
////        page.waitForTimeout(3000);
//        System.out.println(matchingSearchResult);
        Assertions.assertTrue(matchingSearchResult >= 0);

    }

    @Test
    void searchForPliers(){
        page.navigate("https://practicesoftwaretesting.com");

        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        int elementCount = page.locator(".card").count();
        System.out.println(elementCount);

        assertThat(page.locator(".card")).hasCount(4);

        List <String> productName = page.getByTestId("product-name").allTextContents();
        org.assertj.core.api.Assertions.assertThat(productName).allMatch(name -> name.contains("Pliers"));

        Locator outOfStockItem = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name");
        System.out.println(elementCount);
        assertThat(outOfStockItem).hasCount(1);
        assertThat(outOfStockItem).hasText("Long Nose Pliers");
    }
}
