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

package unit

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InnerClass(var prop: Int)

private class TestClass(var inner: InnerClass) {
    var variable by dynamic { inner::prop }
}

internal class VariablePropertyDelegateTest {
    @Test
    fun getFromInitialValue() {
        val test = TestClass(InnerClass(5))

        assertEquals(5, test.variable)
    }

    @Test
    fun setFromInitialValue() {
        val test = TestClass(InnerClass(5))
        test.variable = 8

        assertEquals(8, test.variable)
    }

    @Test
    fun getAfterChangingValue() {
        val test = TestClass(InnerClass(5))
        test.inner = InnerClass(10)


        assertEquals(10, test.variable)
    }

    @Test
    fun setAfterChangingValue() {
        val test = TestClass(InnerClass(5))
        test.inner = InnerClass(10)
        test.variable = 15


        assertEquals(15, test.variable)
    }
}