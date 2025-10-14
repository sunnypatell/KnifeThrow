# KnifeThrow 🎯

Welcome to KnifeThrow, a thrilling 2D knife throwing mini-game written in Java! Test your skills, aim, and timing as you throw knives at spinning targets. Developed by Sunny Jayendra Patel, KnifeThrow features exciting gameplay, customizable options, and potential for further development.

![KnifeThrow Gameplay](/knifethrow_demonstration.jpg)


## Features 🚀
- **Menu Screen:** Start the game with an intuitive menu interface.
- **High Score System:** Compete with yourself and others by aiming for the highest score.
- **Knife Shop:** Unlock various knives with unique abilities to enhance your gameplay.
- **Collision Detection:** Experience realistic interactions with targets and knives.
- **Animation:** Enjoy smooth animations that enhance the gaming experience.
- **Sound Effects:** Immerse yourself in the game with captivating sound effects.

## Running the Game 🎮
To run KnifeThrow, ensure you have the Java Development Kit (JDK) installed on your computer. Then, compile and run the `KnifeThrow.java` file using the following commands:

```bash
javac KnifeThrow.java
java KnifeThrow
```
## Dependencies 🛠️
- `java.awt.*`: Abstract Window Toolkit for basic GUI functionality.
- `java.io.*`: Input/output operations for reading and writing data.
- `java.util.*`: Utility classes for various functionalities.
- `javax.swing.*`: Additional GUI components and features.

## Customization 🎨
KnifeThrow is a project developed by Sunny Jayendra Patel and is intended solely for playing purposes. It is not intended to be modified or distributed for commercial purposes. Users are encouraged to enjoy the game as-is and share it freely with others for entertainment purposes only. Any attempt to modify or distribute the game for commercial gain is strictly prohibited.

