jmix-masquerade
==========

Jmix UI testing library.

<a href="http://www.apache.org/licenses/LICENSE-2.0"><img src="https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat" alt="license" title=""></a>
<a href="https://travis-ci.org/cuba-platform/masquerade"><img src="https://travis-ci.org/cuba-platform/masquerade.svg?branch=master" alt="Build Status" title=""></a>

## Overview

The library provides an ability to create UI tests for Jmix-based applications. 
It can help you to write better tests.

It is based on:

* Java
* Selenide
* Selenium

# Getting started

## Creating test project 
    
Create a simple Java project in IntelliJ IDEA. The project should have the 
following structure:

```
+ src
  + main 
    + java
  + test
    + java
      + com/company/demo
+ build.gradle
+ settings.gradle  
```

Here’s the complete `build.gradle` file:

```groovy
apply plugin: 'java'

group = 'com.company.demo'
version = '0.1'

sourceCompatibility = 1.8

repositories {
    mavenCentral()    
}

dependencies {
    testImplementation 'com.codeborne:selenide:5.14.0'
    testImplementation 'org.codehaus.groovy:groovy:3.0.5'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.6.2'
    testRuntime 'org.junit.jupiter:junit-jupiter-engine:5.6.2'
    
     //the library for the UI testing
    testImplementation 'io.jmix.masquerade:jmix-masquerade:<check the latest version>'
    
    // enable logging
    testImplementation 'org.slf4j:slf4j-simple:1.7.30'
}
```

## Creating a test

In the `src/test/java` folder create a new package in the `com.company.demo` and name it `screen`. 
Create a new Java class in this package and name it `LoginScreen`. This class 
should be inherited from the `Composite\<T>` where `T` is the name of your 
class. This class will be used as a helper class, usually it declares UI 
components of an application screen / fragment / panel that is shown in a web page. 
Also, all test methods can be declared here.
 
All class attributes should be marked with the ```@Wire``` annotation. 
This annotation has optional `path` element which allows userService to define 
the path to the component using the `j-test-id` parameter. If the component does 
not have the `j-test-id` parameter, you can use the ```@FindBy``` annotation 
instead. This annotation has a list of optional parameters, like `name`, 
`className`, `id` and so on, which helps to identify the component.

The type of the attribute in the class corresponds to the type of the screen 
component. If the component has a type which is not defined in the library, use 
the `Untyped` type. 

The name of the attribute corresponds to the `j-test-id` attribute of a DOM 
element that corresponds to the UI component. 

```java
import io.jmix.masquerade.Wire;
import io.jmix.masquerade.base.Composite;
import io.jmix.masquerade.component.Button;
import io.jmix.masquerade.component.ComboBox;
import io.jmix.masquerade.component.Label;
import io.jmix.masquerade.component.PasswordField;
import io.jmix.masquerade.component.TextField;
import org.openqa.selenium.support.FindBy;


public class LoginScreen extends Composite<LoginScreen> {
    @Wire
    protected TextField usernameField;

    @Wire
    protected PasswordField passwordField;

    @Wire(path = {"loginFormLayout", "loginButton"})
    protected Button loginButton;

    @Wire
    protected ComboBox localesField;

    @Wire
    protected Label welcomeLabel;

    @FindBy(className = "c-login-caption")
    protected Label welcomeLabelTest;

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public ComboBox getLocalesField() {
        return localesField;
    }

    public Label getWelcomeLabel() {
        return welcomeLabel;
    }

    public Label getWelcomeLabelTest() {
        return welcomeLabelTest;
    }
}
``` 

Create a Java class in the `com.company.demo` package in the `src/test/java` folder. Name it `LoginScreenUiTest`. 

Create a new method and add ```@Test``` JUnit 5 annotation to it. The ```@Test``` 
annotation tells JUnit 5 that the public void method can be run as a test case. 
 
You can use all JUnit 5 annotations to improve the tests. Also it is possible to 
use a set of assertion methods provided by JUnit 5.
 
