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

package io.jmix.masquerade

import io.jmix.masquerade.components.Notification
import io.jmix.masquerade.composite.GroovyLoginWindow
import org.junit.jupiter.api.Test

import static com.codeborne.selenide.Selenide.open
import static io.jmix.masquerade.Components.wire
import static io.jmix.masquerade.Conditions.*
import static io.jmix.masquerade.Selectors.$j
import static io.jmix.masquerade.components.Notification.Type
import static org.junit.jupiter.api.Assertions.assertNotNull

class GroovyLoginTest {
    @Test
    void login() {
        open("http://localhost:8080/app")

        def loginWindow = wire(GroovyLoginWindow, 'loginMainBox')

        assertNotNull(loginWindow.loginField)
        assertNotNull(loginWindow.passwordField)

        loginWindow.with {
            rememberMeCheckBox.checked = true
            rememberMeCheckBox.checked = false

            loginField.value = "masquerade"
            passwordField.value = "rulezzz"

            loginField
                    .shouldBe(ENABLED)
                    .shouldBe(EDITABLE)
                    .shouldHave(value("masquerade"))
                    .shouldHave(valueContains("ma"))

            def popup = localesSelect.openOptionsPopup()

            popup.shouldBe(VISIBLE)

            loginButton.click()

            $j(Notification)
                    .shouldBe(VISIBLE)
                    .shouldHave(captionContains("Failed"))
                    .shouldHave(descriptionContains("Unknown login"))
                    .shouldHave(type(Type.ERROR))
                    .clickToClose()
                    .should(DISAPPEAR)
        }
    }
}