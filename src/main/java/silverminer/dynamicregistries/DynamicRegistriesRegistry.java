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

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = DynamicRegistries.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DynamicRegistriesRegistry {
    public static IForgeRegistry<RegistryAccessExtension<?>> REGISTRY_ACCESS_EXTENSIONS_REGISTRY;

    @SuppressWarnings("unchecked")
    //Ugly hack to let us pass in a typed Class object. Remove when we remove type specific references.
    private static <T> Class<T> c(Class<?> cls) {
        return (Class<T>) cls;
    }

    @SubscribeEvent
    public static void onCreateNewRegistries(RegistryEvent.NewRegistry event) {
        RegistryBuilder<RegistryAccessExtension<?>> registryBuilder = new RegistryBuilder<>();
        registryBuilder.setName(new ResourceLocation(DynamicRegistries.MOD_ID, "dynamic_registries"));
        registryBuilder.setType(c(RegistryAccessExtension.class));
        REGISTRY_ACCESS_EXTENSIONS_REGISTRY = registryBuilder.create();
    }
}