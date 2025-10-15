package grasspow.tbscclick.impl;

import grasspow.tbscclick.IKeyBind;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.IKeyConflictContext;

public class KeyBind implements IKeyBind {
    private final KeyMapping keyMapping;
    private boolean held = false;

    public KeyBind(KeyMapping keyMapping) {
        this.keyMapping = keyMapping;
    }

    @Override
    public boolean isDown() {
        return keyMapping.isDown() || held;
    }

    @Override
    public void setHeld(boolean held) {
        this.held = held;
        
        // Update the key mapping's conflict context to prevent conflicts when holding
        if (held) {
            keyMapping.setKeyConflictContext(new HeldKeyConflictContext());
        } else {
            // 在1.20.1中移除此行代码
        }
    }

    private static class HeldKeyConflictContext implements IKeyConflictContext {
        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return false;
        }
    }
}