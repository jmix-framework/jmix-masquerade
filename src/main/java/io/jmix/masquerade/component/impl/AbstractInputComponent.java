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

package io.jmix.masquerade.component.impl;

import com.codeborne.selenide.SelenideElement;
import io.jmix.masquerade.component.Component;
import io.jmix.masquerade.condition.SpecificCondition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static io.jmix.masquerade.Selectors.byChain;
import static io.jmix.masquerade.sys.TagNames.INPUT;
import static io.jmix.masquerade.sys.matcher.ConditionCases.fieldApply;
import static com.leacox.motif.Motif.match;

public class AbstractInputComponent<T extends Component> extends AbstractComponent<T> {
    protected AbstractInputComponent(By by) {
        super(by);
    }

    protected SelenideElement getInputDelegate() {
        return $(byChain(by, INPUT));
    }

    @Override
    public boolean apply(SpecificCondition condition) {
        return fieldApply(match(condition), getDelegate(), getInputDelegate())
                .getMatch();
    }
}