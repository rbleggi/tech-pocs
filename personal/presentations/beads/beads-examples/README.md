# Beads + Claude Code: agents build, a coordinator checks

Claude Code plans the work in Beads, gives each task to an **implementer** agent,
has a **reviewer** agent check it, and closes the task only when it is approved.
A web dashboard shows everything live: the board and the activity feed.

The project is a small Java 21 shop described in `template/SPEC.md`: a catalog,
discount codes, a cart and a web page to try it. Built with Maven, tested with JUnit 5.
Money is `BigDecimal`. The web page uses only the JDK's built-in HTTP server, no extra
dependencies. When the agents finish, run it with
`mvn -q compile && java -cp target/classes com.example.shop.Main` and open http://localhost:8080.

Everything runs locally. Tested end to end with bd v1.3.2 and Claude Code.

## Setup

```bash
# needed: git, Java 21, Maven, Node.js 20+, Claude Code
npm i -g @beads/bd                 # Beads CLI
bash setup.sh                      # creates ~/beads-examples/shop
cd ~/beads-examples/shop
mvn -q test                        # once, so Maven downloads JUnit now
```

`setup.sh` copies `template/` into a new folder, renames `claude-config/` to `.claude/`
and runs `bd init` (which also creates the git repository). `bd init` adds `CLAUDE.md`,
`AGENTS.md`, a session hook and the Beads skill, so Claude already knows how to use
Beads. Run `setup.sh` again any time to start clean.

To type `bd init` yourself (for example live in a talk), use `bash setup.sh <dir> --no-init`.
It does everything except `bd init`, then you run `cd <dir> && bd init --non-interactive` (without the flag, `bd init` asks questions).

The first time you open `claude` in the folder, accept "trust this folder".

## The dashboard: Bead Me Up, Scotty

[Bead Me Up, Scotty](https://github.com/brendan-appstart/bead-me-up-scotty) is a free
community web UI for Beads (not part of Beads itself). Views used in the demo:

- **Board:** Backlog, Ready, In Progress, Blocked, Done. Cards move while the agents work.
- **Activity:** what humans and agents did, newest first.
- **Graph:** how tasks and bugs are connected.
- **Insights, Epics, List, Focus:** extra views.

Install once (from a clone):

```bash
git clone https://github.com/brendan-appstart/bead-me-up-scotty
cd bead-me-up-scotty
npm install
npm run build
npm link
```

Run it from the project folder. It opens the browser on that project's board:

```bash
cd ~/beads-examples/shop
POSTHOG_KEY= scotty                # POSTHOG_KEY= turns off its anonymous usage reporting
```

If the browser shows the project picker instead, click **Add project** and choose the
`shop` folder.

Simpler alternative with only a board (no activity feed): `npm i -g beads-ui`, then
`bdui start --open` in the project folder.

## Run the agents

The main Claude session becomes the **coordinator**. It plans the tasks, gives each
ready task to an **implementer** agent, sends the result to a **reviewer** agent, and
closes the task only when the reviewer approves.

```
                    you
                     |  /coordinate Build everything in SPEC.md.
                     v
              +-------------+   bd create / ready / claim / close / note
              | coordinator | -----------------------------------------> Beads
              +-------------+                                              |
                |         ^                                           dashboard
           task |         | report
                v         |
        +---------------+       +---------------+
        |  implementer  | ----> |   reviewer    | --> APPROVED: bd close
        | code + tests  | <---- |  read-only    | --> CHANGES_NEEDED: back (max 2x)
        +---------------+       +---------------+
```

```bash
claude --permission-mode acceptEdits --allowedTools "Bash(bd:*)" "Bash(mvn:*)" "Agent"
```

```
/coordinate Build everything in SPEC.md.
```

Want to check the plan before any code is written? Run `/coordinate Plan only.` first.

### Where `/coordinate` comes from

`/coordinate` is **not** a Beads or Claude Code command. It is a custom Claude Code
skill written for this kit. Claude Code turns any `.claude/skills/<name>/SKILL.md` into a
`/<name>` command, and agents in `.claude/agents/` can be called by name. The kit has:

| File (in `template/claude-config/`, copied to `.claude/`) | What it is |
|---|---|
| `skills/coordinate/SKILL.md` | The coordinator's rules. You run it with `/coordinate`. |
| `agents/implementer.md` | Writes the code and tests for ONE task. Never runs `bd`. |
| `agents/reviewer.md` | Read-only. Runs the tests, checks the spec, answers APPROVED or CHANGES_NEEDED. |

Both agents use `model: haiku` to be fast and cheap. For harder work, remove that
line so they use your default model. You can rename or edit all three files.

**Why only the coordinator touches Beads:** one owner for the task list means no two
agents fight over the same task, and by default Beads runs in embedded mode with a
single writer. Agents only write code and report back.

### What happened in the tests

Two runs, agents on Haiku: about 2:45 to 3:15, about US$0.25 in tokens. **These numbers are
from the earlier spec with 3 tasks (no web page).** The current spec adds a fourth task, the
web page, after the cart, so a run is longer. Measure it again before relying on the time.

1. Planned 1 epic and 3 tasks. The cart waits for the catalog and the discounts.
2. Catalog and discounts were ready, so it started **2 implementers in parallel**, then
   **2 reviewers in parallel**. Both approved, both closed.
3. The cart was unblocked: implementer, reviewer, approved, closed.
4. The reviewers found 2 to 5 small issues outside their tasks (for example, an int
   overflow when the same SKU is added many times). The coordinator did not fix them.
   It filed them as bugs linked with `discovered-from`, for a human to decide.
5. Final check: `mvn -q test`, 26 to 30 tests, all passing. Nothing committed.

### Tips

- Start small: run `Plan only`, read the tasks, then build.
- If the reviewer says CHANGES_NEEDED twice, the task stays in progress with a note.
  That's your signal to look at it yourself.
- Follow-up bugs wait for you in `bd ready` (and in Ready on the dashboard).
- You review and commit at the end. The coordinator never commits or pushes.
