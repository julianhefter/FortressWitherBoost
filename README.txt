# FortressWitherBoost (Paper 26.2)

This is source code, not a precompiled JAR. Build it with Java 25 and Maven (or use the included GitHub Actions workflow).

- Listens only for naturally spawned wither skeletons.
- Checks the exact spawn position for a Nether Fortress using Paper's `World.hasStructureAt`.
- Keeps the original skeleton and attempts to add four more (five total by default).
- Extra spawns use `CUSTOM` reason so they do not recursively multiply.
- If no safe nearby fortress position is found, fewer than five may spawn.

After building, upload `target/FortressWitherBoost-1.0.0.jar` to the server's `plugins` folder and restart the server. Review server logs before using on a live world.
