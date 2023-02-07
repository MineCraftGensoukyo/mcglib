package moe.gensoukyo.lib.rpg;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public final class SimpleStateMachine {

    public enum Phase {
        IN, UPDATE, OUT
    }

    private final Invoker[] invokers;
    private final Object2IntMap<String> stateMap;
    private int current = -1, next = -1;

    public SimpleStateMachine(String... states) {
        if (states.length == 0) throw new IllegalArgumentException();
        Object2IntMap<String> map = new Object2IntOpenHashMap<>();
        for (int i = 0; i < states.length; i++) map.put(states[i], i);
        map.defaultReturnValue(-1);
        stateMap = Object2IntMaps.unmodifiable(map);

        invokers = new Invoker[states.length * 3];
    }

    public SimpleStateMachine stateInvoke(String state, Phase phase, Invoker inv) {
        int sid = stateMap.getInt(state);
        if (sid < 0) throw new IllegalStateException("Unregistered state name");
        if (phase == null) throw new IllegalStateException("phase can not be null");

        invokers[sid * 3 + phase.ordinal()] = inv;
        return this;
    }

    public void update(Object... obj) {
        if (current < 0) {
            current = 0;
            runState(current, Phase.IN, obj);
        }

        // update current state
        runState(current, Phase.UPDATE, obj);

        // if next state is queued, the goto next state.
        if (next >= 0) {
            runState(current, Phase.OUT, obj);
            runState(next, Phase.IN, obj);
            current = next;
            next = -1;
        }
    }

    private void runState(int state, Phase phase, Object... obj) {
        Invoker inv = invokers[state * 3 + phase.ordinal()];
        if (inv == null) return;

        String name = inv.run(obj);
        if (name != null) {
            int n = stateMap.getInt(name);
            if (phase == Phase.UPDATE && n >= 0) {
                next = n;
            }
        }
    }

    public interface Invoker {
        String run(Object... parameters);
    }
}
