# Contributing to KnifeThrow

Thanks for your interest in contributing! This project began as a Grade 12 summative, built entirely by hand without code generators. The legacy monolith is preserved for authenticity; the refactor helps readability. Contributions should respect the original arcade feel and simplicity.

## Project layout
- Legacy (authentic): `src/knifeThrow.java` (nested classes, single file)
- Refactor (readable): `refactored/src/main/java/com/knifethrow/game/*`
- Assets and data at repo root: `sprites/`, `sounds/`, `pixelated.ttf`, `data.txt`, `configuration.txt`, `rules.txt`
- Docs: `Javadoc/`

## Prerequisites
- Java SE 11+ (OpenJDK or Oracle)
- Windows, macOS, or Linux (README commands are Windows PowerShell; adapt for your shell)

## Build and run
Windows PowerShell from repo root:

```powershell
# Legacy build
javac -encoding UTF-8 .\src\knifeThrow.java
java -cp .\src knifeThrow

# Refactored build
javac -d .\out (Get-ChildItem -Recurse .\refactored\src\main\java\com\knifethrow\game\*.java | ForEach-Object { $_.FullName })
java -cp .\out com.knifethrow.game.knifeThrow
```

Ensure the following are present:
- `sprites/`, `sounds/`, `pixelated.ttf`
- `data.txt` (created automatically if missing), `configuration.txt`, `rules.txt`

## Contribution workflow
1. Fork the repo and create a feature branch: `feat/<short-topic>` or `fix/<short-topic>`
2. Prefer changes in the refactored package unless the goal is historical preservation.
3. Keep PRs focused and reasonably small; include screenshots/GIFs for UI changes.
4. Update README or JavaDoc if behavior or APIs change.
5. Manually verify both builds still compile and run.

## Commit messages (Conventional Commits)
- `feat: add knife shop highlight state`
- `fix(panel): correct EMP timer rounding`
- `docs(readme): clarify Windows run steps`
- `refactor(player): extract jump timing to helper`
- `chore(assets): compress bg image`

Scope examples: `panel`, `player`, `knife`, `target`, `particles`, `startGame`, `docs`.

## Issue triage
- Use the provided issue templates (bug, feature, docs). Include OS, Java version, reproduction steps, and whether assets exist.
- Label suggestions: `bug`, `enhancement`, `documentation`, `discussion-needed`.

## Coding guidelines
- Match the existing style (brace placement, naming); do not reformat unrelated code.
- Avoid large architectural changes in the legacy file; favor additive, well-scoped patches in the refactor.
- Keep gameplay feel intact unless a change is explicitly requested.
- Use clear names; minimal inline comments unless clarifying non-obvious logic.

## Testing checklist (manual)
- Compiles cleanly (`javac`) in both legacy and refactor builds.
- Launches to menu; audio plays if available.
- New feature toggles or UI items are reachable and responsive.
- No crashes when assets are missing (graceful behavior preferred).
- Data persists to `data.txt` when expected.

## Reporting security issues
Please avoid filing public issues for security concerns. You may contact Sunny via the email present in the source headers.

## License
Personal and educational use only unless explicitly approved by Sunny Patel. By contributing, you agree your changes are compatible with the repository's license.
