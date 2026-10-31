# Beads talk: dry run

15-minute slot with a **live local demo**: Claude Code agents build a small Java project while the dashboard shows the work. The script runs about 14:30 at a calm, clear pace (about 100 words per minute). The demo is 4 minutes of it. The same text is in the speaker notes of `Beads.pptx`.

Slides 14 to 16 are **BACKUP** slides. Use 14 and 15 only if the demo fails, and 16 if someone asks what `bd init` changes in the repo.

## Timing

| # | Slide | Start | End | Length |
|---|---|---|---|---|
| 1 | Title | 0:00 | 0:30 | 0:30 |
| 2 | AI agents forget things | 0:30 | 1:35 | 1:05 |
| 3 | What is Beads? | 1:35 | 2:45 | 1:10 |
| 4 | Tasks connected like beads | 2:45 | 3:55 | 1:10 |
| 5 | How the agent works | 3:55 | 5:05 | 1:10 |
| 6 | Agents build it, Beads keeps track | 5:05 | 5:50 | 0:45 |
| 7 | Live demo | 5:50 | 9:50 | 4:00 |
| 8 | The dashboard: what the agents did | 9:50 | 10:15 | 0:25 |
| 9 | Under the hood | 10:15 | 11:30 | 1:15 |
| 10 | Setting it up | 11:30 | 12:05 | 0:35 |
| 11 | Other tools like Beads | 12:05 | 12:55 | 0:50 |
| 12 | Pros & cons | 12:55 | 14:10 | 1:15 |
| 13 | What to remember | 14:10 | 14:35 | 0:25 |
| 14 to 16 | Backup slides | demo fails / questions | | |

**Check the clock at:** slide 4 by 2:45, demo start (slide 7) by 5:50, slide 11 by 12:05.

**If the demo runs long:** you don't need to wait for the end. Say "In my tests this finishes in about three minutes", go to slide 8 and show the Activity view at the end of the talk. You can also cut slide 9 to two sentences: "The database is Dolt, a SQL database with Git-style history, in the .beads folder. You share it with bd dolt push, through your normal Git remote."

## The live demo

**Not measured yet with the current spec.** The spec now also builds a web page (a 4th task, after the cart), so the run is longer than the earlier tests (2:45 and 3:15, with 3 tasks). Time the first rehearsal. **Rehearse it at least twice**, the day before and on the day, on the laptop you will use.

### What the audience sees

- Left: Claude Code in a terminal.
- Right: the dashboard (Bead Me Up, Scotty) in the browser, on the **Board** view.

You type `bd init --non-interactive` and start the dashboard, then one line for Claude. Claude plans 4 tasks, two agents build in parallel, a reviewer checks, the cards move, and new bug cards appear. At the end you start the shop and show it working in the browser, then the **Activity** and **Graph** views.

### Already installed on this Mac

Java 21 (SDKMAN), Maven, Git, Node 22, Claude Code, `bd` 1.3.2 (`@beads/bd`) and the dashboard (`scotty`). Nothing to install. The steps to install them are in `beads-examples/README.md`.

### Before every run (each new terminal)

Java must be 21. The default here is 17, and the agents run `mvn`, so with 17 the build fails.

```bash
sdk use java 21.0.8-tem
java -version 2>&1 | head -1      # must say 21
```

### Reset the demo project (before each rehearsal and the day of the talk)

Run in this order. `setup.sh` deletes and recreates the folder. `--no-init` does everything **except `bd init`**: you type that one live.

```bash
bash ~/IdeaProjectsPocs/tech-pocs/personal/presentations/beads/beads-examples/setup.sh ~/beads-demo/shop --no-init
cd ~/beads-demo/shop
mvn -q test                       # downloads JUnit now, not on stage
claude                            # accept "trust this folder", then /exit
```

Tested: after `--no-init` the folder has the spec, the agents in `.claude/` and a plain git repo, with no `.beads`. A live `bd init --non-interactive` then takes about 6 seconds and leaves `git status` clean. Without `--non-interactive`, `bd init` asks questions in a terminal.

### Right before the talk

Open three terminals (the third is for starting the shop at the end), all on Java 21 (see above), all in the project folder, and have the browser ready on the right:

```bash
cd ~/beads-demo/shop
```

Don't start anything yet. The setup is typed on stage (next section).

### Live setup (about 40 seconds, at the start of slide 7)

Terminal 2 (Claude Code terminal, on the left):

