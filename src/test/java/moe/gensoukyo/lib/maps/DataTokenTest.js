var DataToken = Java.type("moe.gensoukyo.lib.maps.DataToken");
var AtomicInteger = Java.type("java.util.concurrent.atomic.AtomicInteger");

var token = new DataToken("DataTokenTest", AtomicInteger.class, function () {
    return new AtomicInteger(42);
})
function interact(e) {
    var num = token.get(e.block.tempdata);
    e.player.message("num = " + num.getAndIncrement());
}