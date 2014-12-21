package fr.unice.vicc;

import java.util.List;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;

/**
 * Just a container to declare your home-made observers.
 *
 * @see fr.unice.vicc.PeakPowerObserver for a sample observer
 * @author Fabien Hermenier
 */
public class Observers {
    public static  AntiAffinityObserver antiAO;// anti affinity observer

    /**
     * Build all the observers.
     */
    public void build(List<PowerHost> hosts, List<Vm> vms) {
        
        antiAO = new AntiAffinityObserver(hosts);
        antiAO.startEntity();

    }
}
