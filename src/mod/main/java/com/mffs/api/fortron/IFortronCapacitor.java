package com.mffs.api.fortron;

import java.util.Set;

/**
 * A grid MFFS uses to search for machines with frequencies that can be linked and spread Fortron
 * energy.
 *
 * @author Calclavia
 */
public interface IFortronCapacitor {
    Set<IFortronFrequency> getLinkedDevices();

    int getTransmissionRange();

    int getTransmissionRate();
}
