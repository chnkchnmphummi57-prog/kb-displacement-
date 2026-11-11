# Knockback Displacement Mod (Fabric 1.21.11)

A Fabric client mod for Minecraft that snaps your camera view by an adjustable angle for 1 tick, snaps back on the next tick, and fires an attack (Left Mouse Button) input simultaneously to achieve knockback displacement.

## Features
- **YACL UI Integration**: Press **Right Shift** to open the config screen.
- **Adjustable Rotation Angle**: Slider from 0° to 360°.
- **Multiple Modes**: Single (1-tick), Hold to Repeat, and Toggle.
- **Action Keybind**: Default **R** to execute the snap & attack.

## Building the JAR
1. Extract this zip file.
2. Open terminal in the project directory.
3. Run:
   ```bash
   ./gradlew build
   ```
4. Find your compiled `.jar` in `build/libs/knockbackdisplacement-1.21.11.jar`.