```bash
bd init --non-interactive         # ~6 s, no questions
ls -a                             # .beads, CLAUDE.md, AGENTS.md appear
bd list                           # "No issues found"
```

Terminal 1 (dashboard): the browser opens on the empty Board. If you see the project picker, click **Add project** and choose `shop`.

```bash
POSTHOG_KEY= scotty
```

Back in Terminal 2, start Claude Code:

```bash
claude --permission-mode acceptEdits --allowedTools "Bash(bd:*)" "Bash(mvn:*)" "Agent"
```

Terminal on the left, browser on the right. Big terminal font (Cmd +). Browser zoom about 125%, so the cards are readable. To stop the dashboard: Control+C.

**Time:** this uses most of the 45 seconds of slack (runs took 2:45 and 3:15 in a 4:00 slot). If you are behind, skip `ls -a` and `bd list`, or fall back to a full `setup.sh` (without `--no-init`) and start at `scotty`.

**Untested, check in the first rehearsal:** the dashboard is started after `bd init`, and `claude` is opened (and trusted) before `bd init` added its hook in `.claude/settings.json`. If Claude Code asks for a new approval on stage, accept it, then rehearse again to see whether it comes back.

### Run it (about 4 minutes)

Type exactly this:

```
/coordinate Build everything in SPEC.md.
```

| Time | What you see | What you say |
|---|---|---|
| 0:00 | Empty board | "The spec: a small Java shop with a web page, four features. On the right, the dashboard. Empty." |
| 0:20 | 4 cards, the cart and the web page are Blocked | "Claude planned four tasks. The cart waits for the catalog and the discounts. The web page waits for the cart." |
| 0:40 | 2 cards In Progress | "Two agents work at the same time. Each one writes the code and the tests. I'm not typing anything." |
| 1:30 | Reviewers run | "Now the reviewer checks each task. It runs the tests and compares the code with the spec." |
| 2:00 | Cards move to Done, the cart starts | "Approved, so the coordinator closes the task. Now the cart is ready, and a new agent starts on it." |
| next | The web page task runs, then its review | "Last task: the web page. It uses the cart we just built." |
| waiting | Bug cards appear in Ready | "If the reviewer finds a small problem outside the task, it's saved as a bug. Watch the new cards." |
| end | Claude prints the final report | "All tests passed. Nothing was committed. I still review the code." |
| end + 0:20 | The shop in the browser | In **Terminal 3** run the two lines below, open http://localhost:8080. Add 2 apples and 1 milk, type `SAVE10`: "Here is the result of the agents' work." Total should show 2.52. |
| end + 0:50 | Activity and Graph | Click **Activity**: "Everything the agents did, newest first." Click **Graph**: "How the tasks and bugs are connected." |

Terminal 3 (Java 21 active), once Claude has finished:

```bash
cd ~/beads-demo/shop && mvn -q compile && java -cp target/classes com.example.shop.Main
```

Stop the shop with Control+C. Check ahead of time that port 8080 is free (`lsof -i :8080`); if not, add a port at the end of the `java` line, for example `... com.example.shop.Main 8081`.

### If something goes wrong

- **Claude asks a question:** answer short, then let it continue.
- **A permission prompt appears:** approve it. (It means a command is not in `--allowedTools`.)
- **The board doesn't move:** refresh the browser. It polls `bd` for changes.
- **Nothing works:** "Live demos, right?" Press Esc, go to slide 8 (the dashboard screenshots) and the BACKUP slides 14 and 15, then continue with slide 9.

## Before you start

- [ ] Presenter view on, notes visible, timer running
- [ ] Demo rehearsed today, then reset with `--no-init` (Java 21 active, new project, folder trusted, `mvn -q test` run once)
- [ ] Three terminals open in `~/beads-demo/shop`, all on Java 21, nothing started yet; port 8080 free; browser on the right with zoom set
- [ ] Live setup ready to type: `bd init --non-interactive`, `ls -a`, `bd list`, `POSTHOG_KEY= scotty`, then the `claude --allowedTools` command; terminal font big
- [ ] Repo link ready to paste in the chat: https://github.com/gastownhall/beads

## Run-through, slide by slide

### 1. Title  (0:00 to 0:30)

**Key message:** Agents forget; Beads helps them remember.

Hi everyone. Today I want to show you Beads. It's a small open source tool that fixes a big problem with AI coding agents: they forget things. I'll show you the problem, how Beads works, how Claude Code uses it, other tools like it, and the good and the bad parts.

