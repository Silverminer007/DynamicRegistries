/*
 * Silverminer007
 * Copyright (c) 2022.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */

package silverminer.dynamicregistries;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class RegistryUtils {
    protected static final Logger LOGGER = LogManager.getLogger(RegistryUtils.class);

    @SuppressWarnings("unchecked")
    public static void addExtensions() {
        RegistryAccess.REGISTRIES = injectRegistryAccessExtensions(RegistryAccess.REGISTRIES, (Map<ResourceKey<? extends Registry<?>>, MappedRegistry<?>>) RegistryAccess.BUILTIN.registries);
    }

    public static Map<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> injectRegistryAccessExtensions(Map<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> registryData, Map<ResourceKey<? extends Registry<?>>, MappedRegistry<?>> builtin) {
        HashMap<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> mutableRegistryDataMap = new HashMap<>(registryData);
        for (var extension : DynamicRegistriesRegistry.REGISTRY_ACCESS_EXTENSIONS_REGISTRY) {
            var key = extension.getRegistryKey();
            var data = createExtensionData(extension);
            var registry = copyExtensionRegistry(extension.getRegistryKey(), extension.getDefaultElementLifecycle());

            // Injects the ResourceKey and Codec to the RegistryAccess.REGISTRIES map
            mutableRegistryDataMap.put(key, data);

            // Injects the mod-provided built-in registry elements to RegistryAccess.BUILTIN
            builtin.put(key, registry);

            LOGGER.info("Injected RegistryAccessExtension: {}", key);
        }
        return mutableRegistryDataMap;
    }

    private static <E extends IForgeRegistryEntry<E>> RegistryAccess.RegistryData<E> createExtensionData(RegistryAccessExtension<E> extension) {
        return new RegistryAccess.RegistryData<>(extension.getRegistryKey(), extension.getDirectCodec(), extension.getNetworkCodec());
    }

    public static <E extends IForgeRegistryEntry<E>> MappedRegistry<E> copyExtensionRegistry(ResourceKey<? extends Registry<E>> resourceKey, Lifecycle lifecycle) {
        var registry = RegistryManager.ACTIVE.getRegistry(resourceKey);
        var mapped = new MappedRegistry<>(resourceKey, lifecycle);

        if (registry == null) {
            return mapped;
        }

        for (var entry : registry.getEntries()) {
            mapped.register(entry.getKey(), entry.getValue(), lifecycle);
        }

        return mapped;
    }
}