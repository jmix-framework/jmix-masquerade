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

import com.codeborne.selenide.SelenideElement;
import io.jmix.masquerade.components.DateField;
import io.jmix.masquerade.conditions.DateValue;
import io.jmix.masquerade.conditions.SpecificCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static io.jmix.masquerade.Selectors.byChain;
import static io.jmix.masquerade.components.impl.DateTimeFieldImpl.DATEPART;
import static io.jmix.masquerade.sys.matchers.ConditionCases.componentApply;
import static io.jmix.masquerade.sys.matchers.InstanceOfCases.hasType;
import static com.leacox.motif.Motif.match;

public class DateFieldImpl extends AbstractInputComponent<DateField> implements DateField {

    public DateFieldImpl(By by) {
        super(by);
    }

    @Override
    public boolean apply(SpecificCondition condition) {
        return componentApply(match(condition), impl)
                .when(hasType(DateValue.class)).get(dv -> {
                    SelenideElement dateFieldImpl = $(byChain(by, DATEPART));
                    return dateFieldImpl.has(exactValue(dv.getExpectedValue()));
                })
                .getMatch();
    }

    @Override
    public String getDateValue() {
        return getInputDelegate()
                .shouldBe(visible)
                .shouldBe(enabled)
                .getValue();
    }

    @Override
    public DateField setDateValue(String value) {
        getInputDelegate().shouldBe(visible)
                .shouldBe(enabled)
                .shouldNotBe(readonly)
                .click();

        getInputDelegate().sendKeys(Keys.HOME, value);

        return this;
    }
}