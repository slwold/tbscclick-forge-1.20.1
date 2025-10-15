package grasspow.tbscclick;

import net.minecraft.client.Minecraft;

public interface IClick {

    Minecraft getMinecraft();

    boolean isGamePaused();

    boolean isInGame();

    boolean isInPauseMenu();

    IRayTrace getRayTrace();

    float getSmartAttackCooldown();

    void postClickInputEvent();

    void swingHandIfShould();

    void setHoldButton(IKeyBind key, boolean held);

    IKeyBind getUseKey();

    IKeyBind getToggleRightKey();

    IKeyBind getToggleLeftKey();

    IKeyBind getToggleSmartAttackKey();

    IKeyBind getToggleHoldRightKey();

    IKeyBind getSpeedKey();

    IKeyBind getCrouchKey();

    int getTicksStepBetweenClicks();

    int getMaxTicksBetweenClicks();

    int getMinTicksBetweenClicks();

    String getRightClickMouseMethodMapping();

    String getLeftClickCounterFieldMapping();

    String getLeftClickMouseMethodMapping();

    String getRightClickDelayTimerFieldMapping();

    void renderTextOnScreen(String text, float x, float y, int color);

    boolean isPlayerHandBusy();

    void sendMessage(String message);

    void sendMessageWithId(String message);
}