# PV260-assignment2 - Pavel Synek, Marek Turis

# Task 1
## Found bugs
- Expected: “You are called (name)”. Actual: “Welcome (name)”.
- In some cases, surname is not displayed. Repro steps: refresh the page enough times.

## Other testing methodologies
- Acceptance testing (Selenium)

## Other questions
- What should the application do with empty input.
- Should it accept numbers as name/surname?
- Is the input limited by length?


# Task 2
- **Attributes:** speed, accessibility, user-friendliness, well-tested, internationalisation and localisation, extendability, availability on all platforms
- **Components:** cart, product list, product detail, registration, checkout, payment, administration, wish-list, help, search, user comments, product rating
- **Capabilities for user-friendliness and wish-list:** easy adding the product to wish-list from product detail, sharing wish-list with friends, public wish-list
- **Other capabilities:** adding comments to products, adding reviews to products, adding new products in administration, deleting comments in administration, payment via credit card, adding new category in administration, adding products to cart, choosing delivery method, buying a gift card, showing related products
- **Test cases:**
  1. Authorized user opens Amazon Fire product detail and adds comment with empty text. It should display error message “You cannot post empty comment”.
  2. Authorized user opens Amazon Fire product detail and adds comment with text “fuck fuck fuck shit”. Adding comment succeeds and comment contains text “**** **** **** ****”.
  3. Authorized user opens Amazon Fire product detail and adds comment with text “This thing changed my life”. Adding comment succeeds and comment contains text “This thing changed my life”.
- **Risky places:** _credit card payment_ (customers may have their credit card info stolen in case of security breach), _authentication_ (attacker can intercept customer’s login credentials), _checkout_ (attacker may get customer’s address and other sensitive data), _user database_ (attacker may physically steal the machine where the database runs on), _product price_ (company would lose money when price is not set correctly), _product handling_ (customer gets incorrect product and we lose him)

# Task 3

```java
public class SeleniumTest {

	private WebDriver webDriver;

	@Before
	public void setUp() {
		webDriver = new ChromeDriver();
	}

	@After
	public void tearDown() {
		webDriver.close();
	}

	@Test
	public void search() {
		webDriver.get("https://en.wikipedia.org");

		WebElement searchField = webDriver.findElement(By.id("searchInput"));

		searchField.sendKeys("Software quality");

		searchField.submit();

		assertThat(getPageHeader()).isEqualTo("Software quality");
	}

	@Test
	public void languageChange() {
		webDriver.get("https://en.wikipedia.org/wiki/Software_quality");

		WebElement link = webDriver.findElement(By.linkText("Deutsch"));

		link.click();

		assertThat(getPageHeader()).isEqualTo("Softwarequalität");
	}

	@Test
	public void loginFail() {
		webDriver.get("https://en.wikipedia.org/w/index.php?title=Special:UserLogin");

		fillInput("wpName1", "admin");
		fillInput("wpPassword1", "admin");

		webDriver.findElement(By.id("wpLoginAttempt")).click();

		assertThat(webDriver.findElement(By.className("errorbox")).getText()).contains("Incorrect password entered");
	}

	@Test
	public void permanentUrlGeneration() throws Exception {
		webDriver.get("https://en.wikipedia.org/wiki/Software_quality");

		WebElement link = webDriver.findElement(By.linkText("Permanent link"));

		link.click();

		String[] urlParts = webDriver.getCurrentUrl().split("=");
		String oldId = urlParts[urlParts.length - 1];

		webDriver.get(String.format("https://en.wikipedia.org/w/index.php?oldid=%s", oldId));

		assertThat(getPageHeader()).isEqualTo("Software quality");
	}

	@Test
	public void printableVersion() throws Exception {
		webDriver.get("https://en.wikipedia.org/wiki/Software_quality");

		WebElement link = webDriver.findElement(By.linkText("Printable version"));

		link.click();

		assertThat(webDriver.findElement(By.id("mw-navigation")).isDisplayed()).isFalse();
	}

	private String getPageHeader() {
		WebElement header = webDriver.findElement(By.id("firstHeading"));

		return header.getText();
	}

	private void fillInput(String id, String text) {
		WebElement input = webDriver.findElement(By.id(id));

		input.sendKeys(text);
	}
}

```

# Task 4
https://github.com/PavelSynek/PV260-assignment2/tree/master/src/test/java/cz.muni.fi.pv260.productfilter
