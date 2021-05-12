## 注意事项：
* 这个mod的设计目的包括在任何Forge环境下使用，包括单机游戏/纯Forge服务端，而不仅是MCG环境/Bukkit服务端环境。

* 所有涉及Bukkit的代码必须放在`moe.gensoukyo.lib.server`包内，  
且必须打上`SideOnly(Side.SERVER)`(针对java)/`@file:SideOnly(Side.SERVER)`(针对Kotlin)

* 所有使用了CNPC的类(作为参数/返回值/局部变量等任何地方)的方法必须打上`@Optional.Method(modid = ModIds.CNPC)`