# Apache Pekko: Actor Architecture - Speaking Script
## 5-Minute Presentation with Detailed Timing

### Slide 1: Title Slide (0:00 - 0:30)
**Duration: 30 seconds**

**0:00-0:05**: "Good morning/afternoon everyone. Today I'll be presenting Apache Pekko and its Actor architecture."

**0:05-0:15**: "We'll explore how Pekko implements the Actor Model for building concurrent and distributed systems, with a brief comparison to Erlang OTP."

**0:15-0:30**: "This presentation will cover the framework's features, architecture, and a code comparison with Erlang OTP. Let's begin by looking at what Apache Pekko offers as a framework."

---

### Slide 2: Apache Pekko Framework Features (0:30 - 1:15)
**Duration: 45 seconds**

**0:30-0:40**: "Apache Pekko is a powerful toolkit for building concurrent, distributed, and resilient message-driven applications on the JVM."

**0:40-0:55**: "The framework provides a comprehensive set of features including the Actor System for hierarchical actor management with supervision, Clustering for distributed systems across multiple nodes, and Streams for reactive data processing."

**0:55-1:10**: "It also offers built-in fault tolerance through supervision strategies, persistence with event sourcing and CQRS patterns, and high-performance HTTP server and client capabilities."

**1:10-1:15**: "Now let's dive deeper into Pekko's actor architecture and how these features work together."

---

### Slide 3: Pekko Actor Architecture & Features (1:15 - 2:30)
**Duration: 75 seconds**

**1:15-1:30**: "In Pekko, actors are the core building blocks of the concurrency model. The architecture consists of three main components:"

**1:30-1:45**: "First, the ActorSystem hosts multiple actors and manages their entire lifecycle. Second, ActorRefs serve as references for sending messages between actors. And third, each actor has its own mailbox that queues incoming messages for processing."

**1:45-2:05**: "Looking at the advanced features, Pekko's clustering capability allows actors to communicate transparently across multiple nodes, forming a truly distributed system. This enables horizontal scaling and geographic distribution of your applications."

**2:05-2:25**: "The supervision hierarchy is particularly powerful - it isolates failures and provides automatic recovery strategies. When an actor fails, its supervisor can decide to restart, resume, stop, or escalate the failure, ensuring system resilience."

**2:25-2:30**: "Event Sourcing support facilitates building event-driven systems, while various message patterns like request-response and publish-subscribe provide flexibility in communication."

---

### Slide 4: Code Comparison - Pekko vs Erlang OTP (2:30 - 3:45)
**Duration: 75 seconds**

**2:30-2:40**: "To provide context for Pekko's approach, let's compare its code with Erlang OTP, another mature framework that implements the Actor Model."

**2:40-2:55**: "On the left, we see Pekko code with strong typing using Scala case classes. The Behavior system provides a functional approach to defining actor behavior, where actors return their next behavior after processing each message."

**2:55-3:15**: "On the right, Erlang OTP uses the gen_server behavior with callback functions. It follows a more procedural approach with separate handler functions for different message types - handle_cast for asynchronous messages and handle_info for direct messages."

**3:15-3:35**: "Both languages use the ! operator for asynchronous message passing, but they differ significantly: Pekko has strong compile-time typing while Erlang is dynamically typed; Pekko uses the Behavior pattern while Erlang uses callbacks; and Pekko runs on the JVM while Erlang runs on the BEAM virtual machine."

**3:35-3:45**: "Additionally, Pekko offers both tell (!) and ask (?) patterns for communication, providing more flexibility in message exchange patterns."

---

### Slide 5: Key Takeaways (3:45 - 4:30)
**Duration: 45 seconds**

**3:45-3:55**: "Let me summarize the key takeaways from our exploration of Apache Pekko's Actor architecture."

**3:55-4:10**: "First, Apache Pekko provides a robust, production-ready implementation of the Actor Model that's perfect for building concurrent and distributed systems on the JVM, with strong type safety and excellent tooling."

**4:10-4:25**: "Second, Pekko's Actor architecture offers state isolation, asynchronous communication, and supervision hierarchy, creating systems that are naturally resilient to failures and can scale horizontally."

**4:25-4:30**: "Third, the framework's comprehensive feature set makes it suitable for everything from simple concurrent applications to complex distributed systems."

---

### Slide 6: References & Further Reading (4:30 - 5:00)
**Duration: 30 seconds**

**4:30-4:40**: "For those interested in learning more, I've included valuable references for further reading."

**4:40-4:50**: "The Apache Pekko documentation provides comprehensive guides and tutorials, while the comparison resources help understand the differences between various Actor Model implementations."

**4:50-5:00**: "These resources will help you deepen your understanding of the Actor Model and its practical implementation in Pekko. Thank you for your attention. I'm happy to answer any questions about Apache Pekko's Actor architecture."

---

## Presentation Tips:

### Timing Guidelines:
- **Total Duration**: 5 minutes (300 seconds)
- **Slide Transitions**: Keep transitions smooth and quick (2-3 seconds)
- **Code Examples**: Point to specific parts of the code while explaining
- **Comparison Slide**: Use gestures to indicate left (Pekko) and right (Erlang) sides

### Key Emphasis Points:
1. **Framework Features**: Emphasize the comprehensive toolkit that Pekko provides
2. **Message Passing**: Stress the asynchronous, non-blocking nature
3. **Type Safety**: Highlight Pekko's compile-time guarantees
4. **Practical Applications**: Connect concepts to real-world use cases
5. **Advanced Features**: Emphasize Pekko's production capabilities

### Delivery Notes:
- Maintain eye contact with the audience
- Use hand gestures to point to code examples and architectural diagrams
- Speak clearly and at a moderate pace
- Pause briefly after key concepts to let them sink in
- Be prepared for questions about performance, scalability, or specific use cases
- Emphasize that the focus is on Pekko, with Erlang only as comparative context
- With the Overview slide removed, spend more time on the framework features and architecture details

