package com.howtodoinjava.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AgentLoopDemo {

    record Event(String tool, String argument, String result) { }

    sealed interface Decision permits Call, Answer { }
    record Call(String tool, String argument) implements Decision { }
    record Answer(String text) implements Decision { }

    interface DecisionMaker {
        Decision next(String goal, List<Event> history);
    }

    interface Tool {
        String run(String argument);
    }

    static class AgentLoop {
        private final DecisionMaker decisionMaker;
        private final Map<String, Tool> tools;
        private final int maxCalls;

        AgentLoop(DecisionMaker decisionMaker, Map<String, Tool> tools, int maxCalls) {
            this.decisionMaker = decisionMaker;
            this.tools = Map.copyOf(tools);
            this.maxCalls = maxCalls;
        }

        String run(String goal) {
            List<Event> history = new ArrayList<>();
            for (int call = 0; call <= maxCalls; call++) {
                Decision decision = decisionMaker.next(goal, List.copyOf(history));
                if (decision instanceof Answer answer) {
                    return answer.text();
                }
                if (call == maxCalls) {
                    throw new IllegalStateException("Tool-call limit reached: " + maxCalls);
                }
                Call requested = (Call) decision;
                Tool tool = tools.get(requested.tool());
                if (tool == null) {
                    throw new IllegalArgumentException("Unknown tool: " + requested.tool());
                }
                String result = tool.run(requested.argument());
                history.add(new Event(requested.tool(), requested.argument(), result));
                System.out.println(requested.tool() + "(" + requested.argument() + ") -> " + result);
            }
            throw new IllegalStateException("Decision maker did not return an answer");
        }
    }

    // A deterministic stand-in used to test the loop; this is not an LLM.
    static class ScriptedDecider implements DecisionMaker {
        public Decision next(String goal, List<Event> history) {
            String orderId = goal.replaceAll("\\D+", "");
            if (history.isEmpty()) {
                return new Call("orderStatus", orderId);
            }
            String status = history.get(0).result();
            if (status.equals("SHIPPED")) {
                return new Answer("Order " + orderId + " has shipped, so it cannot be cancelled.");
            }
            if (history.size() == 1) {
                return new Call("cancellationPolicy", status);
            }
            return new Answer("Order " + orderId + ": " + history.get(1).result());
        }
    }

    public static void main(String[] args) {
        Map<String, Tool> tools = Map.of(
            "orderStatus", id -> Map.of("42", "PACKING", "43", "SHIPPED")
                .getOrDefault(id, "UNKNOWN"),
            "cancellationPolicy", status -> status.equals("PACKING")
                ? "Cancellation is allowed before dispatch."
                : "Check the order with support."
        );
        AgentLoop agent = new AgentLoop(new ScriptedDecider(), tools, 3);
        System.out.println(agent.run("Can I cancel order 42?"));
        System.out.println(agent.run("Can I cancel order 43?"));
    }
}
