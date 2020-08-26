/*
 * Copyright (c) 2008-2017 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.masquerade.components.impl;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.jmix.masquerade.Selectors.*;
import io.jmix.masquerade.components.Component;
import io.jmix.masquerade.components.TabSheet;
import io.jmix.masquerade.conditions.Caption;
import io.jmix.masquerade.conditions.CaptionContains;
import io.jmix.masquerade.conditions.SpecificCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Quotes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.jmix.masquerade.Conditions.SELECTED;
import static io.jmix.masquerade.Conditions.VISIBLE;
import static io.jmix.masquerade.Selectors.*;
import static io.jmix.masquerade.sys.matchers.ConditionCases.componentApply;
import static io.jmix.masquerade.sys.matchers.InstanceOfCases.hasType;
import static com.leacox.motif.MatchesExact.eq;
import static com.leacox.motif.Motif.match;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.xpath;

public class TabSheetImpl extends AbstractComponent<TabSheet> implements TabSheet {
    public TabSheetImpl(By by) {
        super(by);
    }

    @Override
    public Tab getTab(By tabBy) {
        return match(tabBy)
                .when(hasType(ByTargetText.class)).get(byText -> {
                    String text = byText.getElementText();

                    String tabXpath = ".//td[contains(@class, 'v-tabsheet-tabitemcell') " +
                            "and .//text()[normalize-space(.) = " + Quotes.escape(text) + "]]";

                    return new TabImpl(byChain(by, xpath(tabXpath)), "Tab.text: " + text);
                })
                .when(hasType(WithTargetText.class)).get(withText -> {
                    String text = withText.getElementText();

                    String tabXpath = ".//td[contains(@class, 'v-tabsheet-tabitemcell') " +
                            "and .//text()[contains(normalize-space(.), " + Quotes.escape(text) + ")]]";

                    return new TabImpl(byChain(by, xpath(tabXpath)), "Tab.withText: " + text);
                })
                .when(hasType(ByIndex.class)).get(byIndex -> {
                    int index = byIndex.getIndex();

                    String tabXpath = "(.//td[contains(@class, 'v-tabsheet-tabitemcell'))[" + index + "]";

                    return new TabImpl(byChain(by, xpath(tabXpath)), "Tab.index: " + index);
                })
                .when(hasType(ByJTestId.class)).get(byJTestId -> {
                    String id = byJTestId.getJTestId();

                    String tabXpath = ".//td[contains(@class, 'v-tabsheet-tabitemcell') " +
                            "and @j-test-id=" + Quotes.escape(id) + "]";

                    return new TabImpl(byChain(by, xpath(tabXpath)), "Tab.jTestId: " + id);
                })
                .getMatch();
    }

    @Override
    public List<Tab> getVisibleTabs() {
        shouldBe(VISIBLE);

        String tabsXpath = "./div[(contains(@class, 'v-tabsheet-tabcontainer'))]" +
                "//td[contains(@class, 'v-tabsheet-tabitemcell')]";

        ElementsCollection elements = $$(byChain(by, byXpath(tabsXpath)));

        List<Tab> tabs = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {
            tabs.add(new TabImpl(byTarget(elements.get(i)), "Tab.index: " + i));
        }

        return tabs;
    }

    public class TabImpl extends AbstractSpecificConditionHandler<Tab> implements Tab {
        protected final SelenideElement impl;
        protected final String loggingId;
        protected final By by;

        public TabImpl(By by) {
            this(by, null);
        }

        public TabImpl(By by, String loggingId) {
            this.by = by;
            this.impl = $(by);
            this.loggingId = loggingId;
        }

        @Override
        public void select() {
            impl.shouldBe(visible)
                    .shouldNotHave(cssClass("v-tabsheet-tabitemcell-selected"))
                    .find(className("v-caption"))
                    .click();
        }

        @Override
        public void close() {
            $(byChain(by, byClassName("v-tabsheet-caption-close")))
                    .shouldBe(visible)
                    .click();
        }

        @Override
        public By getBy() {
            return by;
        }

        @Override
        public SelenideElement getDelegate() {
            return impl;
        }

        @Override
        public Component getParent() {
            return TabSheetImpl.this;
        }

        @Nullable
        @Override
        public String getLoggingId() {
            return loggingId;
        }

        @SuppressWarnings("CodeBlock2Expr")
        @Override
        public boolean apply(SpecificCondition condition) {
            return componentApply(match(condition), getDelegate())
                    .when(eq(SELECTED)).get(() -> {
                        return impl.has(cssClass("v-tabsheet-tabitemcell-selected"));
                    })
                    .when(hasType(Caption.class)).get(caption -> {
                        return $(byChain(by, byXpath(".//div[contains(@class, 'v-captiontext')]")))
                                .has(exactText(caption.getCaption()));
                    })
                    .when(hasType(CaptionContains.class)).get(caption -> {
                        return $(byChain(by, byXpath(".//div[contains(@class, 'v-captiontext')]")))
                                .has(text(caption.getCaptionSubstring()));
                    })
                    .getMatch();
        }
    }
}