**Bridge to next slide:** "Let's start with the problem."

### 2. AI agents forget things  (0:30 to 1:35)

**Key message:** The problem is not intelligence, it's memory.

The agent works well for a while. Then the chat gets too long, or you start a new one, and the plan is gone. Every morning, you explain everything again.

The usual fix is a markdown file with the plan. That works for a day or two. After a week, you have PLAN.md, TODO.md, PROGRESS.md, and nobody knows which one is right.

And the worst part: the agent finds a bug while fixing something else. It says it once, and then it's lost.

So the problem is not that the agent is not smart. The problem is that it has no good place to write things down.

**Bridge to next slide:** "There's a tool for this. It's called Beads."

### 3. What is Beads?  (1:35 to 2:45)

**Key message:** A to-do list for agents, inside your repo.

So, what is Beads? It's a to-do list made for AI agents, not for people. It's a command line tool called bd. It saves your tasks, and also what each task is waiting for. And it all lives inside your project, next to your code.

Three things make it different. It's a command line tool that can answer in JSON, so it's easy for an agent to use. It lives in your repo, so it's saved with your code. And it was made for agents from day one.

Quick facts: Steve Yegge created it in October 2025. It's free and open source, with more than twenty-eight thousand stars on GitHub. The latest version is 1.3.2, from this month.

**Bridge to next slide:** "Let's look at the tasks."

### 4. Tasks connected like beads  (2:45 to 3:55)

**Key message:** Tasks know what they wait for, so the agent knows what's next.

Each task is called a bead. And like beads on a string, they are connected.

Every task gets a short ID, like shop-s05. It starts with your project name, and the rest is random.

Each task has a priority, from P0, urgent, to P4, someday. And a status: open, in progress, blocked or done.

Look at the picture. The token endpoint is done. The login UI is in progress. The tests are waiting for the UI. Because Beads knows what waits for what, it can tell the agent what it can do right now.

And the red one: while working, the agent found a bug. It saved it as a new task, linked to where it was found. No more lost bugs.

**Bridge to next slide:** "Now, the agent's daily routine."

### 5. How the agent works  (3:55 to 5:05)

**Key message:** Five steps. The agent keeps the list, not you.

How does the agent use it? The same five steps, every time.

One, catch up. A new chat starts and the agent runs bd prime. This prints the rules and saved notes.

Two, pick. The agent runs bd ready. It shows only tasks that are not waiting for anything. The agent just asks: "what can I do now?"

Three, claim. bd update --claim says "this one is mine".

Four, write down. If it finds something new, it creates a new task.

Five, finish. It closes the task, and the tasks that were waiting are free to start.

The big idea: you don't update the list. The agent does. Tomorrow, in a new chat, it asks "what's next?" and keeps going.

**Bridge to next slide:** "Let's go one step further."

### 6. Agents build it, Beads keeps track  (5:05 to 5:50)

**Key message:** A coordinator, implementers and a reviewer do the work. Beads keeps track.

Now let's go one step further: agents. The main Claude session is the coordinator. It reads the spec, plans the small tasks in Beads, and gives each ready task to an implementer agent. A reviewer agent checks the work. The task is closed only when the reviewer approves. Small problems the reviewer finds become new bugs in Beads. And on a dashboard, you see everything as it happens. Let me show you, live.

**Bridge to next slide:** "Let me show you, live." (switch to the demo screen)

### 7. Live demo  (5:50 to 9:50)

**Key message:** From a spec to working code, with no commands typed, live on the dashboard.

[Start] Here is the spec: a small Java shop with three features. A catalog, discount codes, and a cart. On the right, the dashboard. It's called Bead Me Up, Scotty, a free community tool. It's empty. In Claude Code, I type one line: coordinate, build everything in the spec.

[While it plans] Claude reads the spec and plans. Look at the board: three tasks. The cart is blocked, because it needs the catalog and the discounts.

[While the agents build] Now two implementer agents work at the same time: one on the catalog, one on the discounts. Their cards are In Progress. Each agent writes the code and the tests. I'm not typing anything.

Now the reviewer checks each task. It runs the tests and compares the code with the spec. Only when it says "approved", the coordinator closes the task. Look, the cards move to Done. And now the cart is ready, so a new agent starts on it.

[While you wait] If the reviewer finds a small problem outside the task, the coordinator saves it as a bug. Watch: new cards appear in Ready.

