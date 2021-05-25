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

package io.jmix.masquerade.screen;

import io.jmix.masquerade.Wire;
import io.jmix.masquerade.base.Composite;
import io.jmix.masquerade.component.Button;
import io.jmix.masquerade.component.ComboBox;
import io.jmix.masquerade.component.Label;
import io.jmix.masquerade.component.PasswordField;
import io.jmix.masquerade.component.TextField;
import org.openqa.selenium.support.FindBy;


public class LoginScreen extends Composite<LoginScreen> {
    @Wire
    protected TextField usernameField;

    @Wire
    protected PasswordField passwordField;

    @Wire(path = {"loginFormLayout", "loginButton"})
    protected Button loginButton;

    @Wire
    protected ComboBox localesField;

    @Wire
    protected Label welcomeLabel;

    @FindBy(className = "jmix-login-caption")
    protected Label welcomeLabelTest;

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public ComboBox getLocalesField() {
        return localesField;
    }

    public Label getWelcomeLabel() {
        return welcomeLabel;
    }

    public Label getWelcomeLabelTest() {
        return welcomeLabelTest;
    }
}