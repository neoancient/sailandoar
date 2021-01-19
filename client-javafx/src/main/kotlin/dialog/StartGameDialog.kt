package dialog

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.layout.BorderPane
import javafx.util.StringConverter
import tornadofx.*

const val DEFAULT_PORT = 1805

class StartGameDialog : Fragment() {
    private val nameProperty = SimpleStringProperty("New Player")
    var name: String by nameProperty
    private val hostProperty = SimpleStringProperty(params["host"] as? String ?: "")
    val host: String by hostProperty
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
                    textfield {
                        bind(portProperty, converter = object : StringConverter<Number>() {
                            override fun toString(value: Number?) =
                                value?.toInt()?.toString() ?: ""

                            override fun fromString(string: String): Number {
                                val str = string.replace("[^\\d]".toRegex(), "")
                                return if (str.isNotEmpty()) {
                                    str.toInt()
                                } else {
                                    0
                                }
                            }
                        })
                    }
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
                enableWhen(portProperty.greaterThan(0))
                action {
                    close()
                }
            }
        }
        BorderPane.setMargin(center, Insets(5.0, 5.0, 5.0, 5.0))
        BorderPane.setMargin(bottom, Insets(0.0, 5.0, 10.0, 5.0))
    }
}
