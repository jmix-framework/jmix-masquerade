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

package io.jmix.masquerade.composite

import io.jmix.masquerade.Wire
import io.jmix.masquerade.base.Composite
import io.jmix.masquerade.components.Button
import io.jmix.masquerade.components.CheckBox
import io.jmix.masquerade.components.Label
import io.jmix.masquerade.components.ComboBox
import io.jmix.masquerade.components.PasswordField
import io.jmix.masquerade.components.TextField

class GroovyLoginWindow extends Composite<GroovyLoginWindow> {
    @Wire
    Label welcomeLabel

    @Wire
    TextField loginField

    @Wire
    PasswordField passwordField

    @Wire
    Button loginButton

    @Wire
    CheckBox rememberMeCheckBox

    @Wire
    ComboBox localesSelect
}