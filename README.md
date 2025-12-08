# CraftsNet-CLI
### Simple CLI integration for craftsnet

![Latest Release on Maven](https://repo.craftsblock.de/api/badge/latest/releases/de/craftsblock/craftsnet/modules/cli?color=40c14a&name=CraftsNet-CLI&prefix=v)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/CraftsBlock/CraftsNet-CLI)
![GitHub](https://img.shields.io/github/license/CraftsBlock/CraftsNet-CLI)
![GitHub all releases](https://img.shields.io/github/downloads/CraftsBlock/CraftsNet-CLI/total)
![GitHub issues](https://img.shields.io/github/issues-raw/CraftsBlock/CraftsNet-CLI)

---

CraftsNet-CLI is used to add a simple-to-use command line interface to be used within craftsnet.

## Installation

### Maven
```xml
<repositories>
  ...
  <repository>
    <id>craftsblock-releases</id>
    <name>CraftsBlock Repositories</name>
    <url>https://repo.craftsblock.de/releases</url>
  </repository>
</repositories>
```
```xml
<dependencies>
  ...
  <dependency>
    <groupId>de.craftsblock.craftsnet.modules</groupId>
    <artifactId>cli</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```

### Gradle
```gradle
repositories {
  ...
  maven { url "https://repo.craftsblock.de/releases" }
  mavenCentral()
}
```
```gradle
dependencies {
  ...
  implementation "de.craftsblock.craftsnet.modules:cli:VERSION"
}
```

## Quick Start

Firstly you need to create a command executor which is responsible for dispatching you command.

```java
import de.craftsblock.cnet.module.cli.annotation.CommandMeta;
import de.craftsblock.cnet.module.cli.command.Command;
import de.craftsblock.cnet.module.cli.command.CommandExecutor;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.craftsblock.craftsnet.logging.Logger;
import org.jetbrains.annotations.NotNull;

@AutoRegister
@CommandMeta(name = "test", aliases = {"my", "command"})
public class MyCommand implements CommandExecutor {

    @Override
    public void onCommand(@NotNull Command command, String alias, @NotNull String[] args, @NotNull Logger logger) {
        logger.info("Hello, World!");
    }

}
```

You can use the auto register system of craftsnet to automatically register your command.
If you want to auto register you command executor you need to add the `@CommandMeta` annotation to your command executor.

Each command must have a primary name which can not be used twice. Additionally, it can have multiple aliases which are alternative names for the command.

## Open Source Licenses
We are using some third party open source libraries. Below you find a list of all third party open source libraries used:

| Name                                                   | Description                                                      | Licecnse                                                                           |
|--------------------------------------------------------|------------------------------------------------------------------|------------------------------------------------------------------------------------|
| [CraftsNet](https://github.com/CraftsBlock/CraftsNet)  | Easy creation of HTTP routes and WebSocket endpoints in Java.    | [Apache License 2.0](https://github.com/CraftsBlock/CraftsNet/blob/master/LICENSE) |
| [CraftsCore](https://github.com/CrAfTsArMy/CraftsCore) | https://repo.craftsblock.de/#/releases/de/craftsblock/craftscore | [Apache License 2.0](https://github.com/CrAfTsArMy/CraftsCore/blob/master/LICENSE) |

## Support and contribution
If you have any questions or have found a bug, please feel free to let us know in our [issue tracker](https://github.com/CraftsBlock/CraftsNet-CLI/issues). We appreciate any help and welcome your contributions to improve the CraftsNet-CLI project.