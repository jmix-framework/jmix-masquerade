/*
 * Copyright (c) 2008-2020 Haulmont.
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

package io.jmix.masquerade.component.impl;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.jmix.masquerade.Conditions;
import io.jmix.masquerade.component.CheckBox;
import io.jmix.masquerade.condition.Caption;
import io.jmix.masquerade.condition.CaptionContains;
import io.jmix.masquerade.condition.CheckBoxChecked;
import io.jmix.masquerade.condition.SpecificCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static io.jmix.masquerade.Conditions.EDITABLE;
import static io.jmix.masquerade.Conditions.READONLY;
import static io.jmix.masquerade.Selectors.byChain;
import static io.jmix.masquerade.sys.TagNames.INPUT;
import static io.jmix.masquerade.sys.TagNames.LABEL;
import static io.jmix.masquerade.sys.VaadinClassNames.readonlyClass;
import static io.jmix.masquerade.sys.matcher.ConditionCases.componentApply;
import static io.jmix.masquerade.sys.matcher.InstanceOfCases.hasType;
import static com.leacox.motif.MatchesExact.eq;
import static com.leacox.motif.Motif.match;

public class CheckBoxImpl extends AbstractComponent<CheckBox> implements CheckBox {

    protected static final Condition CHECKBOX_CHECKED = new CheckBoxChecked("checked");

    public CheckBoxImpl(By by) {
        super(by);
    }

    @Override
    public boolean apply(SpecificCondition condition) {
        return componentApply(match(condition), getDelegate())
                .when(eq(Conditions.CHECKED)).get(() ->
                        $(byChain(by, INPUT)).is(CHECKBOX_CHECKED)
                )
                .when(eq(Conditions.SELECTED)).get(() ->
                        $(byChain(by, INPUT)).is(Condition.selected)
                )
                .when(hasType(Caption.class)).get(c ->
                        impl.has(exactText(c.getCaption()))
                )
                .when(hasType(CaptionContains.class)).get(cc ->
                        impl.has(text(cc.getCaptionSubstring()))
                )
                .when(eq(READONLY)).get(() ->
                        impl.has(readonlyClass)
                )
                .when(eq(EDITABLE)).get(() ->
                        !impl.has(readonlyClass)
                )
                .getMatch();
    }

    @Override
    public CheckBox setChecked(boolean checked) {
        SelenideElement checkBoxInput = $(byChain(by, INPUT))
                .shouldBe(visible)
                .shouldBe(enabled);

        if (checked != checkBoxInput.is(CHECKBOX_CHECKED)) {
            checkBoxInput.sendKeys(Keys.SPACE);
        }

        return this;
    }

    @Override
    public boolean isChecked() {
        return is(Conditions.CHECKED);
    }

    @Override
    public String getCaption() {
        return $(byChain(by, LABEL)).getText();
    }
}