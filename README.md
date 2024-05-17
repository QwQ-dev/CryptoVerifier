# 🚀 CryptoVerifier
#### 高效、经济的许可证验证解决方案 (保护您的 Minecraft 插件 或 Mod 等)

---

## 🌍 Open Source

无论如何，我想我是时候开源这个东西了，里面的代码有些地方十分复杂且屎山，我想应该是没有人愿意维护这坨东西。

总之，如果这个项目能给您带来帮助，那是我的荣幸。关于更多部分可以查看 [QuickStart](https://github.com/QwQ-dev/CryptoVerifier/tree/main/QuickStart)，在使用中的任何新出现的 Bug 都不会得到解决。

该项目不会有任何的支持，当然我愿意审阅 Pr，依然欢迎。

---

## 📝 简介

CryptoVerifier 是一款高效而经济的许可证验证解决方案，我们专注于 **尽力** 保障您的软件许可证的安全性、完整性和合法性。借助多重加密、验证和防护措施，有效地防止许可证文件的伪造、滥用和非法复制等安全威胁。

我们为您提供灵活多样的许可证保护功能，涵盖硬件标识、IP限制、使用次数限制和并行限制等多种选择，确保多层次的安全措施轻松实现。同时，我们还提供简化许可证更新和过期处理的机制，满足您特定的需求。

---

## 🌟 功能简介

- **⚙️ 自定义**：我们提供了配置文件与非常多的开发者 API，您可以自定义命令、验证器、线程池、数据库，也可以进行事件监听等操作。我们同时确保产品在处理安全问题方面具备自动化能力。
- **👣 多重验证手段**：为了确保许可证的安全性和完整性，我们提供了多种验证方式，包括文件解析验证、UUID 验证和哈希验证等，以提供更加多样化和可靠的验证手段。
- **🦾 自定义验证器**: 我们提供了自定义验证器 API，使开发者能够根据自己的需求创建个性化的验证逻辑，并进行应急响应等操作，以满足特定的验证要求。
- **🎼 命令行**：为了方便开发者使用许可证系统，我们提供了简洁易用的命令行工具，使其能够更便捷地进行操作。
- **📙 数据库**：我们支持 MySQL、MongoDB 和 File 存储，并利用 Redis 对部分内容进行存储，以提供灵活的数据库选择和数据存储方案。
- **📊 日志记录**：我们提供完善的日志记录功能，记录命令、许可证验证时间、返回信息、验证 IP 等信息，以帮助用户进行跟踪和分析。
- **🎲 加密算法**：为确保许可证信息的保密性，我们使用多重加密对许可证进行加密，以保障敏感信息的安全性。
- **🔒 密钥交换**：通过密钥交换协议，确保服务器返回的验证信息的完整性和真实性，以防止数据被篡改、伪造、重放攻击或者遭遇虚假验证服务器等安全威胁。
- **🎳 使用限制**：我们支持多种用户使用限制，如硬件标识 (HWID)、IP、使用次数、并行限制等，以防止滥用问题的发生。
- **🕒 时间限制**：我们提供根据时间限制设置许可证的有效期，以确保许可证在指定时间段内有效，并对其过期时间进行控制。
- **💨 性能优化**：通过缓存机制大幅提高验证性能，在多次验证时的速度得到显著提升，提供更好的用户体验。
- **🚦 临时封禁**：我们支持临时封禁和解封许可证的功能，以在特殊情况下限制许可证的使用，确保系统安全。
- **🧬 文件追踪**：我们支持通过许可证文件追踪到具体用户，以便在许可证文件泄露时能够采取相应的限制措施，加强安全保护。
- **🖥️ 集群支持**：我们支持多验证服务器集群，以确保用户能够使用具有最佳延迟的服务器，提供更快速、稳定的服务体验。
- **✍️ 示例项目**：我们提供了详细的示例代码和集成指南，帮助开发者轻松地将许可证系统集成到他们的项目中，开始插件的开发，降低开发难度和时间成本。

---

## 👻 功能展示

**CryptoVerifier 提供高度自定义的配置，允许用户根据其业务需求对系统进行定制，使其适应不同的应用场景和要求。**


CryptoVerifier 允许您自定义线程池，以更好的满足高并发场景。

```yaml
# 验证服务端口
PORT: 17354

# 线程池设置
THREAD-POOL:
  # 核心线程数
  # 定义线程池中始终保持活动的线程数量
  # 这些线程会一直存在，即使它们处于空闲状态，也不会被回收
  # 将核心线程数设置为处理器核心数的 1 倍到 2 倍之间可以有效利用资源，但实际的最佳值会因系统负载而有所不同
  # 使用 max 将设置为 Integer.MAX_VALUE
  CORE-POOL-SIZE: 8

  # 最大线程数
  # 定义线程池中允许存在的最大线程数量。
  # 当线程池中的活动线程数已经达到了核心线程数，而仍然有新的任务提交到线程池时，线程池会创建新的线程来处理这些任务，直到线程数达到最大线程数
  # 一般情况下，将最大线程数设置为核心线程数的 2 倍是常见的做法，但需要根据具体情况进行调整
  # 使用 max 将设置为 Integer.MAX_VALUE
  MAX-POOL-SIZE: 16

  # 非核心线程的空闲时间（毫秒）
  # 当线程池中的线程数超过核心线程数，并且空闲一段时间后，多余的线程会被回收
  # 这个时间间隔即为空闲线程的存活时间
  KEEP-ALIVE-TIME: 60000

  # 任务队列的容量
  # 用于存储等待执行的任务的队列的最大容量
  # 当线程池的活动线程数达到核心线程数时，后续的任务将被放入任务队列中等待执行
  # 使用 max 将设置为 Integer.MAX_VALUE
  QUEUE-CAPACITY: 100
```

CryptoVerifier 支持集群，并支持您使用 MongoDB、MySQL、File 方式对数据进行存储，这一切都是加密的。

```yaml
# Redis 数据库设置 (必须使用)
REDIS:
  IP: 127.0.0.1
  PORT: 6379
  AUTH:
    USER: ""
    PASSWORD: ""
  CUSTOM-URL: ""

# 数据存储类型，服务端数据是使用文件存储还是数据库，MongoDB、MySQL、File
# 注意，如果您需要使用集群功能，则必须使用数据库来保证数据同步，否则会出现错误
DATA-STORAGE-TYPE: "File"

# Mongo 数据库设置
MONGO:
  IP: 127.0.0.1
  PORT: 27017

  # 使用用户名或密码 (若没有设置用户密码则两个都不填写)
  AUTH:
    USER: ""
    PASSWORD: ""

  # 自定义 Mongo 连接 URL
  #  如果启用则需要自己写连接 URL，上面的指定内容全部作废，数据库名: CryptoVerifier
  #  示例: mongodb://USER:PWD@IP:PORT/CryptoVerifier - 使用用户密码登陆指定服务器的 CryptoVerifier 数据库
  CUSTOM-URL: ""

# MySQL 数据库设置 (使用 MySQL 记得新建 CryptoVerifier 库)
MYSQL:
  # MySQL5 使用: com.mysql.jdbc.Driver
  # MySQL8 使用: com.mysql.cj.jdbc.Driver
  DRIVER: com.mysql.cj.jdbc.Driver
  IP: 127.0.0.1
  PORT: 3306
  AUTH:
    USER: ""
    PASSWORD: ""
  CUSTOM-URL: ""
```

CryptoVerifier 支持通过配置文件更加简单的对返回值进行修改，简单的修改完全不必依赖于 API.

```yaml
# 自定义返回结果
# 默认配置是安全的，且给出的客户端示例兼容的，如果您需要更改此项那么请注意客户端适配。
#
# 开发者可以拆分结果，验证通过后将用户信息打印至控制台等，让用户知道更多关于自己许可证的信息，如过期时间等
#
# 我们不建议您修改所有的返回信息，这可能会导致用户信息泄露。
# 我们只建议您在验证通过、被封禁、被限制 (IP HWID 验证次数 并行)、过期等特殊情况下提供相应信息以便了解情况。
#
# 不支持: UNKNOWN FILE_ERROR FILE_NOT_FOUND_ERROR，因为这些情况我们无法获得许可证信息
#
# 支持变量: <uuid> <ip> <hwid> <extraData> <isTempBanned> <tempBannedInfo>
#          <hwidLimit> <hwidAmount> <ipLimit> <ipAmount> <verificationLimit>
#          <verificationTimes> <parallelLimit> <creationDate> <verificationInfo>
#          <expirationDate>
CUSTOM-RESULT:
  TEMP_BANNED: "BANNED|<tempBannedInfo>"
  SUCCESS: "SUCCESS|<uuid>|<expirationDate>|<verificationInfo>"
```

**CryptoVerifier 提供以简洁为主的 API。您可以轻松实现许可证的生成、自定义验证和更新，也可以实现自定义的验证或返回逻辑，甚至远程类加载，以及其他相关的操作。我们的开发指南提供了清晰的步骤和示例代码，帮助您快速上手开发插件。**

```java
/**
 * 验证器接口，所有验证器类都应实现此接口，并重写所有方法。
 *
 * @author 2000000
 * @version 1.1
 * @since 2023/7/14
 */
public interface Validator {
    /**
     * 验证方法。
     *
     * <p>
     * 注意，此处的 {@code serverVerifierDataObject} 会持续更新，
     * 保证下一个验证器收到的是上一个验证器返回的最新数据。但真正的 IO 操作只有所有验证通过才会进行。
     * </p>
     *
     * @param ip                              客户端 IP 地址
     * @param credentialData                  凭证数据对象
     * @param clientVerifierDataObject        客户端验证数据对象
     * @param serverVerifierDataObject        服务器验证数据对象
     * @return 验证结果
     */
    boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) throws Exception;

    /**
     * 权重，越低越先执行。
     *
     * <p>
     * 范围在 {@code 20} ~ {@code 10000} 之间。
     * </p>
     *
     * <p>
     * 虽然您可以注册 {@code 20} 以下的权重，但是为了安全期间，我永远不会推荐您这么做，除非您真的知道自己在做什么。
     * </p>
     *
     * @return 权重
     */
    int getWeight();

    /**
     * 是否忽略异常。
     *
     * <p>
     * 若为真，当 {@link Validator#validate(String, CredentialData, Object, Object)} )} 出现异常时直接返回 {@link Validator#getFailedMessage()}，否则将打印异常后返回。
     * </p>
     *
     * @return 是否忽略异常
     */
    boolean isIgnoreException();

    /**
     * 验证器名称。此项不能为空。
     *
     * @return 验证器名称
     */
    @NonNull
    String getName();

    /**
     * 验证失败时返回的信息。此项不能为空。
     *
     * <p>
     * 我们支持您根据不同的条件在此处返回不同的信息，但这种动态返回仅支持此方法与 {@link Validator#getServerVerifierData()} 方法。
     * 其余方法动态返回将是不安全且不推荐的做法。
     * </p>
     *
     * @return 验证失败时返回的信息
     */
    @NonNull
    String getFailedMessage();

    /**
     * 对于此验证器的描述。
     *
     * @return 描述
     */
    String getDescription();

    /**
     * 服务器验证数据。
     *
     * <p>
     * 当您尝试修改了服务器验证数据时，这里必须为您修改后的对象，否则将不会有 IO 操作。
     * </p>
     *
     * <p>
     * 若为 {@code null} 则为未进行修改，若未空 {@code ""} 则未删除该用户的数据。
     * </p>
     *
     * @return 服务器验证数据
     */
    Object getServerVerifierData();
}
```

```java
ValidatorManager.getValidatorManager().registerValidator(new ExampleValidation());
LoggerUtils.getLogger().info("[ExamplePlugin] 自定义验证器已成功注册!");
```

**不仅仅是验证器，您还可以写属于您自己的命令操作。**

```java
/**
 * 定义命令接口。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/27
 */
@SuppressWarnings("unused")
public interface Command {
    /**
     * 处理命令。
     *
     * <p>
     * 格式为: {@code main sub sub}，该方法将获取到 {@code sub sub} 数组。
     * </p>
     */
    void handleCommand(String[] args) throws Exception;

    /**
     * 获取主命令。
     *
     * <p>
     * 格式为: {@code main sub sub}，该方法将获取 {@code main} 字段。
     * </p>
     *
     * @return 主命令
     */
    String getMainCommand();

    /**
     * 对于此命令的描述。
     *
     * @return 描述
     */
    String getDescription();

    /**
     * 获取命令执行线程。
     *
     * @return 获取命令执行线程
     */
    default Thread getThread() {
        return Thread.currentThread();
    }
}
```

**使用事件监听器，简单的对操作内容进行干预。**

```java
/**
 * 一个简单的示例事件监听类，事件监听类应实现 {@link Listener} 接口。
 *
 * <p>
 * 事件请查看 {@link twomillions.other.cryptoverifier.events.custom} 软件包下所有类。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/2
 */
@SuppressWarnings("unused")
public class ExampleListener implements Listener {
    /**
     * 事件监听器，处理事件。
     * 必须使用 {@link EventHandler} 注解，否则无效。
     *
     * @param event 初始化完成事件
     */
    @EventHandler
    public void testEventHandler(InitializationCompleteEvent event) {
        Logger logger = LoggerUtils.getLogger();

        logger.info("--------------------");
        logger.info("出现这条消息代表已经成功触发 InitializationCompleteEvent, 这与 Bukkit Event 类似。");
        logger.info("我们不建议使用 System.out.println 打印日志信息, 在高并发情况下效率极低。");
        logger.info("我们推荐使用日志依赖库, 您可以使用 LoggerUtils.getLogger() 获取 Logger 打印日志。");
        logger.info("--------------------");
        logger.info("大多数事件都是异步的, 此处为初始化完成事件, 为同步事件, 可使用 isSync() 检查是否为同步事件。");
        logger.info("可使用线程池 VerifierThreadPool.getVerifierThreadPool() 进行同步、异步、延迟、重复操作, 与 Bukkit 线程池类似。");
        logger.info("--------------------");
        logger.info("是否同步: " + event.isSync());
        logger.info("线程名称: " + event.getThread().getName());
        logger.info("--------------------");

        /*
         * 使用 CommandManager.getCommandManager().callCommand(String) 方法处理命令
         */
        CommandManager.getCommandManager().callCommand("help");
    }
}
```

**CryptoVerifier 提供完善的日志记录功能，记录命令、许可证验证时间、返回信息、验证 IP 等信息，以帮助用户进行跟踪和分析。**

```
[2023/07/18 19:01:00] [INFO]: [Socket] [User1] [xxx.xxx.xxx.xxx] 验证完成, 处理结果: SUCCESS
[2023/07/18 19:01:00] [INFO]: [Socket] [User1] [xxx.xxx.xxx.xxx] 即将断开连接, 本次验证耗时: 125ms

[2023/07/18 19:01:22] [INFO]: 即将清除所有的 IP 黑名单, 在过去的一分钟之内请求状况如下:
[2023/07/18 19:01:22] [INFO]: IP: xxx.xxx.xxx.xxx, 请求次数: 1

[2023/07/18 19:06:22] [INFO]: [Socket] [User2] [xxx.xxx.xxx.xxx] 验证完成, 处理结果: SUCCESS
[2023/07/18 19:06:22] [INFO]: [Socket] [User2] [xxx.xxx.xxx.xxx] 即将断开连接, 本次验证耗时: 110ms

[2023/07/18 19:06:22] [INFO]: 即将清除所有的 IP 黑名单, 在过去的一分钟之内请求状况如下:
[2023/07/18 19:06:22] [INFO]: IP: xxx.xxx.xxx.xxx, 请求次数: 1

[2023/07/18 19:11:29] [INFO]: [Socket] [User3] [xxx.xxx.xxx.xxx] 验证完成, 处理结果: SUCCESS
[2023/07/18 19:11:29] [INFO]: [Socket] [User3] [xxx.xxx.xxx.xxx] 即将断开连接, 本次验证耗时: 122ms
[2023/07/18 19:12:08] [INFO]: [Socket] [User4] [xxx.xxx.xxx.xxx] 验证完成, 处理结果: SUCCESS
[2023/07/18 19:12:08] [INFO]: [Socket] [User4] [xxx.xxx.xxx.xxx] 即将断开连接, 本次验证耗时: 128ms

[2023/07/18 19:12:22] [INFO]: 即将清除所有的 IP 黑名单, 在过去的一分钟之内请求状况如下:
[2023/07/18 19:12:22] [INFO]: IP: xxx.xxx.xxx.xxx, 请求次数: 1
[2023/07/18 19:12:22] [INFO]: IP: xxx.xxx.xxx.xxx, 请求次数: 1

[2023/07/18 19:35:22] [INFO]: [Socket] [User7] [xxx.xxx.xxx.xxx] 验证完成, 处理结果: SUCCESS
[2023/07/18 19:35:22] [INFO]: [Socket] [User7] [xxx.xxx.xxx.xxx] 即将断开连接, 本次验证耗时: 114ms

[2023/07/18 19:36:22] [INFO]: 即将清除所有的 IP 黑名单, 在过去的一分钟之内请求状况如下:
[2023/07/18 19:36:22] [INFO]: IP: xxx.xxx.xxx.xxx, 请求次数: 1
```

**CryptoVerifier 通过缓存机制大幅提高验证性能，在多次验证时的速度得到显著提升，提供更好的用户体验，快速且安全的毫秒级验证。**

```
[2023/07/18 19:01:00] [INFO]: [Socket] [User1] [xxx.xxx.xxx.xxx] 验证完成, 处理结果: SUCCESS
[2023/07/18 19:01:00] [INFO]: [Socket] [User1] [xxx.xxx.xxx.xxx] 即将断开连接, 本次验证耗时: 125ms

[2023/07/18 19:06:22] [INFO]: [Socket] [User2] [xxx.xxx.xxx.xxx] 验证完成, 处理结果: SUCCESS
[2023/07/18 19:06:22] [INFO]: [Socket] [User2] [xxx.xxx.xxx.xxx] 即将断开连接, 本次验证耗时: 110ms

[2023/07/18 19:11:29] [INFO]: [Socket] [User3] [xxx.xxx.xxx.xxx] 验证完成, 处理结果: SUCCESS
[2023/07/18 19:11:29] [INFO]: [Socket] [User3] [xxx.xxx.xxx.xxx] 即将断开连接, 本次验证耗时: 122ms
[2023/07/18 19:12:08] [INFO]: [Socket] [User4] [xxx.xxx.xxx.xxx] 验证完成, 处理结果: SUCCESS
[2023/07/18 19:12:08] [INFO]: [Socket] [User4] [xxx.xxx.xxx.xxx] 即将断开连接, 本次验证耗时: 128ms

[2023/07/18 19:35:22] [INFO]: [Socket] [User7] [xxx.xxx.xxx.xxx] 验证完成, 处理结果: SUCCESS
[2023/07/18 19:35:22] [INFO]: [Socket] [User7] [xxx.xxx.xxx.xxx] 即将断开连接, 本次验证耗时: 114ms
```

---

## 🔓 CryptoVerifier 数据保护机制

- CryptoVerifier 采用多重加密手段对许可证文件进行加密，并使用动态加密技术对配置密钥进行加密存储，以确保许可证文件的数据安全性。
- CryptoVerifier 使用多层加密措施，包括对许可证文件和配置密钥的加密，以提供更高的数据保护级别。

---

## ❓ CryptoVerifier 防伪和滥用许可证的措施

- CryptoVerifier 采用多重加密和验证手段，防止许可证文件被伪造。此外，它提供了多种功能，如硬件标识 (HWID)、IP、使用次数和并行限制等，以防止许可证的滥用问题。

---

## ✅ CryptoVerifier 许可证更新和过期处理

- CryptoVerifier 支持对已有许可证进行更新。开发者可以使用 CryptoVerifier 提供的命令行工具来更改许可证的参数，例如重置硬件标识 (HWID)、IP 或更改限制次数等。
- 对于过期的许可证，CryptoVerifier 提供了自定义选项，开发者可以根据需求进行配置，选择是删除过期许可证还是保留它。

---

## 👀 CryptoVerifier 是否适合您?

CryptoVerifier 是专为 Java 平台设计的许可证保护系统，您可以用它保护 Java 程序、Minecraft 插件、Minecraft 模组等，您也可以使用 API 来拓展功能适应您目前的使用环境。

CryptoVerifier 的主要目标是在网络验证过程中提供安全保护，而并非致力于提供针对字节码级别的代码保护。对于代码保护需求，建议使用其他专门的工具和技术，以确保您的代码在字节码级别的安全性。

尽管 CryptoVerifier 提供了多种功能，但仍需注意以下事项：

- CryptoVerifier 提供了多重保护措施，但安全性并不完全依赖于它。它仅在其控制范围内尽力提供安全性
- 如果您的 Java 文件可以被反编译后阅读，黑客仍然可能绕过 CryptoVerifier 的保护。如果黑客具备足够的技术支持攻克您的混淆代码**，CryptoVerifier 将无法提供绝对保护**。

请注意，安全性不仅取决于 CryptoVerifier，还取决于您采取的其他安全措施和实施的最佳实践，它并不能保证百分之百的安全，它只是为了 **尽量** 保护软件免受盗版、非法复制和滥用等威胁。

---

## ⚠️ 产品上手门槛

要顺利使用 CryptoVerifier，了解 Java 基础知识并能够理解 Java 文档 (JavaDoc) 是必要的。以下是一些需要掌握的基本要求：

- Java 编程语言: CryptoVerifier 是专为 Java 平台设计的许可证保护系统，因此熟悉 Java 语法、关键字、面向对象编程和基本的多线程编程概念是必要的。
- JavaDoc 阅读能力: JavaDoc 是 Java 官方的文档注释工具，它提供了对类、方法、字段等的说明和文档，您必须理解 JavaDoc 才能够使用 API 开始开发插件。

---