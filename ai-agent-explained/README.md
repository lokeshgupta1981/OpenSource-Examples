# Source code for the article https://howtodoinjava.com/?p=44493

Java 21 or later. Run from this directory:

```shell
javac -d out src/main/java/com/howtodoinjava/agent/AgentLoopDemo.java
java -cp out com.howtodoinjava.agent.AgentLoopDemo
```

Check the unknown-tool and tool-call-limit cases:

```shell
javac -d out src/main/java/com/howtodoinjava/agent/AgentLoopDemo.java src/test/java/com/howtodoinjava/agent/AgentLoopDemoCheck.java
java -cp out com.howtodoinjava.agent.AgentLoopDemoCheck
```

`ScriptedDecider` is a deterministic stand-in, not an LLM. It tests the loop's tool dispatch, state passing, branching, and call limit without a model account. A model integration must implement `DecisionMaker` by sending the goal, available tool schemas, and history to a tool-capable model, then validating its returned tool request.
