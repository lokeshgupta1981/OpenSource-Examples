package com.howtodoinjava.agent;

import java.util.Map;

public class AgentLoopDemoCheck {
    public static void main(String[] args) {
        var unknown = new AgentLoopDemo.AgentLoop(
            (goal, history) -> new AgentLoopDemo.Call("missing", "42"),
            Map.of(), 1);
        expect(IllegalArgumentException.class, () -> unknown.run("order 42"));

        var repeated = new AgentLoopDemo.AgentLoop(
            (goal, history) -> new AgentLoopDemo.Call("again", "42"),
            Map.of("again", argument -> "no change"), 2);
        expect(IllegalStateException.class, () -> repeated.run("order 42"));
    }

    private static void expect(Class<? extends Exception> type, Runnable action) {
        try {
            action.run();
            throw new AssertionError("Expected " + type.getSimpleName());
        } catch (Exception error) {
            if (!type.isInstance(error)) {
                throw new AssertionError("Wrong exception: " + error, error);
            }
        }
    }
}