```java
import io.jmix.masquerade.component.Untyped;
import io.jmix.masquerade.screen.LoginScreen;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static io.jmix.masquerade.Components.wire;
import static io.jmix.masquerade.Conditions.EDITABLE;
import static io.jmix.masquerade.Conditions.ENABLED;
import static io.jmix.masquerade.Conditions.VISIBLE;
import static io.jmix.masquerade.Conditions.caption;
import static io.jmix.masquerade.Selectors.$j;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginUiTest {
    @Test
    public void login() {
        open("http://localhost:8080/app");

        LoginScreen loginScreen = $j(LoginScreen.class);

        assertNotNull(loginScreen.getUsernameField());
        assertNotNull(loginScreen.getPasswordField());

        loginScreen.getUsernameField()
                .shouldBe(EDITABLE)
                .shouldBe(ENABLED);

        loginScreen.getUsernameField().setValue("masquerade");
        loginScreen.getPasswordField().setValue("rulezzz");

        loginScreen.getWelcomeLabelTest()
                .shouldBe(VISIBLE);

        loginScreen.getLoginButton()
                .shouldBe(VISIBLE)
                .shouldBe(ENABLED)
                .shouldHave(caption("Submit"));

        String caption = loginScreen.getLoginButton().getCaption();
        boolean enabled = loginScreen.getLoginButton().is(ENABLED);

        Untyped loginFormLayout = wire(Untyped.class, "loginFormLayout");
        loginFormLayout.shouldBe(VISIBLE);

        loginScreen.getLoginButton().click();
    }
}
``` 

The `open()` method is a standard Selenide method. It opens a browser window 
with the given URL. The second line creates an instance of the masquerade 
Component and binds it to the UI component (LoginScreen) on the screen including 
all the annotated fields inside of the LoginScreen class. After that, you can 
access the screen components as class attributes. You can check the attributes 
visibility, get captions, set values, click the buttons and so on.

# Tips & Tricks

Here are some useful tips on how to work with the library.

## How to work with elements

The library has a special method  ```$j``` to define any element on the screen. 
This method has three implementations:

* The first implementation gets the element by its class:

    ```$j(Class<T> clazz)```
* The second implementation gets the element by its class and the path:

    ```$j(Class<T> clazz, String... path)```
* The third implementation gets the element by its class and _by_ selector:

    ```$j(Class<T> clazz, By by)```
    
For example, we can click the button on the screen: 

```java
import static io.jmix.masquerade.Components.$j;

$j(Button, 'logoutButton').click();
```

## How to check the state of an element

Selenide allows you to check some conditions.

To check if the element is enabled, visible or checked, use the `shouldBe` 
element. For example:

```java
loginButton
   .shouldBe(VISIBLE)
   .shouldBe(ENABLED);
```

To check if the element has some properties, use the `shouldHave` element. For example:

```java
welcomeLabel.shouldHave(Conditions.value('Welcome to Jmix application!'));
```    

## How to work with the Selenide elements
    
If the component does not have the `j-test-id` parameter, you can use the 
```@FindBy``` annotation. This annotation has a list of optional parameters, 
like `name`, `className`, `id` and so on, which helps to identify the component.

```java
@FindBy(className = "c-login-caption")
public Label welcomeLabelTest;
```    

Also, using this annotation, you can define `SelenideElement` type for the attribute 
instead of the types provides by masquerade. After that, you can use all test 
methods provided by Selenide. The name of the attribute can be any.

```java
import com.codeborne.selenide.SelenideElement;

@FindBy(className = "c-login-caption")
public SelenideElement welcomeLabelTest;
```   

Another way to define the `SelenideElement` type attribute is using the 
```@Wire``` annotation. You can write the `SelenideElement` type instead of 
masquerade types, but the name of the attribute should correspond to the 
`j-test-id` attribute of a DOM element that corresponds to the UI component.

```java
@Wire
public SelenideElement loginField;
```    

The third way to work with the Selenide elements is to use ```getDelegate()``` 
method. This method returns the `SelenideElement` type component. After that, you 
can use all test methods provided by Selenide.

```java
loginScreen.getDelegate().exists();
```    

## Useful tips for the Groovy tests

You can use any JVM language with the library including Groovy / Scala / Kotlin. 
There are some useful tips for those who use Groovy to write the tests. 

* .with() method.

Groovy closures have a delegate associated with them. The delegate can respond 
to method calls which happen inside of the closure. It enables you to use 
methods/properties within a `with {}` closure without having to repeat the 
object name each time.

```groovy
loginScreen.with {
    loginField.value = 'testUser'
    passwordField.value = '1'
    rememberMeCheckBox.checked = true

    commit()
}
```
* Ability to set the value of the element using "property access" syntax

In Groovy, getters and setters form what we call a "property", and offer a 
shortcut notation for accessing and setting such properties. So instead of the 
Java-way of calling getters / setters, you can use a field-like access notation: 

```groovy
loginField.value = 'testUser'
```

