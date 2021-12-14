/* 需要在客户端安装MCGLib模组，否则会报ClassNotFoundException */
var Task = Java.type("moe.gensoukyo.lib.timer.Task");
var task = new Task();

function interact(e) {
    // 记录开始时间
    var startTime = e.player.world.totalTime;
    // 间隔 20 tick重复执行 12 次 callback
    // function countdown(ticks: int, count: int, callback: () => void): void
    task.countdown(20, 12, function () {
        e.player.message(e.player.world.totalTime - startTime + " ticks passed");
    });
    // 100 tick延迟后执行 callback
    // function delay(ticks: int, callback: () => void): void
    task.delay(180, function () {
        e.player.message("Time starts to flow.");
        // 60 tick延迟后执行 callback
        task.delay(60, function () {
            e.player.message("Road roller da " + (e.player.world.totalTime - startTime));
        });
    });
}