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
import io.jmix.masquerade.component.ComboBox;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.google.common.base.Strings.isNullOrEmpty;
import static io.jmix.masquerade.Selectors.byChain;
import static io.jmix.masquerade.sys.TagNames.DIV;

public class ComboBoxImpl extends AbstractInputComponent<ComboBox> implements ComboBox {
    public static final String EMPTY_OPTION_VALUE = "\u00a0";

    public static final String V_FILTERSELECT_NEXTPAGE = "v-filterselect-nextpage";
    public static final String V_FILTERSELECT_PREVPAGE = "v-filterselect-prevpage";

    public static final String V_FILTERSELECT_BUTTON = "v-filterselect-button";

    public static final By VAADIN_COMBOBOX_OPTIONLIST = By.id("VAADIN_COMBOBOX_OPTIONLIST");
    public static final By EMPTY_OPTION = byText(EMPTY_OPTION_VALUE);

    public ComboBoxImpl(By by) {
        super(by);
    }

    @Override
    public String getValue() {
        return getInputDelegate()
                .shouldBe(visible)
                .getValue();
    }

    @Override
    public ComboBox setValue(String value) {
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
    public ComboBox setFilter(String filter) {
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
    public OptionsPopup<ComboBox> openOptionsPopup() {
        $(byChain(by, byClassName(V_FILTERSELECT_BUTTON)))
                .shouldBe(visible)
                .click();

        OptionsPopupImpl<ComboBox> optionsPopup = getOptionsPopupElement();
        optionsPopup.shouldBe(visible);

        return optionsPopup;
    }

    @Override
    public OptionsPopup<ComboBox> getOptionsPopup() {
        OptionsPopupImpl<ComboBox> optionsPopup = getOptionsPopupElement();
        optionsPopup.shouldBe(visible);

        return optionsPopup;
    }

    @Override
    public ComboBox closeOptionsPopup() {
        OptionsPopupImpl<ComboBox> optionsPopup = getOptionsPopupElement();
        optionsPopup.shouldBe(visible);

        $(byChain(by, DIV))
                .shouldBe(visible)
                .click();

        return this;
    }

    protected OptionsPopupImpl<ComboBox> getOptionsPopupElement() {
        return new OptionsPopupImpl<>(VAADIN_COMBOBOX_OPTIONLIST, this);
    }
}