## GitHub Stats 📊
- [![GitHub Repo Stars](https://img.shields.io/github/stars/sunnypatell/KnifeThrow?style=social)](https://github.com/sunnypatell/KnifeThrow/stargazers)
- [![GitHub Issues](https://img.shields.io/github/issues/sunnypatell/KnifeThrow)](https://github.com/sunnypatell/KnifeThrow/issues)
- [![GitHub Pull Requests](https://img.shields.io/github/issues-pr/sunnypatell/KnifeThrow)](https://github.com/sunnypatell/KnifeThrow/pulls)

## Credits 🙌
- **Developer:** Sunny Jayendra Patel
- **Media:** Any additional media used in the game, such as images and sounds, are property of their respective owners and used with permission.

## Future Developments 🚧
KnifeThrow has exciting potential for further development:
- **New Levels/Modes:** Introduce survival mode or level editor for diverse gameplay.
- **Multiplayer Functionality:** Implement online leaderboards or cooperative play.
- **Enhanced Graphics:** Improve visuals with detailed backgrounds and effects.
- **Audio Enhancements:** Add more sounds and music for an immersive experience.
- **Customization Options:** Allow players to personalize characters and knives.

## Maintenance 🛠️
Regularly review and update KnifeThrow to ensure its functionality and enjoyment:
- **Bug Fixes:** Address any discovered bugs or glitches promptly.
- **Game Balancing:** Maintain a challenging yet fair gaming experience.
- **Content Updates:** Add new features and content to keep the game fresh.
- **Optimization:** Improve performance and compatibility for various devices.

## Conclusion 🎉
KnifeThrow offers an exhilarating gaming experience with its engaging gameplay, customization options, and potential for expansion. Whether you're a seasoned gamer or new to the genre, KnifeThrow promises hours of entertainment. Keep aiming, keep throwing, and enjoy the thrill of KnifeThrow!

---

## Table of Contents 🗂️
1. [Origins and Vision](#origins-and-vision-)
2. [Development Journey Timeline](#development-journey-timeline-)
3. [Architectural Overview](#architectural-overview-)
   1. [Monolithic Legacy Build](#monolithic-legacy-build)
   2. [Modular Refactored Build](#modular-refactored-build)
4. [Gameplay Systems](#gameplay-systems)
   1. [Game Loop and Timing](#game-loop-and-timing)
   2. [Rendering Pipeline](#rendering-pipeline)
   3. [Input Handling](#input-handling)
   4. [Collision and Physics Model](#collision-and-physics-model)
   5. [Scorekeeping and Progression](#scorekeeping-and-progression)
   6. [Audio Design](#audio-design)
   7. [Particle and Visual Feedback](#particle-and-visual-feedback)
5. [Resource and Data Management](#resource-and-data-management)
6. [Interface Design](#interface-design)
7. [Testing and Quality Assurance](#testing-and-quality-assurance)
8. [Lessons Learned in Grade 12](#lessons-learned-in-grade-12)
9. [Frequently Asked Questions](#frequently-asked-questions)
10. [Contribution Guide](#contribution-guide)
11. [License and Usage Notes](#license-and-usage-notes)

## Origins and Vision 🌱
KnifeThrow began as a Grade 12 computer science capstone created four years ago in the pre generative AI era. The goal was to design a responsive arcade experience that could be built by a single student using dedication, curiosity, and late night debugging sessions. Every subsystem was implemented by hand using documentation, textbooks, and experimentation. That journey instilled a deep respect for the craft of game development and for the community of developers who share their knowledge.

Key motivations during the original build:
- Produce a polished game loop that feels fair and reactive even on modest school computers.
- Learn the intricacies of Java Swing without relying on frameworks or engines.
- Showcase creativity in both gameplay and presentation for a high school portfolio showcase.
- Prove that perseverance and attention to detail can overcome the challenges posed by a 5,000 line single file project.

## Development Journey Timeline 🗓️
| Phase | Highlights | Lessons |
| --- | --- | --- |
| Concept Sketching | Pencil paper storyboards, brainstorm of knife types, and target behaviors. | Planning simplifies later coding sessions and reduces feature creep. |
| Prototype Build | Basic window, manual render loop, keyboard listener, and scoring counter. | Rapid feedback loops make debugging easier even before assets exist. |
| Content Pass | Added animated targets, particle effects, knife shop, and menu screen. | Breaking work into micro milestones keeps a large file manageable. |
| Polish and Testing | Balanced difficulty, tuned physics constants, refined hit sounds, and smoothed animation curves. | User feedback from classmates is invaluable when tuning fairness and pacing. |
| Refactor Initiative | Copied each legacy class into package scoped files and added JavaDoc for clarity. | Good architecture documents history while keeping the original spirit alive. |

## Architectural Overview 🧠
The project now offers two complementary layouts so that anyone can study the structure in the style they prefer.

### Monolithic Legacy Build
- **File:** `KnifeThrow.java`
- **Lines of Code:** Just over 5,000 with comments, constants, and helper methods.
- **Structure:** Nested inner classes for every entity, renderer, and manager. This approach allowed quick iteration when IDE navigation tools were not available.
- **Strengths:** Simple to distribute, zero build configuration, and nostalgic insight into how a determined student assembled a complete game.
- **Considerations:** Harder to maintain, limited modularity, and challenging for newcomers to trace control flow.

### Modular Refactored Build
- **Location:** `refactored/src/main/java/com/knifethrow/game`
- **Philosophy:** Each inner class from the legacy build is now a package private file with matching logic. JavaDoc annotations describe the intent of every class and critical method.
- **Entry Points:** `knifeThrow` remains the application launcher while `panel`, `knife`, `target`, and related classes encapsulate behaviour.
- **Benefits:** Easier to test, clearer separation of concerns, and friendly to modern tooling while preserving authenticity.

## Gameplay Systems 🎮
A high level view of the subsystems that power KnifeThrow.

### Game Loop and Timing
- Fixed update ticks keep motion predictable regardless of frame rate.
- `panel` orchestrates updates, rendering, and resource timing. Sleep intervals are tuned for responsive gameplay without overloading the CPU.
- State flags manage transitions between menu, active play, pause, and game over panels.

### Rendering Pipeline
- Double buffering eliminates flicker and ensures smooth animation.
- Each entity implements a `draw` routine that accepts a `Graphics2D` context to render sprites, particle trails, and UI overlays.
- Layering rules guarantee that knives, targets, particles, and HUD elements compose cleanly.

### Input Handling
- Swing key listeners map raw key codes to gameplay actions like throwing knives, rotating the player, and navigating menus.
- Debounce logic prevents accidental multiple throws and allows precise rhythm.
- Menu shortcuts support both keyboard navigation and mouse clicks for accessibility.

### Collision and Physics Model
- Circular bounding checks determine knife to target impacts with sufficient accuracy for arcade pacing.
- Targets store rotation velocity, direction, and embedded knife hitboxes to simulate sticking blades.
- Failure states trigger when a thrown knife collides with a previously stuck blade, rewarding spatial awareness.

### Scorekeeping and Progression
- Successful hits increase the combo multiplier, which in turn boosts the score multiplier tracked per session.
- Persistent high scores are written to `data.txt`, allowing the bragging rights to live between sessions.
- Unlockable knives modify throw speed, rotation tolerance, or scoring bonuses to cater to different play styles.

### Audio Design
- Java `Clip` instances deliver hit confirmations, failure cues, and menu ambiance.
- Volume normalization prevents clipping across different desktop environments.
- Audio toggles let players focus during late night study breaks without disturbing others.

### Particle and Visual Feedback
- The `particles` class emits burst effects when knives land, providing satisfying feedback loops.
- Color palettes adapt to target states, conveying damage levels and pending phase shifts.
- Screen shake and subtle camera easing inject energy without disorienting the player.

## Resource and Data Management 📁
- **Sprites:** Stored under `/sprites` with descriptive filenames to simplify swapping assets.
- **Fonts:** A custom pixel font (`pixelated.ttf`) enhances retro aesthetics while remaining legible.
- **Soundscape:** `sounds/` contains WAV clips curated during the original build weekend with free usage licenses respected.
- **Data Files:** `data.txt`, `rules.txt`, and `configuration.txt` capture player settings, shop unlock rules, and configurable constants.
- **Documentation:** The `Javadoc/` directory hosts generated HTML documentation for the refactored package layout.

## Interface Design 🧭
- The landing menu highlights the Knife Shop, Play option, and High Score leaderboard using large touch friendly buttons.
- Tooltips and descriptive labels guide new players through upgrade mechanics and game rules.
- The HUD presents score, combo status, available knives, and timer information in a balanced layout that avoids clutter.
- Failure screens include motivational prompts that encourage trying again rather than punishing mistakes.

## Testing and Quality Assurance ✅
Testing a 5,000 line file in high school required creativity:
- Manual smoke tests ran daily on school lab computers with varying hardware specifications.
- Classmates served as playtesters, reporting exploits, visual glitches, or tuning suggestions.
- Crash logs were reproduced with added console output to pinpoint problematic states.
- Today, the refactored package compiles cleanly with `javac $(find refactored/src/main/java -name "*.java")`, providing confidence in structural integrity.

## Lessons Learned in Grade 12 🎓
- Plan, commit, and iterate regularly so that momentum never stalls.
- Document decisions early because memory fades faster than expected during exam season.
- Respect the time investment of friends who test your project by acting on their feedback.
- Celebrate milestones. The thrill of seeing the first successful target hit is unforgettable.

## Frequently Asked Questions ❓
**Why keep the original 5,000 line file?**
Maintaining the legacy file preserves the historical context and demonstrates how the entire experience was crafted before modern AI assistants existed.

**Which version should new contributors use?**
The refactored module is easier to extend, but studying the legacy file reveals the raw passion and ingenuity behind the project.

**Can I reuse assets?**
Assets remain copyrighted by their respective owners. Please request permission before repurposing them.

**How is difficulty balanced?**
Difficulty ramps through target rotation speeds, reduced safe angles, and curated particle cues that teach players to anticipate hazards.

## Contribution Guide 🤝
1. Fork the repository and clone your copy locally.
2. Explore both the legacy and refactored codebases to understand their interplay.
3. Follow the established JavaDoc conventions when documenting new features.
4. Open a descriptive pull request that explains motivation, implementation details, and testing steps.
5. Engage in respectful discussion during code reviews to keep the community welcoming.

## License and Usage Notes 📄
KnifeThrow is distributed for personal enjoyment and educational study. Commercial redistribution is not permitted without explicit approval. The project stands as a testament to perseverance, youthful curiosity, and the desire to create something memorable before AI coding copilots entered the mainstream. Thank you for exploring the game and celebrating this journey.


## Detailed Class Breakdown 🧩
| Class | Responsibility | Notes |
| --- | --- | --- |
| `knifeThrow` | Application bootstrapper, window configuration, and audio preloading. | Mirrors the legacy static main method while delegating to `panel`. |
| `panel` | Core game surface that handles updates, rendering, and user interface widgets. | Manages timers, transitions, and orchestrates child entities. |
| `player` | Represents the thrower avatar, tracks equipped knife, animation state, and cooldowns. | Implements upgrade hooks for the knife shop. |
| `knife` | Models the projectile state machine from spawn to collision resolution. | Stores angle, travel speed, rotation, and embed flags. |
| `target` | Controls rotation, hit sections, and the logic that determines safe zones. | Provides callbacks for hit effects and score calculation. |
| `tile` | Decorates the playfield and handles menu tile interactions. | Keeps UI feel consistent between screens. |
| `particles` | Generates visual flourishes for hits, misses, and combo streaks. | Uses arrays of active particles for deterministic updates. |
| `startGame` | Manages the main menu, button layout, and navigation to gameplay scenes. | Performs input debouncing for the start button. |

## Mechanics Walkthrough 🔁
1. **Initialization**
   - The game loads fonts, sounds, and sprites, then instantiates the `panel` and attaches it to the Swing frame.
   - Configuration values from `configuration.txt` populate difficulty constants.
2. **Menu Flow**
   - `startGame` renders background tiles, animated buttons, and the high score board.
   - Selecting Play spawns the `panel` gameplay state, while Shop exposes cosmetic upgrades.
3. **Throw Sequence**
   - When the player presses the throw key, `player` verifies cooldown windows and spawns a `knife` object.
   - The new knife inherits player modifiers and enters the update loop with position, velocity, and rotation data.
4. **Collision Check**
   - Each tick, knives compare positions against the rotating `target` safe zones and previously stuck knives.
   - A successful hit triggers scoring logic; a collision with a stuck knife transitions to game over.
5. **Feedback Delivery**
   - `particles` and audio cues fire instantly to communicate success or failure.
   - The HUD updates the combo meter and displays unlocked rewards in real time.
6. **Session End**
   - Game over prompts the player with their score, updates persistent records, and returns to the menu after input acknowledgment.

## Build and Refactor Playbook 🛠️
- **Legacy Build:** Compile with `javac KnifeThrow.java` and run using `java KnifeThrow` for the authentic Grade 12 experience.
- **Refactored Build:** Navigate to `refactored/src/main/java`, compile using `javac $(find . -name "*.java")`, and run `java com.knifethrow.game.knifeThrow`.
- **Documentation Generation:** Use `javadoc` on the refactored package to regenerate HTML docs when logic changes occur.
- **Asset Editing:** Maintain original resolutions to prevent scaling artifacts. Replacement sprites should respect transparency boundaries used by the collision routines.

## Troubleshooting Guide 🧰
- **Black Window on Launch:** Verify that sound drivers are available. Swing waits for audio initialization before drawing the first frame.
- **No Sound:** Confirm that the WAV files remain in the `sounds/` directory and that the Java sound system has permission to access them.
- **Stuttering Performance:** Reduce particle count in `configuration.txt` or ensure your system is not throttling Java processes.
- **High Score Not Saving:** Check write permissions on `data.txt`. The game writes to this file when the session ends.
- **Controls Feel Unresponsive:** Inspect keyboard repeat rate settings at the OS level and ensure the window remains focused.

## Inspiration and Influences 🌟
- Classic arcade titles that reward precision timing and pattern recognition.
- Indie mobile throwers that emphasize risk versus reward mechanics.
- Classmates and teachers who provided feedback during lunch break playtests.
- Retro aesthetics from pixel art collections that inspired the color palette.

## Extended Architecture Notes 🏗️
- **Event Queue Management:** Swing requires UI updates on the Event Dispatch Thread. `knifeThrow` ensures initialization occurs on the EDT using `SwingUtilities.invokeLater` in the refactored layout.
- **Time Step Control:** A combination of `System.nanoTime()` and sleep calibration keeps update ticks steady around the target frame rate.
- **Memory Footprint:** Arrays and primitive collections were favored over dynamic lists to guarantee predictable garbage collection on school lab machines.
- **Safety Nets:** Null checks and guard clauses prevent state transitions when assets are still loading or when the player is in a paused state.
- **Extensibility Hooks:** Enums describe knife rarities, while configuration tables simplify adding future content without touching engine code.

## Asset Pipeline 🎨
- Sprite sheets were drawn with free pixel editors, exported as PNG, and sliced manually in code using source rectangles.
- Sound effects came from creative commons libraries, normalized with Audacity, and stored at 44.1 kHz for compatibility.
- Fonts were tested for readability across different monitor sizes and brightness levels in the school lab.
- Each asset received a descriptive filename to streamline future batch operations or replacements.

## Community Showcase 📣
If you recreate the game or expand upon it, feel free to open an issue or discussion. Share screenshots, custom knives, or alternate sound packs to inspire others. Community creativity keeps the project fresh and honors the collaborative spirit that sustained the original development.

## Acknowledgements 🙏
- Family and friends who supported long study sessions and provided playtesting feedback.
- Teachers who encouraged pushing beyond the curriculum to explore real world software engineering practices.
- Open source communities that maintain the libraries and documentation that made this project possible.


## Technical Deep Dive 🔬
### Target Rotation Mathematics
- Rotation uses incremental angle updates computed as `angle += angularVelocity * deltaTime` with wraparound at 360 degrees.
- Safe zones are derived from modular arithmetic that maps the total number of knife slots to arc segments on the circle.
- Difficulty adjustments increase angular velocity and reduce safe arc lengths, resulting in a higher probability of collisions if timing is off by even a few degrees.

### Knife Embedding Logic
- Upon collision, knives transition from a flight state to an embedded state that freezes their world transform.
- Embedded knives contribute to the failure detection list, ensuring future throws must route between existing blades.
- The renderer offsets embedded knives based on target rotation to preserve alignment.

### Particle System Implementation
- Uses preallocated arrays for position, velocity, color, and remaining life to avoid frequent allocations.
- Update steps apply gravitational drift and alpha fading, achieving a satisfying burst without heavy computation.
- The system reuses inactive particles, which keeps performance steady even during intense combo streaks.

### Resource Loading Strategy
- Assets load lazily on first use in the legacy build to minimize startup time on older machines.
- The refactored build centralizes loading in a resource manager class, making error handling consistent.
- Fallback mechanisms provide placeholder graphics or mute audio when assets are missing so the game remains playable.

### Persistence Format
- High scores serialize as plain text lines with timestamps for easy inspection and manual backup.
- Configuration files follow a key value pattern, which made it simpler to tweak variables during testing without recompilation.
- Future refactors could adopt JSON, but the original format honors the constraints present during the high school project timeline.

