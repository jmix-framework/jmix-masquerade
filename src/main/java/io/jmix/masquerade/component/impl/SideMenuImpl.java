package io.jmix.masquerade.component.impl;

import com.codeborne.selenide.SelenideElement;
import io.jmix.masquerade.Components;
import io.jmix.masquerade.Conditions;
import io.jmix.masquerade.component.Button;
import io.jmix.masquerade.component.SideMenu;
import io.jmix.masquerade.condition.SpecificCondition;
import com.leacox.motif.MatchesExact;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Quotes;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.Wait;
import static io.jmix.masquerade.Selectors.$j;
import static io.jmix.masquerade.Selectors.byChain;
import static io.jmix.masquerade.Selectors.byClassName;
import static io.jmix.masquerade.Selectors.byJTestId;
import static io.jmix.masquerade.sys.matcher.ConditionCases.componentApply;
import static com.leacox.motif.Motif.match;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class SideMenuImpl extends AbstractComponent<SideMenu> implements SideMenu {

    protected static final String SIDE_MENU_CONTAINER_CLASS_NAME = "jmix-sidemenu-container";
    protected static final String SIDE_MENU_ITEM_HEADER_OPEN = "jmix-sidemenu-item-header-open";
    protected static final String COLLAPSED_CLASS_NAME = "collapsed";

    protected static final By MENU_COLLAPSE_BUTTON = byJTestId("collapseMenuButton");

    public SideMenuImpl(By by) {
        super(by);
    }

    @Override
    public boolean apply(SpecificCondition condition) {
        return componentApply(match(condition), getDelegate())
                .when(MatchesExact.eq(Conditions.EXPANDED)).get(this::isExpanded)
                .when(MatchesExact.eq(Conditions.COLLAPSED)).get(this::isCollapsed)
                .getMatch();
    }

    @Override
    public <T> T openItem(Class<T> screenClass, String... path) {
        openItem(path);
        return Components.wire(screenClass);
    }

    @Override
    public <T> T openItem(Menu<T> menu) {
        openItem(menu.getPath());
        return Components.wire(menu.getScreenClass());
    }

    @Override
    public void openItem(String... path) {
        for (String s : path) {
            String itemXpath = String.format(
                    "//div[contains(@class, 'jmix-sidemenu-item') and @j-test-id=%s]",
                    Quotes.escape(s));

            SelenideElement menuItemElement = $(byXpath(itemXpath))
                    .shouldBe(visible)
                    .shouldBe(enabled);

            Wait().until(elementToBeClickable(menuItemElement));

            if (!menuItemElement.has(cssClass(SIDE_MENU_ITEM_HEADER_OPEN))) {
                menuItemElement.click();
            }
        }
    }

    @Override
    public SideMenu expand() {
        if (!isExpanded()) {
            toggleCollapsed();
        }

        return this;
    }

    @Override
    public SideMenu collapse() {
        if (!isCollapsed()) {
            toggleCollapsed();
        }

        return this;
    }

    protected boolean isCollapsed() {
        SelenideElement sideMenu = $(byChain(by, byClassName(SIDE_MENU_CONTAINER_CLASS_NAME)));
        return sideMenu.exists()
                && sideMenu.shouldBe(visible)
                .has(cssClass(COLLAPSED_CLASS_NAME));
    }

    protected boolean isExpanded() {
        return !isCollapsed();
    }

    protected void toggleCollapsed() {
        Button collapseMenuButton = $j(Button.class, MENU_COLLAPSE_BUTTON);
        if (collapseMenuButton.exists()) {
            collapseMenuButton.shouldBe(visible)
                    .click();
        }
    }
}
