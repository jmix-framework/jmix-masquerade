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
import io.jmix.masquerade.Components;
import io.jmix.masquerade.components.EntityComboBox;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.$;
import static com.google.common.base.Strings.isNullOrEmpty;
import static io.jmix.masquerade.Selectors.byChain;
import static io.jmix.masquerade.Selectors.byCubaId;
import static io.jmix.masquerade.components.impl.ComboBoxImpl.VAADIN_COMBOBOX_OPTIONLIST;
import static io.jmix.masquerade.components.impl.ComboBoxImpl.V_FILTERSELECT_BUTTON;
import static io.jmix.masquerade.sys.TagNames.DIV;
import static io.jmix.masquerade.sys.VaadinClassNames.disabledClass;

public class EntityComboBoxImpl extends AbstractInputComponent<EntityComboBox> implements EntityComboBox {
    public EntityComboBoxImpl(By by) {
        super(by);
    }

    @Override
    public String getValue() {
        return getInputDelegate()
                .shouldBe(visible)
                .getValue();
    }

    @Override
    public EntityComboBox setValue(String value) {
        // todo support textInputAllowed = false
        setFilter(value);

        if (isNullOrEmpty(value)) {
            openOptionsPopup().select(value);
        } else {
            getOptionsPopup().select(value);
        }

        return this;
    }

    @Override
    public EntityComboBox setFilter(String filter) {
        SelenideElement inputImpl = getInputDelegate();

        inputImpl.shouldBe(visible)
                .shouldNotBe(readonly)
                .shouldBe(enabled)
                .click();

        if (StringUtils.isNotEmpty(inputImpl.getValue())) {
            // remove all if needed
            inputImpl.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        }

        if (!isNullOrEmpty(filter)) {
            // todo may be replace with javascript set to speed up this call
            inputImpl.sendKeys(filter);
        }

        return this;
    }

    @Override
    public void triggerAction(Action action) {
        $(byChain(by, DIV, byCubaId(action.getId())))
                .shouldBe(visible)
                .shouldNotHave(disabledClass)
                .click();
    }

    @Override
    public <T> T triggerAction(Class<T> clazz, Action action) {
        triggerAction(action);
        return Components.wire(clazz);
    }

    @Override
    public OptionsPopup<EntityComboBox> openOptionsPopup() {
        $(byChain(by, byClassName(V_FILTERSELECT_BUTTON)))
                .shouldBe(visible)
                .click();

        OptionsPopupImpl<EntityComboBox> optionsPopup = getOptionsPopupElement();
        optionsPopup.shouldBe(visible);

        return optionsPopup;
    }

    @Override
    public OptionsPopup<EntityComboBox> getOptionsPopup() {
        OptionsPopupImpl<EntityComboBox> optionsPopup = getOptionsPopupElement();
        optionsPopup.shouldBe(visible);

        return optionsPopup;
    }

    @Override
    public EntityComboBox closeOptionsPopup() {
        OptionsPopupImpl<EntityComboBox> optionsPopup = getOptionsPopupElement();
        optionsPopup.shouldBe(visible);

        $(byChain(by, DIV))
                .shouldBe(visible)
                .click();

        return this;
    }

    protected OptionsPopupImpl<EntityComboBox> getOptionsPopupElement() {
        return new OptionsPopupImpl<>(VAADIN_COMBOBOX_OPTIONLIST, this);
    }
}