[At the end] Now the Activity view: everything the agents did, newest first. Created, closed, new bugs. And the Graph shows how the tasks and the bugs are connected. All tests passed, and nothing was committed. I still review the code.

**Bridge to next slide:** (back to the slides)

### 8. The dashboard: what the agents did  (9:50 to 10:15)

**Key message:** Board and activity feed, live. Community tool, same data.

So that's the dashboard. The board shows where each task is. The activity feed shows who did what, and when. It updates live, while the agents work. It's a community tool, not part of Beads, but it reads the same data.

**Bridge to next slide:** "So how does it work inside?"

### 9. Under the hood  (10:15 to 11:30)

**Key message:** Dolt database in .beads/, git-ignored, synced through your Git remote; JSONL export; files for the agents.

Now, under the hood. Beads keeps everything in the .beads folder. The database is Dolt. Dolt is a SQL database that works like Git: every change is a commit, so you have the full history of every task. It stores the issues, the links between them, the comments, and every change.

One important detail: the database itself is not in your Git commits. The embeddeddolt folder is git-ignored. To share it, you run bd dolt push, and it goes to your normal Git remote, in a special branch called refs/dolt/data. And if you want plain files, bd export gives you JSONL: one issue per line, as JSON.

Next to it, bd init adds a few files for the agents: CLAUDE.md, AGENTS.md, and some hooks. That's how Claude knows how to use it.

**Bridge to next slide:** "And getting started is easy."

### 10. Setting it up  (11:30 to 12:05)

**Key message:** bd init does most of the work. The dashboard is one command.

Setting it up is quick. Install bd with npm or brew, and run bd init. It also sets up Claude Code, Codex and Cursor. For the dashboard, install Bead Me Up, Scotty, and run it in your project. You can use Beads with your team, on someone else's project, or just for you.

**Bridge to next slide:** "Now, what else is out there?"

### 11. Other tools like Beads  (12:05 to 12:55)

**Key message:** Pick the tool by the size of the work.

Beads is not the only tool like this. Claude Code Tasks is built into Claude Code. It came after Beads, with a similar idea. Nothing to install, but it lives outside your repo and only works with Claude Code. Backlog.md saves each task as a markdown file and has a board built in. Taskmaster is great for turning a spec into tasks.

Simple rule: only Claude Code? Tasks. Plain files and a board? Backlog.md. Long work, many agents? That's where Beads is strongest.

**Bridge to next slide:** "So, is it worth it?"

### 12. Pros & cons  (12:55 to 14:10)

**Key message:** Worth it for long work, too much for small tasks.

Let's be fair and look at both sides.

The good parts. It remembers between chats. The agent always knows what's next. Nothing gets forgotten: new bugs become tasks. And it works with any agent, from your repo.

The bad parts. It's one more tool: you need to learn a bit of Dolt, especially sync and upgrades. It changes a lot: new versions every few weeks, and this year two versions were released by mistake. So pick a version. And the agent has to actually use it. And it's not for managers: no reports or roadmaps. It works next to Jira, not instead of it.

My opinion: for long work, over many days and many chats, it's really worth it. For small, quick tasks, it's too much.

**Bridge to next slide:** "Let me wrap up."

### 13. What to remember  (14:10 to 14:35)

**Key message:** Memory outside the chat. Easy to try.

Three things to remember. One: AI agents need a memory outside the chat. Two: Beads is a to-do list with links, inside your repo. Three: it's easy to try. Run bd init, and ask your agent: "what's ready?"

Thank you! Any questions?

**Bridge to next slide:** Open for questions.

## Backup slides (only if the demo fails)

### 14. Let's see it

Here are the commands. I create a big task and two small tasks inside it. The login UI uses --deps, so it waits for the endpoint.

bd ready shows only the endpoint. The agent claims it, finishes it, and closes it. Now the login UI is ready.

But the good news: you don't need to learn any of this.

### 15. You don't need the commands

I tested this with Claude Code. I never typed a bd command. I just talked to it in plain English.

First prompt: "Add OAuth login. Split it into tasks. The UI needs the endpoint, and the tests need the UI." Claude created three tasks, with the right links between them.

Second prompt: "I finished the endpoint. I found a race condition, we fix it later." Claude closed the task, created a bug, and linked it to where it was found.

Third prompt, in a brand new chat, like the next morning: "What's the state of the OAuth work? What's next?" Claude asked Beads and said: build the login UI next, and don't forget the race condition.

How does it know? When you run bd init, it adds a few lines to CLAUDE.md and a Beads skill. So Claude already knows how to use it. You just talk.

