/**
 * CEssentials | Copyright (c) 2022 LuciferMorningstarDev
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.craftingempire.essentials;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * CEssentials; de.craftingempire.essentials:ComponentSerializer
 *
 * @license MIT <https://opensource.org/licenses/MIT>
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 14.03.2022
 */
public class ComponentSerializer {

    private ComponentSerializer() {} // prevent instantiation

    public static final LegacyComponentSerializer
            sectionAndHEX = LegacyComponentSerializer.builder()
            .character('§').hexCharacter('#').hexColors().build(),
            unusualSectionAndHEX = LegacyComponentSerializer.builder()
                    .character('§').hexCharacter('#').hexColors()
                    .useUnusualXRepeatedCharacterHexFormat().build(),
            etAndHEX = LegacyComponentSerializer.builder()
                    .character('&').hexCharacter('#').hexColors().build(),
            etOnly = LegacyComponentSerializer.builder()
                    .character('&').build(),
            sectionOnly = LegacyComponentSerializer.builder()
                    .character('§').build();

}
