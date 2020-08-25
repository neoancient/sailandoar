/**
 * Sail and Oar
 * Copyright (c) 2020 Carl W Spain
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
 */
package shipeditor

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.stage.FileChooser
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tornadofx.*
import unit.ShipStats
import java.io.File
import kotlin.system.exitProcess


class Menu: View() {
    private val currentFileProperty: ObjectProperty<File> = SimpleObjectProperty()
    private var currentFile by currentFileProperty

    override val root = menubar {
        menu("File") {
            item("New").action {
                (app as ShipEditor).ships.clear()
            }
            item("Load").action {
                handleLoad()
            }
            item("Save").action {
                handleSave()
            }
            item("Save As...").action {
                handleSaveAs()
            }
            item("Exit").action {
                handleExit()
            }
        }
    }

    private fun handleLoad() {
        val fileChooser = FileChooser()
        val extFilter = FileChooser.ExtensionFilter(
                "JSON files (*.json)", "*.json")
        fileChooser.extensionFilters.add(extFilter)

        val file = fileChooser.showOpenDialog(null)
        if (file != null) {
            try {
                val input = file.inputStream().readBytes().toString(Charsets.UTF_8)
                val list: List<ShipStats> = Json.decodeFromString(input)
                (app as ShipEditor).ships.setAll(list.map { ShipModel(it) })
                currentFile = file
            } catch (ex: Exception) {
                val alert = Alert(AlertType.ERROR)
                alert.title = "Error"
                alert.headerText = "Could not load ship file"
                alert.contentText = """
            There was an error attempting to load ships from file:
            ${file.path}
            """.trimIndent()
                alert.showAndWait()
            }
        }
    }

    private fun handleSave() {
        if (currentFile != null) {
            writeShips(currentFile)
        } else {
            handleSaveAs()
        }
    }

    private fun handleSaveAs() {
        val fileChooser = FileChooser()

        val extFilter = FileChooser.ExtensionFilter(
                "JSON files (*.json)", "*.json")
        fileChooser.extensionFilters.add(extFilter)
        val saveDir = currentFile
        if (saveDir != null) {
            fileChooser.initialDirectory = saveDir
        }

        var file = fileChooser.showSaveDialog(null)
        if (null != file) {
            if (!file.path.toLowerCase().endsWith(".json")) {
                file = File(file.path + ".json")
            }
            writeShips(file)
            currentFile = file
        }
    }

    private fun handleExit() {
        exitProcess(0)
    }

    private fun writeShips(file: File) {
        val list = (app as ShipEditor).ships.map{it.export()}.toList()
        val output = Json{prettyPrint = true}.encodeToString(list)
        file.writeText(output)
    }
}
