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

package io.jmix.masquerade;

import io.jmix.masquerade.component.Untyped;
import io.jmix.masquerade.composite.LoginWindow;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static io.jmix.masquerade.Components.wire;
import static io.jmix.masquerade.Conditions.EDITABLE;
import static io.jmix.masquerade.Conditions.ENABLED;
import static io.jmix.masquerade.Conditions.VISIBLE;
import static io.jmix.masquerade.Conditions.caption;
import static io.jmix.masquerade.Selectors.$j;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginTest {
    @Test
    public void login() {
        open("http://localhost:8080/app");

        LoginWindow loginWindow = $j(LoginWindow.class);

        assertNotNull(loginWindow.getLoginField());
        assertNotNull(loginWindow.getPasswordField());

        loginWindow.getLoginField()
                .shouldBe(EDITABLE)
                .shouldBe(ENABLED);

        loginWindow.getLoginField().setValue("masquerade");
        loginWindow.getPasswordField().setValue("rulezzz");

        loginWindow.getRememberMeCheckBox().setChecked(true);
        loginWindow.getRememberMeCheckBox().getCaption();

        loginWindow.getWelcomeLabelTest()
                .shouldBe(VISIBLE);

        loginWindow.getLoginButton()
                .shouldBe(VISIBLE)
                .shouldBe(ENABLED)
                .shouldHave(caption("Submit"));

        String caption = loginWindow.getLoginButton().getCaption();
        boolean enabled = loginWindow.getLoginButton().is(ENABLED);

        Untyped loginFormLayout = wire(Untyped.class, "loginFormLayout");
        loginFormLayout.shouldBe(VISIBLE);

        loginWindow.getLoginButton().click();
    }
}