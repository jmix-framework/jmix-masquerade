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

package io.jmix.masquerade.components;

import io.jmix.masquerade.Selectors;
import io.jmix.masquerade.base.ByLocator;
import io.jmix.masquerade.base.SelenideElementWrapper;
import io.jmix.masquerade.util.Log;
import org.openqa.selenium.By;

import java.util.List;

public interface PopupButton extends Component<PopupButton> {
    void click(String optionText);

    @Log
    PopupContent openPopupContent();
    PopupContent getPopupContent();

    interface PopupContent extends SelenideElementWrapper<PopupContent>, ByLocator {
        void select(String optionText);

        /**
         * Trigger action of popup menu.
         * <br>
         * Supported bys:
         * <ul>
         * <li>{@link Selectors#byText(String)}</li>
         * <li>{@link Selectors#withText(String)}</li>
         * <li>{@link Selectors#byJTestId(String)} </li>
         * </ul>
         *
         * @param actionBy action selector
         */
        @Log
        void trigger(By actionBy);
        @Log
        void trigger(String jTestId);

        List<String> getOptions();
    }
}