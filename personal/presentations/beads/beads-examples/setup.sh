#!/usr/bin/env bash
# Creates a fresh demo project with Claude Code agents, with or without Beads.
# Usage: bash setup.sh [target-dir] [--no-init]      (default dir: ~/beads-examples/shop)
#   (no flag)   full setup, including `bd init`
#   --no-init   stop before `bd init`: you type `bd init` yourself, e.g. live on stage
# Needs: git, Java 21, Maven, bd (npm i -g @beads/bd), claude
set -e
HERE="$(cd "$(dirname "$0")" && pwd)"
DIR="$HOME/beads-examples/shop"
INIT=1
for arg in "$@"; do
  case "$arg" in
    --no-init) INIT=0 ;;
    *) DIR="$arg" ;;
  esac
done

rm -rf "$DIR"
mkdir -p "$DIR"
cp -R "$HERE/template/." "$DIR/"
cd "$DIR"
mv claude-config .claude      # agents and the /coordinate skill for Claude Code

if [ "$INIT" = 1 ]; then
  bd init -q >/dev/null       # also creates the git repo and adds CLAUDE.md, AGENTS.md, hooks and the Beads skill
  git add -A && git commit -qm "Start: spec, agents, beads" || true
  echo "Ready: $DIR"
  echo "Next:  cd $DIR && claude"
else
  git init -q                 # a plain git repo; `bd init` will reuse it
  git add -A && git commit -qm "Start: spec and agents" || true
  echo "Ready (no Beads yet): $DIR"
  echo "Next:  cd $DIR && bd init --non-interactive"
fi
