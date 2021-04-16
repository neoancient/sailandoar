/*
 * Sail and Oar
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

package ui

import game.GameListener
import javafx.application.Platform
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.VBox
import javafx.scene.web.WebView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*
import ui.model.GameModel

class ChatWindow : GameListener, View() {
    private val gameModel: GameModel by inject()
    override val root: VBox by fxml()

    private val txtChatWindow: WebView by fxid()
    private val txtChatEntry: TextField by fxid()
    private val btnSend: Button by fxid()

    private val chatMessages = StringBuilder()

    private var chatWindowAtBottom = true

    init {
        btnSend.enableWhen(txtChatEntry.textProperty().isNotEmpty)
        (app as SailAndOarApp).clientProperty.value?.game?.addListener(this)
        txtChatWindow.engine.loadWorker.stateProperty().onChange {
            it?.let { state ->
                if (state == Worker.State.SUCCEEDED && chatWindowAtBottom) {
                    txtChatWindow.engine.executeScript("window.scrollTo(0, document.body.scrollHeight)")
                }
            }
        }
        txtChatWindow.addEventFilter(ScrollEvent.SCROLL) {
            if (it.isControlDown) {
                if (it.deltaY > 0) {
                    txtChatWindow.zoom *= 1.1
                } else if (it.deltaY < 0) {
                    txtChatWindow.zoom /= 1.1
                }
                it.consume()
            }
        }
    }

    @FXML
    private fun btnSendAction() {
        GlobalScope.launch(Dispatchers.IO) {
            (app as SailAndOarApp).clientProperty.value?.sendChatMessage(txtChatEntry.text)
            Platform.runLater {
                txtChatEntry.text = ""
            }
        }
    }

    override fun appendChat(text: String) {
        Platform.runLater {
            chatMessages.append(text)
            with (txtChatWindow.engine) {
                chatWindowAtBottom = executeScript("(window.innerHeight + window.scrollY) >= document.body.offsetHeight") as Boolean
                loadContent(chatMessages.toString())
            }
        }
    }

    override fun playerAdded(playerId: Int) {
    }

    override fun playerRemoved(playerId: Int) {
    }

    override fun playerChanged(playerId: Int) {
        appendChat(String.format(messages["message.playerChanged"],
            gameModel.gameProperty.value.getPlayer(playerId)?.name))
    }

    override fun unitAdded(unitId: Int) {
    }

    override fun unitRemoved(unitId: Int) {
    }

    override fun boardChanged() {
        appendChat(messages["message.boardChanged"])
    }

    override fun weatherChanged() {
        appendChat(messages["message.weatherChanged"])
    }
}