import kotlinx.serialization.Serializable

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

@Serializable
public sealed class ChatMessage {
    abstract public fun toHtml(): String
}

@Serializable
public data class SystemMessage(val text: String) : ChatMessage() {
    override fun toHtml(): String = "<p><b>$text</b></p>"
}

@Serializable
public data class InfoMessage(val text: String) : ChatMessage() {
    override fun toHtml(): String = "<p><i>$text</i></p>"
}

@Serializable
public data class SimpleMessage(val user: String, val text: String) : ChatMessage() {
    override fun toHtml(): String = "<p><b>$user:</b> $text</p>"
}

@Serializable
public data class WhisperMessage(val sender: String, val recipient: String, val text: String) : ChatMessage() {
    override fun toHtml(): String = "<p>$sender &gt; $recipient: $text</p>"
}

@Serializable
public data class EmoteMessage(val user: String, val text: String) : ChatMessage() {
    override fun toHtml(): String = "<p>$user $text</p>"
}