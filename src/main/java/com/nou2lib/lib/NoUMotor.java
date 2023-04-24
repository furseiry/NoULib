package com.nou2lib.lib;

import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.hal.SimDevice.Direction;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

/**
 * Class for controlling motors using the NoU motor controller developed by <a href="http://www.alfredosys.com/">Alfredo Systems</a>.
 * Implements the {@link MotorController}
 * interface so it can be used within other methods in WPILib.
 */
public class NoUMotor implements MotorController, AutoCloseable {
  private SimDevice simDevice;
  private SimDouble speed;
  private boolean isInverted;

  /**
   * Construct a new handle to a motor port.
   * Creating multiple instances of this class on the same port is not allowed.
   * @param port The port you wish to control on the NoU.
   * Port numbers range from 1-6.
   */
  public NoUMotor(int port) {
    if (port < 1 || port > 6) {
      throw new IllegalArgumentException("Port must be in range 1-6");
    }

    simDevice = SimDevice.create("NoUMotor", port);
    speed = simDevice.createDouble("speed", Direction.kOutput, 0.0);
  }

  /**
   * Set the speed of the motor controller.
   * @param speed The speed to set to the device.
   * This value must be between -1.0 and 1.0 inclusive.
   */
  @Override
  public void set(double speed) {
    if (speed < -1.0 || speed > 1.0) return;
    speed = isInverted ? -speed: +speed;
    speed = Math.round(speed*100)/100.0;
    if (speed == get()) return;
    this.speed.set(speed);
  }

  /**
   * Get the last written speed on the motor controller.
   * @return The current set speed.
   * Value will be between -1.0 and 1.0 inclusive.
   */
  @Override
  public double get() {
    return speed.get();
  }

  /**
   * Invert the direction of the motor controller.
   * @param isInverted Whether or not to invert the motor.
   */
  @Override
  public void setInverted(boolean isInverted) {
    this.isInverted = isInverted;    
  }

  /**
   * Get the current inverted state of the motor.
   * @return Whether the motor is inverted.
   */
  @Override
  public boolean getInverted() {
    return isInverted;
  }

  /**
   * Disable the device and close all internal resources.
   */
  @Override
  public void disable() {
    simDevice.close();
    simDevice = null;
  }

  /**
   * Stop the motor. This method just sets the speed to 0.0,
   * if you wish to disable the motor see {@link #disable()}
   */
  @Override
  public void stopMotor() {
    set(0.0);
  }

  /**
   * Disable the device and close all internal resources.
   */
  @Override
  public void close() throws Exception {
    disable();
  }
}
