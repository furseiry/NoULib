package com.NoULib.lib;

import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.hal.SimDevice.Direction;
import edu.wpi.first.hal.SimInt;

/**
 * Class for interacting with GPIO (General Purpose In/Out) pins on the ESP32.
 */
public class NoUGPIO implements AutoCloseable {
  /**
   * Enum representing the INPUT, OUTPUT, and INPUT_PULLUP pin modes of Arduino.
   */
  public enum GPIOMode {
    /**
     * INPUT
     */
    READ_ONLY(Direction.kInput),
    /**
     * OUTPUT
     */
    WRITE_ONLY(Direction.kOutput),
    /**
     * INPUT_PULLUP
     */
    READ_WRITE(Direction.kBidir);

    Direction value;

    private GPIOMode(Direction value) {
      this.value = value;
    }
  }

  private SimDevice simDevice;
  private SimInt value;
  private GPIOMode pinMode;

  /**
   * Construct a new handle to a GPIO pin.
   * Creating multiple instances of this class on the same pin is not allowed.
   * @param pin The number of the GPIO pin you wish to use.
   * @param mode Either READ_ONLY, WRITE_ONLY, or READ_WRITE.
   * These modes describe behavior between WPILib and the ESP32,
   * NOT the behavior between your code and this library
   * (You can stil read the value set in WRITE_ONLY mode).
   */
  public NoUGPIO(int pin, GPIOMode mode) {
    simDevice = SimDevice.create("NoUGPIO", pin);
    value = simDevice.createInt("value", mode.value, 0);
    pinMode = mode;

    var prepDevice = SimDevice.create("GPIOPrep", pin);
    prepDevice.createInt("mode", Direction.kOutput, 0).set(mode.ordinal());
    prepDevice.close();
  }

  /**
   * Write a value to the GPIO pin.
   * @param value The value to write.
   * This value must be either 0 or 1.
   */
  public void write(int value) {
    if (value != 0 && value != 1) {
      throw new IllegalArgumentException("Value must be 0 or 1");
    }
    if (pinMode == GPIOMode.READ_ONLY) {
      throw new UnsupportedOperationException("Attempt to write to GPIO pin in read only mode");
    }

    this.value.set(value);
  }

  /**
   * Get the value most recently read by the GPIO pin.
   * @return The value read.
   * Value will be either 0 or 1.
   */
  public int read() {
    return value.get();
  }

  /**
   * Disable the device and close all internal resources.
   */
  @Override
  public void close() throws Exception {
    simDevice.close();
    simDevice = null;
  }
}
