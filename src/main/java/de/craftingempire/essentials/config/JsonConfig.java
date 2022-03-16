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
package de.craftingempire.essentials.config;

import java.io.File;
import java.io.IOException;

/**
 * CEssentials; de.craftingempire.essentials.config:JsonConfig
 *
 * @license MIT <https://opensource.org/licenses/MIT>
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 14.03.2022
 */
@SuppressWarnings("unsafe")
public class JsonConfig<T> {

    private final File dataFile;

    private Class<T> configurationClazz;

    private T configuration;

    /**
     * JsonConfig which can hold plain Java Objects which are GSON serializable ( needs to have Full Args Constructor AND Getter and Setter for each field )
     * @param clazz GSON serializable class
     * @param dataFile the configuration file
     */
    public JsonConfig(Class<T> clazz, File dataFile) {
        this.dataFile = dataFile;
        this.configurationClazz = clazz;
    }

    public void setDefault(Class clazz, Object defaultConfig) {
        this.configurationClazz = clazz;
        this.configuration = (T) defaultConfig;
    }

    public void load() throws IOException {
        if(dataFile.exists()) configuration = ConfigLoader.loadConfig(configurationClazz, dataFile);
    }

    public void load(boolean overrideDefault) throws IOException {
        if(!overrideDefault && configuration != null) return;
        load();
    }

    public void save() throws IOException {
        ConfigLoader.saveConfig(configuration, dataFile);
    }

    public void save(boolean overwrite) throws IOException {
        if(!overwrite && dataFile.exists()) return;
        save();
    }

    public T get() {
        return configuration;
    }

}
