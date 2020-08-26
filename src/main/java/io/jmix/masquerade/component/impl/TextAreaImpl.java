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
import io.jmix.masquerade.component.TextArea;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class TextAreaImpl extends AbstractInputComponent<TextArea> implements TextArea {

    public TextAreaImpl(By by) {
        super(by);
    }

    @Override
    public TextArea setValue(String value) {
        getInputDelegate()
                .shouldBe(visible)
                .shouldBe(enabled)
                .shouldNotBe(readonly)
                .setValue(value);
        return this;
    }

    @Override
    public String getValue() {
        return getInputDelegate()
                .shouldBe(visible)
                .getValue();
    }

    @Override
    protected SelenideElement getInputDelegate() {
        return impl;
    }
}