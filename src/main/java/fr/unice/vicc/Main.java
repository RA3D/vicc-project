package fr.unice.vicc;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import org.cloudbus.cloudsim.power.PowerHost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

/**
 * Entry point of the project.
 * It just initialises the simulator, create the right scheduling algorithm and runs the simulation.
 *
    * Nothing to edit on your side.
 * @author Fabien Hermenier
 */
public class Main {

    public static final String WORKLOAD = "planetlab";

    public static boolean doLog = false;

    private static VmAllocationPolicyFactory policies = new VmAllocationPolicyFactory();

    private static Revenue simulateDay(String d, String impl) throws Exception {

        //Initialise the simulator
        CloudSim.init(1, Calendar.getInstance(), false);

        //The broker is the client interface where to submit the VMs
        DatacenterBroker broker = new PowerDatacenterBroker("Broker");

        //The applications to run, on their Vms
        List<Cloudlet> cloudlets = Helper.createCloudletListPlanetLab(broker.getId(), d);
        List<Vm> vms = Helper.createVmList(broker.getId(), cloudlets.size());
        broker.submitVmList(vms);
        broker.submitCloudletList(cloudlets);

        //800 hosts
        List<PowerHost> hosts = Helper.createHostList(800);

        //The scheduling algorithm
        VmAllocationPolicy policy = policies.make(impl, hosts);

        //the datacenter
        PowerDatacenter datacenter =  Helper.createDatacenter("Datacenter",hosts,policy);

        prepareLogging(d);
        CloudSim.terminateSimulation(Constants.SIMULATION_LIMIT);
        PeakPowerObserver peakPowerObserver = new PeakPowerObserver(hosts);
        CloudSim.startSimulation();

        List<Cloudlet> newList = broker.getCloudletReceivedList();
        Log.printLine("Received " + newList.size() + " cloudlets");

        CloudSim.stopSimulation();
        Log.printLine("Finished");
        return new Revenue(peakPowerObserver, datacenter);
    }

    public static void main(String [] args) throws Exception {
        CumulatedRevenue revenues = new CumulatedRevenue();
        if (args.length < 1) {
            quit("Usage: Main scheduler [day]");
        }
        String policy = args[0].substring(2);  //get rid of the leading "--"

        if (args.length == 1) {
            //we process every day
            File input = new File(WORKLOAD);
            if (!input.isDirectory()) {
                quit(WORKLOAD + " is not a folder");
            }
            for (File f : input.listFiles()) {
                System.out.println("Day " + f.getName());
                Revenue r = simulateDay(f.getPath(), policy);
                System.out.println(r);
                revenues.add(r);
            }

        } else {
            //a single day
            Revenue r = simulateDay(args[1], policy);
            System.out.println(r);
            revenues.add(r);
        }
        System.out.println(revenues);
    }

    private static void prepareLogging(String d) throws FileNotFoundException {
        if (!doLog) {
            Log.disable();
            return;
        }
        File output = new File(d);
        if (!output.exists()) {
            if (!output.mkdirs()) {
                quit("Unable to create log folder '" + output + "'");
            }
        }
        Log.setOutput(new FileOutputStream("logs/ " + d + "/log.txt"));
    }

    public static void quit(String msg) {
        System.err.println(msg);
        System.exit(1);
    }
}