package com.nou2lib.lib;

import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.hal.SimDevice.Direction;
import edu.wpi.first.hal.SimDouble;

/**
 * Class for controlling hobby servos using the NoU motor controller developed by <a href="http://www.alfredosys.com/">Alfredo Systems</a>.
 */
public class NoUServo implements AutoCloseable{
  private SimDevice simDevice;
  private SimDouble angle;

  /**
   * Construct a new handle to a servo port.
   * Creating multiple instances of this class on the same port is not allowed.
   * @param port The port you wish to control on the NoU.
   * Port numbers range from 1-4.
   */
  public NoUServo(int port) {
    if (port < 1 || port > 4) {
      throw new IllegalArgumentException("Port must be in range 1-4");
    }

    simDevice = SimDevice.create("NoUServo", port);
    angle = simDevice.createDouble("angle", Direction.kOutput, 0.0);
  }

  /**
   * Set the angle of the servo.
   * @param angle The angle to set to the device.
   * This value must be between 0.0 and 180.0 inclusive.
   */
  public void setAngle(double angle) {
    if (angle < 0.0 || angle > 180.0) return;
    if (angle == getAngle()) return;
    this.angle.set(angle);
  }

  /**
   * Get the last written angle of the servo.
   * @return The current angle.
   * Value will be between 0.0 and 180.0 inclusive.
   */
  public double getAngle() {
    return angle.get();
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
