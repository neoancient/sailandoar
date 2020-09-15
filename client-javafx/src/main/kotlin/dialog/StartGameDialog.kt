package dialog

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

const val DEFAULT_PORT = 1805

class StartGameDialog : Fragment() {
    val nameProperty = SimpleStringProperty("New Player")
    var name by nameProperty
    val portProperty = SimpleIntegerProperty(DEFAULT_PORT)
    var port by portProperty
    val canceledProperty = SimpleBooleanProperty(false)
    var canceled by canceledProperty

    override val root = borderpane {
        center = form {
            fieldset {
                field(messages["playerName"]) {
                    textfield(nameProperty)
                }
                field(messages["port"]) {
                    textfield(portProperty.asString())
                }
            }
        }
        bottom = buttonbar {
            button(messages["cancel"]) {
                isCancelButton = true
                action {
                    canceled = true
                    close()
                }
            }
            button(messages["ok"]) {
                isDefaultButton = true
                action {
                    close()
                }
            }
        }
    }
}
