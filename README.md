# dynamic registries

Dynamic registries is a library mod that allows the creation of new data-pack based registries. It's **possible** to define elements in code and trough data-packs (even the data-pack in your mod), but not necessary; you can also create elements only in code **or** in data-packs. Elements that were defined in code can also be overwritten from data-packs. Elements must be encoded in JSON.

This library is based on the changes introduced in [this PR](https://github.com/MinecraftForge/MinecraftForge/pull/8263). This PR probably isn't going to be merged before 1.18.2 gets released, but I needed this feature already for 1.18.1, so I wrote this mod. There won't be versions of this library for other Minecraft version for this reason.

### How to add this library to your mods dependencies

To add the library to your gradle setup, add modrinth to your dependency repositories.

```
repositories {
    maven {
        name = "Modrinth"
        url = "https://api.modrinth.com/maven"
        content {
            includeGroup "maven.modrinth"
        }
    }
}
```

Add the above codeblock to your build.gradle above your dependencies block (the order doesn't matter that strong, but lets assume that you've placed it there to simplify things a bit). If you already had a ```repositories``` block, only append the ```maven``` block to it.

We now define the dependency itself. Add the line below to your dependencies block in your build.gradle. Version `0.1.0` was the latest at time of writing. Replace that with the latest currently available version.
```
implementation fg.deobf("maven.modrinth:dynamicregistries:0.1.0")

```

You can now refresh your gradle project to download the dependencies and re-run IDE setup tasks (`genIntellijRuns`, `genEclipseRuns` or `genVSCodeRuns`)

You can now use dynamic registries in your development setup, but you should make the forge mod-loader check for it's dependencies when loading. That requires changes in your mods.toml (in `src/main/resources/META-INF`). Add the following block at the end of the file

```
[[dependencies.modid]]
modId = "dynamicregistries"
mandatory = true
versionRange = "[0.0.0,2.0.0)"
ordering = "NONE"
side = "BOTH"
```
Replace `modid` with the modid of your mod and rearrange the version range if necessary

### Credits
- Won-Ton, the author of the PR that was the base for this library
- Mod Icon: [flaticon.com](https://www.flaticon.com/free-icons/bookshelf)