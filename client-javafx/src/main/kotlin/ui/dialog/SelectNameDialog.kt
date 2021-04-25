/*
 *  Sail and Oar
 * Copyright (c) 2021 Carl W Spain
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ui.dialog

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.layout.BorderPane
import tornadofx.*

class SelectNameDialog : Fragment() {
    val suggested: String by param()
    val taken: Set<String> by param()
    val disconnected: Boolean by param()
    private val nameProperty = SimpleStringProperty(suggested)
    val name by nameProperty
    private val reconnectProperty = SimpleBooleanProperty(disconnected)
    val reconnect by reconnectProperty
    private val nameValid = booleanBinding(nameProperty, reconnectProperty, nameProperty) {
        (value.isNotEmpty() && value !in taken) ||
                (reconnectProperty.value && nameProperty.value == suggested)
    }

    init {
        title = messages["title"]
        nameProperty.onChange {
            if (it != suggested) {
                reconnectProperty.value = false
            }
        }
    }

    override val root = borderpane {
        center = form {
            fieldset {
                field(messages["playerName"]) {
                    textfield(nameProperty)
                }
            }
            if (disconnected) {
                checkbox(messages["reconnect"], reconnectProperty)
            }
            label {
                text = messages["invalidMessage"]
                style = "-fx-font-style:italic;-fx-color:red"
                visibleWhen(nameValid.not())
            }
        }
        bottom = buttonbar {
            button(messages["ok"]) {
                isDefaultButton = true
                action {
                    close()
                }
                enableWhen(nameValid)
            }
        }
        BorderPane.setMargin(center, Insets(5.0, 5.0, 5.0, 5.0))
        BorderPane.setMargin(bottom, Insets(0.0, 5.0, 10.0, 5.0))
    }
}