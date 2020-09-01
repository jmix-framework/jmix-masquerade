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

import io.jmix.masquerade.component.Notification
import io.jmix.masquerade.screen.GroovyLoginScreen
import org.junit.jupiter.api.Test

import static com.codeborne.selenide.Selenide.open
import static io.jmix.masquerade.Components.wire
import static io.jmix.masquerade.Conditions.*
import static io.jmix.masquerade.Selectors.$j
import static io.jmix.masquerade.component.Notification.Type
import static org.junit.jupiter.api.Assertions.assertNotNull

class GroovyLoginUiTest {
    @Test
    void login() {
        open("http://localhost:8080/app")

        def loginScreen = wire(GroovyLoginScreen, 'loginMainBox')

        assertNotNull(loginScreen.usernameField)
        assertNotNull(loginScreen.passwordField)

        loginScreen.with {
            usernameField.value = "masquerade"
            passwordField.value = "rulezzz"

            usernameField
                    .shouldBe(ENABLED)
                    .shouldBe(EDITABLE)
                    .shouldHave(value("masquerade"))
                    .shouldHave(valueContains("ma"))

            def popup = localesField.openOptionsPopup()

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