### 16. What bd init adds to your repo

If someone asks what bd init changes in the repo: it adds the .beads folder with the settings, the git hooks and the database. The database is git-ignored. It adds CLAUDE.md and AGENTS.md with the rules for the agents, a Claude Code hook that runs bd prime when a session starts, a Beads skill, and the same setup for Codex and Cursor. Nothing else in your code changes.

## Likely questions

- **What is Gas Town? Is Beads part of it?** Gas Town is Steve Yegge's multi-agent orchestrator (a "mayor" agent coordinating many worker agents). It uses Beads to track the work. Beads came first and works on its own; Gas Town is built on top of it. Both live in the same GitHub org, gastownhall. In 2026 the team also released Gas City, an SDK to build your own orchestrator, also on top of Beads. All three are active.
- **Doesn't Claude Code already do this?** Part of it. Claude Code has a task list with dependencies (Tasks), subagents, and an experimental agent teams mode with a shared task list. But those live in `~/.claude`, are tied to one Claude session, and only work with Claude. Beads lives in your repo, survives across sessions and machines, and works with any agent. They work well together: in the demo, Claude's subagents did the work and Beads kept the memory.
- **Why didn't you use Gas Town in the demo?** It's built for 10 or more agents working at the same time, with more setup. For a 15-minute talk, Beads plus a small Claude coordinator shows the core idea.
- **Where is the data? Is it in Git?** In a Dolt database in `.beads/embeddeddolt/`. Dolt is a SQL database with Git-style commits, so every change has history (`bd history <id>`). The database folder is git-ignored, so it's not in your normal commits. You share it with `bd dolt push`, which sends it to your Git remote under `refs/dolt/data`.
- **Which files does it use?** `.beads/config.yaml` (settings), `.beads/metadata.json` (which backend), `.beads/hooks/` (git hooks), the Dolt files inside `embeddeddolt/`, and, if you export, a `.jsonl` file with one issue per line as JSON. For the agents: `CLAUDE.md`, `AGENTS.md`, `.claude/settings.json` and a skill. See backup slide 16.
- **Can I run it without Dolt?** Yes, there is a no-db mode (`no-db: true` in `config.yaml`) that keeps only a JSONL file. You lose the history and the safe merges.
- **Can many agents write at the same time?** In the default embedded mode there is one writer at a time, that's why only the coordinator writes to Beads in the demo. For many writers, run Dolt in server mode.
- **What is `/coordinate`?** A custom Claude Code skill I wrote for the demo. It's a markdown file in `.claude/skills/`. Beads and Claude Code don't ship it. The two agents are markdown files in `.claude/agents/`.
- **How is this different from Jira or GitHub Issues?** Those are made for people and live outside the code. Beads is made for agents, lives in the repo, and knows what each task waits for. You can use both.
- **Does it work with Jira?** Beads has a `bd jira sync` command to push and pull issues. I didn't test it.
- **Is the dashboard part of Beads?** No. It's Bead Me Up, Scotty, a community project. Beads itself is only a command line tool. There are more than 30 community UIs.
- **Why not Claude Code Tasks?** If you only use Claude Code and the work fits in a few sessions, Tasks is enough. Beads lives in the repo, works with any agent, and is better for long work and many agents.
- **Do I need to learn all the bd commands?** No. bd init adds instructions and a Beads skill for the agent. You talk in plain English.
- **Which model did the agents use?** Haiku, to be fast and cheap. For harder work, use your default model.
- **What did it cost?** About 25 US cents in tokens for this demo.
- **Is it ready for real work?** There is a stable 1.x version, but it changes fast. Pick a version and read the release notes before you upgrade.

## Sources

- Beads repository: https://github.com/gastownhall/beads
- Releases (v1.3.2, Oct 24 2026): https://github.com/gastownhall/beads/releases
- Community tools list: https://github.com/gastownhall/beads/blob/main/docs/community-tools.md
- Bead Me Up, Scotty: https://github.com/brendan-appstart/bead-me-up-scotty
- Claude Code task list docs: https://code.claude.com/docs/en/interactive-mode
- Claude Code agent teams (experimental): https://code.claude.com/docs/en/agent-teams
- Gas Town: https://github.com/gastownhall/gastown
- Gas City: https://github.com/gastownhall/gascity
- Backlog.md: https://github.com/MrLesk/Backlog.md
- Taskmaster: https://github.com/eyaltoledano/claude-task-master
