package dialog

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.layout.BorderPane
import tornadofx.*

const val DEFAULT_PORT = 1805

class StartGameDialog : Fragment() {
    val nameProperty = SimpleStringProperty("New Player")
    var name by nameProperty
    private val hostProperty = SimpleStringProperty(params["host"] as? String ?: "")
    val host by hostProperty
    private val portProperty = SimpleIntegerProperty(DEFAULT_PORT)
    var port by portProperty
    private val canceledProperty = SimpleBooleanProperty(false)
    var canceled by canceledProperty

    init {
        title = messages["title"]
    }

    override val root = borderpane {
        center = form {
            fieldset {
                field(messages["playerName"]) {
                    textfield(nameProperty)
                }
                if (host.isNotEmpty()) {
                    field(messages["host"]) {
                        textfield(hostProperty)
                    }
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
        BorderPane.setMargin(center, Insets(5.0, 5.0, 5.0, 5.0))
        BorderPane.setMargin(bottom, Insets(0.0, 5.0, 10.0, 5.0))
    }
}