* def

```def``` means that the actual type of the value will be automatically inferred 
by the compiler. It eliminates the unnecessary boilerplate in variable 
declarations and makes your code shorter.

```groovy
def loginScreen = $j(LoginScreen)
```

## Run tests

To run the test, first of all, you need to set ```jmix.testMode``` property to 
true in the `application.properties` file in your Jmix application. After that you 
should start the application using Studio or Gradle tasks. To start application 
with Gradle, run the following tasks in the terminal:

    gradle bootRun

### Webdriver containers

[Testcontainers](https://www.testcontainers.org/modules/webdriver_containers/) can 
be used to automatically instantiate and manage containers that include web browsers, 
such as Chrome or Firefox. No need to have specific web browsers, or even a desktop 
environment, installed on test servers. The only dependency is a working Docker 
installation and your Java JUnit test suite.
Creation of browser containers is fast, so it's actually quite feasible to have a 
totally fresh browser instance for every test.

First of all, you need to add the testcontaines dependency in the `build.gradle` file:
```groovy
apply plugin: 'java'

group = 'com.company.demo'
version = '0.1'

sourceCompatibility = 1.8

repositories {
    mavenCentral()    
}

dependencies {
    testImplementation 'com.codeborne:selenide:5.14.0'
    testImplementation 'org.codehaus.groovy:groovy:3.0.5'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.6.2'
    testRuntime 'org.junit.jupiter:junit-jupiter-engine:5.6.2'

    // testcontainers 
    testImplementation 'org.testcontainers:selenium:1.14.3'
    testImplementation 'org.testcontainers:junit-jupiter:1.14.3'
    
     // the library for the UI testing
    testImplementation 'io.jmix.masquerade:jmix-masquerade:<check the latest version>'
    
    // enable logging
    testImplementation 'org.slf4j:slf4j-simple:1.7.30'
}
```

Secondly, you can create a JUnit 5 extension to run and configure a container with a browser.
```groovy
class ChromeExtension implements BeforeEachCallback, AfterEachCallback {

    private BrowserWebDriverContainer chrome

    @Override
    void beforeEach(ExtensionContext context) throws Exception {
        chrome = new BrowserWebDriverContainer()
                .withCapabilities(new ChromeOptions())
        chrome.start()
        WebDriverRunner.setWebDriver(browser.getWebDriver())
        Selenide.open('/')
    }

    @Override
    void afterEach(ExtensionContext context) throws Exception {
        WebDriverRunner.webDriver.manage().deleteAllCookies()
        Selenide.closeWebDriver()
        chrome.stop()
    }
}
```

Thirdly, declare the JUnit 5 extension in your test using `@ExtendWith` annotation
```groovy
@ExtendWith(ChromeExtension)
class ButtonSamplerUiTest {

    @Test
    @DisplayName("Checks that user can click on simple button")
    void clickOnSimpleButton() {
        open('http://localhost:8080/app/button-simple')

        $j(Button.class, 'helloButton')
                .shouldHave(caption('Say Hello!'))
                .click()

        $j(Notification.class)
                .shouldHave(caption('Hello, world!'))
    }
}
```

### Locally installed browser drivers

Please note that you need to download one of the latest versions of the web driver depending on the browser you want to use to testing.
For Chrome browser this is [chromedriver](http://chromedriver.chromium.org/downloads), for Firefox this is [geckodriver](https://github.com/mozilla/geckodriver/releases).

If you run your tests in Chrome browser, you need to edit standard
test configuration for the test project in IntelliJ. To do so, click the 
*Select Run/Debug Configuration* button and select *Edit Configurations*  in the 
drop-down list. In the VM options field, add the following:

    -Dselenide.browser=chrome -Dwebdriver.chrome.driver=<your_path>/chromedriver.exe

where `<your_path>` is the path to the chrome driver on your computer.

![Create Configuration](images/testConfiguration.png)

After that select the simple test or the test class you want to run, right 
click on it and select *Debug* option.

To run the tests using Gradle, add the following task to the ```build.gradle``` file:

```groovy
test {
     systemProperty 'selenide.browser', System.getProperty('selenide.browser')
     systemProperty 'webdriver.chrome.driver', System.getProperty('webdriver.chrome.driver')
}
```

After that, run the following task in the terminal:

    gradle test -Dselenide.browser=chrome -Dwebdriver.chrome.driver=<your_path>/chromedriver.exe
    
where `<your_path>` is the path to the chrome driver on your computer.
